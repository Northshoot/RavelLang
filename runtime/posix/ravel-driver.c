/**
 * driver.h: the POSIX driver
 *
 */

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>
#include <stdio.h>
#include <assert.h>

#include <sys/types.h>
#include <sys/socket.h>
#ifdef __linux__
#include <netinet/ip.h>
#endif
#include <unistd.h>
#include <arpa/inet.h>

#include "api/intrinsics.h"
#include "ravel/posix-driver.h"

extern void ravel_generated_app_dispatcher_started(struct AppDispatcher*);

static void
load_endpoint_table(RavelPosixDriver *self, const char *file_name)
{
    FILE *fp = fopen(file_name, "r");
    int32_t id;
    char *url;
    char *port_str;
    unsigned long port;
    RavelPosixEndpoint *endpoint;

    if (fp == NULL) {
        fprintf(stderr, "Failed to load endpoint table\n");
        abort();
    }

    while (!feof(fp)) {
        if (fscanf(fp, "%d %ms ", &id, &url) != 2) {
            fprintf(stderr, "Badly formatted line\n");
            continue;
        }

        if (strncmp(url, "tcp://", strlen("tcp://")) != 0) {
            fprintf(stderr, "Unsupported endpoint %s\n", url);
            abort();
        }

        port_str = strchr(url + strlen("tcp://"), ':');
        if (port_str != NULL) {
            port = strtoul(port_str + 1, NULL, 10);
            *port_str = 0;
        } else {
            port = 1234;
        }

        endpoint = calloc(sizeof(RavelPosixEndpoint), 1);
        endpoint->base.id = id;
        endpoint->port = port;
        endpoint->address = inet_addr (url + strlen("tcp://"));
        endpoint->base.is_local = true;
        ravel_posix_driver_register_endpoint (self, endpoint);

        free(url);
    }

    fclose(fp);
}

static uint8_t
hex_to_nibble(char c)
{
    if (c >= 'A' && c <= 'F')
        return 0xA + (c - 'A');
    if (c >= 'a' && c <= 'f')
        return 0xa + (c - 'a');
    if (c >= '0' && c <= '9')
        return (c - '0');
    return 0;
}

static uint8_t*
hex_to_key(const char *key_hex, size_t* plength)
{
    size_t length = strlen(key_hex) / 2;
    size_t i;

    uint8_t *buffer = calloc(length, sizeof(uint8_t));
    if (buffer == NULL)
        return NULL;

    for (i = 0; i < length; i++) {
        char c1 = key_hex[2*i];
        char c2 = key_hex[2*i + 1];

        buffer[i] = hex_to_nibble(c1) << 4 | hex_to_nibble(c2);
    }

    *plength = length;
    return buffer;
}

static void
load_key_table(RavelPosixDriver *self, const char *file_name)
{
    FILE *fp = fopen(file_name, "r");
    RavelKeyProvider *key_provider = &self->base.key_provider;
    uint32_t key_id;
    char *key_type;
    char *key_hex;
    uint8_t *key;
    size_t length;

    if (fp == NULL) {
        fprintf(stderr, "Failed to load key table\n");
        abort();
    }

    while (!feof(fp)) {
        if (fscanf(fp, "%u %ms %ms ", &key_id, &key_type, &key_hex) != 3) {
            fprintf(stderr, "Badly formatted line\n");
            continue;
        }
        if (strcmp(key_type, "AES") != 0 && strcmp(key_type, "HMAC") != 0) {
            fprintf(stderr, "Unsupported key type %s\n", key_type);
            free(key_type);
            continue;
        }
        free(key_type);

        key = hex_to_key(key_hex, &length);
        free(key_hex);
        if (key == NULL)
            continue;

        ravel_key_provider_add_key(key_provider, key_id, length, key);
    }

    fclose(fp);
}

void
ravel_posix_driver_init(RavelPosixDriver *self, const char *app_name, int32_t app_id, int argc, const char * const * argv)
{
    ravel_key_provider_init(&self->base.key_provider);

    self->app_name = app_name;
    self->app_id = app_id;

    memset(self->endpoint_table, 0, sizeof(self->endpoint_table));

    memset(self->poll_fds, 0, sizeof(self->poll_fds));
    memset(self->watched_fds, 0, sizeof(self->watched_fds));
    self->nfds = 0;
    memset(self->callbacks, 0, sizeof(self->callbacks));
    self->ncallbacks = 0;

    if (argc > 1)
        load_endpoint_table (self, argv[1]);

    if (argc > 2)
        load_key_table (self, argv[2]);
}

void
ravel_posix_driver_finalize(RavelPosixDriver *self)
{
    /* Free any context resource here */
    size_t i, j;

    ravel_key_provider_finalize(&self->base.key_provider);

    for (i = 0; i < RAVEL_ENDPOINT_TABLE_SIZE; i++) {
        RavelPosixEndpoint **endpoints = self->endpoint_table[i];

        for (j = 0; endpoints && endpoints[j]; j++)
            free(endpoints[j]);

        free(endpoints);
    }
}

static bool
insert_endpoint_in_table(RavelPosixDriver *self, RavelPosixEndpoint *endpoint)
{
    size_t i = 0;
    size_t j = 0;
    size_t hash;

    hash = endpoint->base.id;
    for (i = 0; i < RAVEL_ENDPOINT_TABLE_SIZE; i++) {
         RavelPosixEndpoint **endpoints = self->endpoint_table[(hash + i) % RAVEL_ENDPOINT_TABLE_SIZE];

         if (endpoints == NULL) {
             endpoints = self->endpoint_table[(hash + i) % RAVEL_ENDPOINT_TABLE_SIZE] = calloc(RAVEL_MAX_ENDPOINTS + 1, sizeof(RavelEndpoint*));
             if (endpoints == NULL) abort();

             endpoints[0] = endpoint;
             return true;
         } else if (endpoints[0]->base.id == endpoint->base.id) {
             for (j = 0; j < RAVEL_MAX_ENDPOINTS; j++) {
                 if (endpoints[j] == NULL) {
                     endpoints[j] = endpoint;
                     return true;
                 }
             }

             // no space in this hash table cell
             return false;
        }
    }

    // hash table is full
    return false;
}

static void
add_poll_fd(RavelPosixDriver *self, int fd, int pollflags, bool is_listen, RavelPosixEndpoint *endpoint, void (*callback)(void*), void *data)
{
    if (self->nfds == RAVEL_MAX_ENDPOINTS)
        abort();

    self->poll_fds[self->nfds].fd = fd;
    self->poll_fds[self->nfds].events = pollflags;
    self->poll_fds[self->nfds].revents = 0;

    self->watched_fds[self->nfds].callback = callback;
    self->watched_fds[self->nfds].data = data;
    self->watched_fds[self->nfds].is_listen = is_listen;
    self->watched_fds[self->nfds].endpoint = endpoint;

    self->nfds++;
}

void
ravel_posix_driver_add_fd(RavelPosixDriver *self, int fd, void (*callback)(void*), void *data) {
    add_poll_fd(self, fd, POLLIN, false, NULL, callback, data);
}

static void
start_local_endpoint(RavelPosixDriver *self, RavelPosixEndpoint *endpoint)
{
    int sock;
    struct sockaddr_in address;
    int ok;

    sock = socket(AF_INET, SOCK_STREAM, 0);
    assert (sock >= 0);

    address.sin_family = AF_INET;
    address.sin_port = htons(endpoint->port);
    address.sin_addr.s_addr = endpoint->address;

    ok = bind(sock, (struct sockaddr*) &address, sizeof(address));
    assert (ok >= 0);

    ok = listen(sock, 10);
    assert (ok >= 0);

    add_poll_fd(self, sock, POLLIN, true, NULL, NULL, NULL);
}

static RavelError write_loop(int fd, size_t length, uint8_t* buffer);

static int
start_remote_endpoint(RavelPosixDriver *self, RavelPosixEndpoint *endpoint)
{
    int fd;
    int ok;
    struct sockaddr_in address;
    uint8_t id_buf[4];
    size_t done;
    ssize_t written;

    fd = socket(AF_INET, SOCK_STREAM, 0);
    if (fd < 0)
        return fd;

    address.sin_family = AF_INET;
    address.sin_port = htons(endpoint->port);
    address.sin_addr.s_addr = endpoint->address;

    ok = connect(fd, (struct sockaddr*)&address, sizeof(address));
    if (ok < 0) {
        close(fd);
        return -1;
    }

    ravel_intrinsic_write_int32 (id_buf, 0, self->app_id);

    done = 0;
    while (done < 4) {
        written = write(fd, id_buf+done, 4-done);
        if (written < 0) {
            close(fd);
            return -1;
        }
        done += written;
    }

    endpoint->base.connected = true;
    add_poll_fd(self, fd, POLLIN, false, endpoint, NULL, NULL);
    return fd;
}


void
ravel_posix_driver_register_endpoint(RavelPosixDriver *self, RavelPosixEndpoint *endpoint)
{
    if (!insert_endpoint_in_table(self, endpoint))
        abort();

    bool is_local = endpoint->base.id == self->app_id;
    endpoint->is_local =  is_local;
    endpoint->base.is_local = is_local;
    if (endpoint->is_local)
        start_local_endpoint(self, endpoint);
    else
        start_remote_endpoint(self, endpoint);
}

static uint8_t *
read_loop(int fd, size_t *plength)
{
    uint8_t packet_length[2];
    uint8_t *buffer;
    size_t length;
    ssize_t ok;
    size_t done;

    ok = read(fd, packet_length, 2);
    if (ok < 0)
        return NULL;
    if (ok == 1) {
        ok = read(fd, packet_length+1, 1);
        if (ok < 0)
            return NULL;
    }

    length = packet_length[1] << 8 | packet_length[0];
    buffer = calloc(length, sizeof(uint8_t));
    if (buffer == NULL) abort();

    done = 0;
    while (done < length) {
        ok = read(fd, buffer+done, length-done);
        if (ok < 0) {
            free(buffer);
            return NULL;
        }
        done += ok;
    }

    *plength = length;
    return buffer;
}

static void handle_new_connection(RavelPosixDriver *self, int listenfd)
{
    int fd;
    struct sockaddr_in address;
    socklen_t address_len;
    RavelPosixEndpoint *endpoint;
    ssize_t ok;
    uint8_t id_buf[4];
    int32_t id;
    size_t done;

    address_len = sizeof(address);
    fd = accept(listenfd, (struct sockaddr*)&address, &address_len);
    if (fd < 0)
        return;

    assert (address_len == sizeof(address));

    endpoint = calloc(1, sizeof(RavelPosixEndpoint));
    if (endpoint == NULL) abort();
    add_poll_fd(self, fd, POLLIN, false, endpoint, NULL, NULL);

    done = 0;
    while (done < 4) {
        ok = read(fd, id_buf+done, 4-done);
        if (ok < 0)
            return;
        done += ok;
    }
    id = ravel_intrinsic_extract_int32 (id_buf, 0);

    endpoint->base.id = id;
    endpoint->port = ntohs(address.sin_port);
    endpoint->address = address.sin_addr.s_addr;
    endpoint->base.connected = true;

    insert_endpoint_in_table (self, endpoint);
}

static void forward_packet (RavelPosixDriver *self, RavelPacket *packet);

static void handle_new_data(RavelPosixDriver *self, int fd, RavelPosixEndpoint *endpoint)
{
    uint8_t *buffer;
    size_t length;
    RavelPacket packet;

    buffer = read_loop(fd, &length);
    if (buffer == NULL)
        return;

    ravel_packet_init_from_network(&packet, buffer, length);
    free(buffer);

    if (ravel_packet_get_source (&packet) == self->app_id) {
        // routing loop or malicious packet, drop
        ravel_packet_finalize (&packet);
        return;
    }

    if (ravel_packet_get_destination (&packet) != self->app_id) {
        // not for us
        forward_packet (self, &packet);
        return;
    }

    fprintf(stderr, "received packet model id %d record id %d flags %u\n", packet.model_id, packet.record_id, (uint32_t)packet.packet_data[2]);

    ravel_base_dispatcher_data_received(self->base.dispatcher, &packet, &endpoint->base);

    ravel_packet_finalize (&packet);
}

void
ravel_posix_driver_main_loop(RavelPosixDriver *self)
{
    int i;
    RavelPosixCallback callbacks[RAVEL_MAX_CALLBACKS];
    int ncallbacks;

    while (true) {
        memcpy(callbacks, self->callbacks, sizeof(callbacks));
        ncallbacks = self->ncallbacks;
        self->ncallbacks = 0;

        for (i = 0; i < ncallbacks; i++) {
            callbacks[i].fn(callbacks[i].ptr1, callbacks[i].ptr2);
        }
        if (self->nfds == 0 && self->ncallbacks == 0)
            break;
        if (self->ncallbacks > 0 || self->nfds == 0)
            continue;
        int ok = poll(self->poll_fds, self->nfds, -1);
        assert (ok >= 0);

        for (i = 0; i < self->nfds; i++) {
            if (self->poll_fds[i].revents & POLLIN) {
                if (self->watched_fds[i].callback) {
                    self->watched_fds[i].callback(self->watched_fds[i].data);
                } else if (self->watched_fds[i].is_listen) {
                    handle_new_connection (self, self->poll_fds[i].fd);
                } else {
                    handle_new_data (self, self->poll_fds[i].fd, self->watched_fds[i].endpoint);
                }
            }
        }
    }
}

void
ravel_posix_driver_app_dispatcher_ready(RavelPosixDriver *self)
{
    ravel_generated_app_dispatcher_started((struct AppDispatcher*)self->base.dispatcher);
}

static RavelEndpoint * const no_endpoints[] = { NULL };

RavelEndpoint * const *
ravel_driver_get_endpoints_by_name(RavelDriver *driver, int32_t name)
{
    RavelPosixDriver *self = ravel_container_of(driver, RavelPosixDriver, base);
    size_t i = 0;
    size_t hash;

    hash = name;
    for (i = 0; i < RAVEL_ENDPOINT_TABLE_SIZE; i++) {
        RavelPosixEndpoint **endpoints = self->endpoint_table[(hash + i) % RAVEL_ENDPOINT_TABLE_SIZE];

        if (endpoints == NULL)
            return no_endpoints;

        if (endpoints[0]->base.id == name)
            return (RavelEndpoint * const *)endpoints;
    }

    return no_endpoints;
}

static RavelError
write_loop(int fd, size_t length, uint8_t* buffer)
{
    uint8_t packet_length[2];
    ssize_t ok;
    size_t done;

    assert (length <= 65536);
    packet_length[0] = length;
    packet_length[1] = length >> 8;

    ok = write(fd, packet_length, 2);
    if (ok < 0)
        return RAVEL_ERROR_NETWORK_ERROR;
    if (ok == 1) {
        ok = write(fd, packet_length+1, 1);
        if (ok < 0)
            return RAVEL_ERROR_NETWORK_ERROR;
    }

    done = 0;
    while (done < length) {
        ok = write(fd, buffer + done, length - done);
        if (ok < 0)
            return RAVEL_ERROR_NETWORK_ERROR;
        done += ok;
    }

    return RAVEL_ERROR_SUCCESS;
}

typedef struct
{
    RavelPacket packet;
    RavelError error;
    RavelEndpoint *endpoint;
} SendDoneClosure;

static void send_done_callback(void *ptr1, void *ptr2)
{
    RavelPosixDriver *self = ptr1;
    SendDoneClosure *closure = ptr2;

    ravel_base_dispatcher_send_done(self->base.dispatcher, closure->error, &closure->packet, closure->endpoint);

    ravel_packet_finalize(&closure->packet);
    free(closure);
}

static RavelError
send_data(RavelPosixDriver *self, int fd, RavelPacket *packet, RavelEndpoint *endpoint)
{
    SendDoneClosure *closure;

    closure = calloc (1, sizeof(SendDoneClosure));
    if (closure == NULL)
        return RAVEL_ERROR_OUT_OF_STORAGE;

    closure->packet = *packet;
    closure->endpoint = endpoint;
    closure->error = write_loop(fd, packet->packet_length, packet->packet_data);
    ravel_driver_queue_callback (&self->base, send_done_callback, self, closure);

    return RAVEL_ERROR_IN_TRANSIT;
}

RavelError
ravel_driver_send_data(RavelDriver *driver, RavelPacket *packet, RavelEndpoint *endpoint)
{
    RavelPosixDriver *self = ravel_container_of(driver, RavelPosixDriver, base);
    size_t i;
    int fd;

    assert (!((RavelPosixEndpoint*)endpoint)->is_local);
    ravel_packet_set_source_destination (packet, self->app_id, endpoint->id);

    for (i = 0; i < self->nfds; i++) {
        if (self->watched_fds[i].endpoint == (RavelPosixEndpoint*)endpoint) {
            return send_data(self, self->poll_fds[i].fd, packet, endpoint);
        }
    }

    fd = start_remote_endpoint(self, (RavelPosixEndpoint*)endpoint);
    if (fd >= 0)
        return send_data(self, self->poll_fds[i].fd, packet, endpoint);

    ravel_packet_finalize(packet);
    return RAVEL_ERROR_ENDPOINT_UNREACHABLE;
}

static void
forward_packet_to_endpoint (RavelPosixDriver *self,
                            RavelPacket      *packet,
                            RavelEndpoint    *endpoint)
{
    size_t i;
    int fd;

    assert (!((RavelPosixEndpoint*)endpoint)->is_local);

    for (i = 0; i < self->nfds; i++) {
        if (self->watched_fds[i].endpoint == (RavelPosixEndpoint*)endpoint) {
            write_loop (self->poll_fds[i].fd, packet->packet_length, packet->packet_data);
            // ignore errors
        }
    }

    fd = start_remote_endpoint(self, (RavelPosixEndpoint*)endpoint);
    if (fd >= 0) {
        write_loop (fd, packet->packet_length, packet->packet_data);
        // ignore errors
    }
}

static void
forward_packet (RavelPosixDriver *self, RavelPacket *packet)
{
    RavelEndpoint * const *endpoints = ravel_driver_get_endpoints_by_name (&self->base, ravel_packet_get_destination (packet));
    size_t i;

    for (i = 0; endpoints && endpoints[i]; i++)
        forward_packet_to_endpoint (self, packet, endpoints[i]);
}

void ravel_driver_queue_callback(RavelDriver *driver, void (*fn)(void*,void*), void* ptr1, void* ptr2)
{
    RavelPosixDriver *self = ravel_container_of(driver, RavelPosixDriver, base);

    if (self->ncallbacks == RAVEL_MAX_CALLBACKS) abort();
    self->callbacks[self->ncallbacks].fn = fn;
    self->callbacks[self->ncallbacks].ptr1 = ptr1;
    self->callbacks[self->ncallbacks].ptr2 = ptr2;
    self->ncallbacks++;
}

typedef struct
{
    RavelPacket packet;
    RavelError error;
} SavedDurablyClosure;

static void saved_durably_callback(void *ptr1, void *ptr2)
{
    RavelPosixDriver *self = ptr1;
    SavedDurablyClosure *closure = ptr2;

    ravel_base_dispatcher_saved_durably (self->base.dispatcher, &closure->packet, closure->error);

    ravel_packet_finalize(&closure->packet);
    free(closure);
}

void ravel_driver_save_durably(RavelDriver *driver, RavelPacket *packet)
{
    SavedDurablyClosure *closure;

    closure = calloc (1, sizeof(SavedDurablyClosure));
    if (closure == NULL) abort();

    closure->packet = *packet;
    closure->error = RAVEL_ERROR_SUCCESS;
    ravel_driver_queue_callback (driver, saved_durably_callback, driver, closure);
}

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
#include <netinet/ip.h>
#include <unistd.h>
#include <arpa/inet.h>

#include "api/intrinsics.h"
#include "ravel/posix-driver.h"

extern void ravel_generated_app_dispatcher_started(struct AppDispatcher*);

static void
load_endpoint_table(RavelPosixDriver *self, const char *file_name)
{
    FILE *fp = fopen(file_name, "r");
    char *name;
    char *url;
    char *port_str;
    unsigned long port;
    RavelPosixEndpoint *endpoint;

    if (fp == NULL) {
        fprintf(stderr, "Failed to load endpoint table\n");
        abort();
    }

    while (!feof(fp)) {
        if (fscanf(fp, "%ms %ms ", &name, &url) != 2) {
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
        endpoint->base.name = name;
        endpoint->port = port;
        endpoint->address = inet_addr (url + strlen("tcp://"));
        ravel_posix_driver_register_endpoint (self, endpoint);

        free(url);
    }

    fclose(fp);
}

static uint8_t
hex_to_nibble(char c)
{
    if (c >= 'A' && c <= 'F')
        return (c - 'A');
    if (c >= 'a' && c <= 'f')
        return (c - 'a');
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
    RavelBaseDispatcher *dispatcher = (RavelBaseDispatcher*) self->base.dispatcher;
    RavelKeyProvider *key_provider = ravel_base_dispatcher_get_key_provider(dispatcher);
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
ravel_posix_driver_init(RavelPosixDriver *self, RavelBaseDispatcher *dispatcher, const char *app_name, int argc, const char * const * argv)
{
    self->base.dispatcher = dispatcher;
    self->app_name = app_name;

    memset(self->endpoint_table, 0, sizeof(self->endpoint_table));

    memset(self->poll_fds, 0, sizeof(self->poll_fds));
    memset(self->watched_fds, 0, sizeof(self->watched_fds));
    self->nfds = 0;

    if (argc > 1)
        load_endpoint_table (self, argv[1]);

    if (argc > 2)
        load_key_table (self, argv[2]);
}

static void posix_endpoint_free(RavelPosixEndpoint *endpoint)
{
    free((char*)endpoint->base.name);
    free(endpoint);
}

void
ravel_posix_driver_finalize(RavelPosixDriver *self)
{
    /* Free any context resource here */
    size_t i, j;

    for (i = 0; i < RAVEL_ENDPOINT_TABLE_SIZE; i++) {
        RavelPosixEndpoint **endpoints = self->endpoint_table[i];

        for (j = 0; endpoints[j]; j++)
            posix_endpoint_free(endpoints[j]);

        free(endpoints);
    }
}

static size_t
string_hash(const char *string)
{
    const char *p = string;
    size_t hash = 33;

    while (*p) {
        hash = hash * 7 + *p;
        p++;
    }

    return hash;
}

static bool
insert_endpoint_in_table(RavelPosixDriver *self, RavelPosixEndpoint *endpoint)
{
    size_t i = 0;
    size_t j = 0;
    size_t hash;

    hash = string_hash(endpoint->base.name);
    for (i = 0; i < RAVEL_ENDPOINT_TABLE_SIZE; i++) {
         RavelPosixEndpoint **endpoints = self->endpoint_table[(hash + i) % RAVEL_ENDPOINT_TABLE_SIZE];

         if (endpoints == NULL) {
             endpoints = self->endpoint_table[(hash + i) % RAVEL_ENDPOINT_TABLE_SIZE] = calloc(RAVEL_MAX_ENDPOINTS + 1, sizeof(RavelEndpoint*));
             if (endpoints == NULL) abort();

             endpoints[0] = endpoint;
             return true;
         } else if (strcmp(endpoints[0]->base.name, endpoint->base.name) == 0) {
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

static RavelPosixEndpoint *
add_poll_fd(RavelPosixDriver *self, int fd, int pollflags, bool is_listen)
{
    if (self->nfds == RAVEL_MAX_ENDPOINTS)
        abort();

    self->poll_fds[self->nfds].fd = fd;
    self->poll_fds[self->nfds].events = pollflags;
    self->poll_fds[self->nfds].revents = 0;

    if (is_listen) {
        self->watched_fds[self->nfds].is_listen = true;
        self->watched_fds[self->nfds].endpoint = NULL;
    } else {
        RavelPosixEndpoint *endpoint = calloc(1, sizeof(RavelPosixEndpoint));
        if (endpoint == NULL) abort();

        self->watched_fds[self->nfds].is_listen = false;
        self->watched_fds[self->nfds].endpoint = endpoint;
    }

    self->nfds++;
    return self->watched_fds[self->nfds-1].endpoint;
}

static void start_local_endpoint(RavelPosixDriver *self, RavelPosixEndpoint *endpoint)
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

    add_poll_fd(self, sock, POLLIN, true);
}

void
ravel_posix_driver_register_endpoint(RavelPosixDriver *self, RavelPosixEndpoint *endpoint)
{
    if (!insert_endpoint_in_table(self, endpoint))
        abort();

    endpoint->is_local = strcmp(endpoint->base.name, self->app_name) == 0;
    if (endpoint->is_local)
        start_local_endpoint(self, endpoint);
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
    uint8_t *name_buffer;
    size_t name_len;
    char *name;
    RavelPosixEndpoint *endpoint;

    address_len = sizeof(address);
    fd = accept(listenfd, (struct sockaddr*)&address, &address_len);
    if (fd < 0)
        return;

    assert (address_len == sizeof(address));

    endpoint = add_poll_fd(self, fd, POLLIN, false);
    name_buffer = read_loop(fd, &name_len);

    name = malloc(name_len + 1);
    if (name == NULL) abort();
    memcpy(name, name_buffer, name_len);
    name[name_len] = 0;
    free(name_buffer);

    endpoint->base.name = name;
    endpoint->port = ntohs(address.sin_port);
    endpoint->address = address.sin_addr.s_addr;

    insert_endpoint_in_table (self, endpoint);
}

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

    ravel_base_dispatcher_data_received(self->base.dispatcher, &packet, &endpoint->base);

    ravel_packet_finalize (&packet);
}

void
ravel_posix_driver_main_loop(RavelPosixDriver *self)
{
    int i;

    if (self->nfds == 0)
        return;

    while (true) {
        int ok = poll(self->poll_fds, self->nfds, -1);
        assert (ok >= 0);

        for (i = 0; i < self->nfds; i++) {
            if (self->poll_fds[i].revents & POLLIN) {
                if (self->watched_fds[i].is_listen) {
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
    ravel_generated_app_dispatcher_started(self->base.dispatcher);
}

static RavelEndpoint * const no_endpoints[] = { NULL };

RavelEndpoint * const *
ravel_driver_get_endpoints_by_name(RavelDriver *driver, const char *name)
{
    RavelPosixDriver *self = ravel_container_of(driver, RavelPosixDriver, base);
    size_t i = 0;
    size_t hash;

    hash = string_hash(name);
    for (i = 0; i < RAVEL_ENDPOINT_TABLE_SIZE; i++) {
        RavelPosixEndpoint **endpoints = self->endpoint_table[(hash + i) % RAVEL_ENDPOINT_TABLE_SIZE];

        if (endpoints == NULL)
            return no_endpoints;

        if (strcmp(endpoints[0]->base.name, name) == 0)
            return (RavelEndpoint * const *)endpoints;
    }

    return no_endpoints;
}

static RavelError
send_data(int fd, RavelPacket *packet)
{
    uint8_t packet_length[2];
    ssize_t ok;
    size_t done;

    assert (packet->packet_length <= 65536);
    packet_length[0] = packet->packet_length;
    packet_length[1] = packet->packet_length >> 8;
    ok = write(fd, packet_length, 2);
    if (ok < 0)
        return RAVEL_ERROR_NETWORK_ERROR;
    if (ok == 1) {
        ok = write(fd, packet_length+1, 1);
        if (ok < 0)
            return RAVEL_ERROR_NETWORK_ERROR;
    }

    done = 0;
    while (done < packet->packet_length) {
        ok = write(fd, packet->packet_data + done, packet->packet_length - done);
        if (ok < 0)
            return RAVEL_ERROR_NETWORK_ERROR;
        done += ok;
    }

    return RAVEL_ERROR_SUCCESS;
}

RavelError
ravel_driver_send_data(RavelDriver *driver, RavelPacket *packet, RavelEndpoint *endpoint)
{
    RavelPosixDriver *self = ravel_container_of(driver, RavelPosixDriver, base);
    size_t i;

    for (i = 0; i < self->nfds; i++) {
        if (self->watched_fds[i].endpoint == (RavelPosixEndpoint*)endpoint) {
            return send_data(self->poll_fds[i].fd, packet);
        }
    }

    return RAVEL_ERROR_ENDPOINT_UNREACHABLE;
}

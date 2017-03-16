/**
 * driver.h: the POSIX driver
 *
 */

#ifndef POSIX_DRIVER_H
#define POSIX_DRIVER_H

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <poll.h>

#include <api/driver.h>
#include <api/base_dispatcher.h>

typedef struct {
    RavelEndpoint base;

    in_addr_t address;
    uint16_t port;

    bool is_local;
} RavelPosixEndpoint;

#define RAVEL_ENDPOINT_TABLE_SIZE 10
#define RAVEL_MAX_ENDPOINTS 10

typedef struct {
    bool is_listen;
    RavelPosixEndpoint *endpoint;
    void (*callback)(void*);
    void *data;
} RavelWatchedFd;

typedef struct {
    void (*fn)(void *ptr1, void *ptr2);
    void *ptr1;
    void *ptr2;
} RavelPosixCallback;

#define RAVEL_MAX_CALLBACKS 20

typedef struct {
    RavelDriver base;

    const char *app_name;
    int32_t app_id;
    RavelPosixEndpoint **endpoint_table[RAVEL_ENDPOINT_TABLE_SIZE];

    struct pollfd poll_fds[RAVEL_MAX_ENDPOINTS];
    RavelWatchedFd watched_fds[RAVEL_MAX_ENDPOINTS];
    int nfds;

    RavelPosixCallback callbacks[RAVEL_MAX_CALLBACKS];
    int ncallbacks;
} RavelPosixDriver;

struct AppDispatcher;

void ravel_posix_driver_init(RavelPosixDriver *self, const char *app_name, int32_t app_id, int argc, const char * const * argv);
void ravel_posix_driver_finalize(RavelPosixDriver *self);

void ravel_posix_driver_register_endpoint(RavelPosixDriver *self, RavelPosixEndpoint *endpoint);
void ravel_posix_driver_add_fd(RavelPosixDriver *self, int fd, void (*callback)(void*), void *data);

void ravel_posix_driver_main_loop(RavelPosixDriver *self);

void ravel_posix_driver_app_dispatcher_ready(RavelPosixDriver *self);

#endif /* POSIX_DRIVER_H */

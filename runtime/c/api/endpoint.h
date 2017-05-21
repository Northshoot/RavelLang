/**
 * endpoint.h: Ravel network endpoints
 * Endpoint API
 */

#ifndef API_ENDPOINT_H
#define API_ENDPOINT_H

#include <stddef.h>
#include <stdbool.h>
#include <stdint.h>

typedef struct {
    uint8_t id;
    bool is_local;
    bool connected;
} RavelEndpoint;

typedef RavelEndpoint Endpoint;

static inline int32_t ravel_endpoint_get_id(Endpoint *self) {
    return self->id;
}

static inline int32_t ravel_endpoint_is_local(Endpoint *self) {
    return self->is_local;
}

static inline int32_t ravel_endpoint_is_connected(Endpoint *self) {
    return self->connected;
}

#endif /* API_PACKET_H */

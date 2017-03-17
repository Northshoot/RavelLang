/**
 * endpoint.h: Ravel network endpoints
 *
 */

#ifndef API_ENDPOINT_H
#define API_ENDPOINT_H

#include <stddef.h>
#include <stdbool.h>
#include <stdint.h>

typedef struct {
    uint8_t id;
    bool connected;
} RavelEndpoint;

static inline int32_t ravel_endpoint_get_id(RavelEndpoint *self) {
    return self->id;
}

#endif /* API_PACKET_H */

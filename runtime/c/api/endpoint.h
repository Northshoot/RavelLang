/**
 * endpoint.h: Ravel network endpoints
 *
 */

#ifndef API_ENDPOINT_H
#define API_ENDPOINT_H

#include <stddef.h>
#include <stdbool.h>

typedef struct {
    const char *name;
    bool connected;
} RavelEndpoint;

static inline const char *ravel_endpoint_get_name(RavelEndpoint *self) {
    return self->name;
}

#endif /* API_PACKET_H */

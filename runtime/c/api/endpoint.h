/**
 * endpoint.h: Ravel network endpoints
 *
 */

#ifndef API_ENDPOINT_H
#define API_ENDPOINT_H

#include <stddef.h>

typedef struct {
    const char *name;
} RavelEndpoint;

static inline const char *ravel_endpoint_get_name(RavelEndpoint *self) {
    return self->name;
}

#endif /* API_PACKET_H */
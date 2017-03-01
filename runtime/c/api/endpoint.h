/**
 * endpoint.h: Ravel network endpoints
 *
 */

#ifndef API_ENDPOINT_H
#define API_ENDPOINT_H

#include <stddef.h>

typedef struct {
    int FILLME;
} RavelEndpoint;

const char *ravel_endpoint_get_name(RavelEndpoint *self);

#endif /* API_PACKET_H */
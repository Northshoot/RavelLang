/**
 * base_driver.h: Base model classes
 *
 */

#ifndef API_BASE_DRIVER_H
#define API_BASE_DRIVER_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

#include "endpoint.h"
#include "packet.h"
#include "context.h"

typedef struct {
    void *dispatcher;
} RavelDriver;

RavelEndpoint * const * ravel_driver_get_endpoints_by_name(RavelDriver *driver, const char *name);

RavelError ravel_driver_send_data(RavelDriver *driver, RavelPacket *packet, RavelEndpoint *endpoint);

#endif /* API_BASE_DRIVER_H */
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
#include "keys.h"

struct RavelBaseDispatcher;

typedef struct {
    struct RavelBaseDispatcher *dispatcher;
    RavelKeyProvider key_provider;

} RavelDriver;

RavelEndpoint * const * ravel_driver_get_endpoints_by_name(RavelDriver *driver, int32_t name);

RavelError ravel_driver_send_data(RavelDriver *driver, RavelPacket *packet, RavelEndpoint *endpoint);
void       ravel_driver_save_durably(RavelDriver *driver, RavelPacket *packet);
void       ravel_driver_queue_callback(RavelDriver *driver, void(*callback)(void*, void*), void *ptr1, void *ptr2);

#endif /* API_BASE_DRIVER_H */

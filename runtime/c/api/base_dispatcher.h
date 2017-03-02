/**
 * base_dispatcher.h: Base dispatcher class
 *
 */

#ifndef API_BASE_DISPATCHER_H
#define API_BASE_DISPATCHER_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

#include "packet.h"
#include "endpoint.h"
#include "context.h"
#include "system.h"
#include "keys.h"

#include "driver.h"

typedef struct {
    void (*data_received)(void *self, RavelPacket *pkt, RavelEndpoint *endpoint);
    void (*send_done)(void *self, RavelError error, RavelPacket *data, RavelEndpoint *endpoint);
} RavelDispatcherVTable;

typedef struct {
    const RavelDispatcherVTable *vtable;
    RavelSystemAPI sys_api;
    RavelKeyProvider key_provider;
    RavelDriver *driver;
} RavelBaseDispatcher;

void ravel_base_dispatcher_init(RavelBaseDispatcher *self);
void ravel_base_dispatcher_finalize(RavelBaseDispatcher *self);

static inline RavelKeyProvider *ravel_base_dispatcher_get_key_provider(RavelBaseDispatcher *self) {
    return &self->key_provider;
}

static inline void ravel_base_dispatcher_set_driver(RavelBaseDispatcher *self, RavelDriver *driver) {
    self->driver = driver;
}

static inline void ravel_base_dispatcher_data_received(RavelBaseDispatcher *self, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    self->vtable->data_received(self, pkt, endpoint);
}

static inline void ravel_base_dispatcher_send_done(RavelBaseDispatcher *self, RavelError error, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    self->vtable->send_done(self, error, pkt, endpoint);
}

static inline RavelEndpoint * const * ravel_base_dispatcher_get_endpoints_by_name(RavelBaseDispatcher *self, const char *name)
{
    return ravel_driver_get_endpoints_by_name(self->driver, name);
}

static inline RavelError ravel_base_dispatcher_send_data(RavelBaseDispatcher *self, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    return ravel_driver_send_data(self->driver, pkt, endpoint);
}

#endif /* API_BASE_DISPATCHER_H */
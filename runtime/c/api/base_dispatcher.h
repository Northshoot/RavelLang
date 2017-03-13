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

struct RavelBaseModel;

typedef struct {
    void (*data_received)(void *self, RavelPacket *pkt, RavelEndpoint *endpoint);
    void (*send_done)(void *self, RavelError error, RavelPacket *pkt, RavelEndpoint *endpoint);
    void (*saved_durably)(void *self, RavelPacket *pkt, RavelError error);
    void (*endpoint_connected)(void *self, RavelEndpoint *endpoint);
} RavelDispatcherVTable;

typedef struct RavelBaseDispatcher {
    const RavelDispatcherVTable *vtable;
    RavelSystemAPI sys_api;
    RavelDriver *driver;
} RavelBaseDispatcher;

void ravel_base_dispatcher_init(RavelBaseDispatcher *self);
void ravel_base_dispatcher_finalize(RavelBaseDispatcher *self);

static inline void ravel_base_dispatcher_queue_callback(RavelBaseDispatcher *self, void (*callback)(void*, void*), void* ptr1, void *ptr2) {
    ravel_driver_queue_callback (self->driver, callback, ptr1, ptr2);
}

static inline RavelKeyProvider *ravel_base_dispatcher_get_key_provider(RavelBaseDispatcher *self) {
    return &self->driver->key_provider;
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

static inline void ravel_base_dispatcher_saved_durably(RavelBaseDispatcher *self, RavelPacket *pkt, RavelError error)
{
    self->vtable->saved_durably(self, pkt, error);
}

static inline void ravel_base_dispatcher_endpoint_connected(RavelBaseDispatcher *self, RavelEndpoint *endpoint)
{
    self->vtable->endpoint_connected(self, endpoint);
}

static inline RavelEndpoint * const * ravel_base_dispatcher_get_endpoints_by_name(RavelBaseDispatcher *self, const char *name)
{
    return ravel_driver_get_endpoints_by_name(self->driver, name);
}

static inline RavelError ravel_base_dispatcher_send_data(RavelBaseDispatcher *self, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    return ravel_driver_send_data(self->driver, pkt, endpoint);
}

static inline void ravel_base_dispatcher_save_durably(RavelBaseDispatcher *self, RavelPacket *pkt)
{
    ravel_driver_save_durably(self->driver, pkt);
}

#endif /* API_BASE_DISPATCHER_H */

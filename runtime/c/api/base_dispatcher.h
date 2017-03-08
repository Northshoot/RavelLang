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

//Forward declaration
typedef  struct RavelBaseDispatcher_s RavelBaseDispatcher;

typedef struct {
    void (*data_received)(void *self, RavelPacket *pkt, RavelEndpoint *endpoint);
    void (*send_done)(void *self, RavelError error, RavelPacket *pkt, RavelEndpoint *endpoint);
    void (*saved_durably)(void *self, RavelPacket *pkt, RavelError error);
} RavelDispatcherVTable;

typedef struct RavelBaseDispatcher_s {
//
////Ravel Dispatcher Network Callbacks
///**@brief Ravel Dispatcher receive event. */
//typedef void (*net_data_rx_t) (RavelBaseDispatcher *self, RavelPacket *pkt, RavelEndpoint *endpoint);
///**@brief Ravel Dispatcher send_done event. */
//typedef void (*net_data_send_done_t) (RavelBaseDispatcher *self, RavelError error, RavelPacket *pkt, RavelEndpoint *endpoint);
//
///**@brief Ravel Dispatcher connected. */
//typedef void (*net_connected_t) (void);
///**@brief Ravel Dispatcher disconnected event. */
//typedef void (*net_disconnected_t) (void);


//typedef struct {
//    net_data_rx_t           data_rx;
//    net_data_send_done_t    send_done;
//    net_connected_t         connected;
//    net_disconnected_t      disconnected;
//}RDNetC;

    const RavelDispatcherVTable *vtable;
//    const RDNetC *net_clb;
    RavelSystemAPI sys_api;
    RavelKeyProvider key_provider;
    RavelDriver *driver;
} ;

void ravel_base_dispatcher_init(RavelBaseDispatcher *self);
void ravel_base_dispatcher_finalize(RavelBaseDispatcher *self);

static inline void ravel_base_dispatcher_queue_callback(RavelBaseDispatcher *self, void (*callback)(void*, void*), void* ptr1, void *ptr2) {
    ravel_driver_queue_callback (self->driver, callback, ptr1, ptr2);
}

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

static inline void ravel_base_dispatcher_saved_durably(RavelBaseDispatcher *self, RavelPacket *pkt, RavelError error)
{
    self->vtable->saved_durably(self, pkt, error);
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

/**
 * driver.h: the nrf52 driver
 *
 */

#ifndef NRF_DRIVER_H
#define NRF_DRIVER_H


#include "api/driver.h"
#include "ravel/module.h"
typedef struct {
    RAVEL_MODULE __module;

} ravel_schedule_event_cntx;

typedef struct {
    RavelDriver base;
} RavelNrf52Driver;

struct AppDispatcher;

void ravel_nrf52_driver_init(RavelNrf52Driver *self, struct AppDispatcher *dispatcher);

void ravel_nrf52_driver_finalize(RavelNrf52Driver *self);

void ravel_nrf52__driver_main_loop(RavelNrf52Driver *self);

void ravel_nrf52_driver_app_dispatcher_ready(RavelNrf52Driver *self);

void ravel_nrf_driver_dispatch_event(RavelNrf52Driver *self);
#endif /* NRF_DRIVER_H */
/**
 * driver.h: the nrf52 driver
 *
 */

#ifndef NRF_DRIVER_H
#define NRF_DRIVER_H


#include "api/driver.h"
#include "ravel/module.h"
#include "api/base_dispatcher.h"


typedef struct  Nrf52Driver RavelNrf52Driver;
#include "nrf52_network.h"
#include "nrf52_ravel_endpoint.h"

typedef struct {
    void *data1;
    void *data2;
    void (*callback) (void*, void*);
} ravel_schedule_event_cntx;


struct Nrf52Driver{
    RavelDriver base;
    NetworkClb network;
} ;

struct AppDispatcher;
extern RavelNrf52Driver driver;

void ravel_nrf52_driver_set_endpoint(RavelNrf52Driver *driver, nrf52_endpoint *endpoint);

void ravel_nrf52_driver_init(RavelNrf52Driver *self, RavelBaseDispatcher *dispatcher, const char *app_name);

void ravel_nrf52_driver_finalize(RavelNrf52Driver *self);

void ravel_nrf52__driver_main_loop(RavelNrf52Driver *self);

void ravel_nrf52_driver_app_dispatcher_ready(RavelNrf52Driver *self);

void ravel_nrf_driver_dispatch_event(RavelNrf52Driver *self);

void ravel_nrf52_driver_send_done_from_low(RavelDriver *self);
#endif /* NRF_DRIVER_H */

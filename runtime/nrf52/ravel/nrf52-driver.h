/**
 * driver.h: the nrf52 driver
 *
 */

#ifndef NRF_DRIVER_H
#define NRF_DRIVER_H

#include <nrf.h>

#include <api/driver.h>

typedef struct {
    RavelDriver base;
} RavelNRFDriver;

struct AppDispatcher;
void ravel_NRF_driver_init(RavelNRFDriver *self, struct AppDispatcher *dispatcher);

void ravel_NRF_driver_finalize(RavelNRFDriver *self);

void ravel_NRF_driver_dispatch_event(RavelNRFDriver *self, process_event_t event);

void ravel_NRF_driver_app_dispatcher_ready(RavelNRFDriver *self);

#endif /* NRF_DRIVER_H */
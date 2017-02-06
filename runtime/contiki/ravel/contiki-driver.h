/**
 * driver.h: the Contiki driver
 *
 */

#ifndef CONTIKI_DRIVER_H
#define CONTIKI_DRIVER_H

#include <contiki.h>

#include <api/driver.h>

typedef struct {
    RavelDriver base;
} RavelContikiDriver;

struct AppDispatcher;
void ravel_contiki_driver_init(RavelContikiDriver *self, struct AppDispatcher *dispatcher);

void ravel_contiki_driver_finalize(RavelContikiDriver *self);

void ravel_contiki_driver_dispatch_event(RavelContikiDriver *self, process_event_t event);

void ravel_contiki_driver_app_dispatcher_ready(RavelContikiDriver *self);

#endif /* CONTIKI_DRIVER_H */
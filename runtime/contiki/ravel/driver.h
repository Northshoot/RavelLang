/**
 * driver.h: the Contiki driver
 *
 */

#ifndef CONTIKI_DRIVER_H
#define CONTIKI_DRIVER_H

#include <contiki.h>

typedef struct {
    int dummy;
} RavelContikiDriver;

struct AppDispatcher;
void ravel_contiki_driver_init(RavelContikiDriver *self, struct AppDispatcher *dispatcher);

void ravel_contiki_driver_finalize(RavelContikiDriver *self);

void ravel_contiki_driver_dispatch_event(RavelContikiDriver *self, process_event_t event);

#endif /* CONTIKI_DRIVER_H */
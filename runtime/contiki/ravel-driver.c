/**
 * driver.h: the Contiki driver
 *
 */

#include <ravel/contiki-driver.h>
#include "AppDispatcher.h"

void
ravel_contiki_driver_init(RavelContikiDriver *self, AppDispatcher *dispatcher)
{
    /* TODO */
    self->base.dispatcher = dispatcher;
}

void
ravel_contiki_driver_finalize(RavelContikiDriver *self)
{
    /* Free any context resource here */
}

void
ravel_contiki_driver_dispatch_event(RavelContikiDriver *self, process_event_t event)
{
    /* TODO */
}

void
ravel_contiki_driver_app_dispatcher_ready(RavelContikiDriver *self)
{
    ravel_generated_app_dispatcher_started(self->base.dispatcher);
}

/**
 * driver.h: the Contiki driver
 *
 */

#include <ravel/driver.h>
#include "AppDispatcher.h"

void
ravel_contiki_driver_init(RavelContikiDriver *self, AppDispatcher *dispatcher)
{
    /* TODO */
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
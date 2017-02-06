/**
 * driver.h: the POSIX driver
 *
 */

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

#include <ravel/posix-driver.h>

extern void ravel_generated_app_dispatcher_started(struct AppDispatcher*);

void
ravel_posix_driver_init(RavelPosixDriver *self, struct AppDispatcher *dispatcher)
{
    /* TODO */
    self->base.dispatcher = dispatcher;
}

void
ravel_posix_driver_finalize(RavelPosixDriver *self)
{
    /* Free any context resource here */
}

void
ravel_posix_driver_main_loop(RavelPosixDriver *self)
{
    // TODO
    while (true) {
        // poll
        // dispatch
    }
}

void
ravel_posix_driver_app_dispatcher_ready(RavelPosixDriver *self)
{
    ravel_generated_app_dispatcher_started(self->base.dispatcher);
}
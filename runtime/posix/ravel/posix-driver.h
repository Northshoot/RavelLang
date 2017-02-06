/**
 * driver.h: the POSIX driver
 *
 */

#ifndef POSIX_DRIVER_H
#define POSIX_DRIVER_H

#include <api/driver.h>

typedef struct {
    RavelDriver base;
} RavelPosixDriver;

struct AppDispatcher;
void ravel_posix_driver_init(RavelPosixDriver *self, struct AppDispatcher *dispatcher);

void ravel_posix_driver_finalize(RavelPosixDriver *self);

void ravel_posix_driver_main_loop(RavelPosixDriver *self);

void ravel_posix_driver_app_dispatcher_ready(RavelPosixDriver *self);

#endif /* POSIX_DRIVER_H */
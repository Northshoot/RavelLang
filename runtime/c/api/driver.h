/**
 * base_driver.h: Base model classes
 *
 */

#ifndef API_BASE_DRIVER_H
#define API_BASE_DRIVER_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

typedef struct {
    void *dispatcher;
} RavelDriver;

#endif /* API_BASE_DRIVER_H */
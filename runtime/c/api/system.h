/**
 * system.h: System event API
 *
 */

#ifndef API_SYSTEM_H
#define API_SYSTEM_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

typedef struct {
    int dummy;
} SystemAPI;

static inline void system_api_init(SystemAPI *self)
{}

static inline void system_api_finalize(SystemAPI *self)
{}

#endif /* API_SYSTEM_H */


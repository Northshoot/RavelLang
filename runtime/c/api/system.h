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
} RavelSystemAPI;

static inline void ravel_system_init(RavelSystemAPI *self)
{}

static inline void ravel_system_finalize(RavelSystemAPI *self)
{}

void ravel_system_print(RavelSystemAPI *self, const char *msg);

#endif /* API_SYSTEM_H */


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

static inline void ravel_system_init(SystemAPI *self)
{}

static inline void ravel_system_finalize(SystemAPI *self)
{}

void ravel_system_print(SystemAPI *self, const char *msg);

#endif /* API_SYSTEM_H */


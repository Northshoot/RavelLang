/**
 * context.h: Common data structures shared by Model and Controller
 *
 */

#ifndef API_CONTEXT_H
#define API_CONTEXT_H

#include <stddef.h>

typedef enum {
    RAVEL_ERROR_SUCCESS,
    RAVEL_ERROR_NETWORK_ERROR,
    RAVEL_ERROR_WAITING_FOR_NETWORK,
    RAVEL_ERROR_WRITE_ERROR,
    RAVEL_ERROR_READ_ERROR,
    RAVEL_ERROR_OUT_OF_STORAGE,
    RAVEL_ERROR_SYSTEM_ERROR,
    RAVEL_ERROR_IN_TRANSIT,
    RAVEL_ERROR_ENDPOINT_UNREACHABLE,
    RAVEL_ERROR_SECURITY_ERROR,
    RAVEL_ERROR_BUSY
} RavelError;

typedef struct {
    void       *record;
    RavelError  error;
} Context;

typedef struct {
    void       *record;
    RavelError  error;
} SystemContext;



void ravel_context_init_ok(Context *ctx, void *record);
void ravel_context_init_error(Context *ctx, RavelError error);

void ravel_context_finalize(Context *ctx);

static inline void ravel_context_clear(Context **pctx) {
    if (*pctx != NULL) {
        ravel_context_finalize(*pctx);
        *pctx = NULL;
    }
}

#endif /* API_CONTEXT_H */
/**
 * context.h: Common data structures shared by Model and Controller
 *
 */

#ifndef API_CONTEXT_H
#define API_CONTEXT_H

#include <stddef.h>

typedef enum {
    RAVEL_ERROR_SUCCESS,
    RAVEL_ERROR_WRITE_ERROR
} RavelError;

typedef struct {
    void       *record;
    RavelError  error;
} Context;

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
/**
 * context.h: Common data structures shared by Model and Controller
 *
 */

#ifndef API_CONTEXT_H
#define API_CONTEXT_H

#include <stddef.h>

typedef struct {
    void *record;
    int   error;
} Context;

void Context_init_ok(Context *ctx, void *record);
void Context_init_error(Context *ctx, int error);

void Context_finalize(Context *ctx);

static inline void Context_clear(Context **pctx) {
    if (*pctx != NULL) {
        Context_finalize(*pctx);
        *pctx = NULL;
    }
}
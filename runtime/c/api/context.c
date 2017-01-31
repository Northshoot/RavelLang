/**
 * context.c: Common data structures shared by Model and Controller
 *
 */

#include "context.h"

void
Context_init_ok(Context *ctx, void *record)
{
    ctx->error = 0;
    ctx->record = record;
}

void Context_init_error(Context *ctx, int error)
{
    ctx->error = error;
    ctx->record = NULL;
}

void Context_finalize(Context *ctx)
{
    /* Free any context resource here */
}
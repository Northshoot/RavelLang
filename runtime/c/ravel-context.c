/**
 * context.c: Common data structures shared by Model and Controller
 *
 */

#include <api/context.h>

void
ravel_context_init_ok(Context *ctx, void *record)
{
    ctx->error = 0;
    ctx->record = record;
}

void
ravel_context_init_error(Context *ctx, RavelError error)
{
    ctx->error = error;
    ctx->record = NULL;
}

void
ravel_context_finalize(Context *ctx)
{
    /* Free any context resource here */
}
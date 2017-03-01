/**
 * ravel-base-dispatcher.c: Base dispatcher classes
 *
 */

#include <stdlib.h>

#include <api/base_dispatcher.h>

void
ravel_base_dispatcher_init(RavelBaseDispatcher *self)
{
    ravel_key_provider_init(&self->key_provider);
}

void
ravel_base_dispatcher_finalize(RavelBaseDispatcher *self)
{
    ravel_key_provider_finalize(&self->key_provider);
}



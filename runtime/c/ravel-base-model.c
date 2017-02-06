/**
 * ravel-base-model.c: Base model classes
 *
 */

#include <stdlib.h>

#include <api/base_model.h>

void
ravel_base_model_init(RavelBaseModel *self, size_t num_records, size_t record_size)
{
    self->vtable = NULL; /* will be set by the actual model */
    self->record_size = record_size;
    self->num_records = num_records;
    self->state = calloc(num_records, sizeof(RavelRecordState));
    self->records = calloc(num_records, record_size);

    self->current_pos = 0;

    ravel_context_init_ok(&self->current_ctx, NULL);
}

void
ravel_base_model_finalize(RavelBaseModel *self)
{
    ravel_context_finalize(&self->current_ctx);

    free(self->state);
    free(self->records);
}

void *
ravel_base_model_create(RavelBaseModel *self)
{
    if (self->current_pos == self->num_records)
        return NULL;

    void *ret = ((char*)self->records) + self->current_pos * self->record_size;
    self->current_pos++;
    return ret;
}

Context *
ravel_local_model_save(RavelLocalModel *self, void *record)
{
    /* Nothing to do, the model is already saved */
    /* FIXME handle durable and reliable */

    ravel_context_finalize(&self->base.current_ctx);
    ravel_context_init_ok(&self->base.current_ctx, record);
    return &self->base.current_ctx;
}

Context *
ravel_replicated_model_save(RavelReplicatedModel *self, void *record)
{
    /* FIXME actually send the data around */

    ravel_context_finalize(&self->base.current_ctx);
    ravel_context_init_error(&self->base.current_ctx, RAVEL_ERROR_WRITE_ERROR);
    return &self->base.current_ctx;
}
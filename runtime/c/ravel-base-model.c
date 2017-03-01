/**
 * ravel-base-model.c: Base model classes
 *
 */

#include <stdlib.h>

#include <api/base_model.h>
#include <api/array.h>
#include <api/base_dispatcher.h>

void
ravel_base_model_init(RavelBaseModel *self, void *dispatcher, size_t num_records, size_t record_size)
{
    size_t i;

    self->vtable = NULL; /* will be set by the actual model */
    self->dispatcher = dispatcher;
    self->record_size = record_size;
    self->num_records = num_records;
    self->state = calloc(num_records, sizeof(RavelRecordState));
    if (self->state == NULL) abort();
    self->records = calloc(num_records, record_size);
    if (self->records == NULL) abort();

    self->record_ptrs = ravel_array_new(num_records, sizeof(void*));
    if (self->record_ptrs == NULL) abort();
    for (i = 0; i < num_records; i++) {
        self->record_ptrs[i] = ((uint8_t*)(self->records) + record_size * i);
    }
    ravel_array_set_length(self->record_ptrs, 0);

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
ravel_base_model_allocate(RavelBaseModel *self)
{
    if (self->current_pos == self->num_records)
        return NULL;

    void *ret = ((char*)self->records) + self->current_pos * self->record_size;
    self->current_pos++;

    // FIXME: this makes the record immediately visible to .all(), even though it
    // has not been saved
    // maybe we need two counters instead
    ravel_array_set_length(self->record_ptrs, self->current_pos);
    return ret;
}

void
ravel_base_model_record_arrived(RavelBaseModel *self, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    void *record;
    RavelPacket ack;

    if (pkt->is_ack) {
         /* TODO do something with ack */
         return;
    }

    // Let the controllers and local model deal with it first...
    ravel_context_finalize(&self->current_ctx);

    record = self->vtable->unmarshall(self, pkt->record_data, pkt->record_length, endpoint);
    if (record == NULL) {
        /* TODO handle out of memory error */
        return;
    }
    ravel_context_init_ok(&self->current_ctx, record);

    self->vtable->dispatch_arrived(self, &self->current_ctx);

    ravel_packet_init_ack(&ack, pkt->model_id, pkt->record_id);
    ravel_base_dispatcher_send_data((RavelBaseDispatcher*)self->dispatcher, &ack, endpoint);
}

void
ravel_base_model_record_departed(RavelBaseModel *self, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    void *record;

    if (pkt->is_ack) {
        return;
    }

    ravel_context_finalize(&self->current_ctx);

    record = self->record_ptrs[pkt->record_id];
    ravel_context_init_ok(&self->current_ctx, record);

    self->vtable->dispatch_departed(self, &self->current_ctx);
}

void ravel_base_model_record_failed_to_send(RavelBaseModel *self, RavelPacket *packet, RavelEndpoint *endpoint, RavelError error)
{
    // TODO retransmit
}

void
ravel_base_model_record_saved_durably(RavelBaseModel *self, RavelPacket *pkt)
{
    /* TODO */
}

void
ravel_base_model_record_saved_endpoint(RavelBaseModel *self, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    /* TODO */
}

void
ravel_base_model_endpoint_connected(RavelBaseModel *self, RavelEndpoint *endpoint)
{
    /* TODO */
}

static RavelError
ravel_base_model_send_record(RavelBaseModel *self, void *record, const char * const *endpoint_names)
{
    RavelPacket packet;
    uint8_t *byte_array;
    int i, j;
    RavelError error, local_error;

    if (endpoint_names[0] == NULL)
        return RAVEL_ERROR_SUCCESS;

    error = RAVEL_ERROR_SUCCESS;
    for (i = 0; endpoint_names[i]; i++) {
        const char *endpoint_name = endpoint_names[i];
        RavelEndpoint * const *endpoints = ravel_base_dispatcher_get_endpoints_by_name((RavelBaseDispatcher*)self->dispatcher, endpoint_name);

        for (j = 0; endpoints[j]; j++) {
            RavelEndpoint *endpoint = endpoints[j];

            // serialize the record
            byte_array = self->vtable->marshall(self, record, endpoint);
            ravel_packet_init_from_record(&packet, byte_array, ravel_array_length(byte_array));
            ravel_array_free(byte_array);

            local_error = ravel_base_dispatcher_send_data((RavelBaseDispatcher*)self->dispatcher, &packet, endpoint);
            if (local_error != RAVEL_ERROR_SUCCESS)
                error = local_error;
        }
    }
    return error;
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

Context *
ravel_streaming_model_send(RavelStreamingModel *self, void *record)
{
    RavelError send_error = ravel_base_model_send_record(&self->base, record, self->endpoints);

    if (send_error != RAVEL_ERROR_SUCCESS) {
        ravel_context_finalize(&self->base.current_ctx);
        ravel_context_init_error(&self->base.current_ctx, send_error);
        return &self->base.current_ctx;
    } else {
        ravel_context_finalize(&self->base.current_ctx);
        ravel_context_init_ok(&self->base.current_ctx, record);
        return &self->base.current_ctx;
    }
}
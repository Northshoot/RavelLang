/**
 * ravel-base-model.c: Base model classes
 *
 */

#include <stdlib.h>
#include <errno.h>
#include <string.h>

#include "api/base_model.h"
#include "api/array.h"
#include "api/base_dispatcher.h"

static void dl_init(DoubleLinkedList *list)
{
    list->next = NULL;
    list->previous = NULL;
}

static void dl_append_tail(DoubleLinkedList *list, DoubleLinkedList *node)
{
    assert (list != node);

    node->previous = NULL;
    node->next = NULL;

    if (list->previous == NULL) {
        assert (list->next == NULL);
        list->previous = list->next = node;
    } else {
        list->previous->next = node;
        node->previous = list->previous;
        list->previous = node;
    }
}

static void dl_remove(DoubleLinkedList *list, DoubleLinkedList *node)
{
    assert (list != node);

    if (list->previous == node)
        list->previous = node->previous;
    if (list->next == node)
        list->next = node->next;
    assert ((list->next == NULL) == (list->previous == NULL));

    if (node->next)
        node->next->previous = node->previous;
    if (node->previous)
        node->previous->next = node->next;

    node->previous = NULL;
    node->next = NULL;
}

static inline void *record_at(RavelBaseModel *self,
                              int             pos)
{
    assert (pos >= 0 && pos < self->num_records);
    return ((uint8_t*)self->record_memory + self->record_size * pos);
}

void
ravel_base_model_init(RavelBaseModel *self,
                      struct RavelBaseDispatcher *dispatcher,
                      size_t num_records,
                      size_t record_size,
                      bool reliable,
                      bool durable)
{
    size_t i;

    self->vtable = NULL; /* will be set by the actual model */
    self->dispatcher = dispatcher;
    self->reliable = reliable;
    self->durable = durable;
    self->record_size = record_size;
    self->num_records = num_records;
    self->state = calloc(num_records, sizeof(RavelRecordState));
    if (self->state == NULL) abort();
    self->record_memory = calloc(num_records, record_size);
    if (self->record_memory == NULL) abort();

    dl_init(&self->valid_records);
    dl_init(&self->free_list);
    dl_init(&self->nursery);
    for (i = 0; i < num_records; i++)
        dl_append_tail (&self->free_list, record_at(self, i));

    self->num_valid_records = 0;
    self->next_record_id = 1;

    ravel_context_init_ok(&self->current_ctx, NULL);
}

void* ravel_base_model_get(RavelBaseModel *self, int idx)
{
    DoubleLinkedList *record;

    for (record = self->valid_records.next; idx > 0 && record != NULL;
         record = record->next, idx--) {}

    return record;
}

static inline int record_pos_from_record(RavelBaseModel *self, void *record)
{
    assert(record >= self->record_memory && (uint8_t*)record < ((uint8_t*)self->record_memory + self->record_size * self->num_records));
    assert(((uint8_t*)record - (uint8_t*)self->record_memory) % self->record_size == 0);

    return ((uint8_t*)record - (uint8_t*)self->record_memory)/self->record_size;
}

static inline uint32_t record_id_from_record(void *record)
{
    return ((RavelRecord*)record)->record_id;
}
static inline void set_record_id(void *record, uint32_t id)
{
    ((RavelRecord*)record)->record_id = id;
}

static int
find_record_with_id(RavelBaseModel *self,
                    uint32_t id)
{
    size_t i;

    // FIXME make it a binary search when we're confident enough

    for (i = 0; i < self->num_records; i++) {
        if (record_id_from_record(record_at(self, i)) == id)
            return i;
    }

    return -1;
}

void
ravel_base_model_finalize(RavelBaseModel *self)
{
    ravel_context_finalize(&self->current_ctx);

    free(self->state);
    free(self->record_memory);
}

void *
ravel_base_model_allocate(RavelBaseModel *self)
{
    void *record;
    if (self->free_list.next == NULL)
        return NULL;

    record = self->free_list.next;
    dl_remove(&self->free_list, record);

    self->state[record_pos_from_record (self, record)].is_allocated = true;
    return record;
}

static void
ravel_record_free(RavelBaseModel *self, void *record, bool is_in_nursery)
{
    int record_pos = record_pos_from_record (self, record);

    assert (self->state[record_pos].is_allocated);
    assert (!self->state[record_pos].is_valid);
    assert (!self->state[record_pos].in_save);
    assert (self->state[record_pos].in_transit == 0);
    self->state[record_pos].is_allocated = false;
    self->vtable->record_finalize(self, record);

    memset(&self->state[record_pos], 0, sizeof(RavelRecordState));

    if (is_in_nursery)
        dl_remove (&self->nursery, record);
    dl_append_tail (&self->free_list, record);
}

void
ravel_base_model_reset_alloc(RavelBaseModel *self) {
    void *record;
    void *next;

    for (record = self->nursery.next; record; record = next) {
        next = ((DoubleLinkedList*)record)->next;
        ravel_record_free(self, record, true);
    }

    ravel_context_finalize(&self->current_ctx);
    ravel_context_init_ok(&self->current_ctx, NULL);
}

int32_t
ravel_base_model_size(RavelBaseModel *self)
{
    return (int32_t)self->num_valid_records;
}

static RavelError
ravel_base_model_send_record_endpoint(RavelBaseModel     *self,
                                      void               *record,
                                      RavelEndpoint      *endpoint)
{
    RavelPacket packet;
    uint8_t *byte_array;
    int record_pos = record_pos_from_record (self, record);

    // serialize the record
    byte_array = self->vtable->marshall(self, record, endpoint);
    if (byte_array == NULL)
        return RAVEL_ERROR_OUT_OF_STORAGE;

    self->state[record_pos].in_transit++;
    if (self->reliable)
        self->state[record_pos].expected_acks++;

    ravel_packet_init_from_record(&packet, byte_array, ravel_array_length(byte_array));
    ravel_array_free(byte_array);

    return ravel_base_dispatcher_send_data(self->dispatcher, &packet, endpoint);
}

static RavelError
ravel_base_model_send_record(RavelBaseModel *self, void *record, const char * const *endpoint_names)
{

    int i, j;
    RavelError error, local_error;

    if (endpoint_names[0] == NULL)
        return RAVEL_ERROR_SUCCESS;

    error = RAVEL_ERROR_SUCCESS;
    for (i = 0; endpoint_names[i]; i++) {
        const char *endpoint_name = endpoint_names[i];
        RavelEndpoint * const *endpoints = ravel_base_dispatcher_get_endpoints_by_name(self->dispatcher, endpoint_name);

        for (j = 0; endpoints[j]; j++) {
            RavelEndpoint *endpoint = endpoints[j];

            local_error = ravel_base_model_send_record_endpoint(self, record, endpoint);
            if (local_error != RAVEL_ERROR_SUCCESS)
                error = local_error;
        }
    }
    return error;
}

void
ravel_local_model_init(RavelLocalModel *self,
                       struct RavelBaseDispatcher *dispatcher,
                       size_t num_records,
                       size_t record_size,
                       bool reliable,
                       bool durable)
{
    ravel_base_model_init (&self->base, dispatcher, num_records, record_size, reliable, durable);
}

void
ravel_local_model_finalize(RavelLocalModel *self)
{
    ravel_base_model_finalize (&self->base);
}

static void
save_done_out_of_storage(void *ptr1, void *ptr2)
{
    RavelBaseModel *self = ptr1;
    void *record = ptr2;
    int record_pos = record_pos_from_record(self, record);
    self->state[record_pos].in_save = false;

    ravel_context_init_error (&self->current_ctx, RAVEL_ERROR_OUT_OF_STORAGE);
    self->current_ctx.record = record;
    self->vtable->dispatch_save_done(self, &self->current_ctx);
}

static void
save_done_next_loop(void *ptr1, void *ptr2)
{
    RavelBaseModel *self = ptr1;
    void *record = ptr2;
    int record_pos = record_pos_from_record(self, record);
    self->state[record_pos].in_save = false;

    assert (self->state[record_pos].is_allocated);
    if (self->state[record_pos].is_valid) {
        ravel_context_init_ok (&self->current_ctx, record);
        self->vtable->dispatch_save_done(self, &self->current_ctx);
    } else {
        ravel_record_free(self, record, false);
    }
}

static void
full_callback(void *ptr1, void *ptr2)
{
    RavelBaseModel *self = ptr1;

    ravel_context_init_error(&self->current_ctx, RAVEL_ERROR_OUT_OF_STORAGE);
    self->vtable->dispatch_full(self, &self->current_ctx);
}

static Context *
ravel_base_model_save(RavelBaseModel *self, void *record)
{
    int record_pos = record_pos_from_record(self, record);

    assert (self->state[record_pos].is_allocated);

    if (!self->state[record_pos].is_valid) {
        set_record_id(record, self->next_record_id++);

        dl_remove(&self->nursery, record);
        dl_append_tail(&self->valid_records, record);
        self->state[record_pos].is_valid = true;
        self->num_valid_records ++;

        if (self->num_valid_records == self->num_records) {
            ravel_base_dispatcher_queue_callback(self->dispatcher, full_callback, self, NULL);
        }
    }

    self->state[record_pos].in_save = true;
    if (self->durable) {
        RavelPacket packet;
        uint8_t *data;
        data = self->vtable->marshall(self, record, NULL);
        if (data == NULL) {
            ravel_base_dispatcher_queue_callback(self->dispatcher, save_done_out_of_storage, self, record);
            ravel_context_init_error(&self->current_ctx, RAVEL_ERROR_OUT_OF_STORAGE);
            return &self->current_ctx;
        }

        ravel_packet_init_from_record(&packet, data, ravel_array_length(data));
        ravel_array_free(data);
        // the dispatcher takes ownership of the packet, do not free it
        ravel_base_dispatcher_save_durably(self->dispatcher, &packet);
        ravel_context_init_error(&self->current_ctx, RAVEL_ERROR_IN_TRANSIT);
        return &self->current_ctx;
    } else {
        ravel_context_finalize(&self->current_ctx);
        ravel_context_init_ok(&self->current_ctx, record);
        return &self->current_ctx;
    }
}

// returns true if the record was saved at some point, false otherwise
static bool
ravel_base_model_delete(RavelBaseModel *self, void *record)
{
    int record_pos = record_pos_from_record(self, record);

    assert (self->state[record_pos].is_allocated);

    if (!self->state[record_pos].is_valid) {
        // do nothing, the record was never saved
        // it will be freed at the end of this controller call
        return false;
    } else {
        // mark this record as deleted
        self->state[record_pos].is_valid = false;
        assert (self->num_valid_records > 0);
        self->num_valid_records --;
        dl_remove(&self->valid_records, record);

        if (self->state[record_pos].in_save ||
            self->state[record_pos].in_transit > 0 ||
            self->state[record_pos].expected_acks > 0) {
            // we cannot delete right away
            return true;
        } else {
            // move to the nursery again, which will be freed
            // when the controller is done with this pointer
            dl_append_tail(&self->nursery, record);
        }
        return true;
    }
}

Context *
ravel_local_model_save(RavelLocalModel *self, void *record)
{
    Context *ctx;

    ctx = ravel_base_model_save(&self->base, record);
    if (ctx->error == RAVEL_ERROR_SUCCESS)
        ravel_base_dispatcher_queue_callback(self->base.dispatcher, save_done_next_loop, &self->base, record);
    return ctx;
}

void
ravel_local_model_delete(RavelLocalModel *self, void *record)
{
    bool was_valid;

    was_valid = ravel_base_model_delete(&self->base, record);
    if (was_valid && self->base.durable) {
        // TODO remove from durable storage
    }
}

static void *
ravel_base_model_record_saved_durably(RavelBaseModel *self, RavelPacket *pkt, RavelError error)
{
    int record_pos;
    void *record;

    assert(self->durable);

    record_pos = find_record_with_id(self, pkt->record_id);
    if (record_pos < 0)
        return NULL;
    record = record_at(self, record_pos);

    assert(self->state[record_pos].in_save);
    assert(self->state[record_pos].is_allocated);
    self->state[record_pos].in_save = false;

    if (self->state[record_pos].is_valid) {
        return record;
    } else {
        self->state[record_pos].is_allocated = false;
        self->vtable->record_finalize(self, record);
        dl_append_tail(&self->free_list, record);
        return NULL;
    }
}

void
ravel_local_model_record_saved_durably(RavelLocalModel *self, RavelPacket *pkt, RavelError error)
{
    void *record;

    record = ravel_base_model_record_saved_durably (&self->base, pkt, error);
    if (record == NULL)
        return;

    ravel_context_init_ok (&self->base.current_ctx, record);
    self->base.vtable->dispatch_save_done(&self->base, &self->base.current_ctx);
}

static bool
ravel_base_model_handle_ack(RavelBaseModel *self, void *record, RavelEndpoint *endpoint)
{
    int record_pos = record_pos_from_record (self, record);

    self->state[record_pos].expected_acks--;
    return self->state[record_pos].expected_acks == 0;
}

static Context *
ravel_base_model_handle_record(RavelBaseModel *self, void *record, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    record = self->vtable->unmarshall(self, record, pkt->record_data, pkt->record_length, endpoint);
    if (record == NULL) {
        if (errno == EPERM)
            ravel_context_init_error(&self->current_ctx, RAVEL_ERROR_SECURITY_ERROR);
        else if (errno == ENOMEM)
            ravel_context_init_error(&self->current_ctx, RAVEL_ERROR_OUT_OF_STORAGE);
        else
            ravel_context_init_error(&self->current_ctx, RAVEL_ERROR_SYSTEM_ERROR);
        return &self->current_ctx;
    } else {
        return ravel_base_model_save(self, record);
    }
}

void
ravel_replicated_model_init(RavelReplicatedModel *self,
                            struct RavelBaseDispatcher *dispatcher,
                            size_t num_records,
                            size_t record_size,
                            bool reliable,
                            bool durable)
{
    ravel_base_model_init (&self->base, dispatcher, num_records, record_size, reliable, durable);
}

void
ravel_replicated_model_finalize(RavelReplicatedModel *self)
{
    ravel_base_model_finalize (&self->base);
}

Context *
ravel_replicated_model_save(RavelReplicatedModel *self, void *record)
{
    /* FIXME actually send the data around */

    ravel_context_finalize(&self->base.current_ctx);
    ravel_context_init_error(&self->base.current_ctx, RAVEL_ERROR_WRITE_ERROR);
    return &self->base.current_ctx;
}


void
ravel_streaming_model_init(RavelStreamingModel *self,
                           struct RavelBaseDispatcher *dispatcher,
                           size_t num_records,
                           size_t record_size,
                           bool reliable,
                           bool durable)
{
    ravel_base_model_init (&self->base, dispatcher, num_records, record_size, reliable, durable);
}

void
ravel_streaming_model_finalize(RavelStreamingModel *self)
{
    ravel_base_model_finalize (&self->base);
}

Context *
ravel_streaming_model_save(RavelStreamingModel *self, void *record)
{
    Context *ctx;

    ctx = ravel_base_model_save(&self->base, record);
    if (ctx->error == RAVEL_ERROR_SUCCESS) {
        RavelError send_error = ravel_base_model_send_record(&self->base, record, self->sink_endpoints);
        int record_pos = record_pos_from_record (&self->base, record);

        // clear the save flag because we won't send a save done until much later
        self->base.state[record_pos].in_save = false;

        ravel_context_finalize(&self->base.current_ctx);
        ravel_context_init_error(&self->base.current_ctx, send_error);
        self->base.current_ctx.record = record;
        return &self->base.current_ctx;
    } else {
        // OUT_OF_STORAGE or IN_TRANSIT (= during save)
        return ctx;
    }
}

void
ravel_streaming_model_delete(RavelStreamingModel *self, void *record)
{
    bool was_valid;

    was_valid = ravel_base_model_delete(&self->base, record);
    if (was_valid && self->base.durable) {
        // TODO remove from durable storage
    }
}

static RavelError
ravel_base_model_forward_packet(RavelBaseModel *self, RavelPacket *pkt, const char * const *endpoint_names, void *record)
{
    int i, j;
    RavelError error, local_error;

    error = RAVEL_ERROR_SUCCESS;
    for (i = 0; endpoint_names[i]; i++) {
        const char *endpoint_name = endpoint_names[i];
        RavelEndpoint * const *endpoints = ravel_base_dispatcher_get_endpoints_by_name(self->dispatcher, endpoint_name);

        for (j = 0; endpoints[j]; j++) {
            RavelEndpoint *endpoint = endpoints[j];
            RavelPacket copy;

            ravel_packet_init_copy(&copy, pkt);

            if (record != NULL) {
                int record_pos = record_pos_from_record (self, record);
                self->state[record_pos].in_transit ++;

                if (self->reliable)
                    self->state[record_pos].expected_acks ++;
            }

            local_error = ravel_base_dispatcher_send_data (self->dispatcher, &copy, endpoint);
            if (local_error != RAVEL_ERROR_SUCCESS)
                error = local_error;
        }
    }
    return error;
}

static RavelError
ravel_streaming_model_forward(RavelStreamingModel *self, RavelPacket *pkt, void *record)
{
    const char * const *endpoint_names;

    if (pkt->is_save_done)
        endpoint_names = self->source_endpoints;
    else
        endpoint_names = self->sink_endpoints;

    if (endpoint_names[0] == NULL) {
        if (!pkt->is_save_done) {
            RavelPacket save_done;

            ravel_packet_init_save_done (&save_done, pkt->model_id, pkt->record_id);
            return ravel_base_model_forward_packet (&self->base, &save_done, self->source_endpoints, NULL);
        } else {
            return RAVEL_ERROR_SUCCESS;
        }
    }

    return ravel_base_model_forward_packet(&self->base, pkt, endpoint_names, record);
}

void
ravel_streaming_model_record_saved_durably(RavelStreamingModel *self, RavelPacket *pkt, RavelError error)
{
    void *record;
    int record_pos;

    record = ravel_base_model_record_saved_durably (&self->base, pkt, error);
    if (record == NULL)
        return;

    record_pos = record_pos_from_record (&self->base, record);
    if (self->base.state[record_pos].is_arrived) {
        self->base.state[record_pos].is_arrived = false;
        ravel_context_init_ok (&self->base.current_ctx, record);
        self->base.vtable->dispatch_arrived(self, &self->base.current_ctx);

        ravel_streaming_model_forward(self, pkt, record);
    } else {
        ravel_base_model_send_record(&self->base, record, self->sink_endpoints);
        // ignore errors
        // if we're reliable, we'll retry with a timeout
    }
}

void
ravel_streaming_model_record_arrived(RavelStreamingModel *self, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    if (pkt->is_ack) {
        int record_pos = find_record_with_id(&self->base, pkt->record_id);

        if (record_pos < 0 || !self->base.state[record_pos].is_allocated || !self->base.state[record_pos].is_valid)
            return;

        if (self->base.reliable) {
            if (ravel_base_model_handle_ack (&self->base, record_at(&self->base, record_pos), endpoint)) {
                ravel_context_init_ok(&self->base.current_ctx, record_at(&self->base, record_pos));
                self->base.vtable->dispatch_departed(self, &self->base.current_ctx);
            }
        }
    } else if (pkt->is_save_done) {
        // forward the save done no matter what
        ravel_streaming_model_forward(self, pkt, NULL);

        int record_pos = find_record_with_id(&self->base, pkt->record_id);

        if (record_pos < 0 || !self->base.state[record_pos].is_allocated || !self->base.state[record_pos].is_valid)
            return;

        ravel_context_init_ok(&self->base.current_ctx, record_at(&self->base, record_pos));
        self->base.vtable->dispatch_save_done(self, &self->base.current_ctx);
    } else {
        RavelPacket ack;
        int record_pos;
        void *record;
        Context *ctx;

        // send the ack first
        ravel_packet_init_ack(&ack, pkt->model_id, pkt->record_id);
        ravel_base_dispatcher_send_data(self->base.dispatcher, &ack, endpoint);

        record_pos = find_record_with_id(&self->base, pkt->record_id);
        if (record_pos >= 0 && self->base.state[record_pos].is_allocated) {
            // duplicate record (spurious retransmission)
            // ignore
            return;
        }

        record = ravel_base_model_allocate (&self->base);
        if (record == NULL) {
            // uh oh!
            // FIXME what to do here?
            return;
        }

        record_pos = record_pos_from_record(&self->base, record);
        ctx = ravel_base_model_handle_record(&self->base, record, pkt, endpoint);
        if (ctx->error == RAVEL_ERROR_SUCCESS) {
            // clear the save flag
            self->base.state[record_pos].in_save = false;

            self->base.vtable->dispatch_arrived(self, &self->base.current_ctx);

            ravel_streaming_model_forward(self, pkt, record);
        } else if (ctx->error == RAVEL_ERROR_IN_TRANSIT) {
            // saving, wait until done saving to tell the app
            assert (self->base.state[record_pos].is_valid);
            self->base.state[record_pos].is_arrived = true;
        } else {
            // security error or some other error, free the allocation
            assert (!self->base.state[record_pos].is_valid);
            ravel_record_free(&self->base, record, true);
        }
    }
}

void
ravel_streaming_model_record_departed(RavelStreamingModel *self, RavelPacket *pkt, RavelEndpoint *endpoint)
{
    int record_pos;
    void *record;

    if (pkt->is_ack || pkt->is_save_done) {
        return;
    }

    record_pos = find_record_with_id(&self->base, pkt->record_id);
    // we cannot free stuff that is in transit
    assert (record_pos >= 0);
    assert (self->base.state[record_pos].is_allocated);

    record = record_at(&self->base, record_pos);
    self->base.state[record_pos].in_transit--;
    if (self->base.state[record_pos].in_transit == 0) {
        if (!self->base.state[record_pos].is_valid) {
            // record was deleted after sending
            ravel_record_free(&self->base, record, false);
        } else if (self->base.reliable) {
            // do nothing and wait for the acks
        } else {
            ravel_context_init_ok(&self->base.current_ctx, record_at(&self->base, record_pos));
            self->base.vtable->dispatch_departed(self, &self->base.current_ctx);
        }
    }
}

void
ravel_streaming_model_record_failed_to_send(RavelStreamingModel *self, RavelPacket *pkt, RavelEndpoint *endpoint, RavelError error)
{
    int record_pos;
    void *record;

    if (pkt->is_ack || pkt->is_save_done) {
        // unconditionally try to retransmit
        RavelPacket copy;
        ravel_packet_init_copy(&copy, pkt);
        ravel_base_dispatcher_send_data(self->base.dispatcher, &copy, endpoint);
        return;
    }

    record_pos = find_record_with_id(&self->base, pkt->record_id);
    // we cannot free stuff that is in transit
    assert (record_pos >= 0);
    assert (self->base.state[record_pos].is_allocated);

    record = record_at(&self->base, record_pos);
    self->base.state[record_pos].in_transit--;
    if (self->base.state[record_pos].in_transit == 0 && !self->base.state[record_pos].is_valid) {
        // if the record was deleted after sending, no matter what we're
        // not going to try resending
        ravel_record_free(&self->base, record, false);
    } else {
        // balance the retransmission increasing the counter
        if (self->base.reliable)
            self->base.state[record_pos].expected_acks--;

        // try resending
        ravel_base_model_send_record_endpoint(&self->base, record, endpoint);
    }
}


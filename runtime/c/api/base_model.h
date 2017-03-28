/**
 * base_model.h: Base model classes
 *
 */

#ifndef API_BASE_MODEL_H
#define API_BASE_MODEL_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>
#include <assert.h>

#include "context.h"
#include "endpoint.h"
#include "packet.h"

struct RavelBaseDispatcher;

typedef struct DoubleLinkedList {
  struct DoubleLinkedList *previous;
  struct DoubleLinkedList *next;
} DoubleLinkedList;

typedef struct {
  DoubleLinkedList list;
  uint16_t record_id;
} RavelRecord;

typedef struct {
  void *next;
} RavelIterator;

typedef struct {
    // the number of acks we're waiting for (for
    // reliable models)
    int32_t expected_acks;

    // true if this record is currently in use by the
    // storage (resp. network) subsystems
    bool in_save;
    int32_t in_transit;

    // true if this record was saved
    // false if the record was deleted
    bool is_valid;

    // true if this record is initialized
    bool is_allocated;

    // true if this record arrived and we
    // haven't told the controller yet
    bool is_arrived;

    // true if this record was sent but we never called
    // departed because we never got a successfull send_done()
    bool is_transmit_failed;
} RavelRecordState;

typedef struct {
    uint8_t *(*marshall)(void *self, void *record, RavelEndpoint*);
    void *(*unmarshall)(void *self, void *record, uint8_t* data, size_t length, RavelEndpoint*);
    void (*record_finalize)(void *self, void *record);
    void (*dispatch_arrived)(void *self, Context *ctx);
    void (*dispatch_departed)(void *self, Context *ctx);
    void (*dispatch_full)(void *self, Context *ctx);
    void (*dispatch_save_done)(void *self, Context *ctx);
} RavelModelVTable;

typedef struct RavelBaseModel {
    const RavelModelVTable *vtable;
    struct RavelBaseDispatcher *dispatcher;
    RavelRecordState *state;
    void *record_memory;

    // memory management lists:
    //
    // - valid records is the list of all
    //   records that have been saved (durably or not)
    //   and not deleted
    //
    // - free list is the list of all records
    //   that are free and not initialized
    //
    // - nursery is the list of records that have been
    //   created but not saved
    //   records in this list are garbage, and they are
    //   flushed at the end of the controller call
    //
    // records can be in none of these three list, if
    // they have been deleted but they are in use by the
    // system (for storage or network)
    DoubleLinkedList valid_records;
    DoubleLinkedList free_list;
    DoubleLinkedList nursery;

    bool reliable;
    bool durable;
    size_t record_size;
    size_t num_records;

    size_t num_valid_records;
    uint16_t next_record_id;

    Context current_ctx;
} RavelBaseModel;

#define ravel_base_model(m) (&(m)->__base.base)

void ravel_base_model_init(RavelBaseModel *self, struct RavelBaseDispatcher *dispatcher, size_t num_records, size_t record_size, bool reliable, bool durable);
void ravel_base_model_finalize(RavelBaseModel *self);

void ravel_base_model_reset_alloc(RavelBaseModel *self);

void** ravel_base_model_all(RavelBaseModel *self);
static inline RavelIterator ravel_base_model_iterator(RavelBaseModel *self)
{
  RavelIterator iter = { self->valid_records.next };
  return iter;
}
static inline bool ravel_base_model_iterator_has_next(RavelIterator *iter)
{
  return iter->next != NULL;
}
static inline void * ravel_base_model_iterator_next(RavelIterator *iter)
{
  DoubleLinkedList *record = iter->next;
  iter->next = record->next;
  return record;
}

void* ravel_base_model_get(RavelBaseModel *self, int idx);
static inline void* ravel_base_model_first(RavelBaseModel *self)
{
    return self->valid_records.next;
}
static inline void* ravel_base_model_last(RavelBaseModel *self)
{
    return self->valid_records.previous;
}
int32_t ravel_base_model_size(RavelBaseModel *self);

void    *ravel_base_model_allocate(RavelBaseModel *self);

/**
 *
 *
 */
typedef struct {
    RavelBaseModel base;
} RavelLocalModel;

void ravel_local_model_init(RavelLocalModel *self, struct RavelBaseDispatcher *dispatcher, size_t num_records, size_t record_size, bool reliable, bool durable);
void ravel_local_model_finalize(RavelLocalModel *self);

static inline void ravel_local_model_endpoint_connected(RavelLocalModel *self, RavelEndpoint   *endpoint) {}
static inline void ravel_local_model_record_arrived(RavelLocalModel *self, RavelPacket *packet, RavelEndpoint *endpoint) {
    __builtin_unreachable();
}
static inline void ravel_local_model_record_departed(RavelLocalModel *self, RavelPacket *packet, RavelEndpoint *endpoint) {
    __builtin_unreachable();
}
static inline void ravel_local_model_record_failed_to_send(RavelLocalModel *self, RavelPacket *packet, RavelEndpoint *endpoint, RavelError error) {
    __builtin_unreachable();
}
void ravel_local_model_record_saved_durably(RavelLocalModel *self, RavelPacket *pkt, RavelError error);
Context *ravel_local_model_save(RavelLocalModel *self, void *record);
void ravel_local_model_delete(RavelLocalModel *self, void *record);

static inline void ravel_local_model_add_sink_endpoints(RavelLocalModel *self, const int32_t * endpoints) {
    assert(endpoints[0] == -1);
}
static inline void ravel_local_model_add_source_endpoints(RavelLocalModel *self, const int32_t * endpoints) {
    assert(endpoints[0] == -1);
}

///Streaming model
typedef struct {
    RavelBaseModel base;
    const int32_t *sink_endpoints;
    const int32_t *source_endpoints;
} RavelStreamingModel;

void ravel_streaming_model_init(RavelStreamingModel *self, struct RavelBaseDispatcher *dispatcher, size_t num_records, size_t record_size, bool reliable, bool durable);
void ravel_streaming_model_finalize(RavelStreamingModel *self);

void ravel_streaming_model_endpoint_connected(RavelStreamingModel *self, RavelEndpoint *endpoint);
void ravel_streaming_model_record_arrived(RavelStreamingModel *self, RavelPacket *packet, RavelEndpoint *endpoint);
void ravel_streaming_model_record_departed(RavelStreamingModel *self, RavelPacket *packet, RavelEndpoint *endpoint);
void ravel_streaming_model_record_failed_to_send(RavelStreamingModel *self, RavelPacket *packet, RavelEndpoint *endpoint, RavelError error);
void ravel_streaming_model_record_saved_durably(RavelStreamingModel *self, RavelPacket *pkt, RavelError error);
Context *ravel_streaming_model_save(RavelStreamingModel *self, void *record);
void ravel_streaming_model_delete(RavelStreamingModel *self, void *record);

static inline void ravel_streaming_model_add_sink_endpoints(RavelStreamingModel *self, const int32_t * endpoints) {
    self->sink_endpoints = endpoints;
}
static inline void ravel_streaming_model_add_source_endpoints(RavelStreamingModel *self, const int32_t * endpoints) {
    self->source_endpoints = endpoints;
}

//Replicated model

typedef struct {
    RavelBaseModel base;
    const int32_t *sink_endpoints;
} RavelReplicatedModel;

void ravel_replicated_model_init(RavelReplicatedModel *self, struct RavelBaseDispatcher *dispatcher, size_t num_records, size_t record_size, bool reliable, bool durable);
void ravel_replicated_model_finalize(RavelReplicatedModel *self);

void ravel_replicated_model_endpoint_connected(RavelReplicatedModel *self, RavelEndpoint *endpoint);
void ravel_replicated_model_record_arrived(RavelReplicatedModel *self, RavelPacket *packet, RavelEndpoint *endpoint);
void ravel_replicated_model_record_departed(RavelReplicatedModel *self, RavelPacket *packet, RavelEndpoint *endpoint);
void ravel_replicated_model_record_failed_to_send(RavelReplicatedModel *self, RavelPacket *packet, RavelEndpoint *endpoint, RavelError error);
void ravel_replicated_model_record_saved_durably(RavelStreamingModel *self, RavelPacket *pkt, RavelError error);
Context *ravel_replicated_model_save(RavelReplicatedModel *self, void *record);
void ravel_replicated_model_delete(RavelReplicatedModel *self, void *record);

static inline void ravel_replicated_model_add_sink_endpoints(RavelReplicatedModel *self, const int32_t * endpoints) {
    self->sink_endpoints = endpoints;
}
static inline void ravel_replicated_model_add_source_endpoints(RavelReplicatedModel *self, const int32_t * endpoints) {
    // ignore
}

#endif /* API_BASE_MODEL_H */

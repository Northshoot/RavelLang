/**
 * base_model.h: Base model classes
 *
 */

#ifndef API_BASE_MODEL_H
#define API_BASE_MODEL_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

#include "context.h"
#include "endpoint.h"
#include "packet.h"

typedef struct {
    bool in_rest;
    bool in_transit;
    bool in_use;
    bool ack;
} RavelRecordState;

typedef struct {
    uint8_t *(*marshall)(void *self, void *record);
    void *(*unmarshall)(void *self, uint8_t* data, size_t length);
    void (*dispatch_arrived)(void *self, Context *ctx);
    void (*dispatch_departed)(void *self, Context *ctx);
    void (*dispatch_full)(void *self, Context *ctx);
    void (*dispatch_save_done)(void *self, Context *ctx);
} RavelModelVTable;

typedef struct {
    const RavelModelVTable *vtable;
    void *dispatcher;
    RavelRecordState *state;
    void *records;
    void ** record_ptrs;

    size_t record_size;
    size_t num_records;

    size_t current_pos;

    Context current_ctx;
} RavelBaseModel;

#define ravel_base_model(m) (&(m)->__base.base)

void ravel_base_model_init(RavelBaseModel *self, void *dispatcher, size_t num_records, size_t record_size);
void ravel_base_model_finalize(RavelBaseModel *self);

static inline void** ravel_base_model_all(RavelBaseModel *self)
{
    return self->record_ptrs;
}
static inline void* ravel_base_model_get(RavelBaseModel *self, int idx)
{
    return self->record_ptrs[idx];
}
static inline void* ravel_base_model_first(RavelBaseModel *self)
{
    return self->record_ptrs[0];
}
static inline void* ravel_base_model_last(RavelBaseModel *self)
{
    return self->record_ptrs[self->current_pos-1];
}
static inline int ravel_base_model_size(RavelBaseModel *self)
{
    return (int)self->current_pos;
}

void ravel_base_model_record_arrived(RavelBaseModel *self, RavelPacket *packet, RavelEndpoint *endpoint);
void ravel_base_model_record_departed(RavelBaseModel *self, RavelPacket *packet, RavelEndpoint *endpoint);
void ravel_base_model_record_saved_durably(RavelBaseModel *self, RavelPacket *packet);
void ravel_base_model_record_saved_endpoint(RavelBaseModel *self, RavelPacket *packet, RavelEndpoint *endpoint);
void ravel_base_model_record_failed_to_send(RavelBaseModel *self, RavelPacket *packet, RavelEndpoint *endpoint, RavelError error);
void ravel_base_model_endpoint_connected(RavelBaseModel *self, RavelEndpoint *endpoint);

void    *ravel_base_model_allocate(RavelBaseModel *self);

typedef struct {
    RavelBaseModel base;
} RavelLocalModel;

static inline void ravel_local_model_init(RavelLocalModel *self, void *dispatcher, size_t num_records, size_t record_size) {
    ravel_base_model_init(&self->base, dispatcher, num_records, record_size);
}
static inline void ravel_local_model_finalize(RavelLocalModel *self) {
    ravel_base_model_finalize(&self->base);
}
Context *ravel_local_model_save(RavelLocalModel *self, void *record);

static inline void ravel_local_model_add_endpoints(RavelLocalModel *self, const char * const * endpoints) {}

typedef struct {
    RavelBaseModel base;
    const char * const *endpoints;
} RavelStreamingModel;

static inline void ravel_streaming_model_init(RavelStreamingModel *self, void *dispatcher, size_t num_records, size_t record_size) {
    ravel_base_model_init(&self->base, dispatcher, num_records, record_size);
    self->endpoints = NULL;
}
static inline void ravel_streaming_model_finalize(RavelStreamingModel *self) {
    ravel_base_model_finalize(&self->base);
}
Context *ravel_streaming_model_save(RavelStreamingModel *self, void *record);

static inline void ravel_streaming_model_add_endpoints(RavelStreamingModel *self, const char * const * endpoints) {
    self->endpoints = endpoints;
}

typedef struct {
    RavelBaseModel base;
} RavelReplicatedModel;

static inline void ravel_replicated_model_init(RavelReplicatedModel *self, void *dispatcher, size_t num_records, size_t record_size) {
    ravel_base_model_init(&self->base, dispatcher, num_records, record_size);
}
static inline void ravel_replicated_model_finalize(RavelReplicatedModel *self) {
    ravel_base_model_finalize(&self->base);
}
Context *ravel_replicated_model_save(RavelReplicatedModel *self, void *record);

static inline void ravel_replicated_model_add_endpoints(RavelReplicatedModel *self, const char * const * endpoints) {
    // FIXME
}

#endif /* API_BASE_MODEL_H */
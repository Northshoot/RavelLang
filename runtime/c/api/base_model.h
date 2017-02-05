/**
 * base_model.h: Base model classes
 *
 */

#ifndef API_BASE_MODEL_H
#define API_BASE_MODEL_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

#include <api/context.h>
#include <api/endpoint.h>

typedef struct {
    bool in_rest;
    bool in_transit;
    bool in_use;
    bool ack;
} RavelRecordState;

typedef struct {
    void (*dispatch_arrived)(void *self, Context *ctx);
    void (*dispatch_departed)(void *self, Context *ctx);
    void (*dispatch_full)(void *self, Context *ctx);
    void (*dispatch_save_done)(void *self, Context *ctx);
} RavelModelVTable;

typedef struct {
    const RavelModelVTable *vtable;
    RavelRecordState *state;
    void *records;
    size_t record_size;
    size_t num_records;

    size_t current_pos;
} RavelBaseModel;

void ravel_base_model_init(RavelBaseModel *self, size_t num_records, size_t record_size);
void ravel_base_model_finalize(RavelBaseModel *self);

void    *ravel_base_model_create(RavelBaseModel *self);
Context *ravel_base_model_save(RavelBaseModel *self, void *record);

typedef struct {
    RavelBaseModel base;
} RavelLocalModel;

static inline void ravel_local_model_init(RavelLocalModel *self, size_t num_records, size_t record_size) {
    ravel_base_model_init(&self->base, num_records, record_size);
}
static inline void ravel_local_model_finalize(RavelLocalModel *self) {
    ravel_base_model_finalize(&self->base);
}

typedef struct {
    RavelBaseModel base;
    RavelEndpoint *endpoint;
} RavelStreamingModel;

static inline void ravel_streaming_model_init(RavelStreamingModel *self, size_t num_records, size_t record_size) {
    ravel_base_model_init(&self->base, num_records, record_size);
    self->endpoint = NULL;
}
static inline void ravel_streaming_model_finalize(RavelStreamingModel *self) {
    ravel_base_model_finalize(&self->base);
}

typedef struct {
    RavelBaseModel base;
} RavelReplicatedModel;

static inline void ravel_replicated_model_init(RavelReplicatedModel *self, size_t num_records, size_t record_size) {
    ravel_base_model_init(&self->base, num_records, record_size);
}
static inline void ravel_replicated_model_finalize(RavelReplicatedModel *self) {
    ravel_base_model_finalize(&self->base);
}

#endif /* API_BASE_MODEL_H */
/**
 * base_model.h: Base model classes
 *
 */

#ifndef API_BASE_MODEL_H
#define API_BASE_MODEL_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

typedef struct {
    bool in_rest;
    bool in_transit;
    bool in_use;
    bool ack;
} RavelRecordState;

typedef struct {
    RavelRecordState *state;
    void *records;
    size_t record_size;
    size_t num_records;

    size_t current_pos;
} RavelBaseModel;

void ravel_base_model_init(RavelBaseModel *self, size_t num_records, size_t record_size);
void ravel_base_model_finalize(RavelBaseModel *self);

#endif /* API_BASE_MODEL_H */
//
// Created by lauril on 2/23/17.
//

#ifndef C_GROWABLE_BYTE_ARRAY_H
#define C_GROWABLE_BYTE_ARRAY_H

#include <stdint.h>
#include <stddef.h>
#include <time.h>
#include <stdbool.h>

#include "context.h"

typedef struct {
    uint8_t *data;
    size_t capacity;
    size_t used;
} RavelGrowableByteArray;

void ravel_growable_byte_array_init(RavelGrowableByteArray *self);
void ravel_growable_byte_array_finalize(RavelGrowableByteArray *self);

static inline RavelGrowableByteArray ravel_growable_byte_array_create(void) {
    RavelGrowableByteArray array;
    ravel_growable_byte_array_init(&array);
    return array;
}

void ravel_growable_byte_array_write_uint16(RavelGrowableByteArray *self, int32_t value);
void ravel_growable_byte_array_write_int32(RavelGrowableByteArray *self, int32_t value);
void ravel_growable_byte_array_write_error_msg(RavelGrowableByteArray *self, RavelError error);
void ravel_growable_byte_array_write_str(RavelGrowableByteArray *self, const char * str);
void ravel_growable_byte_array_write_byte(RavelGrowableByteArray *self, uint8_t v);
void ravel_growable_byte_array_write_timestamp(RavelGrowableByteArray *self, time_t v);
void ravel_growable_byte_array_write_double(RavelGrowableByteArray *self, double v);
void ravel_growable_byte_array_write_bool(RavelGrowableByteArray *self, bool b);
void ravel_growable_byte_array_write_byte_array(RavelGrowableByteArray *self, uint8_t* array);

uint8_t *ravel_growable_byte_array_to_byte_array(RavelGrowableByteArray *self);

#endif //C_GROWABLE_BYTE_ARRAY_H

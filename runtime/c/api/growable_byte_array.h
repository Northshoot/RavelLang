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
    size_t used;
} GrowableByteArray;

void ravel_growable_byte_array_init(GrowableByteArray *self);
void ravel_growable_byte_array_finalize(GrowableByteArray *self);

static inline GrowableByteArray ravel_growable_byte_array_create(void) {
    GrowableByteArray array;
    ravel_growable_byte_array_init(&array);
    return array;
}

void ravel_growable_byte_array_write_uint16(GrowableByteArray *self, int32_t value);
void ravel_growable_byte_array_write_int32(GrowableByteArray *self, int32_t value);
void ravel_growable_byte_array_write_error_msg(GrowableByteArray *self, RavelError error);
void ravel_growable_byte_array_write_str(GrowableByteArray *self, const char * str);
void ravel_growable_byte_array_write_byte(GrowableByteArray *self, uint8_t v);
void ravel_growable_byte_array_write_timestamp(GrowableByteArray *self, time_t v);
void ravel_growable_byte_array_write_double(GrowableByteArray *self, double v);
void ravel_growable_byte_array_write_bool(GrowableByteArray *self, bool b);
void ravel_growable_byte_array_write_byte_array(GrowableByteArray *self, uint8_t* array);

uint8_t *ravel_growable_byte_array_to_byte_array(GrowableByteArray *self);

#endif //C_GROWABLE_BYTE_ARRAY_H

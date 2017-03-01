/**
 * ravel-growable-byte-array.c: a byte array that resizes in exponential (* 1.5) increments
 *
 */

#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include <api/growable_byte_array.h>
#include <api/array.h>

#define DEFAULT_SIZE 32

static inline size_t max_size(size_t one, size_t two) {
    if (one > two)
        return one;
    else
        return two;
}

void
ravel_growable_byte_array_init(RavelGrowableByteArray *self)
{
    self->data = calloc(DEFAULT_SIZE, sizeof(uint8_t));
    if (self->data == NULL) abort() /* FIXME */;
    self->capacity = DEFAULT_SIZE;
    self->used = 0;
}

void
ravel_growable_byte_array_finalize(RavelGrowableByteArray *self)
{
    free(self->data);
}

static void
ensure_capacity(RavelGrowableByteArray *self, size_t capacity)
{
    size_t new_capacity;
    uint8_t *new_buffer;

    if (self->capacity >= capacity)
        return;

    new_capacity = max_size(capacity, self->capacity * 3 / 2);
    new_buffer = realloc(self->data, new_capacity);
    if (new_buffer == NULL) {
        new_capacity = capacity;
        new_buffer = realloc(self->data, new_capacity);
        if (new_buffer == NULL) abort() /* FIXME */;
    }

    self->data = new_buffer;
    memset(self->data + self->capacity, 0, new_capacity - self->capacity);
    self->capacity = new_capacity;

    assert (self->used <= self->capacity);
}

uint8_t *
ravel_growable_byte_array_to_byte_array(RavelGrowableByteArray *self)
{
    uint8_t *result;

    result = ravel_array_new(self->used, sizeof(uint8_t));
    if (result == NULL)
        return NULL;

    memcpy(result, self->data, self->used);
    return result;
}

void
ravel_growable_byte_array_write_uint16(RavelGrowableByteArray *self, int32_t value)
{
    ensure_capacity(self, self->used + 2);

    // Little endian!
    self->data[self->used ++] = value;
    self->data[self->used ++] = value >> 8;

    assert (self->used <= self->capacity);
}

void
ravel_growable_byte_array_write_int32(RavelGrowableByteArray *self, int32_t value)
{
    ensure_capacity(self, self->used + 4);

    // Big endian!
    self->data[self->used ++] = value >> 24;
    self->data[self->used ++] = value >> 16;
    self->data[self->used ++] = value >> 8;
    self->data[self->used ++] = value;

    assert (self->used <= self->capacity);
}

void
ravel_growable_byte_array_write_error_msg(RavelGrowableByteArray *self, RavelError error)
{
    ravel_growable_byte_array_write_int32(self, error);
}

void
ravel_growable_byte_array_write_str(RavelGrowableByteArray *self, const char * str)
{
    size_t len = strlen(str);
    assert (len < 65536);

    ravel_growable_byte_array_write_uint16(self, len);

    ensure_capacity(self, self->used + len);
    memcpy(self->data + self->used, str, len);
    self->used += len;

    assert (self->used <= self->capacity);
}

void
ravel_growable_byte_array_write_byte(RavelGrowableByteArray *self, uint8_t v)
{
    ensure_capacity(self, self->used + 1);

    self->data[self->used ++] = v;

    assert (self->used <= self->capacity);
}

void
ravel_growable_byte_array_write_timestamp(RavelGrowableByteArray *self, time_t v)
{
    ravel_growable_byte_array_write_int32(self, v);
}

void
ravel_growable_byte_array_write_double(RavelGrowableByteArray *self, double v)
{
    uint64_t data;
    ensure_capacity(self, self->used + 8);

    memcpy(&data, &v, sizeof(double));

    // Big endian!
    self->data[self->used ++] = data >> 56;
    self->data[self->used ++] = data >> 48;
    self->data[self->used ++] = data >> 40;
    self->data[self->used ++] = data >> 32;
    self->data[self->used ++] = data >> 24;
    self->data[self->used ++] = data >> 16;
    self->data[self->used ++] = data >> 8;
    self->data[self->used ++] = data;

    assert (self->used <= self->capacity);
}

void
ravel_growable_byte_array_write_bool(RavelGrowableByteArray *self, bool b)
{
    ensure_capacity(self, self->used + 1);

    self->data[self->used ++] = b ? 1 : 0;

    assert (self->used <= self->capacity);
}





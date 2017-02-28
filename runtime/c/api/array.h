/**
 * array.h: Abstractions over arrays
 *
 */

#ifndef API_ARRAY_H
#define API_ARRAY_H

#include <stdlib.h>
#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

static inline void *ravel_array_new(size_t length, size_t element_size)
{
    void *buffer;

    if (length * element_size < length)
        return NULL;

    buffer = malloc(length * element_size + sizeof(size_t));
    if (buffer == NULL)
        return NULL;
    *((size_t*)buffer) = length;

    return ((uint8_t*)buffer + sizeof(size_t));
}

static inline void ravel_array_free(void *array)
{
    free((uint8_t*)array - sizeof(size_t));
}

static inline size_t ravel_array_length(void *array)
{
    size_t *plength = (size_t*)((uint8_t*)array - sizeof(size_t));
    return *plength;
}

// UNSAFE! Sets the length that will be returned by ravel_array_length()
// (which is visible to Ravel code)
//
// It must be less than the maximum length (which is the size of the overall
// memory buffer)
static inline void ravel_array_set_length(void *array, size_t length)
{
    // TODO in glibc, check the allocated size (which is 8 bytes before the
    // memory buffer for a malloced buffer) and complain loudly if length
    // is not valid

    size_t *plength = (size_t*)((uint8_t*)array - sizeof(size_t));
    *plength = length;
}

#endif /* API_ARRAY_H */
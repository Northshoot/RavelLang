/**
 * array.c: Abstractions over arrays
 *
 */

#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include <api/array.h>

static char empty;

void *
ravel_array_new(size_t length, size_t element_size)
{
    void *buffer;

    if (length == 0)
        return &empty;

    if (length * element_size < length)
        return NULL;
    buffer = malloc(length * element_size + sizeof(size_t));
    if (buffer == NULL)
        return NULL;
    *((size_t*)buffer) = length;

    return ((uint8_t*)buffer + sizeof(size_t));
}

void
ravel_array_free(void *array)
{
    if (array == NULL || array == &empty)
        return;
    free((uint8_t*)array - sizeof(size_t));
}

size_t
ravel_array_length(void *array)
{
    size_t *plength;

    if (array == NULL || array == &empty)
        return 0;
    plength = (size_t*)((uint8_t*)array - sizeof(size_t));
    return *plength;
}

// UNSAFE! Sets the length that will be returned by ravel_array_length()
// (which is visible to Ravel code)
//
// It must be less than the maximum length (which is the size of the overall
// memory buffer)
void
ravel_array_set_length(void *array, size_t length)
{
    // TODO in glibc, check the allocated size (which is 8 bytes before the
    // memory buffer for a malloced buffer) and complain loudly if length
    // is not valid

    assert (array);
    assert (array != &empty);

    size_t *plength = (size_t*)((uint8_t*)array - sizeof(size_t));
    *plength = length;
}
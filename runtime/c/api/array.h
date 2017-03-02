/**
 * array.h: Abstractions over arrays
 *
 */

#ifndef API_ARRAY_H
#define API_ARRAY_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

void *ravel_array_new(size_t length, size_t element_size);
void ravel_array_free(void *array);
size_t ravel_array_length(void *array);

void ravel_array_set_length(void *array, size_t length);

#endif /* API_ARRAY_H */
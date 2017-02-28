/**
 * intrinsics.h: wrappers over special operations in the Ravel language
 *
 */

#ifndef API_INTRINSICS_H
#define API_INTRINSICS_H

#include <stdint.h>
#include <stdlib.h>
#include <string.h>

#include "array.h"

static inline int32_t ravel_intrinsic_array_length(void *array) {
    return (int32_t)ravel_array_length(array);
}

static inline int32_t ravel_intrinsic_extract_uint16(uint8_t *array, int32_t pos) {
    // Little endian!!

    uint8_t low = array[pos];
    uint8_t high = array[pos+1];

    return (high << 8) | low;
}

static inline int32_t ravel_intrinsic_extract_int32(uint8_t *array, int32_t pos) {
    // Big endian!!

    uint8_t one = array[pos];
    uint8_t two = array[pos+1];
    uint8_t three = array[pos+2];
    uint8_t four = array[pos+3];

    return (one << 24) | (two << 16) | (three << 8) | four;
}

static inline char *ravel_intrinsic_extract_str(uint8_t *array, int32_t pos, int32_t length) {
    char *buffer = malloc(length+1);
    if (buffer == NULL)
        return NULL;
    buffer[length] = 0;
    strncpy(buffer, (char*)(array + pos), length);
    return buffer;
}

#endif /* API_INTRINSIC_H */
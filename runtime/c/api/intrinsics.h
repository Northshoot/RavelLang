/**
 * intrinsics.h: wrappers over special operations in the Ravel language
 *
 */

#ifndef API_INTRINSICS_H
#define API_INTRINSICS_H

#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#include "array.h"
#include "keys.h"
#include "context.h"
#include "system.h"
#include "base_dispatcher.h"
#include "endpoint.h"

#define ravel_container_of(ptr, type, member) ({             \
     const typeof( ((type *)0)->member ) *__mptr = (ptr);    \
     (type *)( (char *)__mptr - offsetof(type,member) );})

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

static inline void ravel_intrinsic_write_int32(uint8_t *array, int32_t pos, int32_t value) {
    // Big endian!!

    array[pos] = value >> 24;
    array[pos+1] = value >> 16;
    array[pos+2] = value >> 8;
    array[pos+3] = value;
}

static inline char *ravel_intrinsic_extract_str(uint8_t *array, int32_t pos, int32_t length) {
    char *buffer = malloc(length+1);
    if (buffer == NULL)
        return NULL;
    buffer[length] = 0;
    strncpy(buffer, (char*)(array + pos), length);
    return buffer;
}

static inline RavelError ravel_intrinsic_extract_error_msg(uint8_t *array, int32_t pos) {
    return ravel_intrinsic_extract_int32(array, pos);
}

static inline uint8_t ravel_intrinsic_extract_byte(uint8_t *array, int32_t pos) {
    return array[pos];
}

static inline RavelKey *ravel_intrinsic_load_key(RavelSystemAPI *sys_api, int32_t key_id) {
    RavelBaseDispatcher *dispatcher = ravel_container_of(sys_api, RavelBaseDispatcher, sys_api);
    RavelKeyProvider *key_provider = ravel_base_dispatcher_get_key_provider(dispatcher);

    RavelKey *key = ravel_key_provider_load_key(key_provider, key_id);
    if (key == NULL)
        errno = EPERM;
    return key;
}

static inline const char *ravel_intrinsic_endpoint_get_name(RavelEndpoint *endpoint) {
    return ravel_endpoint_get_name(endpoint);
}

bool ravel_intrinsic_verify_mac(uint8_t *data, int32_t endofdata, int32_t macoffset, RavelKey *key);
void ravel_intrinsic_apply_mac(uint8_t *data, int32_t endofdata, int32_t writeOffset, RavelKey *key);
void ravel_intrinsic_encrypt(uint8_t *data, int32_t offset, int32_t length, RavelKey *key);
void ravel_intrinsic_decrypt(uint8_t *data, int32_t offset, int32_t length, RavelKey *key);

#endif /* API_INTRINSIC_H */
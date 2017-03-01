/**
 * keys.h: Keys and key management
 *
 */

#ifndef API_KEYS_H
#define API_KEYS_H

#include <stdlib.h>
#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

typedef struct {
    uint32_t key_id;

    size_t length;
    uint8_t *buffer;
} RavelKey;

void ravel_key_init(RavelKey *self, size_t length, uint8_t *buffer);
void ravel_key_finalize(RavelKey *self);

#define RAVEL_KEY_PROVIDER_TABLE_SIZE 10

typedef struct {
    RavelKey keys[RAVEL_KEY_PROVIDER_TABLE_SIZE];
} RavelKeyProvider;

void ravel_key_provider_init(RavelKeyProvider *self);
void ravel_key_provider_finalize(RavelKeyProvider *self);

RavelKey *ravel_key_provider_load_key(RavelKeyProvider *self, uint32_t key_id);

#endif /* API_KEYS_H */
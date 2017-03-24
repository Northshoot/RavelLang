/**
 * key-provider.c: Keys and key management
 *
 */

#include <stdlib.h>
#include <stddef.h>
#include <string.h>

#include <api/keys.h>
#include <api/system.h>

void
ravel_key_init(RavelKey *self, uint32_t key_id, size_t length, uint8_t *buffer)
{
    ravel_system_print_number(NULL, "ravel_key_init ", key_id);

    self->key_id = key_id;

    self->buffer = buffer;
    self->length = length;
}

void
ravel_key_finalize(RavelKey *self)
{
    free((uint8_t*)self->buffer);
    self->buffer = NULL;
}

void
ravel_key_provider_init(RavelKeyProvider *self)
{
    size_t i;

    memset(&self->keys, 0, sizeof(RavelKey) * RAVEL_KEY_PROVIDER_TABLE_SIZE);

    for (i = 0; i < RAVEL_KEY_PROVIDER_TABLE_SIZE; i++)
        self->keys[i].key_id = -1;
}

void
ravel_key_provider_finalize(RavelKeyProvider *self)
{
    size_t i;

    for (i = 0; i < RAVEL_KEY_PROVIDER_TABLE_SIZE; i++)
        ravel_key_finalize(&self->keys[i]);
}

RavelKey *
ravel_key_provider_load_key(RavelKeyProvider *self, uint32_t key_id)
{
    size_t i;

    for (i = 0; i < RAVEL_KEY_PROVIDER_TABLE_SIZE; i++) {
        RavelKey *key = &self->keys[(i + key_id) % RAVEL_KEY_PROVIDER_TABLE_SIZE];
        if (key->key_id == key_id) {
            return key;
        }
        if (key->key_id == -1)
            break;
    }
    return NULL;
}

bool
ravel_key_provider_add_key(RavelKeyProvider *self, uint32_t key_id, size_t length, uint8_t *buffer)
{
    size_t i;

    for (i = 0; i < RAVEL_KEY_PROVIDER_TABLE_SIZE; i++) {
        RavelKey *key = &self->keys[(i + key_id) % RAVEL_KEY_PROVIDER_TABLE_SIZE];
        if (key->key_id == key_id) {
            ravel_key_finalize(key);
            ravel_key_init(key, key_id, length, buffer);
            return true;
        }
        if (key->key_id == -1) {
            ravel_key_init(key, key_id, length, buffer);
            return true;
        }
    }

    return false;
}

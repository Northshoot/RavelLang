#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "api/keys.h"
#include "api/crypto.h"

#include <wolfssl/options.h>
#define WOLFSSL_AES_COUNTER
#define WOLFSSL_AES_DIRECT
#include <wolfssl/wolfcrypt/aes.h>
#include <wolfssl/wolfcrypt/sha256.h>

#define USE_WOLF_SHA

#ifndef USE_WOLF_SHA
#include "sha256.h"
typedef SHA256_CTX sha256_context_t;
#else
typedef Sha256 sha256_context_t;
static void sha256_init(sha256_context_t *ctx)
{
    wc_InitSha256(ctx);
}
static void sha256_update(sha256_context_t *ctx, const uint8_t data[], size_t len)
{
    wc_Sha256Update(ctx, data, len);
}
static void sha256_final(sha256_context_t *ctx, uint8_t hash[])
{
    wc_Sha256Final(ctx, hash);
}
#endif

#define IV_SIZE 8
#define FULL_IV_SIZE 16
#define CIPHER_BLOCK_SIZE 16
#define AES_KEY_SIZE 16
#define MAC_SIZE 16
#define FULL_MAC_SIZE 32
#define MAC_KEY_SIZE 64

#define IPAD 0x36
#define OPAD 0x5C

#ifndef SHA256_BLOCK_SIZE
#define SHA256_BLOCK_SIZE 64
#endif
#define SHA256_OUTPUT_SIZE 32


static void reverse_bytes(uint8_t *bytes, size_t length)
{
    size_t i;

    for (i = 0; i < length/2; i++) {
        uint8_t tmp = bytes[i];
        bytes[i] = bytes[length-1-i];
        bytes[length-1-i] = tmp;
    }
}

static void
hmac_sha256(uint8_t *data, size_t length, const uint8_t *key, uint8_t *output, size_t output_length)
{
    uint8_t sha256_block[SHA256_BLOCK_SIZE];
    uint8_t sha256_output[SHA256_OUTPUT_SIZE];
    size_t i;
    sha256_context_t inner_hash, outer_hash;

    sha256_init(&inner_hash);
    sha256_init(&outer_hash);

    for (i = 0; i < SHA256_BLOCK_SIZE; i++)
        sha256_block[i] = key[i] ^ OPAD;
    sha256_update (&outer_hash, sha256_block, SHA256_BLOCK_SIZE);

    for (i = 0; i < SHA256_BLOCK_SIZE; i++)
        sha256_block[i] = key[i] ^ IPAD;
    sha256_update (&inner_hash, sha256_block, SHA256_BLOCK_SIZE);
    sha256_update (&inner_hash, data, length);

    sha256_final (&inner_hash, sha256_output);
    //reverse_bytes (sha256_output, SHA256_OUTPUT_SIZE);
    sha256_update (&outer_hash, sha256_output, SHA256_OUTPUT_SIZE);
    sha256_final (&outer_hash, sha256_output);
    //reverse_bytes (sha256_output, SHA256_OUTPUT_SIZE);

    memcpy(output, sha256_output, output_length);
}


static void
gen_data(uint8_t *into, size_t length) {
    size_t i;

    for (i = 0; i < length; i++) {
        into[i] = rand();
    }
}

static const uint8_t key_buffer[64] = {
    0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
    0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
    0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
    0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

static void test_hmac() {
    RavelKey key = { .key_id = 0, .buffer = key_buffer, .length = sizeof(key_buffer) };
    uint8_t *buffer = malloc(1024 + 16);
    uint8_t mac_1[16], *mac_2;
    int i, j;

    static const uint8_t first_packet[] = { 3, 1, 0, 0, 0, 0, 42 };

    memcpy(buffer, first_packet, sizeof(first_packet));
    ravel_crypto_apply_mac (buffer, sizeof(first_packet), sizeof(first_packet), &key);
    fprintf (stderr, "\nWolfcrypt: ");
    for (j = 0; j < 16; j++) {
        fprintf(stderr, "%x", buffer[sizeof(first_packet) + j]);
    }
    hmac_sha256 (buffer, sizeof (first_packet), key.buffer, mac_1, 16);
    fprintf (stderr, "\nOur: ");
    for (j = 0; j < 16; j++) {
        fprintf(stderr, "%x", mac_1[j]);
    }
    fprintf (stderr, "\n");

    for (i = 0; i < 100000; i++) {
        gen_data(buffer, 1024);

        // first the wolfssl implementation
        ravel_crypto_apply_mac (buffer, 1024, 1024, &key);
        mac_2 = buffer+1024;

        // then our implementation
        hmac_sha256 (buffer, 1024, key.buffer, mac_1, 16);

        if (memcmp(mac_1, mac_2, 16) != 0) {
            fprintf(stderr, "Found problem!\nBuffer: ");
            for (j = 0; j < 1024; j++) {
                fprintf(stderr, "%x", buffer[j]);
            }
            fprintf (stderr, "\nWolfcrypt: ");
            for (j = 0; j < 16; j++) {
                fprintf(stderr, "%x", mac_2[j]);
            }
            fprintf (stderr, "\nOur: ");
            for (j = 0; j < 16; j++) {
                fprintf(stderr, "%x", mac_1[j]);
            }
            fprintf (stderr, "\n");
            break;
        }
    }

    free(buffer);
}

static void test_sha() {
    uint8_t *buffer = malloc(1024 + 16);
    uint8_t hash_1[32], hash_2[32];
    int i, j;

    for (i = 0; i < 100000; i++) {
        gen_data(buffer, 1024);

        // first the wolfssl implementation
        Sha256 sha;
        wc_InitSha256(&sha);
        wc_Sha256Update(&sha, buffer, 1024);
        wc_Sha256Final(&sha, hash_1);

        // then our implementation
        sha256_context_t lib_hash;
        sha256_init(&lib_hash);
        sha256_update (&lib_hash, buffer, 1024);
        sha256_final(&lib_hash, hash_2);

        if (memcmp(hash_1, hash_2, 32) != 0) {
            fprintf(stderr, "Found problem!\nBuffer: ");
            for (j = 0; j < 1024; j++) {
                fprintf(stderr, "%x", buffer[j]);
            }
            fprintf (stderr, "\nWolfcrypt: ");
            for (j = 0; j < 32; j++) {
                fprintf(stderr, "%x", hash_1[j]);
            }
            fprintf (stderr, "\nLib: ");
            for (j = 0; j < 32; j++) {
                fprintf(stderr, "%x", hash_2[j]);
            }
            fprintf (stderr, "\n");
            break;
        }
    }

    free(buffer);
}

int main() {
    fprintf(stderr, "Test SHA\n");
    test_sha();
    fprintf(stderr, "Test HMAC\n");
    test_hmac();
}


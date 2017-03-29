//#include "api/crypto.h"

#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#include "api/keys.h"
#include "api/crypto.h"

#include "nrf_soc.h"
#include "nrf52_network.h"
#include "nrf52_log.h"
#include "nrf52_config.h"
#include "nrf_drv_rng.h"
#include <sha256.h>

#define NRF_LOG_MODULE_NAME "CRYPT"
#define NRF_LOG_LEVEL 4
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#define IV_SIZE 8
#define FULL_IV_SIZE 16
#define CIPHER_BLOCK_SIZE 16
#define AES_KEY_SIZE 16
#define MAC_SIZE 16
#define FULL_MAC_SIZE 32
#define MAC_KEY_SIZE 64

#define IPAD 0x36
#define OPAD 0x5C

#define SHA256_BLOCK_SIZE 64
#define SHA256_OUTPUT_SIZE 32

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

    sha256_final (&inner_hash, sha256_output, 0);
    sha256_update (&outer_hash, sha256_output, SHA256_OUTPUT_SIZE);
    sha256_final (&outer_hash, sha256_output, 0);

    memcpy(output, sha256_output, output_length);
}

static bool
constant_time_memcmp(uint8_t *one, uint8_t *two, size_t length)
{
    size_t i = 0;
    int different = 0;

    for (i = 0; i < length; i++)
        different |= (one[i] != two[i]);

    return different;
}

bool
ravel_crypto_verify_mac(uint8_t *data, int32_t endofdata, int32_t macoffset, RavelKey *key)
{
    uint8_t local_mac[MAC_SIZE];

    hmac_sha256 (data, endofdata, key->buffer, local_mac, MAC_SIZE);
    return !constant_time_memcmp (local_mac, data + macoffset, MAC_SIZE);
}

void ravel_crypto_apply_mac(uint8_t *data, int32_t endofdata, int32_t writeOffset, RavelKey *key)
{
    hmac_sha256 (data, endofdata, key->buffer, data + writeOffset, MAC_SIZE);
}


static void AES_encrypt(uint8_t *in, uint8_t *out, const uint8_t* aes_key) {
	nrf_ecb_hal_data_t datain = {
        .key        = { 0 },
        .cleartext  = { 0 },
        .ciphertext = { 0 }
    };

    /* set the key and the clear text */
	memcpy(datain.key,       aes_key, AES_KEY_SIZE);
	memcpy(datain.cleartext, in,      CIPHER_BLOCK_SIZE);

    uint32_t err;
    /* do the actual encryption */
	err = sd_ecb_block_encrypt(&datain);
	if(err != NRF_SUCCESS)
	    NRF_LOG_DEBUG("Error encrypting");

    /* read the ciphertext */
	memcpy(out, datain.ciphertext, CIPHER_BLOCK_SIZE);
}

static void increment_iv(uint8_t *iv)
{
    size_t n = FULL_IV_SIZE - 1;
    while ((n >= 0) && (++iv[n] == 0)) {
        n--;
    }
}

static void
counter_mode(uint8_t *data, size_t length, const uint8_t *key)
{
    uint8_t iv[FULL_IV_SIZE];
    size_t done = 0;
    uint8_t encrypted[CIPHER_BLOCK_SIZE];
    size_t i = 0;

    memcpy(iv, data, IV_SIZE);
    memset(iv + IV_SIZE, 0, IV_SIZE);

    length -= IV_SIZE;

    while (done < length) {
        AES_encrypt(iv, encrypted, key);
        if (length - done >= CIPHER_BLOCK_SIZE) {
            for (i = 0; i < CIPHER_BLOCK_SIZE; i++)
                data[IV_SIZE + done + i] ^= encrypted[i];
            increment_iv(iv);
            //NRF_LOG_HEXDUMP_DEBUG(iv+IV_SIZE, IV_SIZE);

            done += CIPHER_BLOCK_SIZE;
        } else {
            for (i = 0; i < length - done; i++)
                data[IV_SIZE + done + i] ^= encrypted[i];
            done = length;
        }
    }
}

void ravel_crypto_encrypt(uint8_t *data, int32_t offset, int32_t length, RavelKey *key)
{
    counter_mode(data+offset, length, key->buffer);
}

void ravel_crypto_decrypt(uint8_t *data, int32_t offset, int32_t length, RavelKey *key)
{
    //NRF_LOG_DEBUG("crypto_decrypt %d %d %d\r\n", offset, length, key->key_id);

    counter_mode(data+offset, length, key->buffer);
    memmove(data+offset, data+offset+IV_SIZE, length-IV_SIZE);

    NRF_LOG_HEXDUMP_DEBUG(data+offset, length-IV_SIZE);
}

void ravel_crypto_array_fill_random(uint8_t *array, int32_t offset, int32_t length)
{
    nrf_drv_rng_block_rand(array + offset, length);
}


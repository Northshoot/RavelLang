/**
 * crypto-wolfssl.c: Crypto implementation using wolfssl
 *
 */

#include <stdlib.h>
#include <stddef.h>
#include <string.h>
#include <assert.h>

#include <api/keys.h>
#include <api/crypto.h>

#include <wolfssl/wolfcrypt/aes.h>
#include <wolfssl/wolfcrypt/hmac.h>

#define IV_SIZE 16
#define CIPHER_BLOCK_SIZE 16
#define MAC_SIZE 16

bool
ravel_crypto_verify_mac(uint8_t *data, int32_t endofdata, int32_t macoffset, RavelKey *key)
{
    Hmac hmac;
    int res;
    uint8_t mac[MAC_SIZE];

    res = wc_HmacSetKey(&hmac, SHA256, key->buffer, key->length);
    assert (res == 0);

    res = wc_HmacUpdate(&hmac, data, endofdata);
    assert (res == 0);

    res = wc_HmacFinal(&hmac, mac);
    assert (res == 0);

    return memcmp(data + macoffset, mac, MAC_SIZE) == 0;
}

void
ravel_crypto_apply_mac(uint8_t *data, int32_t endofdata, int32_t writeOffset, RavelKey *key)
{
    Hmac hmac;
    int res;

    res = wc_HmacSetKey(&hmac, SHA256, key->buffer, key->length);
    assert (res == 0);

    res = wc_HmacUpdate(&hmac, data, endofdata);
    assert (res == 0);

    res = wc_HmacFinal(&hmac, data + writeOffset);
    assert (res == 0);
}

void
ravel_crypto_encrypt(uint8_t *data, int32_t offset, int32_t length, RavelKey *key)
{
    Aes aes;
    int res;
    uint8_t *encrypted;

    assert((length - IV_SIZE) % CIPHER_BLOCK_SIZE == 0);
    encrypted = malloc(length - IV_SIZE);
    if (encrypted == NULL) abort();

    res = wc_AesSetKey(&aes, key->buffer, key->length, data + offset, AES_ENCRYPTION);
    assert (res == 0);

    res = wc_AesCbcEncrypt(&aes, encrypted, data + offset + IV_SIZE, length - IV_SIZE);
    assert (res == 0);
    memcpy(data + offset + IV_SIZE, encrypted, length - IV_SIZE);
    free(encrypted);
}

void
ravel_crypto_decrypt(uint8_t *data, int32_t offset, int32_t length, RavelKey *key)
{
    Aes aes;
    int res;
    uint8_t *decrypted;

    assert((length - IV_SIZE) % CIPHER_BLOCK_SIZE == 0);
    decrypted = malloc(length - IV_SIZE);
    if (decrypted == NULL) abort();

    res = wc_AesSetKey(&aes, key->buffer, key->length, data + offset, AES_DECRYPTION);
    assert (res == 0);

    res = wc_AesCbcDecrypt(&aes, decrypted, data + offset + IV_SIZE, length - IV_SIZE);
    assert (res == 0);
    memcpy(data + offset + IV_SIZE, decrypted, length - IV_SIZE);
    free(decrypted);
}
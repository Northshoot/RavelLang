/**
 * crypto.h: Platform crypto API
 *
 */

#ifndef API_CRYPTO_H
#define API_CRYPTO_H

#include "keys.h"

bool ravel_crypto_verify_mac(uint8_t *data, int32_t endofdata, int32_t macoffset, RavelKey *key);
void ravel_crypto_apply_mac(uint8_t *data, int32_t endofdata, int32_t writeOffset, RavelKey *key);
void ravel_crypto_encrypt(uint8_t *data, int32_t offset, int32_t length, RavelKey *key);
void ravel_crypto_decrypt(uint8_t *data, int32_t offset, int32_t length, RavelKey *key);

#endif /* API_CRYPTO_H */
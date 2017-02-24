/*------------------------------------------------------------------------
 / OCB Version 3 Reference Code (Unoptimized C)   Last modified 12-JUN-2013
 /-------------------------------------------------------------------------
 / Copyright (c) 2013 Ted Krovetz.
 /
 / Permission to use, copy, modify, and/or distribute this software for any
 / purpose with or without fee is hereby granted, provided that the above
 / copyright notice and this permission notice appear in all copies.
 /
 / THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 / WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 / MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 / ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 / WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 / ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 / OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 /
 / Phillip Rogaway holds patents relevant to OCB. See the following for
 / his free patent grant: http://www.cs.ucdavis.edu/~rogaway/ocb/grant.htm
 /
 / Comments are welcome: Ted Krovetz <ted@krovetz.net>
 /------------------------------------------------------------------------- */

#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#include "nrf_soc.h"
#include "network.h"
#include "log.h"
#include "config.h"

#include "encryption.h"

/* Encrypt a block with AES */
static inline void AES_encrypt(block in, block out, uint8_t* aes_key) {
	nrf_ecb_hal_data_t datain = {
        .key        = { 0 },
        .cleartext  = { 0 },
        .ciphertext = { 0 }
    };

    /* set the key and the clear text */
	memcpy(datain.key,       aes_key, KEYBYTES);
	memcpy(datain.cleartext, in,      BLOCKBYTES);

    /* do the actual encryption */
	sd_ecb_block_encrypt(&datain);

    /* read the ciphertext */
	memcpy(out, datain.ciphertext, BLOCKBYTES);
}

/* xor a two blocks together */
static void xor_block(block out, block s1, block s2) {
	unsigned i;
	for (i = 0; i < 16; i++) {
		out[i] = s1[i] ^ s2[i];
    }
}

/* perform the "doubling" operation on a block */
static void double_block(block d, block s) {
	unsigned i;
	uint8_t tmp = s[0];
	for (i = 0; i < 15; i++) {
		d[i] = (s[i] << 1) | (s[i + 1] >> 7);
    }
	d[15] = (s[15] << 1) ^ ((tmp >> 7) * 135);
}

/* perform the "L[i]" operation on a block */
static void calc_L_i(block l, block ldollar, unsigned i) {
	double_block(l, ldollar); /* l is now L_0               */
	for (; (i & 1) == 0; i >>= 1) {
		double_block(l, l); /* double for each trailing 0 */
    }
}

/* */
static
void hash(
    block result,
    uint8_t *k,
    uint8_t *a,
    unsigned abytes)
{
	block lstar, ldollar, offset, sum, tmp;
	unsigned i;

	/* Key-dependent variables */

	/* L_* = ENCIPHER(K, zeros(128)) */
	memset(tmp, 0, 16);
	AES_encrypt(tmp, lstar, k);

	/* L_$ = double(L_*) */
	double_block(ldollar, lstar);

	/* Process any whole blocks */

	/* Sum_0 = zeros(128) */
	memset(sum, 0, 16);
	/* Offset_0 = zeros(128) */
	memset(offset, 0, 16);
	for (i = 1; i <= abytes / 16; i++, a = a + 16) {
		/* Offset_i = Offset_{i-1} xor L_{ntz(i)} */
		calc_L_i(tmp, ldollar, i);
		xor_block(offset, offset, tmp);
		/* Sum_i = Sum_{i-1} xor ENCIPHER(K, A_i xor Offset_i) */
		xor_block(tmp, offset, a);
		AES_encrypt(tmp, tmp, k);
		xor_block(sum, sum, tmp);
	}

	/* Process any final partial block; compute final hash value */

	abytes = abytes % 16; /* Bytes in final block */
	if (abytes > 0) {
		/* Offset_* = Offset_m xor L_* */
		xor_block(offset, offset, lstar);
		/* tmp = (A_* || 1 || zeros(127-bitlen(A_*))) xor Offset_* */
		memset(tmp, 0, 16);
		memcpy(tmp, a, abytes);
		tmp[abytes] = 0x80;
		xor_block(tmp, offset, tmp);
		/* Sum = Sum_m xor ENCIPHER(K, tmp) */
		AES_encrypt(tmp, tmp, k);
		xor_block(sum, tmp, sum);
	}

	memcpy(result, sum, 16);
}


/* ocb encrypt */
static
int ocb_crypt(
    uint8_t *out,
    uint8_t *k,
    uint8_t *n,
    uint8_t *a,
	unsigned abytes,
    uint8_t *in,
    unsigned inbytes)
{
	block lstar, ldollar, sum, offset, ktop, pad, nonce, tag, tmp;
	uint8_t stretch[24];
	unsigned bottom, byteshift, bitshift, i;

	/* L_* = ENCIPHER(K, zeros(128)) */
	memset(tmp, 0, 16);
	AES_encrypt(tmp, lstar, k);

	/* L_$ = double(L_*) */
	double_block(ldollar, lstar);

	/* Nonce-dependent and per-encryption variables */

	/* Nonce = zeros(127-bitlen(N)) || 1 || N */
	memset(nonce, 0, 16);
	memcpy(&nonce[16 - NONCEBYTES], n, NONCEBYTES);
	nonce[0] = (uint8_t) (((TAGBYTES * 8) % 128) << 1);
	nonce[16 - NONCEBYTES - 1] |= 0x01;
	/* bottom = str2num(Nonce[123..128]) */
	bottom = nonce[15] & 0x3F;
	/* Ktop = ENCIPHER(K, Nonce[1..122] || zeros(6)) */
	nonce[15] &= 0xC0;
	AES_encrypt(nonce, ktop, k);
	/* Stretch = Ktop || (Ktop[1..64] xor Ktop[9..72]) */
	memcpy(stretch, ktop, 16);
	memcpy(tmp, &ktop[1], 8);
	xor_block(tmp, tmp, ktop);
	memcpy(&stretch[16], tmp, 8);
	/* Offset_0 = Stretch[1+bottom..128+bottom] */
	byteshift = bottom / 8;
	bitshift = bottom % 8;
	if (bitshift != 0) {
		for (i = 0; i < 16; i++) {
			offset[i] = (stretch[i + byteshift] << bitshift)
					  | (stretch[i + byteshift + 1] >> (8 - bitshift));
        }
    } else {
		for (i = 0; i < 16; i++) {
			offset[i] = stretch[i + byteshift];
        }
    }

	/* Checksum_0 = zeros(128) */
	memset(sum, 0, 16);

	/* Process any whole blocks */

	for (i = 1; i <= inbytes / 16; i++, in = in + 16, out = out + 16) {
		/* Offset_i = Offset_{i-1} xor L_{ntz(i)} */
		calc_L_i(tmp, ldollar, i);
		xor_block(offset, offset, tmp);

		xor_block(tmp, offset, in);

        /* P_i = Offset_i xor DECIPHER(K, C_i xor Offset_i) */
        AES_encrypt(tmp, tmp, k);
        xor_block(out, offset, tmp);
        /* Checksum_i = Checksum_{i-1} xor P_i */
        xor_block(sum, in, sum);
	}

	/* Process any final partial block and compute raw tag */

	inbytes = inbytes % 16; /* Bytes in final block */
	if (inbytes > 0) {
		/* Offset_* = Offset_m xor L_* */
		xor_block(offset, offset, lstar);
		/* Pad = ENCIPHER(K, Offset_*) */
		AES_encrypt(offset, pad, k);

        /* Checksum_* = Checksum_m xor (P_* || 1 || zeros(127-bitlen(P_*))) */
        memset(tmp, 0, 16);
        memcpy(tmp, in, inbytes);
        tmp[inbytes] = 0x80;
        xor_block(sum, tmp, sum);
        /* C_* = P_* xor Pad[1..bitlen(P_*)] */
        xor_block(pad, tmp, pad);
        memcpy(out, pad, inbytes);
        out = out + inbytes;
	}

	/* Tag = ENCIPHER(K, Checksum xor Offset xor L_$) xor HASH(K,A) */
	xor_block(tmp, sum, offset);
	xor_block(tmp, tmp, ldollar);
	AES_encrypt(tmp, tag, k);
	hash(tmp, k, a, abytes);
	xor_block(tag, tmp, tag);

    memcpy(out, tag, TAGBYTES);
    return 0;
}

uint16_t encrypt_buffer(
    uint8_t *ciphertext,
    uint8_t *plaintext,
    uint16_t plaintext_len,
    uint32_t seqno)
{
    uint8_t nonce[NONCEBYTES];
    memset(nonce, 0, NONCEBYTES);
    memcpy(nonce, &seqno, sizeof seqno);

    uint8_t *key = (uint8_t *)config_get_key();
    LOG("Encrypting with key %02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X",
        key[ 0], key[ 1], key[ 2], key[ 3],
        key[ 4], key[ 5], key[ 6], key[ 7],
        key[ 8], key[ 9], key[10], key[11],
        key[12], key[13], key[14], key[15]
    );

    ocb_crypt(ciphertext, key, nonce, NULL, 0, plaintext, plaintext_len);

    return plaintext_len + TAGBYTES;
}
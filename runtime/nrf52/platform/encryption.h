#ifndef ENCRYPTION_H
#define ENCRYPTION_H

#include <stdint.h>

#include "network.h"

#define KEYBYTES   (128/8)
#define NONCEBYTES ( 96/8)
#define TAGBYTES   (128/8)
#define BLOCKBYTES (128/8)

#if     !(KEYBYTES==16 || KEYBYTES==24 || KEYBYTES==32)
#error "KEYBYTES is an illegal value"

#elif   (NONCEBYTES > 15 || NONCEBYTES < 0)
#error "NONCEBYTES is an illegal value"

#elif   (TAGBYTES > 16 || TAGBYTES < 1)
#error "TAGBYTES is an illegal value"

#elif   (BLOCKBYTES != 16)
#error "BLOCKBYTES is an illegal value"

#endif

typedef uint8_t block[BLOCKBYTES];
uint16_t encrypt_buffer(
    uint8_t *ciphertext, 
    uint8_t *plaintext, 
    uint16_t plaintext_len,
    uint32_t seqno);
    
#endif
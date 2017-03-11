/**
 * ravel-packet.c: Implementation of RavelPacket
 *
 */

#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "api/packet.h"
#include "api/intrinsics.h"

#define SRC 0 // 8 bits for source
#define DST 1 // 8 bits for destination
#define FLAGS 2 // 8 bits for flags
#define RESERVED 3 // reserved for byte mapping

#define FLAG_PARTIAL 1
#define FLAG_LAST 2
#define FLAG_ACK 4
#define FLAG_SAVE_DONE 8

#define MIN_LENGTH RESERVED

/**
 * Packet mapping POST fragmentation
 */
void
ravel_packet_init_empty (RavelPacket *self, size_t record_size, int model_id, int record_id)
{
    self->packet_data = calloc(record_size + RESERVED, 1);
    if (self->packet_data == NULL) abort();
    self->record_data = self->packet_data + RESERVED;
    self->record_length = record_size;
    self->packet_length = self->record_length + RESERVED;

    self->model_id = model_id;
    self->record_id = record_id;
    self->is_ack = false;
    self->is_save_done = false;
}

void
ravel_packet_init_copy (RavelPacket *self, RavelPacket *from)
{
    *self = *from;

    self->packet_data = malloc(from->packet_length);
    if (self->packet_data == NULL) abort();
    if (from->record_length > 0) {
        memcpy(self->packet_data, from->packet_data, from->packet_length);
    }
    self->record_data = self->packet_data + RESERVED;
}

void
ravel_packet_init_from_record (RavelPacket *self, uint8_t *data, size_t length)
{
    self->packet_data = calloc(length + RESERVED, 1);
    if (self->packet_data == NULL) abort();
    self->record_data = self->packet_data + RESERVED;
    self->record_length = length;
    if (length > 0)
        memcpy(self->record_data, data, length);
    self->packet_length = self->record_length + RESERVED;

    self->model_id = ravel_intrinsic_extract_byte(self->record_data, 0);
    self->record_id = ravel_intrinsic_extract_byte(self->record_data, 1);
    self->is_ack = false;
    self->is_save_done = false;
}

void
ravel_packet_init_from_network (RavelPacket *self, uint8_t *data, size_t length)
{
    self->packet_data = calloc(length, 1);
    if (self->packet_data == NULL) abort();
    self->record_data = self->packet_data + RESERVED;
    self->record_length = length - RESERVED;
    self->packet_length = length;
    memcpy(self->packet_data, data, length);

    self->model_id = ravel_intrinsic_extract_byte(self->record_data, 0);
    self->record_id = ravel_intrinsic_extract_byte(self->record_data, 1);
    self->is_ack = self->packet_data[FLAGS] & FLAG_ACK;
    self->is_save_done = self->packet_data[FLAGS] & FLAG_SAVE_DONE;

    assert (!(self->is_ack && self->is_save_done));
}

static void
ravel_packet_init_control (RavelPacket *self, int model_id, int record_id, int flags)
{
    self->packet_data = calloc(2 + RESERVED, 1);
    if (self->packet_data == NULL) abort();
    self->record_data = self->packet_data + RESERVED;
    self->record_length = 2;
    self->packet_length = 2 + RESERVED;

    self->record_data[0] = model_id;
    self->record_data[1] = record_id;

    self->packet_data[FLAGS] = flags;

    self->model_id = model_id;
    self->record_id = record_id;
    self->is_ack = true;
}

void
ravel_packet_init_ack (RavelPacket *self, int model_id, int record_id)
{
    ravel_packet_init_control (self, model_id, record_id, FLAG_ACK);
}

void
ravel_packet_init_save_done (RavelPacket *self, int model_id, int record_id)
{
    ravel_packet_init_control (self, model_id, record_id, FLAG_SAVE_DONE);
}

void
ravel_packet_finalize (RavelPacket *self)
{
    free (self->packet_data);
}

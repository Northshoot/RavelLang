/**
 * ravel-packet.c: Implementation of RavelPacket
 *
 */

#include <stdlib.h>
#include <string.h>

#include <api/packet.h>
#include <api/intrinsics.h>

#define SRC 0 // 32 bits for source
#define DST 4 // 32 bits for destination
#define FLAGS 8 // 32 bits for flags
#define RESERVED 12 // reserved for byte mapping

#define FLAG_PARTIAL 1
#define FLAG_LAST 2
#define FLAG_ACK 4

#define MIN_LENGTH RESERVED

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
}

void
ravel_packet_init_copy (RavelPacket *self, RavelPacket *from)
{
    *self = *from;

    self->packet_data = malloc(from->record_length);
    if (self->packet_data == NULL) abort();
    if (from->record_length > 0) {
        memcpy(self->packet_data, from->packet_data, from->record_length);
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

    self->model_id = ravel_intrinsic_extract_int32(self->record_data, 0);
    self->record_id = ravel_intrinsic_extract_int32(self->record_data, 4);
    self->is_ack = false;
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

    self->model_id = ravel_intrinsic_extract_int32(self->record_data, 0);
    self->record_id = ravel_intrinsic_extract_int32(self->record_data, 4);
    self->is_ack = (*(uint32_t*)(self->packet_data + FLAGS)) & FLAG_ACK;
}

static inline void write_int32(uint8_t *buffer, int32_t value)
{
    // Big Endian!!

    buffer[0] = value >> 24;
    buffer[1] = value >> 16;
    buffer[2] = value >> 8;
    buffer[3] = value;
}

void
ravel_packet_init_ack (RavelPacket *self, int model_id, int record_id)
{
    uint32_t flags;

    self->packet_data = calloc(8 + RESERVED, 1);
    if (self->packet_data == NULL) abort();
    self->record_data = self->packet_data + RESERVED;
    self->record_length = 8;
    self->packet_length = 8 + RESERVED;

    write_int32(self->record_data, model_id);
    write_int32(self->record_data + 4, record_id);

    flags = FLAG_ACK;
    write_int32(self->packet_data + FLAGS, flags);

    self->model_id = model_id;
    self->record_id = record_id;
    self->is_ack = true;
}

void
ravel_packet_finalize (RavelPacket *self)
{
    free (self->packet_data);
}

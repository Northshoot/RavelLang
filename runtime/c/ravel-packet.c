/**
 * ravel-packet.c: Implementation of RavelPacket
 *
 */

#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "api/packet.h"
#include "api/intrinsics.h"
#include "api/system.h"

#define TIER 0 // 8 bits for tier ID
#define SRC 1 // 8 bits for source should be expanded later
#define DST 3 // 8 bits for destination
#define FLAGS 4 // 8 bits for flags
#define RESERVED 4 // reserved for byte mapping

#define FLAG_ACK 1
#define FLAG_SAVE_DONE 4
#define FLAG_DELETE 8

#define MIN_LENGTH RESERVED

/**
 * Packet mapping POST fragmentation
 */
//void
//ravel_packet_init_empty (RavelPacket *self, size_t record_size, uint8_t model_id, uint16_t record_id)
//{
//    self->packet_data = calloc(record_size + RESERVED, 1);
//    if (self->packet_data == NULL) abort();
//    self->record_data = self->packet_data + RESERVED;
//    self->record_length = record_size;
//    self->packet_length = self->record_length + RESERVED;
//
//    self->model_id = model_id;
//    self->record_id = record_id;
//    self->is_ack = false;
//    self->is_save_done = false;
//}

static int32_t pkt_counter = 0;

static void incr_counter() {
    pkt_counter++;
//    ravel_system_print_number(NULL, " allocated packet, now", pkt_counter);
}

static void decr_counter() {
    assert (pkt_counter > 0);
    pkt_counter--;
//    ravel_system_print_number(NULL, " freed packet, now", pkt_counter);
}

void
ravel_packet_init_copy (RavelPacket *self, RavelPacket *from)
{
    incr_counter();
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
    incr_counter();
    self->packet_data = calloc(length + RESERVED, 1);
    if (self->packet_data == NULL) abort();
    self->record_data = self->packet_data + RESERVED;
    self->record_length = length;
    if (length > 0)
        memcpy(self->record_data, data, length);
    self->packet_length = self->record_length + RESERVED;

    self->model_id = ravel_intrinsic_extract_byte(self->record_data, 0);
    self->record_id = ravel_intrinsic_extract_uint16(self->record_data, 1);
    self->is_ack = false;
    self->is_save_done = false;
    self->is_delete = false;
}

void
ravel_packet_init_from_network (RavelPacket *self, uint8_t *data, size_t length)
{
    incr_counter();
    self->packet_data = calloc(length, 1);
    if (self->packet_data == NULL) abort();
    self->record_data = self->packet_data + RESERVED;
    self->record_length = length - RESERVED;
    self->packet_length = length;
    memcpy(self->packet_data, data, length);

    self->model_id = ravel_intrinsic_extract_byte(self->record_data, 0);
    self->record_id = ravel_intrinsic_extract_uint16(self->record_data, 1);
    self->is_ack = self->packet_data[FLAGS] & FLAG_ACK;
    self->is_save_done = self->packet_data[FLAGS] & FLAG_SAVE_DONE;
    self->is_delete = self->packet_data[FLAGS] & FLAG_DELETE;

    assert (!(self->is_ack && self->is_save_done));
}

static void
ravel_packet_init_control (RavelPacket *self, uint8_t model_id, uint16_t record_id, int flags)
{
    incr_counter();
    self->packet_data = calloc(3 + RESERVED, 1);
    if (self->packet_data == NULL) abort();
    self->record_data = self->packet_data + RESERVED;
    self->record_length = 3;
    self->packet_length = 3 + RESERVED;

    self->record_data[0] = model_id;
    ravel_intrinsic_write_uint16 (self->record_data, 1, record_id);

    self->packet_data[FLAGS] = flags;

    self->model_id = model_id;
    self->record_id = record_id;
    self->is_ack = flags & FLAG_ACK;
    self->is_save_done = flags & FLAG_SAVE_DONE;
    self->is_delete = flags & FLAG_DELETE;
}

void
ravel_packet_init_ack (RavelPacket *self, uint8_t model_id, uint16_t record_id)
{
    ravel_packet_init_control (self, model_id, record_id, FLAG_ACK);
}

void
ravel_packet_init_save_done (RavelPacket *self, uint8_t model_id, uint16_t record_id)
{
    ravel_packet_init_control (self, model_id, record_id, FLAG_SAVE_DONE);
}

void
ravel_packet_init_delete (RavelPacket *self, uint8_t model_id, uint16_t record_id)
{
    ravel_packet_init_control (self, model_id, record_id, FLAG_DELETE);
}

void
ravel_packet_finalize (RavelPacket *self)
{
    decr_counter();
    free (self->packet_data);
}

void
ravel_packet_set_source_destination (RavelPacket *self, int32_t tier, int32_t source, int32_t destination)
{
    assert (tier >= 0 && tier < 256);
    assert (source >= 0 && source < 256);
    assert (destination >= 0 && destination < 256);
    self->packet_data[TIER] = tier;
    self->packet_data[SRC] = source;
    self->packet_data[DST] = destination;
}

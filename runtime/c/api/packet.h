/**
 * packet.h: Ravel network packets
 *
 */

#ifndef API_PACKET_H
#define API_PACKET_H

#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

typedef struct {
    uint8_t model_id;
    uint16_t record_id;

    bool is_ack;
    bool is_save_done;
    bool is_delete;

    uint8_t *packet_data;
    uint8_t *record_data;
    size_t record_length;
    size_t packet_length;
} RavelPacket;

void ravel_packet_init_empty (RavelPacket *self, size_t record_size, uint8_t model_id, uint16_t record_id);
void ravel_packet_init_ack (RavelPacket *self, uint8_t model_id, uint16_t record_id);
void ravel_packet_init_save_done (RavelPacket *self, uint8_t model_id, uint16_t record_id);
void ravel_packet_init_delete (RavelPacket *self, uint8_t model_id, uint16_t record_id);

void ravel_packet_init_from_record (RavelPacket *self, uint8_t *data, size_t length);
void ravel_packet_init_from_network (RavelPacket *self, uint8_t *data, size_t length);

void ravel_packet_init_copy (RavelPacket *self, RavelPacket *from);

static inline int32_t ravel_packet_get_source (RavelPacket *self) {
  return self->packet_data[0];
}
static inline int32_t ravel_packet_get_destination (RavelPacket *self) {
  return self->packet_data[1];
}
void ravel_packet_set_source_destination (RavelPacket *self, int32_t source, int32_t destination);

void ravel_packet_finalize (RavelPacket *self);

#endif /* API_PACKET_H */


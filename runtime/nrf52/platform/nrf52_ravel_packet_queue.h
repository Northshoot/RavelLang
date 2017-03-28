#ifndef NRF_RAVEL_PKT_H__
#define NRF_RAVEL_PKT_H__

#include <stdint.h>
#include <stdbool.h>
#include <api/packet.h>


#ifdef __cplusplus
extern "C" {
#endif




uint32_t nrf52_ravel_packet_queue_init(void);


uint32_t nrf52_ravel_packet_queue_finalize(void);

// enqueue Ravel Packet
uint32_t enqueue_ravel_packet(RavelPacket *pkt);

//dequeue Ravel Packet
uint32_t dequeue_ravel_packet(RavelPacket *pkt);

//returns size of queue
size_t size(void);

//returns available buffer
size_t available(void);

#ifdef __cplusplus
}
#endif

#endif //NRF_RAVEL_PKT_H__
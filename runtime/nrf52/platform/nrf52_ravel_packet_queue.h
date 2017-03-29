#ifndef NRF_RAVEL_PKT_H__
#define NRF_RAVEL_PKT_H__

#include <stdint.h>
#include <stdbool.h>
#include <api/packet.h>


#ifdef __cplusplus
extern "C" {
#endif


typedef struct RavelNetworkQueueData {
    struct RavelNetworkQueueData * next;
    struct RavelNetworkQueueData * previous;

    RavelPacket m_ravel_packet;
    RavelEndpoint *p_endpoint;
} RavelNetworkQueueData;

uint32_t nrf52_ravel_packet_queue_init(void);


uint32_t nrf52_ravel_packet_queue_finalize(void);

// enqueue Ravel Packet
RavelError enqueue_ravel_packet(const RavelNetworkQueueData m_network_q);

//dequeue Ravel Packet
bool dequeue_ravel_packet( RavelNetworkQueueData *p_network_q);

#ifdef __cplusplus
}
#endif

#endif //NRF_RAVEL_PKT_H__
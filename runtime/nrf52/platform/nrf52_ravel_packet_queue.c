#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>

#define NRF_LOG_MODULE_NAME "QUE"
#define NRF_LOG_LEVEL 4
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "nrf52_ravel_packet_queue.h"
#include "app_fifo.h"

#define FIFO_LENGTH(F) fifo_length(&F)              /**< Macro to calculate length of a FIFO. */

static __INLINE uint32_t fifo_length(app_fifo_t * const fifo)
{
  uint32_t tmp = fifo->read_pos;
  return fifo->write_pos - tmp;
}

#ifndef BUFF_MAX_SIZE
#define BUFF_MAX_SIZE        20 //can be set through make -DBUFF_MAX_SIZE=xxx
#endif

static RavelPacket           packet_buf[BUFF_MAX_SIZE];

bool m_full                 = false;
size_t m_current            = 0;
size_t m_sending            = 0;
// enqueue Ravel Packet

uint32_t nrf52_ravel_packet_queue_init()
{
    NRF_LOG_INFO("INIT!\r\n");
    return 0;
}
uint32_t nrf52_ravel_packet_queue_finalize(void)
{
    return 0;
}


uint32_t enqueue_ravel_packet(RavelPacket *pkt)
{
    NRF_LOG_DEBUG("enqueue_ravel_packet\r\n");
    if(m_full)
        return 1;

    //copy packet
    RavelPacket copy;
    ravel_packet_init_copy(&copy, pkt);
    //add to the buffer
    packet_buf[m_current] = copy;
    m_current++;
    if(m_current == BUFF_MAX_SIZE)
        m_full = true;

    return 0;
}

//dequeue Ravel Packet
uint32_t dequeue_ravel_packet(RavelPacket *pkt)
{
    NRF_LOG_DEBUG("dequeue_ravel_packett\r\n");
    //copy over packet from queue to the pointer
    pkt = &packet_buf[m_sending];
    ravel_packet_finalize(&packet_buf[m_sending]);
    if(m_sending == m_current)
        m_sending = m_current = 0;
}

//returns size of queue
size_t size(void){
    return BUFF_MAX_SIZE;
}

//returns available buffer
size_t available(void){
    return BUFF_MAX_SIZE - m_current;
}
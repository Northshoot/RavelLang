#include <stddef.h>
#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>
#include <assert.h>

#define NRF_LOG_MODULE_NAME "QUE"
#define NRF_LOG_LEVEL 4
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "api/context.h"
#include "nrf52_ravel_packet_queue.h"

// enqueue Ravel Packet

static RavelNetworkQueueData *queue_head;
static RavelNetworkQueueData *queue_tail;

uint32_t nrf52_ravel_packet_queue_init()
{
    NRF_LOG_INFO("INIT!\r\n");
    return 0;
}


uint32_t nrf52_ravel_packet_queue_finalize(void)
{
    RavelNetworkQueueData *iter, *next;

    for (iter = queue_head; iter; iter = next) {
        next = iter->next;
        ravel_packet_finalize(&iter->m_ravel_packet);
        free(iter);
    }
    queue_head = queue_tail = NULL;
    return 0;
}


RavelError enqueue_ravel_packet(const RavelNetworkQueueData m_network_q)
{
    RavelNetworkQueueData *copy;

    NRF_LOG_DEBUG("enqueue_ravel_packet\r\n");

    copy = calloc(sizeof(RavelNetworkQueueData), 1);
    if (copy == NULL)
        return RAVEL_ERROR_OUT_OF_STORAGE;

    memcpy(copy, &m_network_q, sizeof(RavelNetworkQueueData));

    if (queue_head == NULL) {
        assert (queue_tail == NULL);
        queue_head = queue_tail = copy;
        copy->next = copy->previous = NULL;
    } else {
        copy->next = queue_head;
        copy->previous = NULL;
        queue_head->previous = copy;
        queue_head = copy;
    }

    return RAVEL_ERROR_IN_TRANSIT;
}

//dequeue Ravel Packet
bool dequeue_ravel_packet( RavelNetworkQueueData *p_network_q)
{
    RavelNetworkQueueData *last;
    NRF_LOG_DEBUG("dequeue_ravel_packet\r\n");

    if (queue_tail == NULL) {
        assert (queue_head == NULL);
        return false;
    }

    last = queue_tail;
    if (last->previous == NULL) {
        queue_tail = queue_head = NULL;
    } else {
        last->previous->next = NULL;
        queue_tail = last->previous;
    }

    memcpy(p_network_q, last, sizeof(RavelNetworkQueueData));
    free(last);
    return true;
}
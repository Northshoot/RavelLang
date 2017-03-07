#ifndef QUEUE_H
#define QUEUE_H

#include <stdint.h>

#include "nrf52_network.h"

uint32_t queue_init(uint32_t,uint32_t);
uint32_t queue_enqueue(const data_packet_t *packet,uint32_t);
uint32_t queue_read_n(data_packet_t *packet, uint32_t n);
void queue_dequeue_n(uint32_t);

#endif

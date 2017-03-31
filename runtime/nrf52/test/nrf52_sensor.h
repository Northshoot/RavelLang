#ifndef SENSOR_H
#define SENSOR_H

#include <stdint.h>

#include "nrf52_network.h"

typedef struct {
    uint32_t timestamp;
    uint8_t  flow;
    uint8_t  temp;
} sensor_data_t;

#define NUM_DATA_PER_PACKET    3
typedef struct {
    uint8_t  timestamp_upper_byte;
    struct {
        uint32_t timestamp_lower_bytes : 24;
        uint8_t  flow;
        uint8_t  temp;
    } __attribute__((packed)) data[NUM_DATA_PER_PACKET];
} sensor_encoded_data_t;

extern uint32_t m_global_time;

void sensor_init(void);
void sensor_init_test(void);
void sensor_start(void);
uint32_t sensor_read_from_offset(sensor_data_t *data, uint32_t index, uint32_t stride);
uint32_t sensor_remove_up_to_index(uint32_t index, uint32_t stride);
uint32_t sensor_get_time(void);

#endif

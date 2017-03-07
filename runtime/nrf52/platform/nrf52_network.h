#ifndef NETWORK_H
#define NETWORK_H

#include <stdint.h>

#include "nrf52_sensor.h"

/*
 * Packet layouts
 */
#define APP_CFG_CHAR_READ_SIZE 20
typedef union {
    struct {
        uint32_t seqno;
        sensor_encoded_data_t payload;
    } s;

    uint8_t data[APP_CFG_CHAR_READ_SIZE];
} __attribute__((packed)) data_packet_t;

#define APP_CFG_CHAR_WRITE_SIZE 20
typedef union {
    struct {
        uint32_t ackno;
        uint8_t  mac[APP_CFG_CHAR_WRITE_SIZE - sizeof(uint32_t)];
    } s;

    uint8_t data[APP_CFG_CHAR_WRITE_SIZE];
} __attribute__((packed)) ack_packet_t;

/* sensor -> network */
void network_send(const uint8_t *data, uint16_t len);
//void network_init(volatile uint8_t *need_softdevice);

/* network <-> ble */
void network_on_connect(void);
uint32_t network_on_write(const uint8_t *data, uint16_t len);
uint32_t network_on_read_request(uint8_t *data, uint16_t *len);
void network_on_indicate(void);
void network_init(uint8_t ack_handle, uint8_t seq_handle,
    int old_data);

#endif
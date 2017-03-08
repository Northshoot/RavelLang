#ifndef NETWORK_H
#define NETWORK_H

#include <stdint.h>


#include "packet.h"
#include "endpoint.h"



/* driver -> network */
void network_send(RavelPacket *packet, RavelEndpoint *endpoint);

/* network -> ble */
void network_on_write(const uint8_t *data, uint16_t len);
void network_on_read_request(const uint8_t *data, uint16_t len);
//void network_on_send_done(const uint8_t *data, uint16_t len);
void network_on_send_done();
void network_on_indicate(void);
void network_on_notify(void);
void network_on_connected(void);
void network_on_disconnected(void);




/**@brief Ravel Dispatcher receive event. */
typedef void (*net_on_write_t) (const uint8_t *data, uint16_t len);
typedef void (*net_on_read_t) (const uint8_t *data, uint16_t len);
/**@brief Ravel Dispatcher send_done event. */
//typedef void (*net_data_send_done_t) (const uint8_t *data, uint16_t len);
typedef void (*net_data_send_done_t) ();

/**@brief Ravel Dispatcher connected. */
typedef void (*net_connected_t) (void);
/**@brief Ravel Dispatcher disconnected event. */
typedef void (*net_disconnected_t) (void);
/**@brief Ravel Network nofity  event. */
typedef void (*net_notify_enable_t) (void);
/**@brief Ravel Network nofity  event. */
typedef void (*net_indicate_enable_t) (void);

typedef struct {
    net_on_write_t          on_write;
    net_on_read_t           on_read;
    net_data_send_done_t    send_done;
    net_notify_enable_t     notify;
    net_indicate_enable_t   indicate_enabled;
    net_connected_t         connected;
    net_disconnected_t      disconnected;
} NetworkClb;

void nrf52_network_init(NetworkClb *self);

//
///*
// * Packet layouts
// * Fragmentation happens here,
// */
//
// typedef struct {
//        uint32_t seqno;
// } __attribute__((packed)) data_packet_t;}
////#define APP_CFG_CHAR_READ_SIZE 20
////typedef union {
////    struct {
////        uint32_t seqno;
////        sensor_encoded_data_t payload;
////    } s;
////
////    uint8_t data[APP_CFG_CHAR_READ_SIZE];
////} __attribute__((packed)) data_packet_t;
////
////#define APP_CFG_CHAR_WRITE_SIZE 20
////typedef union {
////    struct {
////        uint32_t ackno;
////        uint8_t  mac[APP_CFG_CHAR_WRITE_SIZE - sizeof(uint32_t)];
////    } s;
////
////    uint8_t data[APP_CFG_CHAR_WRITE_SIZE];
////} __attribute__((packed)) ack_packet_t;





#endif
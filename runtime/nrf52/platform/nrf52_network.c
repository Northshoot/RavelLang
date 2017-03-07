#include <stdint.h>
#include <string.h>

#include "nrf_error.h"

#include "nrf52_network.h"
#include "nrf52_sensor.h"
#include "nrf52_queue.h"
#include "nrf52_config.h"
#include "nrf52_hmac-sha1.h"
#include "nrf52_link_constants.c"

#include "nrf52_encryption.h"
#include "nrf52_log.h"
#include "nrf52_flash.h"
#include "nrf_soc.h"

/* total number of packets seen so far */
static uint8_t ackno_code;
static uint8_t store_code;
static uint32_t m_packet_count = 0;

static uint32_t m_ackno = 0;
static uint32_t m_seqno = 0;

static
uint32_t network_enqueue(
    const uint8_t *data,
          uint16_t len)
{
    uint32_t err_code;
    uint8_t buffer[4];

    data_packet_t packet;
    for (int i = 0; i < len; i += sizeof packet.s.payload) {
        /* check to make sure there's data to packetize */
        if (i + sizeof packet.s.payload > len) {
            LOG("NETWORK - not enough data to packetize. This is a bug!");
            LOG("NETWORK - dropping %u bytes of data", len - i);
            return NRF_ERROR_INVALID_STATE;
        }

        /* convert the data in the queue to a data packet */
        memcpy(&packet.s.payload, &data[i], sizeof packet.s.payload);
        packet.s.seqno = m_packet_count;
        LOG("network enqueue SEQUENCE NUMBER %u ",packet.s.seqno);

        /* push the data to non volatile memory */
        err_code = queue_enqueue(&packet, m_packet_count);
        if (err_code != NRF_SUCCESS) {
            LOG("NETWORK - could not place packet in permanant storage");
            return err_code;
        }

        /* if we succeeded in sending the packet, increment the packet count */
        m_packet_count++;
        LOG("Storing m_packet_count %u ackno %u",m_packet_count,m_ackno);
        memcpy(buffer, &m_packet_count, sizeof m_packet_count);
        flash_write(store_code, buffer);
    }
    config_persist(sensor_get_time());

    return NRF_SUCCESS;
}

void network_init(uint8_t ack_code, uint8_t str_code,
    int old_data){

    ackno_code = ack_code;
    store_code = str_code;

    if (old_data > 0){
        uint8_t buffer[4];
        flash_read(ackno_code,buffer);
        memcpy(&m_ackno, buffer, sizeof m_ackno);
        LOG("Reading ackno %u",m_ackno);
        flash_read(store_code, buffer);
        memcpy(&m_packet_count, buffer, sizeof m_packet_count);
        LOG("Reading m_packet_count %u",m_packet_count);
        queue_init(m_ackno,m_packet_count);
    }
    else {
        queue_init(0,0);
    }

    return;
}

#define PLAINTEXT_BUFFER_MAX_LEN 144
static  uint8_t m_plaintext_buffer[PLAINTEXT_BUFFER_MAX_LEN];
static uint16_t m_plaintext_buffer_len = 0;

#define CIPHERTEXT_BUFFER_MAX_LEN (PLAINTEXT_BUFFER_MAX_LEN + TAGBYTES)
static  uint8_t m_ciphertext_buffer[CIPHERTEXT_BUFFER_MAX_LEN];

#define MIN(a,b) ((a) < (b) ? (a) : (b))
void network_send(
    const uint8_t *data,
          uint16_t len)
{
    uint32_t err_code;

    while (len > 0) {
        uint16_t bytes_left_in_buffer = PLAINTEXT_BUFFER_MAX_LEN - m_plaintext_buffer_len;
        uint16_t bytes_to_copy        = MIN(len, bytes_left_in_buffer);

        /* copy as much data as we can to the buffer */
        memcpy(
            &m_plaintext_buffer[m_plaintext_buffer_len],
            data,
            bytes_to_copy
        );

        len -= bytes_to_copy;
        m_plaintext_buffer_len += bytes_to_copy;
        //LOG("Enqueued %u bytes. Buffer now has %u bytes total", bytes_to_copy, m_plaintext_buffer_len);

        /* if the buffer if full, encrypt then place in network queue */
        if (PLAINTEXT_BUFFER_MAX_LEN == m_plaintext_buffer_len) {
            /* encrypt */
            LOG("encrypting buffer");
            uint16_t bytes_encrypted = encrypt_buffer(
                m_ciphertext_buffer,
                m_plaintext_buffer,
                m_plaintext_buffer_len,
                m_packet_count
            );

            /* enqueue */
            err_code = network_enqueue(m_ciphertext_buffer, bytes_encrypted);
            if (err_code != NRF_SUCCESS) {
                LOG("NETWORK - packet queue full. Dropping data");
            }

            /* empty the buffer */
            m_plaintext_buffer_len = 0;
        }
    }
}

/*
 * Network state
 */

/* called when a connection event occurs on the network */
void network_on_connect(void) {
    m_seqno = m_ackno;
    LOG("connected. current ackno %lu, current seqno %lu", m_ackno, m_seqno);
}

/* called when a write event occurs on the network (ie an ack is recived) */
uint32_t network_on_write(const uint8_t *data, uint16_t len) {
    /* check to make sure we have a valid ack packet */
    if (len < sizeof(ack_packet_t)) {
        //return NRF_ERROR_INVALID_DATA;
    }

    /* convert the raw data packet into a ack packet */
    const ack_packet_t *ack = (const ack_packet_t *)data;

    uint8_t hmac[20];
    hmac_sha1((void*) hmac, (const void*)&DEFAULT_KEY, 16*8, (const void*) &ack->s.ackno, 4*8);
    if(memcmp(hmac,ack->s.mac,16)){
        LOG("Mac incorrect!");
        return NRF_SUCCESS;
    }

    /* check to make sure this is a new ackno */
    uint32_t ackno = ack->s.ackno;
    LOG("MAC verified, got ackno %lu, current ackno is %lu", ackno, m_ackno);
    if (ackno <= m_ackno) {
        return NRF_SUCCESS;
    }

    if (ackno > m_packet_count + 1) {
        LOG("Got invalid packet %lu, current packet count is %lu", ackno, m_packet_count);
        return NRF_SUCCESS;
    }

    /* set the ackno to the new value */
    uint8_t buffer[4];
    m_ackno = ackno;
    LOG("Storing ackno %u",m_ackno);
    memcpy(buffer, &m_ackno, sizeof m_ackno);
    flash_write(ackno_code, buffer);

    /* delete the sensor data up to the ackno */
    queue_dequeue_n(m_ackno);

    return NRF_SUCCESS;
}

uint32_t network_on_read_request(uint8_t *data, uint16_t *len) {
    uint32_t err_code;

    /* convert the data buffer into a data_packet */
    data_packet_t *packet = (data_packet_t *)data;

    /* read from the queue */
    err_code = queue_read_n(packet, m_seqno);
    if (err_code != NRF_SUCCESS) {
        return err_code;
    }
    memcpy(data,packet,sizeof(data_packet_t));

    /* set the write length to the correct amount */
    *len = sizeof(data_packet_t);

    return NRF_SUCCESS;
}

/* called when a flow control event occurs on the network (ie indicate hvc packet is recived) */
void network_on_indicate(void) {
    m_seqno++;
}
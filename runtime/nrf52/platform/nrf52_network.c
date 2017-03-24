
#include <stdlib.h>
#include <assert.h>
#include "nrf52_network.h"
#include "nrf_error.h"
#include "nrf52_ravel_endpoint.h"
#include "nrf52_ravel_frame.h"
#include "ravel/nrf52-driver.h"
#include "api/intrinsics.h"

#define NRF_LOG_MODULE_NAME "NET::"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "nrf52_ble_rad.h"


static nrf52_endpoint endpoint_space;
extern RavelNrf52Driver driver;

static bool m_connected = false;
static bool m_notify_enabled = false;


static uint32_t m_dropped = 0;
static uint32_t m_total = 0;

//TX stuff
static uint8_t m_tx_enqueued_pkt=0;
static uint8_t m_tx_fragment_enqueued=0;
static uint8_t m_tx_sent_done_fragment=0;
//RX stuff
static uint8_t m_rx_enqueued_pkt=0;
static uint8_t m_rx_enqueued_fragment=0;
//Buffer for the ravel packet
static uint8_t *pkt_buffer;
static size_t pkt_length =0;

static bool m_endpoint_is_set = false;
static bool m_receiving = false;

//class for handling RAD networking
void
create_endpoint(const uint8_t *data, uint16_t len)
{
    //as part of bootstrapping the connected device should write its space name
    // we should get uuid from the device connection
    //both create an physical endpoint
    //NOTE: for now we just operate on the space name and ignore the uuid
    //TODO: remove string out of e-space
    //TODO: check that it is the expected packet or hell will break loose

    // FIXME! 2 = GatewaySpace
    endpoint_space.m_ravel_endpoint.id = 2;
    endpoint_space.m_tx_uuid = BLE_UUID_RAD_TX_CHARACTERISTIC;
    m_endpoint_is_set = true;
}


//called when packet is completed
static void packetRxCompleted()
{
    NRF_LOG_DEBUG("packetRxCompleted \r\n");
    RavelPacket pkt;

    uint8_t* pkt_data = pkt_buffer;

    int32_t value = 0;
    if (pkt_length > 6)
        value = ravel_intrinsic_extract_int32(pkt_data, 6);
    uint16_t record_id = ravel_intrinsic_extract_uint16(pkt_data, 4);
    NRF_LOG_DEBUG("pkt buffer %u %u %u %u %u %u\r\n", pkt_data[0], pkt_data[1], pkt_data[2], pkt_data[3], record_id,
        value);

    ravel_packet_init_from_network(&pkt, pkt_data, pkt_length);
    NRF_LOG_ERROR("packetRxCompleted free: %p\r\n", pkt_buffer);
    free(pkt_buffer);
    pkt_length =0;
    pkt_buffer = NULL;
    ravel_nrf52_driver_rx_data_from_low(&driver.base, &pkt, &endpoint_space);
    //TODO: re-enable rx
    m_receiving = false;
    m_rx_enqueued_pkt--; //should be zero now
    m_rx_enqueued_fragment = 0;
    ravel_packet_finalize(&pkt);
}




static void fragment_rx(data_packet_t *m_pkt, const uint8_t* pkt_data)
{
    //TODO: we handle/assume one packet a time
    if(!m_receiving && (m_pkt->indx == 0)){
        //first fragment
        assert(pkt_buffer == NULL);
        //FIXME: MAX length set here :S
        pkt_buffer = calloc(BLE_RAD_MAX_DATA_LEN, 10);
        NRF_LOG_ERROR("fragment_rx malloc: %p\r\n", pkt_buffer);
        if (pkt_buffer == NULL) {
            //NRF_LOG_ERROR("OUT OF MEMORY\r\n");
            return;
        }
        m_rx_enqueued_pkt++;
    }
    m_rx_enqueued_fragment++;
    pkt_length += m_pkt->length;
    //append to the packet buffer
    memcpy(pkt_buffer + m_pkt->indx * (BLE_RAD_MAX_DATA_LEN-3), pkt_data, m_pkt->length);

    NRF_LOG_DEBUG("fragment_rx indx: %u length %u \r\n", m_pkt->indx, m_pkt->length);
    if( m_pkt->ctrf_flags == 1) {
        //finalize the data and send it off
        packetRxCompleted();
    }
}

void
network_on_write(const uint8_t *data, uint16_t len)
{
    NRF_LOG_DEBUG("network_on_write\r\n");
    //TODO: VERIFY the dispatching to the right place
    data_packet_t m_pkt;
    memcpy(&m_pkt, data, sizeof(m_pkt));
    if( m_pkt.ctrf_flags == SET_ENDPOINT ){
        create_endpoint( data, len);
    } else {
        m_rx_enqueued_fragment++;
        fragment_rx(&m_pkt, data+sizeof(m_pkt));
    }
}


void
network_on_read_request(const uint8_t *data, uint16_t len)
{
    //FIXME: tbi
    NRF_LOG_DEBUG("network_on_read_request \r\n");
}

static void inline packetTxCompleted()
{
    m_tx_enqueued_pkt --;
    m_tx_fragment_enqueued = m_tx_sent_done_fragment = 0;
    NRF_LOG_DEBUG("packetTxCompleted\r\n");
    //TODO: signal to the driver
    ravel_nrf52_driver_send_done_from_low(&driver.base);
}
void
network_on_send_done()
{
    m_total++;
    NRF_LOG_DEBUG("network_on_send_done %u\r\n", m_total);
    m_tx_sent_done_fragment++;
    if(m_tx_fragment_enqueued == m_tx_sent_done_fragment) {
        packetTxCompleted();
    }
}

void
network_on_indicate(void)
{
    //FIXME: tbi
    NRF_LOG_DEBUG("network_on_indicate \r\n");
}


void
network_on_notify()
{
    m_notify_enabled = !m_notify_enabled;

    if(m_endpoint_is_set)
    {
        endpoint_space.m_ravel_endpoint.connected = m_notify_enabled;

        if (m_notify_enabled)
        {
            NRF_LOG_DEBUG("Notifications enabled \r\n");
            ravel_nrf52_driver_set_endpoint(&driver, &endpoint_space);
        }
        else
        {
            NRF_LOG_DEBUG("Notifications disabled \r\n");
            endpoint_space.m_ravel_endpoint.id = -1;
            ravel_nrf52_driver_set_endpoint(&driver, NULL);
            m_endpoint_is_set = false;
         }
   } else {
        NRF_LOG_DEBUG("unauthorized attempt to write\r\n");
   }
}

void
network_on_connected(void)
{
    m_connected = true;
    NRF_LOG_DEBUG("network_on_connected \r\n");
}
void
network_on_disconnected(void)
{
    m_connected = false;
    NRF_LOG_DEBUG("network_on_disconnected  %u\r\n", m_connected);
}


/**
 * called to send full packet
 */

static void
network_send_data( uint8_t * p_data, uint16_t length)
{
    NRF_LOG_DEBUG("fragmenting data\r\n");
    bool has_fragment = true; //we always have at least one fragment
   // data pointer is for recursive use so we can traverse the rad_data
    uint8_t * data_ptr = (uint8_t *)p_data;
    int32_t value = 0;
    // buffer to be sent over ble
    uint8_t buffer[BLE_RAD_MAX_DATA_LEN];
    memset(buffer, 0, BLE_RAD_MAX_DATA_LEN);

    // ravel header
    data_packet_t ravel_pkt;
    while(length > 0)
    {
        if ( length > BLE_RAD_MAX_DATA_LEN - sizeof( data_packet_t) ){
            NRF_LOG_DEBUG("data >= bt %u\r\n", sizeof( data_packet_t));
            ravel_pkt.length = BLE_RAD_MAX_DATA_LEN - sizeof( data_packet_t);
            //only one flag TODO: need whole protocol suite
            ravel_pkt.ctrf_flags = 0;
        } else {
            NRF_LOG_DEBUG("data < bt frame length\r\n");
            ravel_pkt.length = length;
            ravel_pkt.ctrf_flags = 1;
        }
        // set index
        ravel_pkt.indx = m_tx_fragment_enqueued;
        // copy extended bluetooth header to buffer
        memcpy(buffer, &ravel_pkt, sizeof( data_packet_t));

        // copy RAD packet data to buffer
        memcpy(buffer + sizeof(data_packet_t), data_ptr, ravel_pkt.length);

        // TODO: enqueue the packet for sending
        // FIXME: can not handle more than 7 pkt due to out buffer
        //needs global buffer, that releasing the send through the sch
       uint32_t send_result = ble_rad_send_data_interface(buffer, sizeof( data_packet_t)+ravel_pkt.length);
       m_tx_fragment_enqueued++;
       NRF_LOG_DEBUG("send_fragment %u [%u]\r\n", m_tx_fragment_enqueued, send_result);
        // updating
        length -= ravel_pkt.length;
        memset(buffer, 0, BLE_RAD_MAX_DATA_LEN);
        data_ptr = data_ptr + ravel_pkt.length;
     }
}

bool
network_send(RavelPacket *packet, RavelEndpoint *endpoint)
{
    NRF_LOG_DEBUG("packet size  %u\r\n", packet->packet_length);
    if(m_connected && m_notify_enabled)
    {
        m_tx_enqueued_pkt++;
        network_send_data(packet->packet_data, packet->packet_length);
        return true;
    } else {
        //TODO: enqueue packet
        //now drop
        NRF_LOG_DEBUG("no connection dropping %u\r\n", ++m_dropped);
        ravel_packet_finalize(packet);
        return false;
    }

}
/**
 * innitialize the network
 */

void
nrf52_network_init(NetworkClb *self)
{
//    NRF_LOG_DEBUG("network_init \r\n");
    self->on_write = network_on_write;
    self->on_read = network_on_read_request;
    self->send_done = network_on_send_done;
    self->notify = network_on_notify;
    self->indicate_enabled = network_on_indicate;
    self->connected = network_on_connected;
    self->disconnected = network_on_disconnected;

//    ackno_code = ack_code;
//    store_code = str_code;
//
//    if (old_data > 0){
//        uint8_t buffer[4];
//        flash_read(ackno_code,buffer);
//        memcpy(&m_ackno, buffer, sizeof m_ackno);
//        NRF_LOG_DEBUG("network_init %u\r\n",m_ackno);
//        flash_read(store_code, buffer);
//        memcpy(&m_packet_count, buffer, sizeof m_packet_count);
//        NRF_LOG_DEBUG("GAP_CONNECTED %u\r\n",m_packet_count);
//        queue_init(m_ackno,m_packet_count);
//    }
//    else {
//        queue_init(0,0);
//    }

}

//#define PLAINTEXT_BUFFER_MAX_LEN 144
//static  uint8_t m_plaintext_buffer[PLAINTEXT_BUFFER_MAX_LEN];
//static uint16_t m_plaintext_buffer_len = 0;


#define MIN(a,b) ((a) < (b) ? (a) : (b))
//void network_send(const uint8_t *data,  uint16_t len)
//{
//    uint32_t err_code;
//
//    while (len > 0) {
//        uint16_t bytes_left_in_buffer = PLAINTEXT_BUFFER_MAX_LEN - m_plaintext_buffer_len;
//        uint16_t bytes_to_copy        = MIN(len, bytes_left_in_buffer);
//
//        /* copy as much data as we can to the buffer */
//        memcpy(
//            &m_plaintext_buffer[m_plaintext_buffer_len],
//            data,
//            bytes_to_copy
//        );
//
//        len -= bytes_to_copy;
//        m_plaintext_buffer_len += bytes_to_copy;
//
//        if (PLAINTEXT_BUFFER_MAX_LEN == m_plaintext_buffer_len) {
//            /* enqueue */
//            err_code = network_enqueue(m_plaintext_buffer, m_plaintext_buffer_len);
//            if (err_code != NRF_SUCCESS) {
//                NRF_LOG_DEBUG("NETWORK - packet queue full. Dropping data");
//            }
//            /* empty the buffer */
//            m_plaintext_buffer_len = 0;
//        }
//    }
//}

/*
 * Network state
 */

/* called when a connection event occurs on the network */
//void network_on_connect(void) {
//    m_seqno = m_ackno;
//    NRF_LOG_DEBUG("connected. current ackno %lu, current seqno %lu", m_ackno, m_seqno);
//}

/* called when a write event occurs on the network (ie an ack is recived) */
//void network_on_write(const uint8_t *data, uint16_t len) {
    /* check to make sure we have a valid ack packet */
//    if (len < sizeof(ack_packet_t)) {
//        //return NRF_ERROR_INVALID_DATA;
//    }

    /* convert the raw data packet into a ack packet */
//    const ack_packet_t *ack = (const ack_packet_t *)data;
//
//    uint8_t hmac[20];
//    hmac_sha1((void*) hmac, (const void*)&DEFAULT_KEY, 16*8, (const void*) &ack->s.ackno, 4*8);
//    if(memcmp(hmac,ack->s.mac,16)){
//        NRF_LOG_DEBUG("Mac incorrect!");
//        return NRF_SUCCESS;
//    }
//
//    /* check to make sure this is a new ackno */
//    uint32_t ackno = ack->s.ackno;
//    NRF_LOG_DEBUG("MAC verified, got ackno %lu, current ackno is %lu", ackno, m_ackno);
//    if (ackno <= m_ackno) {
//        return NRF_SUCCESS;
//    }
//
//    if (ackno > m_packet_count + 1) {
//        NRF_LOG_DEBUG("Got invalid packet %lu, current packet count is %lu", ackno, m_packet_count);
//        return NRF_SUCCESS;
//    }
//
//    /* set the ackno to the new value */
//    uint8_t buffer[4];
//    m_ackno = ackno;
//    NRF_LOG_DEBUG("Storing ackno %u",m_ackno);
//    memcpy(buffer, &m_ackno, sizeof m_ackno);
//    flash_write(ackno_code, buffer);
//
//    /* delete the sensor data up to the ackno */
//    queue_dequeue_n(m_ackno);

//}
//
//void network_on_read_request(uint8_t *data, uint16_t *len) {
//    uint32_t err_code;
//
//    /* convert the data buffer into a data_packet */
//    data_packet_t *packet = (data_packet_t *)data;
//
//    /* read from the queue */
//    err_code = queue_read_n(packet, m_seqno);
//    if (err_code != NRF_SUCCESS) {
//        return err_code;
//    }
//    memcpy(data,packet,sizeof(data_packet_t));
//
//    /* set the write length to the correct amount */
//    *len = sizeof(data_packet_t);
//
//}
//
///* called when a flow control event occurs
//on the network (ie indicate hvc packet is rereceived*/
//void network_on_indicate(void) {
//    m_seqno++;
//}
/* total number of packets seen so far */
//static uint8_t ackno_code;
//static uint8_t store_code;
//static uint32_t m_packet_count = 0;
//
//static uint32_t m_ackno = 0;
//static uint32_t m_seqno = 0;

//static
//uint32_t network_enqueue(
//    const uint8_t *data,
//          uint16_t len)
//{
//    uint32_t err_code;
//    uint8_t buffer[4];
//
//    data_packet_t packet;
//    for (int i = 0; i < len; i += sizeof packet.s.payload) {
//        /* check to make sure there's data to packetize */
//        if (i + sizeof packet.s.payload > len) {
//            NRF_LOG_DEBUG("NETWORK - not enough data to packetize. This is a bug!");
//            NRF_LOG_DEBUG("NETWORK - dropping %u bytes of data", len - i);
//            return NRF_ERROR_INVALID_STATE;
//        }
//
//        /* convert the data in the queue to a data packet */
//        memcpy(&packet.s.payload, &data[i], sizeof packet.s.payload);
//        packet.s.seqno = m_packet_count;
//        NRF_LOG_DEBUG("network enqueue SEQUENCE NUMBER %u ",packet.s.seqno);
//
//        /* push the data to non volatile memory */
//        err_code = queue_enqueue(&packet, m_packet_count);
//        if (err_code != NRF_SUCCESS) {
//            NRF_LOG_DEBUG("NETWORK - could not place packet in permanant storage");
//            return err_code;
//        }
//
//        /* if we succeeded in sending the packet, increment the packet count */
//        m_packet_count++;
//        NRF_LOG_DEBUG("Storing m_packet_count %u ackno %u",m_packet_count,m_ackno);
//        memcpy(buffer, &m_packet_count, sizeof m_packet_count);
//        flash_write(store_code, buffer);
//    }
//   // config_persist(sensor_get_time());
//
//    return NRF_SUCCESS;
//}
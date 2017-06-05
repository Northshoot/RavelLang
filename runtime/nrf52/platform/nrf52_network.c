
#include <stdlib.h>
#include <assert.h>
#include "nrf52_network.h"
#include "nrf_error.h"
#include "nrf52_ravel_endpoint.h"
#include "nrf52_ravel_frame.h"
#include "ravel/nrf52-driver.h"
#include "api/intrinsics.h"

#define NRF_LOG_MODULE_NAME "NET::"
#define NRF_LOG_LEVEL 4
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
 /**
  * tier 8b 0
  * src 32 1 
  * dst 8b 5
  * flags 8b 6
  * model_id 8b 7
  * record_id 16b 8
  */
    uint8_t* pkt_data = pkt_buffer;
    int32_t value = 0;
    int32_t src = ravel_intrinsic_extract_int32(pkt_data, 1);
    int32_t src1 = (pkt_data[4] << 24) | (pkt_data[3] & 0xFF) << 16 | (pkt_data[2] & 0xFF) << 8 | (pkt_data[1] & 0xFF);
    NRF_LOG_DEBUG("Packet lenght: %u src: %u src1 %u \r\n", pkt_length, src, src1);
            //    if (pkt_length > 6)
    value = ravel_intrinsic_extract_int32(pkt_data, 10);
    uint16_t record_id = ravel_intrinsic_extract_uint16(pkt_data, 8);

    NRF_LOG_DEBUG("pkt buffer src %u dst %u flags %u mid %u rid %u val %u \r\n",  src, pkt_data[5], pkt_data[6], pkt_data[7],record_id, value);

    NRF_LOG_HEXDUMP_DEBUG(pkt_data, pkt_length);
    NRF_LOG_DEBUG("packet about to innit from network\r\n");
    ravel_packet_init_from_network(&pkt, pkt_data, pkt_length);

    free(pkt_buffer);
    pkt_length =0;
    pkt_buffer = NULL;
    //TODO: re-enable rx
    m_receiving = false;
    m_rx_enqueued_pkt--; //should be zero now
    m_rx_enqueued_fragment = 0;
    NRF_LOG_DEBUG("packet assambled\r\n");
    ravel_nrf52_driver_rx_data_from_low(&driver.base, &pkt, &endpoint_space);

    ravel_packet_finalize(&pkt);
}




static void fragment_rx(data_packet_t *m_pkt, const uint8_t* pkt_data)
{
//    //TODO: we handle/assume one packet a time
//    if(!m_receiving && (m_pkt->indx == 0)){
//        //first fragment
//        NRF_LOG_DEBUG("assert ok\r\n");
//        //FIXME: MAX length set here
//        pkt_buffer = malloc(BLE_RAD_MAX_DATA_LEN * 5);
//        //NRF_LOG_ERROR("fragment_rx malloc: %p\r\n", pkt_buffer);
//        if (pkt_buffer == NULL) {
//            NRF_LOG_ERROR("fragment_rx callo returner NULL\r\n");
//            return;
//        }
//        m_rx_enqueued_pkt++;
//    }
//    m_rx_enqueued_fragment++;
//    pkt_length += m_pkt->length;
//    //append to the packet buffer
//    memcpy(pkt_buffer + m_pkt->indx * (BLE_RAD_MAX_DATA_LEN-3), pkt_data, m_pkt->length);
//
//    NRF_LOG_DEBUG("fragment_rx indx: %u length %u \r\n", m_pkt->indx, m_pkt->length);
//    if( m_pkt->ctrf_flags == 1) {
//        //finalize the data and send it off
//        packetRxCompleted();
//    }
}

void
network_on_write(const uint8_t *data, uint16_t len)
{
    NRF_LOG_DEBUG("RX data from BLE: network_on_write length %u\r\n", len);
    //TODO: VERIFY the dispatching to the right place
    data_packet_t m_pkt;
    memcpy(&m_pkt, data, sizeof(m_pkt));
    //TODO: protocol implementation
    if( m_pkt.ctrf_flags == SET_ENDPOINT ){
        create_endpoint( data, len);
    } else {
        //the rest is only data fragments
        m_rx_enqueued_fragment++;
        fragment_rx(&m_pkt, data);
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
    m_tx_enqueued_pkt--;
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


   // data pointer is for recursive use so we can traverse the rad_data
    uint8_t * data_ptr = (uint8_t *)p_data;
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
       NRF_LOG_DEBUG("RAW send results %u\r\n", send_result);
       m_tx_fragment_enqueued++;
       //NRF_LOG_DEBUG("send_fragment %u [%u]\r\n", m_tx_fragment_enqueued, send_result);
        // updating
        length -= ravel_pkt.length;
        memset(buffer, 0, BLE_RAD_MAX_DATA_LEN);
        data_ptr = data_ptr + ravel_pkt.length;
     }
}

bool
network_send(RavelPacket *packet, RavelEndpoint *endpoint)
{
    NRF_LOG_DEBUG("\tnetwork_send packet size  %u\r\n", packet->packet_length);
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
}

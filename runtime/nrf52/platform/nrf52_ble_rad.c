#include <stdlib.h>
#include "sdk_common.h"
#include "nrf52_ble_rad.h"
#include "ble_srv_common.h"
#include "nrf52_ravel_frame.h"

#define NRF_LOG_MODULE_NAME "BLE_RAD"
#define NRF_LOG_LEVEL 1
#include "nrf_log.h"
#include "nrf_log_ctrl.h"
#include "nrf52_ble_interface.h"
#include "nrf52_ble_rad.h"

#include "api/intrinsics.h"

static ble_rad_t                        m_rad;
#define RAD_SERVICE_UUID_TYPE           BLE_UUID_TYPE_VENDOR_BEGIN

struct BleInterfaceVtable rad_handler;

ble_uuid_t m_rad_adv_uuids = {BLE_UUID_RAD_SERVICE, RAD_SERVICE_UUID_TYPE};


static uint32_t ble_rad_init(ble_rad_t * p_rad);
static uint32_t ble_rad_send_data(ble_rad_t * p_rad, uint8_t * p_string, uint16_t length);
static void ble_rad_on_ble_evt(ble_rad_t * p_rad, ble_evt_t * p_ble_evt);

void
ble_rad_init_interface(NetworkClb *network)
{
     uint32_t       err_code;
     NRF_LOG_DEBUG("ble_rad_init_interface\r\n");
     m_rad.network = network;

     err_code = ble_rad_init(&m_rad);
     APP_ERROR_CHECK(err_code);
}

uint32_t
ble_rad_send_data_interface(uint8_t * p_data, uint16_t length){
    uint32_t err =  ble_rad_send_data(&m_rad, p_data, length);
    return err;
}

void ble_rad_on_ble_evt_interface(ble_evt_t * p_ble_evt)
{
    NRF_LOG_DEBUG("ble_rad_on_ble_evt_interface\r\n");
    //pass on the event to the implementation
    ble_rad_on_ble_evt(&m_rad, p_ble_evt);
}



void ble_rad_init_handler()
{
    NRF_LOG_DEBUG("ble_rad_init_handler\r\n");
    //add init call
    rad_handler.init = ble_rad_init_interface;
    //add a callback to the ble handler event
    rad_handler.ble_generic_event = ble_rad_on_ble_evt_interface;
    //add uuids for service
    rad_handler.p_adv_uuids = m_rad_adv_uuids;
    //add this handler
    nrf52_ble_interface_add_handler(&rad_handler);
}



#define BLE_RAD_MAX_RX_CHAR_LEN        BLE_RAD_MAX_DATA_LEN        /**< Maximum length of the RX Characteristic (in bytes). */
#define BLE_RAD_MAX_TX_CHAR_LEN        BLE_RAD_MAX_DATA_LEN        /**< Maximum length of the TX Characteristic (in bytes). */

#define RAD_BASE_UUID                  {{0x01, 0x02, 0x03, 0x024, 0x05, 0x06, 0x07, 0x08, 0x09, 0xA3, 0xA3, 0xA3, 0xA3, 0xA3, 0xA3, 0xA3}}

#define CALL_UP_SEND_DONE(P_STRUCT) (P_STRUCT->network)->send_done()
#define CALL_UP_CONNECTED(P_STRUCT) (P_STRUCT->network)->connected()
#define CALL_UP_DISCONNECTED(P_STRUCT) (P_STRUCT->network)->disconnected()
#define CALL_UP_NOTIFY(P_STRUCT) (P_STRUCT->network)->notify()
#define CALL_UP_SEND_DONE(P_STRUCT) (P_STRUCT->network)->send_done()
#define CALL_UP_RX(P_STRUCT, P_DATA, LEN) (P_STRUCT->network)->on_write(P_DATA, LEN)

//this could speedup the process of sending
//static uint8_t m_tx_pkt_available=0;

/**@brief Function for handling the @ref BLE_GAP_EVT_CONNECTED event from the S110 SoftDevice.
 *
 * @param[in] p_rad     Ravel Data Service structure.
 * @param[in] p_ble_evt Pointer to the event received from BLE stack.
 */
static void on_connect(ble_rad_t * p_rad, ble_evt_t * p_ble_evt)
{
    NRF_LOG_DEBUG("on_connect\r\n");
    //TODO: signal upwards
    p_rad->conn_handle = p_ble_evt->evt.gap_evt.conn_handle;
    CALL_UP_CONNECTED(p_rad);
}


/**@brief Function for handling the @ref BLE_GAP_EVT_DISCONNECTED event from the S110 SoftDevice.
 *
 * @param[in] p_rad     Ravel Data Service structure.
 * @param[in] p_ble_evt Pointer to the event received from BLE stack.
 */
static void on_disconnect(ble_rad_t * p_rad, ble_evt_t * p_ble_evt)
{
    NRF_LOG_DEBUG("on_disconnect\r\n");
    UNUSED_PARAMETER(p_ble_evt);
    //TODO: signal upwards
    p_rad->conn_handle = BLE_CONN_HANDLE_INVALID;
    CALL_UP_DISCONNECTED(p_rad);
}


/**@brief Function for handling the @ref BLE_GATTS_EVT_WRITE event from the S110 SoftDevice.
 *
 * @param[in] p_rad     Ravel Data Service structure.
 * @param[in] p_ble_evt Pointer to the event received from BLE stack.
 */
static void on_write(ble_rad_t * p_rad, ble_evt_t * p_ble_evt)
{
    NRF_LOG_DEBUG("on write\r\n");
    ble_gatts_evt_write_t * p_evt_write = &p_ble_evt->evt.gatts_evt.params.write;

    if (
        (p_evt_write->handle == p_rad->rx_handles.cccd_handle)
        &&
        (p_evt_write->len == 2)
       )
    {
        if (ble_srv_is_notification_enabled(p_evt_write->data))
        {
            NRF_LOG_DEBUG("notification enabled\r\n");
            p_rad->is_notification_enabled = true;
//            sd_ble_tx_packet_count_get(p_rad->conn_handle, &m_tx_pkt_available);
//            NRF_LOG_DEBUG("available tx packets %u\r\n", m_tx_pkt_available);
            CALL_UP_NOTIFY(p_rad);
        }
        else
        {
            NRF_LOG_DEBUG("notification disabled\r\n");
            p_rad->is_notification_enabled = false;
            CALL_UP_NOTIFY(p_rad);
        }
    }
    else if ( (p_evt_write->handle == p_rad->tx_handles.value_handle) )
    {
        NRF_LOG_DEBUG("data received\r\n");
        //need to demangle data from fragments
        CALL_UP_RX( p_rad, p_evt_write->data, p_evt_write->len);
    }
    else
    {
        // Do Nothing. This event is not relevant for this service.
    }
}


/***
 *
 * Service function for starting up
 */


/**@brief Function for adding RX characteristic.
 *
 * @param[in] p_rad       Ravel Data Service structure.
 * @param[in] p_rad_init  Information needed to initialize the service.
 *
 * @return NRF_SUCCESS on success, otherwise an error code.
 */
static uint32_t rx_char_add(ble_rad_t * p_rad)
{
    /**@snippet [Adding proprietary characteristic to S110 SoftDevice] */
    ble_gatts_char_md_t char_md;
    ble_gatts_attr_md_t cccd_md;
    ble_gatts_attr_t    attr_char_value;
    ble_uuid_t          ble_uuid;
    ble_gatts_attr_md_t attr_md;

    memset(&cccd_md, 0, sizeof(cccd_md));

    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.read_perm);
    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.write_perm);

    cccd_md.vloc = BLE_GATTS_VLOC_STACK;

    memset(&char_md, 0, sizeof(char_md));

    char_md.char_props.notify = 1;
    char_md.p_char_user_desc  = NULL;
    char_md.p_char_pf         = NULL;
    char_md.p_user_desc_md    = NULL;
    char_md.p_cccd_md         = &cccd_md;
    char_md.p_sccd_md         = NULL;

    ble_uuid.type = p_rad->uuid_type;
    ble_uuid.uuid = BLE_UUID_RAD_RX_CHARACTERISTIC;

    memset(&attr_md, 0, sizeof(attr_md));

    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.read_perm);
    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.write_perm);

    attr_md.vloc    = BLE_GATTS_VLOC_STACK;
    attr_md.rd_auth = 0;
    attr_md.wr_auth = 0;
    attr_md.vlen    = 1;

    memset(&attr_char_value, 0, sizeof(attr_char_value));

    attr_char_value.p_uuid    = &ble_uuid;
    attr_char_value.p_attr_md = &attr_md;
    attr_char_value.init_len  = sizeof(uint8_t);
    attr_char_value.init_offs = 0;
    attr_char_value.max_len   = BLE_RAD_MAX_RX_CHAR_LEN;

    return sd_ble_gatts_characteristic_add(p_rad->service_handle,
                                           &char_md,
                                           &attr_char_value,
                                           &p_rad->rx_handles);
    /**@snippet [Adding proprietary characteristic to S110 SoftDevice] */
}


/**@brief Function for adding TX characteristic.
 *
 * @param[in] p_rad       Ravel Data Service structure.
 * @param[in] p_rad_init  Information needed to initialize the service.
 *
 * @return NRF_SUCCESS on success, otherwise an error code.
 */
static uint32_t tx_char_add(ble_rad_t * p_rad)
{
    ble_gatts_char_md_t char_md;
    ble_gatts_attr_t    attr_char_value;
    ble_uuid_t          ble_uuid;
    ble_gatts_attr_md_t attr_md;

    memset(&char_md, 0, sizeof(char_md));

    char_md.char_props.write         = 1;
    char_md.char_props.write_wo_resp = 1;
    char_md.p_char_user_desc         = NULL;
    char_md.p_char_pf                = NULL;
    char_md.p_user_desc_md           = NULL;
    char_md.p_cccd_md                = NULL;
    char_md.p_sccd_md                = NULL;

    ble_uuid.type = p_rad->uuid_type;
    ble_uuid.uuid = BLE_UUID_RAD_TX_CHARACTERISTIC;

    memset(&attr_md, 0, sizeof(attr_md));

    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.read_perm);
    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.write_perm);

    attr_md.vloc    = BLE_GATTS_VLOC_STACK;
    attr_md.rd_auth = 0;
    attr_md.wr_auth = 0;
    attr_md.vlen    = 1;

    memset(&attr_char_value, 0, sizeof(attr_char_value));

    attr_char_value.p_uuid    = &ble_uuid;
    attr_char_value.p_attr_md = &attr_md;
    attr_char_value.init_len  = 1;
    attr_char_value.init_offs = 0;
    attr_char_value.max_len   = BLE_RAD_MAX_TX_CHAR_LEN;

    return sd_ble_gatts_characteristic_add(p_rad->service_handle,
                                           &char_md,
                                           &attr_char_value,
                                           &p_rad->tx_handles);
}


static void ble_rad_on_ble_evt(ble_rad_t * p_rad, ble_evt_t * p_ble_evt)
{
    NRF_LOG_DEBUG("on_ble_event %u \r\n", p_ble_evt->header.evt_id);
    if ((p_rad == NULL) || (p_ble_evt == NULL))
    {
        return;
    }

    switch (p_ble_evt->header.evt_id)
    {
        case BLE_GAP_EVT_CONNECTED:
            on_connect(p_rad, p_ble_evt);
            break;

        case BLE_GAP_EVT_DISCONNECTED:
            on_disconnect(p_rad, p_ble_evt);
            break;

        case BLE_GATTS_EVT_WRITE:
            on_write(p_rad, p_ble_evt);
            break;
        case BLE_GATTS_EVT_HVN_TX_COMPLETE:
            NRF_LOG_DEBUG("TX_COMPLETE \r\n");
            CALL_UP_SEND_DONE(p_rad);
//            err_code = app_sched_event_put(NULL, 0, update_timers_state);
//                APP_ERROR_CHECK(err_code);
        default:
            // No implementation needed.
            break;
    }
}

static uint32_t ble_rad_init(ble_rad_t * p_rad)
{
    uint32_t      err_code;
    ble_uuid_t    ble_uuid;
    ble_uuid128_t rad_base_uuid = RAD_BASE_UUID;

    VERIFY_PARAM_NOT_NULL(p_rad);

    // Initialize the service structure.
    p_rad->conn_handle             = BLE_CONN_HANDLE_INVALID;
    p_rad->is_notification_enabled = false;

    /**@snippet [Adding proprietary Service to S110 SoftDevice] */
    // Add a custom base UUID.
    err_code = sd_ble_uuid_vs_add(&rad_base_uuid, &p_rad->uuid_type);
    NRF_LOG_DEBUG("sd_ble_uuid_vs_add %d\r\n", err_code);
    VERIFY_SUCCESS(err_code);

    ble_uuid.type = p_rad->uuid_type;
    ble_uuid.uuid = BLE_UUID_RAD_SERVICE;

    // Add the service.
    err_code = sd_ble_gatts_service_add(BLE_GATTS_SRVC_TYPE_PRIMARY,
                                        &ble_uuid,
                                        &p_rad->service_handle);
    NRF_LOG_DEBUG("sd_ble_gatts_service_add %d\r\n", err_code);
    /**@snippet [Adding proprietary Service to S110 SoftDevice] */
    VERIFY_SUCCESS(err_code);

    // Add the RX Characteristic.
    err_code = rx_char_add(p_rad);
    NRF_LOG_DEBUG("rx (tx add) %d\r\n", err_code);
    VERIFY_SUCCESS(err_code);
    // Add the TX Characteristic.
    err_code = tx_char_add(p_rad);
    VERIFY_SUCCESS(err_code);
NRF_LOG_DEBUG("tx (rx add) t\r\n");
    return NRF_SUCCESS;
}


/**
 * sending of the fragment packets
 */
static uint32_t
ble_rad_send_data(ble_rad_t * p_rad, uint8_t * p_string, uint16_t length)
{
    ble_gatts_hvx_params_t hvx_params;
    NRF_LOG_DEBUG("send_fragment\r\n");
    int32_t value = 0;
    if (length > 9)
        value = ravel_intrinsic_extract_int32(p_string, 9);

    NRF_LOG_DEBUG("ble_rad_send_data val %u\r\n", value);
    VERIFY_PARAM_NOT_NULL(p_rad);
    if (length > BLE_RAD_MAX_DATA_LEN)
    {
        return NRF_ERROR_INVALID_PARAM;
    }

    memset(&hvx_params, 0, sizeof(hvx_params));

    hvx_params.handle = p_rad->rx_handles.value_handle;
    hvx_params.p_data = p_string;
    hvx_params.p_len  = &length;
    hvx_params.type   = BLE_GATT_HVX_NOTIFICATION;

    return sd_ble_gatts_hvx(p_rad->conn_handle, &hvx_params);
}

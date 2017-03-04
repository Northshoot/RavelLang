//
// Created by lauril on 3/3/17.
//
#define NRF_LOG_MODULE_NAME "R_SRV"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"
#include "nrf_sdm.h"
#include "sdk_common.h"
#include "ble_srv_common.h"

#include "counter.h"
#include "ble_ravel_service.h"



#define BLE_UUID_RAD_TX_CHARACTERISTIC 0x0003                      /**< The UUID of the TX Characteristic. */
#define BLE_UUID_RAD_RX_CHARACTERISTIC 0x0004                      /**< The UUID of the RX Characteristic. */

#define BLE_RAD_MAX_RX_CHAR_LEN        BLE_RAD_MAX_DATA_LEN        /**< Maximum length of the RX Characteristic (in bytes). */
#define BLE_RAD_MAX_TX_CHAR_LEN        BLE_RAD_MAX_DATA_LEN        /**< Maximum length of the TX Characteristic (in bytes). */

#define RAD_BASE_UUID                  {{0x9E, 0xCA, 0xDC, 0x24, 0x0E, 0xE5, 0xA9, 0xE0, 0x93, 0xF3, 0xA3, 0xB5, 0x00, 0x00, 0x40, 0x6E}} /**< Used vendor specific UUID. */

static ble_rad_t  m_rad; //Ravel Data Service structure.


/**@brief Function for handling the @ref BLE_GAP_EVT_CONNECTED event from the S110 SoftDevice.
 *
 * @param[in] p_ble_evt Pointer to the event received from BLE stack.
 */
static void on_connect(ble_evt_t * p_ble_evt)
{
    NRF_LOG_DEBUG("on_connect %d\r\n", counter_get());
    m_rad.conn_handle = p_ble_evt->evt.gap_evt.conn_handle;
}


/**@brief Function for handling the @ref BLE_GAP_EVT_DISCONNECTED event from the S110 SoftDevice.
 *
 * @param[in] p_ble_evt Pointer to the event received from BLE stack.
 */
static void on_disconnect(ble_evt_t * p_ble_evt)
{
    NRF_LOG_DEBUG("on disconnect %d\r\n", counter_get());
    UNUSED_PARAMETER(p_ble_evt);
    m_rad.conn_handle = BLE_CONN_HANDLE_INVALID;
}


/**@brief Function for handling the @ref BLE_GATTS_EVT_WRITE event from the S110 SoftDevice.
 *
 * @param[in] p_ble_evt Pointer to the event received from BLE stack.
 */
static void on_write(ble_evt_t * p_ble_evt)
{

    ble_gatts_evt_write_t * p_evt_write = &p_ble_evt->evt.gatts_evt.params.write;
    NRF_LOG_DEBUG("ON write %d", counter_get());

    if (
        (p_evt_write->handle == m_rad.rx_handles.cccd_handle)
        &&
        (p_evt_write->len == 2)
       )
    {
        if (ble_srv_is_notification_enabled(p_evt_write->data))
        {
            m_rad.is_notification_enabled = true;
        }
        else
        {
            m_rad.is_notification_enabled = false;
        }
    }
    else if (
             (p_evt_write->handle == m_rad.tx_handles.value_handle)
             &&
             (m_rad.data_handler != NULL)
            )
    {
        m_rad.data_handler(p_evt_write->data, p_evt_write->len);
    }
    else
    {
        // Do Nothing. This event is not relevant for this service.
    }
}


/**@brief Function for adding RX characteristic.
 *
 * @param[in] p_rad_init  Information needed to initialize the service.
 *
 * @return NRF_SUCCESS on success, otherwise an error code.
 */
static uint32_t rx_char_add( ble_rad_init_t * p_rad_init)
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

    ble_uuid.type = m_rad.uuid_type;
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

    return sd_ble_gatts_characteristic_add(m_rad.service_handle,
                                           &char_md,
                                           &attr_char_value,
                                           &m_rad.rx_handles);
    /**@snippet [Adding proprietary characteristic to S110 SoftDevice] */
}


/**@brief Function for adding TX characteristic.
 *
 * @param[in] p_rad_init  Information needed to initialize the service.
 *
 * @return NRF_SUCCESS on success, otherwise an error code.
 */
static uint32_t tx_char_add( ble_rad_init_t * p_rad_init)
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

    ble_uuid.type = m_rad.uuid_type;
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

    return sd_ble_gatts_characteristic_add(m_rad.service_handle,
                                           &char_md,
                                           &attr_char_value,
                                           &m_rad.tx_handles);
}


void ble_rad_on_ble_evt(ble_evt_t * p_ble_evt)
{
    NRF_LOG_DEBUG("ble rad event %d", counter_get());
    if (p_ble_evt == NULL)
    {
        return;
    }

    switch (p_ble_evt->header.evt_id)
    {
        case BLE_GAP_EVT_CONNECTED:
            on_connect(p_ble_evt);
            break;

        case BLE_GAP_EVT_DISCONNECTED:
            on_disconnect(p_ble_evt);
            break;

        case BLE_GATTS_EVT_WRITE:
            on_write(p_ble_evt);
            break;

        default:
            // No implementation needed.
            break;
    }
}




static
void rad_data_handler(uint8_t * p_data, uint16_t length)
{

}

uint32_t
ble_rad_string_send( uint8_t * p_string, uint16_t length)
{
    ble_gatts_hvx_params_t hvx_params;


    if ((m_rad.conn_handle == BLE_CONN_HANDLE_INVALID) || (!m_rad.is_notification_enabled))
    {
        return NRF_ERROR_INVALID_STATE;
    }

    if (length > BLE_RAD_MAX_DATA_LEN)
    {
        return NRF_ERROR_INVALID_PARAM;
    }

    memset(&hvx_params, 0, sizeof(hvx_params));

    hvx_params.handle = m_rad.rx_handles.value_handle;
    hvx_params.p_data = p_string;
    hvx_params.p_len  = &length;
    hvx_params.type   = BLE_GATT_HVX_NOTIFICATION;

    return sd_ble_gatts_hvx(m_rad.conn_handle, &hvx_params);
}


uint32_t ble_rad_init()
{
    uint32_t      err_code;
    ble_uuid_t    ble_uuid;
    ble_uuid128_t rad_base_uuid = RAD_BASE_UUID;
    ble_rad_init_t m_rad_init;

    NRF_LOG_DEBUG("init %d", counter_get());

    m_rad_init.data_handler = rad_data_handler;

//    VERIFY_PARAM_NOT_NULL(m_rad);
//    VERIFY_PARAM_NOT_NULL(m_rad_init);

    // Initialize the service structure.
    m_rad.conn_handle             = BLE_CONN_HANDLE_INVALID;
    m_rad.data_handler            = m_rad_init.data_handler;
    m_rad.is_notification_enabled = false;

    /**@snippet [Adding proprietary Service to S110 SoftDevice] */
    // Add a custom base UUID.
    err_code = sd_ble_uuid_vs_add(&rad_base_uuid, &m_rad.uuid_type);
    VERIFY_SUCCESS(err_code);

    ble_uuid.type = m_rad.uuid_type;
    ble_uuid.uuid = BLE_UUID_RAD_SERVICE;

    // Add the service.
    err_code = sd_ble_gatts_service_add(BLE_GATTS_SRVC_TYPE_PRIMARY,
                                        &ble_uuid,
                                        &m_rad.service_handle);
    /**@snippet [Adding proprietary Service to S110 SoftDevice] */
    VERIFY_SUCCESS(err_code);

    // Add the RX Characteristic.
    err_code = rx_char_add(&m_rad_init);
    VERIFY_SUCCESS(err_code);

    // Add the TX Characteristic.
    err_code = tx_char_add(&m_rad_init);
    VERIFY_SUCCESS(err_code);

    return NRF_SUCCESS;
}
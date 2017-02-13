#include "app_error.h"
#include "ble.h"
#include "ble_core.h"
#include "ble_hci.h"
#include "ble_advdata.h"
#include "ble_srv_common.h"
#include "softdevice_handler.h"
#include "pstorage.h"
#include "nrf_delay.h"

#include "network.h"
#include "sensor.h"
#include "config.h"

#include "log.h"

/* Simple utility define */
#define COUNT_OF(x) ((sizeof(x)/sizeof(0[x])) / ((size_t)(!(sizeof(x) % sizeof(0[x])))))

/* Identifier for Nordic Semiconducter */
#define COMPANY_IDENTIFIER            0x0059

/* UUID of service to advertise with base uuid */
#define ADV_SERVICE_UUID              0x7A45

/* UUID for the Data service */
#define DATA_SERVICE_UUID             0x1523
/* UUID for the Data characteristic */
#define DATA_CHAR_UUID                0x1524
#define DATA_NAME_UUID                0x152C

/* UUID for the Config service */
//#define CONFIG_SERVICE_UUID           0x1525
//#define CONFIG_CHAR_TIME_UUID         0x1526
//#define CONFIG_CHAR_THRESHOLD_UUID    0x1527
//#define CONFIG_CHAR_DEVICE_KEY_UUID   0x1528
//#define CONFIG_CHAR_NAME_UUID         0x1529
//#define CONFIG_CHAR_REBOOT_UUID       0x152A
//#define CONFIG_CHAR_RESET_UUID        0x152B

/* Advertising interval. This value can vary between 20ms to 10.24s. */
#define ADV_INTERVAL       MSEC_TO_UNITS(1285, UNIT_0_625_MS)
/* Time for which the device must be advertising in connectable mode
 * (in seconds). */
#define ADV_TIMEOUT        30
#define ADV_DATA_LEN       31
/* Required length of the complete advertisement packet. (must be >= 8) */

#define MIN_CONN_INTERVAL  MSEC_TO_UNITS(379, UNIT_1_25_MS)
#define MAX_CONN_INTERVAL  MSEC_TO_UNITS(399, UNIT_1_25_MS)
#define SLAVE_LATENCY      4
/* connection supervisory timeout */
#define CONN_SUP_TIMEOUT   MSEC_TO_UNITS(6000, UNIT_10_MS)
/* Radio transmit power in dBm (accepted values are -40, -30, -20, -16, -12,
 * -8, -4, 0, and 4 dBm) */
#define TX_POWER_LEVEL     (-8)

/* Time from initiating event (connect or start of notification) to first time
 * sd_ble_gap_conn_param_update is called */
#define FIRST_CONN_PARAMS_UPDATE_DELAY APP_TIMER_TICKS(5000, APP_TIMER_PRESCALER)
/* Time between each call to sd_ble_gap_conn_param_update after the first call */
#define NEXT_CONN_PARAMS_UPDATE_DELAY  APP_TIMER_TICKS(30000, APP_TIMER_PRESCALER)
/* Number of attempts before giving up the connection parameter negotiation. */
#define MAX_CONN_PARAMS_UPDATE_COUNT   3
/* Size of the characteristic value being notified (in bytes). */
#define APP_CFG_CHAR_LEN               20

/* Length of encoded ad type in advertisement data. */
#define ADV_ENCODED_AD_TYPE_LEN       1
/* Length of the 'length field' of each ad type in advertisement data. */
#define ADV_ENCODED_AD_TYPE_LEN_LEN   1
/* Length of flags field that will be placed in advertisement data. */
#define ADV_FLAGS_LEN                 1
/* Length of flags field in advertisement packet. (1 byte for encoded ad type
 * plus 1 byte for length of flags plus the length of the flags itself). */
#define ADV_ENCODED_FLAGS_LEN         (ADV_ENCODED_AD_TYPE_LEN +       \
                                       ADV_ENCODED_AD_TYPE_LEN_LEN +   \
                                       ADV_FLAGS_LEN)
/* Length of the encoded Company Identifier in the Manufacturer Specific Data
 * part of the advertisement data. */
#define ADV_ENCODED_COMPANY_ID_LEN    2
/* < Length of Manufacturer Specific Data field that will be placed on the air
 * during advertisement. This is computed based on the value of
 * APP_CFG_ADV_DATA_LEN (required advertisement data length). */
#define ADV_ADDL_MANUF_DATA_LEN       (ADV_DATA_LEN - (ADV_ENCODED_FLAGS_LEN + \
    ADV_ENCODED_AD_TYPE_LEN + ADV_ENCODED_AD_TYPE_LEN_LEN + \
    ADV_ENCODED_COMPANY_ID_LEN))

#if APP_CFG_ADV_DATA_LEN > BLE_GAP_ADV_MAX_SIZE
    #error "The required advertisement data size (APP_CFG_ADV_DATA_LEN) is " \
        "greater than the value allowed by stack (BLE_GAP_ADV_MAX_SIZE). " \
        "Reduce the value of APP_CFG_ADV_DATA_LEN and recompile."
#endif

/* Check whether the maximum characteristic length + opcode length (1) +
 * handle length (2) is not greater than default MTU size. */
#if (APP_CFG_CHAR_LEN + 1 + 2) > BLE_L2CAP_MTU_DEF
    #error "The APP_CFG_CHAR_LEN is too large for the maximum MTU size."
#endif

#if ADV_ADDL_MANUF_DATA_LEN < 1
    #error "The required length of additional manufacturer specific data " \
        "computed based on the user configured values is computed to be less " \
        "than 1. Consider increasing the value of APP_CFG_ADV_DATA_LEN."
#endif

typedef struct {
    uint16_t vendor_uuid;
    uint16_t handle;
    uint8_t  uuid_type;
} ble_service_t;

typedef struct {
    uint16_t vendor_uuid;
    ble_gatts_char_handles_t handle;
} ble_char_t;

/* Tethys base uuid */
static const ble_uuid128_t m_base_uuid128 =
{
    {
        0xFB, 0x40, 0xEC, 0xF9, 0xE9, 0xE5, 0xFD, 0x2B,
        0x60, 0x0A, 0x6E, 0xEB, 0x00, 0x00, 0x00, 0x00
    }
};

static ble_gatts_char_handles_t m_data_char_handle;
static ble_gatts_char_handles_t m_data_name_handle;
static ble_gatts_char_handles_t m_read_time_char_handle;
//static ble_gatts_char_handles_t m_time_char_handle;
//static ble_gatts_char_handles_t m_threshold_char_handle;
//static ble_gatts_char_handles_t m_key_char_handle;
//static ble_gatts_char_handles_t m_name_char_handle;
//static ble_gatts_char_handles_t m_reboot_char_handle;

static uint16_t                 m_conn_handle = BLE_CONN_HANDLE_INVALID;
static bool                     m_indicate_confirmation_pending = false;

void pstorage_sys_event_handler(uint32_t p_evt);


static
void advertising_init(size_t adv_uuids_cnt, ble_uuid_t adv_uuids[]) {
    uint32_t      err_code;
    ble_advdata_t advdata;
    uint8_t       flags = BLE_GAP_ADV_FLAGS_LE_ONLY_LIMITED_DISC_MODE;

    /* setup the advertising data */
    memset(&advdata, 0, sizeof advdata);

    advdata.name_type               = BLE_ADVDATA_FULL_NAME;
    advdata.include_appearance      = false;
    advdata.flags.size              = sizeof flags;
    advdata.flags.p_data            = &flags;
    advdata.uuids_complete.uuid_cnt = adv_uuids_cnt;
    advdata.uuids_complete.p_uuids  = adv_uuids;

    /* set the advertising data */
    err_code = ble_advdata_set(&advdata, NULL);
    APP_ERROR_CHECK(err_code);
}

static
void advertising_start(void) {
    uint32_t err_code;

    /* Initialize advertising parameters */
    ble_gap_adv_params_t adv_params;
    memset(&adv_params, 0, sizeof(adv_params));

    adv_params.type        = BLE_GAP_ADV_TYPE_ADV_IND;
    adv_params.p_peer_addr = NULL;  /* Undirected advertisement */
    adv_params.fp          = BLE_GAP_ADV_FP_ANY;
    adv_params.interval    = ADV_INTERVAL;
    adv_params.timeout     = ADV_TIMEOUT;

    /* start advertising */
    err_code = sd_ble_gap_adv_start(&adv_params);
    APP_ERROR_CHECK(err_code);
}

static
uint32_t char_indicate(ble_gatts_char_handles_t char_handle, uint8_t *data,
    uint16_t len) {
    uint32_t err_code;

    /* check to make sure we're connected */
    if (m_conn_handle != BLE_CONN_HANDLE_INVALID) {

        /* setup the indication */
        ble_gatts_hvx_params_t hvx_params;
        uint16_t hvx_len = len;

        memset(&hvx_params, 0, sizeof(hvx_params));
        hvx_params.handle   = char_handle.value_handle;
        hvx_params.type     = BLE_GATT_HVX_INDICATION;
        hvx_params.offset   = 0;
        hvx_params.p_len    = &hvx_len;
        hvx_params.p_data   = data;

        err_code = sd_ble_gatts_hvx(m_conn_handle, &hvx_params);
        if ((err_code == NRF_SUCCESS) && (hvx_len != len)) {
            err_code = NRF_ERROR_DATA_SIZE;
        }
    } else {
        err_code = NRF_ERROR_INVALID_STATE;
    }

    return err_code;
}

static
void data_send(void) {
    uint32_t err_code;

    /* check to make sure that indication is on and there are no pending
     * transactions */
    uint8_t  cccd_value_buf[BLE_CCCD_VALUE_LEN];
    uint16_t len = BLE_CCCD_VALUE_LEN;
    err_code = sd_ble_gatts_value_get(
        m_data_char_handle.cccd_handle,
        0,
        &len,
        cccd_value_buf
    );
    APP_ERROR_CHECK(err_code);

    if (ble_srv_is_indication_enabled(cccd_value_buf)
        && !m_indicate_confirmation_pending) {
        /* read the sensor measurements from storage */
        uint8_t  encoded_data[APP_CFG_CHAR_READ_SIZE];
        uint16_t len;

        err_code = network_on_read_request(encoded_data, &len);
        if (err_code != NRF_SUCCESS) {
            LOG("NETWORK - READ FAILED");
            return;
        }

        uint32_t print_seqno;
        memcpy(&print_seqno,encoded_data,4);
        LOG("DATA to send seqno %u",print_seqno);

        err_code = char_indicate(m_data_char_handle, encoded_data, len);
        switch (err_code) {
            case NRF_SUCCESS:
                // Measurement was successfully sent, wait for confirmation.
                m_indicate_confirmation_pending = true;
                break;

            case NRF_ERROR_INVALID_STATE:
                // Ignore error.
                break;

            case NRF_ERROR_DATA_SIZE:
                LOG("ERROR_DATA_SIZE %u", len);
                APP_ERROR_HANDLER(err_code);
                break;

            default:
                APP_ERROR_HANDLER(err_code);
                break;
        }
    }
}

static
void on_connect(ble_evt_t * p_ble_evt) {
    /* set the connection handle */
    m_conn_handle = p_ble_evt->evt.gap_evt.conn_handle;

    /* if we just connected, we're not waiting for indicates */
    m_indicate_confirmation_pending = false;
}

static
void on_disconnect(ble_evt_t * p_ble_evt) {
    /* reset the connection */
    m_conn_handle = BLE_CONN_HANDLE_INVALID;

    /* restart advertising */
    advertising_start();
}

static
void on_cccd_write(ble_gatts_evt_write_t * p_evt_write) {
    if (p_evt_write->len == 2) {
        if (ble_srv_is_indication_enabled(p_evt_write->data)) {
            /* indication enabled */
            LOG("GATT EVT - INDICATION ENABLED");

            /* initialize the network stack */
            network_on_connect();

            /* send the first payload */
            data_send();
        } else {
            /* INDICATION DISABLED */
            LOG("GATT EVT - INDICATION DISABLED");
        }
    }
}

void on_read(ble_evt_t * p_ble_evt) {
    ble_gatts_rw_authorize_reply_params_t reply;
    memset(&reply, 0, sizeof(ble_gatts_rw_authorize_reply_params_t));
    reply.type = BLE_GATTS_AUTHORIZE_TYPE_READ;
    reply.params.read.gatt_status = BLE_GATT_STATUS_SUCCESS;

    uint8_t  time_buf[sizeof(m_global_time)];
    memcpy(time_buf,&m_global_time,sizeof(m_global_time));
    uint16_t len = sizeof(uint32_t);

    sd_ble_gatts_value_set(
        m_read_time_char_handle.value_handle,
        0,
        &len,
        time_buf
    );

    sd_ble_gatts_rw_authorize_reply(p_ble_evt->evt.gap_evt.conn_handle, &reply);
}

static
void reboot_wrapper(void *p_event, uint16_t event_len) {
    uint32_t err_code;
    uint32_t count;

    /* get the number of outstanding storage operations */
    err_code = pstorage_access_status_get(&count);
    APP_ERROR_CHECK(err_code);

    /* if no operations outstanding, reboot */
    if (count == 0) {
        sd_nvic_SystemReset();
    }

    /* otherwise, reschedule a reboot */
    err_code = app_sched_event_put(NULL, 0, reboot_wrapper);
    APP_ERROR_CHECK(err_code);
}
static void flash_wrapper(void *p_event, uint16_t event_len) {
    uint32_t current_time = sensor_get_time();
    config_persist(current_time);
}

static void* flash_data;
static uint16_t flash_data_len;
static void flash_write_wrapper(void *p_event, uint16_t event_len){
    network_on_write(flash_data,flash_data_len);
}

static
void on_write(ble_evt_t * p_ble_evt) {
    uint32_t err_code;
    ble_gatts_evt_write_t * p_evt_write =
        &p_ble_evt->evt.gatts_evt.params.write;

    if (p_evt_write->handle == m_data_char_handle.cccd_handle) {
        on_cccd_write(p_evt_write);
    }

    if (p_evt_write->handle == m_data_char_handle.value_handle) {
        /* ack recived */
        LOG("GATT EVT - ACK RECIVED");
        flash_data = (void *)p_evt_write->data;
        flash_data_len = p_evt_write->len;
        err_code = app_sched_event_put(NULL, 0, flash_write_wrapper);
        APP_ERROR_CHECK(err_code);
    }

    //if (p_evt_write->handle == m_key_char_handle.value_handle) {
    //    /* the key must be 16 bytes */
    //    APP_ERROR_CHECK_BOOL(p_evt_write->len == 16);

    //    /* change the key */
    //    err_code = config_set_key((const uint8_t *)p_evt_write->data);
    //    APP_ERROR_CHECK(err_code);
    //}

    //if (p_evt_write->handle == m_name_char_handle.value_handle) {
    //    LOG("Changing name to %d %s", p_evt_write->len, p_evt_write->data);

    //    /* change the name */
    //    err_code = config_set_name(p_evt_write->data, p_evt_write->len);
    //    APP_ERROR_CHECK(err_code);
    //}

    //if (p_evt_write->handle == m_time_char_handle.value_handle) {
    //    uint32_t time_offset;

    //    /* the time offset must be 4 bytes */
    //    APP_ERROR_CHECK_BOOL(p_evt_write->len == 4);
    //    memcpy(&time_offset, p_evt_write->data, p_evt_write->len);

    //    /* change the time offset */
    //    err_code = config_set_global_time_offset(time_offset);
    //    APP_ERROR_CHECK(err_code);

    //    LOG("Changing time offset to %lu", time_offset);
    //}

    //if (p_evt_write->handle == m_threshold_char_handle.value_handle) {
    //    uint32_t threshold;

    //    /* the threshold must be 4 bytes */
    //    APP_ERROR_CHECK_BOOL(p_evt_write->len == 4);
    //    memcpy(&threshold, p_evt_write->data, p_evt_write->len);

    //    /* change the threshold */
    //    err_code = config_set_sensor_threshold(threshold);
    //    APP_ERROR_CHECK(err_code);

    //    LOG("Changing threshold to %lu", threshold);
    //}

    //err_code = app_sched_event_put(NULL, 0, flash_wrapper);
    //APP_ERROR_CHECK(err_code);

    //if (p_evt_write->handle == m_reboot_char_handle.value_handle) {
    //    /* persist the change */
    //    LOG("Writing config to flash");

    //    /* schedule a system reset */
    //    err_code = app_sched_event_put(NULL, 0, reboot_wrapper);
    //    APP_ERROR_CHECK(err_code);
    //}
}

static
void on_hvc(ble_evt_t * p_ble_evt) {
    ble_gatts_evt_hvc_t * p_hvc = &p_ble_evt->evt.gatts_evt.params.hvc;

    if (p_hvc->handle == m_data_char_handle.value_handle) {
        LOG("GATTS EVT - INDICATION CONFIRMED");
        /* Indication confirmed */
        m_indicate_confirmation_pending = false;

        /* let the network stack know we recived the data */
        network_on_indicate();

        /* send the next packet */
        data_send();
    }
}

static
void on_ble_evt(ble_evt_t * p_ble_evt) {
    uint32_t err_code;

    switch (p_ble_evt->header.evt_id) {
        case BLE_GAP_EVT_CONNECTED:
            LOG("GAP EVT - CONNECTED");
            on_connect(p_ble_evt);
            break;

        case BLE_GAP_EVT_DISCONNECTED:
            LOG("GAP EVT - DISCONNECTED");
            on_disconnect(p_ble_evt);
            break;

        case BLE_GAP_EVT_TIMEOUT:
            LOG("GAP EVT - TIMEOUT");
            /* restart advertising if we stopped due to timeout */
            if (p_ble_evt->evt.gap_evt.params.timeout.src ==
                BLE_GAP_TIMEOUT_SRC_ADVERTISEMENT) {
                advertising_start();
            }
            break;

        case BLE_GATTS_EVT_WRITE: //80
            LOG("GATTS EVT - WRITE");
            on_write(p_ble_evt);
            break;

        case BLE_GATTS_EVT_RW_AUTHORIZE_REQUEST: //81
            // use authorization to ensure the time is up to date
            LOG("GATT EVT - READ REQUEST");
            on_read(p_ble_evt);
            break;

        case BLE_GATTS_EVT_HVC:
            LOG("GATTS EVT - HVC");
            on_hvc(p_ble_evt);
            break;

        case BLE_GATTS_EVT_SYS_ATTR_MISSING:
            LOG("GAP EVT - SYS_ATTR_MISSING");
            err_code = sd_ble_gatts_sys_attr_set(m_conn_handle, NULL, 0);
            APP_ERROR_CHECK(err_code);
            break;

        default:
            LOG("GAP EVT - Other event %u", p_ble_evt->header.evt_id);
            // No implementation needed.
            break;
    }

}

static
void ble_evt_dispatch(ble_evt_t * p_ble_evt) {
    on_ble_evt(p_ble_evt);
}

static
void sys_evt_dispatch(uint32_t sys_evt) {
    pstorage_sys_event_handler(sys_evt);
}

void gap_params_init(void) {
    uint32_t                err_code;
    ble_gap_conn_params_t   gap_conn_params;
    ble_gap_conn_sec_mode_t sec_mode;

    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&sec_mode);

    /* set the device name */
    const uint8_t * device_name = config_get_name();

    err_code = sd_ble_gap_device_name_set(
        &sec_mode,
        device_name,
        strlen((const char *)device_name)
    );
    APP_ERROR_CHECK(err_code);

    /* Set GAP Peripheral Preferred Connection Parameters */
    memset(&gap_conn_params, 0, sizeof(gap_conn_params));

    gap_conn_params.min_conn_interval = MIN_CONN_INTERVAL;
    gap_conn_params.max_conn_interval = MAX_CONN_INTERVAL;
    gap_conn_params.slave_latency     = SLAVE_LATENCY;
    gap_conn_params.conn_sup_timeout  = CONN_SUP_TIMEOUT;

    err_code = sd_ble_gap_ppcp_set(&gap_conn_params);
    APP_ERROR_CHECK(err_code);

    err_code = sd_ble_gap_tx_power_set(TX_POWER_LEVEL);
    APP_ERROR_CHECK(err_code);
}

static
ble_char_t char_add(const uint16_t uuid, const ble_service_t service, bool need_read_auth, uint8_t value_len) {

    uint32_t                 err_code;
    ble_gatts_char_md_t      char_md;
    ble_gatts_attr_md_t      cccd_md;
    ble_gatts_attr_t         attr_char_value;
    ble_uuid_t               char_uuid;
    ble_gatts_attr_md_t      attr_md;
    ble_gatts_char_handles_t char_handle;

    memset(&cccd_md, 0, sizeof(cccd_md));

    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.read_perm);
    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.write_perm);
    cccd_md.vloc = BLE_GATTS_VLOC_STACK;

    memset(&char_md, 0, sizeof(char_md));

    char_md.char_props.read     = 1;
    char_md.char_props.write    = 1;
    char_md.char_props.indicate = 1;
    char_md.p_char_user_desc    = NULL;
    char_md.p_char_pf           = NULL;
    char_md.p_user_desc_md      = NULL;
    char_md.p_cccd_md           = &cccd_md;
    char_md.p_sccd_md           = NULL;

    char_uuid.type = service.uuid_type;
    char_uuid.uuid = uuid;

    memset(&attr_md, 0, sizeof(attr_md));

    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.read_perm);
    BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.write_perm);

    attr_md.vloc    = BLE_GATTS_VLOC_STACK;
    if (need_read_auth) {
        attr_md.rd_auth = 1;
    }
    else {
        attr_md.rd_auth = 0;
    }
    attr_md.wr_auth = 0;
    attr_md.vlen    = 0;

    memset(&attr_char_value, 0, sizeof(attr_char_value));

    uint8_t init_values[value_len];
    memset(init_values, 0x00, value_len);

    attr_char_value.p_uuid    = &char_uuid;
    attr_char_value.p_attr_md = &attr_md;
    attr_char_value.init_len  = value_len;;
    attr_char_value.init_offs = 0;
    attr_char_value.max_len   = value_len;
    attr_char_value.p_value   = init_values;

    err_code = sd_ble_gatts_characteristic_add(
        service.handle,
        &char_md,
        &attr_char_value,
        &char_handle
    );
    APP_ERROR_CHECK(err_code);

    ble_char_t characteristic = {
        .vendor_uuid = uuid,
        .handle      = char_handle
    };

    return characteristic;
}

ble_service_t service_add(uint16_t vendor_uuid, bool default_base) {
    uint32_t   err_code;
    ble_uuid_t service_uuid;
    uint16_t   service_handle;

    /* initialize the uuid structure */
    memset(&service_uuid, 0, sizeof service_uuid);
    service_uuid.uuid = vendor_uuid;

    if (default_base){
        service_uuid.type = BLE_UUID_TYPE_BLE;
    }
    else {
        /* create a new ble uuid from the service uuid */
        err_code = sd_ble_uuid_vs_add(&m_base_uuid128, &service_uuid.type);
        APP_ERROR_CHECK(err_code);
    }

    /* add the service to the device */
    err_code = sd_ble_gatts_service_add(
        BLE_GATTS_SRVC_TYPE_PRIMARY,    /* Always make services primary */
        &service_uuid,
        &service_handle
    );
    APP_ERROR_CHECK(err_code);

    /* set the return values if we correctly added the service */
    ble_service_t service = {
        .vendor_uuid = vendor_uuid,
        .handle      = service_handle,
        .uuid_type   = service_uuid.type
    };

    return service;
}

void ble_disable(void) {
    uint32_t err_code;

    if (m_conn_handle != BLE_CONN_HANDLE_INVALID) {
        err_code = sd_ble_gap_disconnect(
            m_conn_handle, BLE_HCI_REMOTE_USER_TERMINATED_CONNECTION);
        APP_ERROR_CHECK(err_code);
    }

    err_code = sd_ble_gap_adv_stop();
    APP_ERROR_CHECK(err_code);

    err_code = softdevice_handler_sd_disable();
    APP_ERROR_CHECK(err_code);

    LOG("BLE disabled");
}

void ble_start(void) {
    uint32_t err_code;
    ble_gap_addr_t mac_addr;

    /* setup the gap parameters (includes device name) */
    gap_params_init();

    /* setup the data service */
    ble_service_t data_service = service_add(DATA_SERVICE_UUID,false);
    m_data_char_handle = char_add(DATA_CHAR_UUID, data_service,false,20).handle;
    m_data_name_handle = char_add(DATA_NAME_UUID, data_service,false,20).handle;

    const uint8_t *name_buffer= config_get_name();
    uint16_t name_len = strlen((const char *)name_buffer);
    sd_ble_gatts_value_set(
        m_data_name_handle.value_handle,
        0,
        &name_len,
        name_buffer
    );

    /* setup the time service */
    ble_service_t time_service = service_add(BLE_UUID_CURRENT_TIME_SERVICE,true);
    m_read_time_char_handle = char_add(BLE_UUID_CURRENT_TIME_CHAR, time_service, true,4).handle;

    /* setup the config service */
    //ble_service_t config_service = service_add(CONFIG_SERVICE_UUID,false);

    //m_time_char_handle =
    //    char_add(CONFIG_CHAR_TIME_UUID, config_service, false,4).handle;
    //m_threshold_char_handle =
    //    char_add(CONFIG_CHAR_THRESHOLD_UUID, config_service, false,1).handle;
    //m_key_char_handle =
    //    char_add(CONFIG_CHAR_DEVICE_KEY_UUID, config_service, false,16).handle;
    //m_name_char_handle =
    //    char_add(CONFIG_CHAR_NAME_UUID, config_service,false,20).handle;
    //m_reboot_char_handle =
    //    char_add(CONFIG_CHAR_REBOOT_UUID, config_service,false,1).handle;
    //char_add(CONFIG_CHAR_RESET_UUID, config_service,false,1);

    /* set up advertising */
    ble_uuid_t adv_uuids[] =
    {
        // {DATA_SERVICE_UUID,                   BLE_UUID_TYPE_BLE},
        {ADV_SERVICE_UUID,                    BLE_UUID_TYPE_BLE},
        {BLE_UUID_DEVICE_INFORMATION_SERVICE, BLE_UUID_TYPE_BLE}
    };
    advertising_init(COUNT_OF(adv_uuids), adv_uuids);

    /* start advertising */
    advertising_start();

    /* get the current MAC address */
    err_code = sd_ble_gap_address_get(&mac_addr);
    APP_ERROR_CHECK(err_code);

    LOG(
        "BLE enabled - advertising MAC %02X:%02X %02X:%02X:%02X:%02X",
        mac_addr.addr[5],
        mac_addr.addr[4],
        mac_addr.addr[3],
        mac_addr.addr[2],
        mac_addr.addr[1],
        mac_addr.addr[0]
    );
}

void ble_stack_init(void) {
    uint32_t err_code;

    /* Initialize the SoftDevice handler module. */
    SOFTDEVICE_HANDLER_INIT(NRF_CLOCK_LFCLKSRC_XTAL_20_PPM, false);

    /* Enable BLE stack */
    ble_enable_params_t ble_enable_params;
    memset(&ble_enable_params, 0, sizeof(ble_enable_params));

    ble_enable_params.gatts_enable_params.service_changed = false;
    err_code = sd_ble_enable(&ble_enable_params);
    APP_ERROR_CHECK(err_code);

    /* Register with the SoftDevice handler module for BLE events. */
    err_code = softdevice_ble_evt_handler_set(ble_evt_dispatch);
    APP_ERROR_CHECK(err_code);

    /* Register with the SoftDevice handler module for System events. */
    err_code = softdevice_sys_evt_handler_set(sys_evt_dispatch);
    APP_ERROR_CHECK(err_code);

}
0
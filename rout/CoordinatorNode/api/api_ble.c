/*
 * api_ble.c
 *
 *  Created on:11:44:58 10/12/2016
 *      Author: Ravel
 */
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include "nordic_common.h"
#include "nrf.h"
#include "app_error.h"
#include "ble.h"
#include "ble_hci.h"
#include "ble_srv_common.h"
#include "ble_advdata.h"
#include "ble_advertising.h"
#include "ble_conn_params.h"
#include "ble_conn_state.h"
#include "peer_manager.h"
#include "boards.h"
#include "softdevice_handler.h"
#include "app_timer.h"
#include "bsp_btn_ble.h"

#include "ravel_layer.h"
#include "api_timer.h"
#include "../models.h"



#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "api_ble.h"

//add models with they event data
#include "../coordinator_ctr.h"

static uint16_t m_conn_handle = BLE_CONN_HANDLE_INVALID; /**< Handle of the current connection. */
//write and notify is essentially the same
void p_phase_model__sys_write_to_BLE(ble_os_t *p_ravel_service, pPhaseModel *model) {

	if (p_ravel_service->conn_handle != BLE_CONN_HANDLE_INVALID) {
		uint16_t len = sizeof(pPhaseModel);
		ble_gatts_hvx_params_t hvx_params;
		memset(&hvx_params, 0, sizeof(hvx_params));
		hvx_params.handle = p_ravel_service->p_phase_model_handles.value_handle;
		hvx_params.type = BLE_GATT_HVX_NOTIFICATION;
		hvx_params.offset = 0;
		hvx_params.p_len = &len;
		hvx_params.p_data = (uint8_t*) model;
		sd_ble_gatts_hvx(p_ravel_service->conn_handle, &hvx_params);
	}
}
//write and notify is essentially the same
void compressed_sample_model__sys_write_to_BLE(ble_os_t *p_ravel_service, CompressedSampleModel *model) {

	if (p_ravel_service->conn_handle != BLE_CONN_HANDLE_INVALID) {
		uint16_t len = sizeof(CompressedSampleModel);
		ble_gatts_hvx_params_t hvx_params;
		memset(&hvx_params, 0, sizeof(hvx_params));
		hvx_params.handle = p_ravel_service->compressed_sample_model_handles.value_handle;
		hvx_params.type = BLE_GATT_HVX_NOTIFICATION;
		hvx_params.offset = 0;
		hvx_params.p_len = &len;
		hvx_params.p_data = (uint8_t*) model;
		sd_ble_gatts_hvx(p_ravel_service->conn_handle, &hvx_params);
	}
}
//write and notify is essentially the same
void transmit_c_s_model__sys_write_to_BLE(ble_os_t *p_ravel_service, TransmitCSModel *model) {

	if (p_ravel_service->conn_handle != BLE_CONN_HANDLE_INVALID) {
		uint16_t len = sizeof(TransmitCSModel);
		ble_gatts_hvx_params_t hvx_params;
		memset(&hvx_params, 0, sizeof(hvx_params));
		hvx_params.handle = p_ravel_service->transmit_c_s_model_handles.value_handle;
		hvx_params.type = BLE_GATT_HVX_NOTIFICATION;
		hvx_params.offset = 0;
		hvx_params.p_len = &len;
		hvx_params.p_data = (uint8_t*) model;
		sd_ble_gatts_hvx(p_ravel_service->conn_handle, &hvx_params);
	}
}

//create ble service flow arrival functions
//creating lowest level model notification about data pipeline events
inline void p_phase_model__data_flow_event(ble_os_t * p_ravel_service, ble_evt_t * p_ble_evt){
    ble_gatts_evt_write_t * p_evt_write = &p_ble_evt->evt.gatts_evt.params.write;
    NRF_LOG_INFO("p_phase_model__data_flow_event \r\n");
    if (p_evt_write->len == 2) {
    				if (ble_srv_is_notification_enabled(p_evt_write->data)) {
    					NRF_LOG_INFO("Notification enabled \r\n");
    					p_phase_model__channel_enabled();
    				} else {
    					NRF_LOG_INFO("Notification disabled \r\n");
    					p_phase_model__channel_disabled();
    				}
    			} else {
    				//TODO: create error model and save it
    				////SEGGER_RTT_printf(0, "size error!\r\n");
    			}
}
//creating lowest level model notification about data pipeline events
inline void compressed_sample_model__data_flow_event(ble_os_t * p_ravel_service, ble_evt_t * p_ble_evt){
    ble_gatts_evt_write_t * p_evt_write = &p_ble_evt->evt.gatts_evt.params.write;
    NRF_LOG_INFO("compressed_sample_model__data_flow_event \r\n");
    if (p_evt_write->len == 2) {
    				if (ble_srv_is_notification_enabled(p_evt_write->data)) {
    					NRF_LOG_INFO("Notification enabled \r\n");
    					compressed_sample_model__channel_enabled();
    				} else {
    					NRF_LOG_INFO("Notification disabled \r\n");
    					compressed_sample_model__channel_disabled();
    				}
    			} else {
    				//TODO: create error model and save it
    				////SEGGER_RTT_printf(0, "size error!\r\n");
    			}
}
//creating lowest level model notification about data pipeline events
inline void transmit_c_s_model__data_flow_event(ble_os_t * p_ravel_service, ble_evt_t * p_ble_evt){
    ble_gatts_evt_write_t * p_evt_write = &p_ble_evt->evt.gatts_evt.params.write;
    NRF_LOG_INFO("transmit_c_s_model__data_flow_event \r\n");
        NRF_LOG_INFO("model is replicated\r\n");
    	if (p_evt_write->len != sizeof(TransmitCSModel)) {
    			//TODO: create error model
    			NRF_LOG_INFO("p_evt_write->len != sizeof(TransmitCSModel)\r\n");
    		} else {
    			//save the model
    			NRF_LOG_INFO("Got Data\r\n");
    			TransmitCSModel model;
    			memcpy(&model, p_evt_write->data, sizeof(TransmitCSModel));
    			transmit_c_s_model__coordinator_ctr__arrived((void*) &model);
    		}
}

static void on_ble_write(ble_os_t * p_ravel_service, ble_evt_t * p_ble_evt) {
	ble_gatts_evt_write_t * p_evt_write = &p_ble_evt->evt.gatts_evt.params.write;

	if (p_evt_write->handle == p_ravel_service->p_phase_model_handles.cccd_handle) {
         p_phase_model__data_flow_event(p_ravel_service, p_ble_evt);
	}else if (p_evt_write->handle == p_ravel_service->compressed_sample_model_handles.value_handle){
            compressed_sample_model__data_flow_event(p_ravel_service, p_ble_evt);else if (p_evt_write->handle == p_ravel_service->transmit_c_s_model_handles.value_handle){
            transmit_c_s_model__data_flow_event(p_ravel_service, p_ble_evt); 
	 }else {
		//we in trouble here
		//TODO: create error model
		NRF_LOG_INFO("did not recognized handler\r\n");

	}
}

void ble_ravel__on_ble_evt(ble_os_t * p_ravel_service, ble_evt_t * p_ble_evt) {
	// OUR_JOB: Step 3.D Implement switch case handling BLE events related to our service.
	switch (p_ble_evt->header.evt_id) {
	case BLE_GATTS_EVT_WRITE:
		on_ble_write(p_ravel_service, p_ble_evt);
		break;
	case BLE_GAP_EVT_CONNECTED:
		p_ravel_service->conn_handle = p_ble_evt->evt.gap_evt.conn_handle;
		system_api__device_connected();
		break;
	case BLE_GAP_EVT_DISCONNECTED:
		p_ravel_service->conn_handle = BLE_CONN_HANDLE_INVALID;
		system_api__device_disconnected();
		break;
	default:
		// No implementation needed.
		break;
	}
}

static void ble_evt_dispatch(ble_evt_t * p_ble_evt) {
    ble_conn_state_on_ble_evt(p_ble_evt);
    pm_on_ble_evt(p_ble_evt);
    ble_conn_params_on_ble_evt(p_ble_evt);
    bsp_btn_ble_on_ble_evt(p_ble_evt);
    ble_advertising_on_ble_evt(p_ble_evt);
	//ble connection related
	ble_ravel__on_ble_evt(&m_ravel_service, p_ble_evt);
}

/**@brief Function for initializing the BLE stack.
 *
 * @details Initializes the SoftDevice and the BLE event interrupt.
 */
void ble_stack_init(void) {
	uint32_t err_code;

	nrf_clock_lf_cfg_t clock_lf_cfg = NRF_CLOCK_LFCLKSRC;

	// Initialize the SoftDevice handler module.
	SOFTDEVICE_HANDLER_INIT(&clock_lf_cfg, NULL);

	ble_enable_params_t ble_enable_params;
	err_code = softdevice_enable_get_default_config(CENTRAL_LINK_COUNT,
	PERIPHERAL_LINK_COUNT, &ble_enable_params);
	APP_ERROR_CHECK(err_code);

	//Check the ram settings against the used number of links
	CHECK_RAM_START_ADDR(CENTRAL_LINK_COUNT, PERIPHERAL_LINK_COUNT);

	// Enable BLE stack.
	ble_enable_params.gatts_enable_params.service_changed =
	IS_SRVC_CHANGED_CHARACT_PRESENT;
	err_code = softdevice_enable(&ble_enable_params);
	APP_ERROR_CHECK(err_code);

	// Register with the SoftDevice handler module for BLE events.
	err_code = softdevice_ble_evt_handler_set(ble_evt_dispatch);
	APP_ERROR_CHECK(err_code);

	// Register with the SoftDevice handler module for BLE events.
	err_code = softdevice_sys_evt_handler_set(sys_evt_dispatch);
	APP_ERROR_CHECK(err_code);
}

void advertising_init(void) {
	uint32_t err_code;
	ble_advdata_t advdata;
	ble_adv_modes_config_t options;
	// Build advertising data struct to pass into ble_advertising_init().
	memset(&advdata, 0, sizeof(advdata));

	advdata.name_type = BLE_ADVDATA_FULL_NAME;
	advdata.include_appearance      = true;
	advdata.flags = BLE_GAP_ADV_FLAGS_LE_ONLY_GENERAL_DISC_MODE;

	memset(&options, 0, sizeof(options));
	options.ble_adv_fast_enabled = true;
	options.ble_adv_fast_interval = APP_ADV_INTERVAL;
	options.ble_adv_fast_timeout = APP_ADV_TIMEOUT_IN_SECONDS;

    err_code = ble_advertising_init(&advdata, NULL, &options, on_adv_evt, NULL);
    APP_ERROR_CHECK(err_code);
}
/**@brief Function for starting advertising.
 */
void advertising_start(void)
{
    ret_code_t err_code;

    err_code = ble_advertising_start(BLE_ADV_MODE_FAST);
    APP_ERROR_CHECK(err_code);
}

/**@brief Function for handling advertising events.
 *
 * @details This function will be called for advertising events which are passed to the application.
 *
 * @param[in] ble_adv_evt  Advertising event.
 */
void on_adv_evt(ble_adv_evt_t ble_adv_evt) {
	uint32_t err_code;

	switch (ble_adv_evt) {
	case BLE_ADV_EVT_FAST:
		err_code = bsp_indication_set(BSP_INDICATE_ADVERTISING);
		APP_ERROR_CHECK(err_code);
		break;
	case BLE_ADV_EVT_IDLE:
		sleep_mode_enter();
		break;
	default:
		break;
	}
}


static uint32_t p_phase_model__characteristic_add(ble_os_t * p_ravel_service) {
	uint32_t err_code = 0; // Variable to hold return codes from library and softdevice functions

	// OUR_JOB: Step 2.A, Add a custom characteristic UUID
	ble_uuid_t char_uuid;
	ble_uuid128_t base_uuid = BLE_UUID_RAVEL_BASE_UUID;
	char_uuid.uuid = BLE_UUID_P_PHASE_MODEL_CHAR_UUID;
	sd_ble_uuid_vs_add(&base_uuid, &char_uuid.type);
	APP_ERROR_CHECK(err_code);

	// OUR_JOB: Step 2.F Add read/write properties to our characteristic
	ble_gatts_char_md_t char_md;
	memset(&char_md, 0, sizeof(char_md));
	char_md.char_props.auth_signed_wr = 0;
	char_md.char_props.indicate = 0;
	char_md.char_props.write_wo_resp = 0;
	char_md.char_props.broadcast = 0;
	char_md.char_props.notify = 1;
    char_md.char_props.read = 0; //only notifications
    char_md.char_props.write = 0;
	// OUR_JOB: Step 3.A, Configuring Client Characteristic Configuration Descriptor metadata and add to char_md structure
	ble_gatts_attr_md_t cccd_md;
	memset(&cccd_md, 0, sizeof(cccd_md));
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.read_perm);
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.write_perm);
	cccd_md.vloc = BLE_GATTS_VLOC_STACK;
	char_md.p_cccd_md = &cccd_md;

	// OUR_JOB: Step 2.B, Configure the attribute metadata
	ble_gatts_attr_md_t attr_md;
	memset(&attr_md, 0, sizeof(attr_md));
	attr_md.vloc = BLE_GATTS_VLOC_STACK;

	// OUR_JOB: Step 2.G, Set read/write security levels to our characteristic
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.read_perm);
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.write_perm);

	// OUR_JOB: Step 2.C, Configure the characteristic value attribute
	ble_gatts_attr_t attr_char_value;
	memset(&attr_char_value, 0, sizeof(attr_char_value));
	attr_char_value.p_uuid = &char_uuid;
	attr_char_value.p_attr_md = &attr_md;

	// OUR_JOB: Step 2.H, Set characteristic length in number of bytes
	attr_char_value.max_len = sizeof(pPhaseModel);
	attr_char_value.init_len = 0;
	attr_char_value.p_value = NULL;

	// OUR_JOB: Step 2.E, Add our new characteristic to the service
	err_code = sd_ble_gatts_characteristic_add(p_ravel_service->service_handle,
			&char_md, &attr_char_value, &p_ravel_service->p_phase_model_handles);
	APP_ERROR_CHECK(err_code);

	return NRF_SUCCESS;
}

static uint32_t compressed_sample_model__characteristic_add(ble_os_t * p_ravel_service) {
	uint32_t err_code = 0; // Variable to hold return codes from library and softdevice functions

	// OUR_JOB: Step 2.A, Add a custom characteristic UUID
	ble_uuid_t char_uuid;
	ble_uuid128_t base_uuid = BLE_UUID_RAVEL_BASE_UUID;
	char_uuid.uuid = BLE_UUID_COMPRESSED_SAMPLE_MODEL_CHAR_UUID;
	sd_ble_uuid_vs_add(&base_uuid, &char_uuid.type);
	APP_ERROR_CHECK(err_code);

	// OUR_JOB: Step 2.F Add read/write properties to our characteristic
	ble_gatts_char_md_t char_md;
	memset(&char_md, 0, sizeof(char_md));
	char_md.char_props.auth_signed_wr = 0;
	char_md.char_props.indicate = 0;
	char_md.char_props.write_wo_resp = 0;
	char_md.char_props.broadcast = 0;
	char_md.char_props.notify = 1;
    char_md.char_props.read = 0; //only notifications
    char_md.char_props.write = 0;
	// OUR_JOB: Step 3.A, Configuring Client Characteristic Configuration Descriptor metadata and add to char_md structure
	ble_gatts_attr_md_t cccd_md;
	memset(&cccd_md, 0, sizeof(cccd_md));
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.read_perm);
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.write_perm);
	cccd_md.vloc = BLE_GATTS_VLOC_STACK;
	char_md.p_cccd_md = &cccd_md;

	// OUR_JOB: Step 2.B, Configure the attribute metadata
	ble_gatts_attr_md_t attr_md;
	memset(&attr_md, 0, sizeof(attr_md));
	attr_md.vloc = BLE_GATTS_VLOC_STACK;

	// OUR_JOB: Step 2.G, Set read/write security levels to our characteristic
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.read_perm);
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.write_perm);

	// OUR_JOB: Step 2.C, Configure the characteristic value attribute
	ble_gatts_attr_t attr_char_value;
	memset(&attr_char_value, 0, sizeof(attr_char_value));
	attr_char_value.p_uuid = &char_uuid;
	attr_char_value.p_attr_md = &attr_md;

	// OUR_JOB: Step 2.H, Set characteristic length in number of bytes
	attr_char_value.max_len = sizeof(CompressedSampleModel);
	attr_char_value.init_len = 0;
	attr_char_value.p_value = NULL;

	// OUR_JOB: Step 2.E, Add our new characteristic to the service
	err_code = sd_ble_gatts_characteristic_add(p_ravel_service->service_handle,
			&char_md, &attr_char_value, &p_ravel_service->compressed_sample_model_handles);
	APP_ERROR_CHECK(err_code);

	return NRF_SUCCESS;
}

static uint32_t transmit_c_s_model__characteristic_add(ble_os_t * p_ravel_service) {
	uint32_t err_code = 0; // Variable to hold return codes from library and softdevice functions

	// OUR_JOB: Step 2.A, Add a custom characteristic UUID
	ble_uuid_t char_uuid;
	ble_uuid128_t base_uuid = BLE_UUID_RAVEL_BASE_UUID;
	char_uuid.uuid = BLE_UUID_TRANSMIT_C_S_MODEL_CHAR_UUID;
	sd_ble_uuid_vs_add(&base_uuid, &char_uuid.type);
	APP_ERROR_CHECK(err_code);

	// OUR_JOB: Step 2.F Add read/write properties to our characteristic
	ble_gatts_char_md_t char_md;
	memset(&char_md, 0, sizeof(char_md));
	char_md.char_props.auth_signed_wr = 0;
	char_md.char_props.indicate = 0;
	char_md.char_props.write_wo_resp = 0;
	char_md.char_props.broadcast = 0;
    char_md.char_props.notify = 0;
    char_md.char_props.read = 1; //replicated model, read write
    char_md.char_props.write = 1;
	// OUR_JOB: Step 3.A, Configuring Client Characteristic Configuration Descriptor metadata and add to char_md structure
	ble_gatts_attr_md_t cccd_md;
	memset(&cccd_md, 0, sizeof(cccd_md));
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.read_perm);
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&cccd_md.write_perm);
	cccd_md.vloc = BLE_GATTS_VLOC_STACK;
	char_md.p_cccd_md = &cccd_md;

	// OUR_JOB: Step 2.B, Configure the attribute metadata
	ble_gatts_attr_md_t attr_md;
	memset(&attr_md, 0, sizeof(attr_md));
	attr_md.vloc = BLE_GATTS_VLOC_STACK;

	// OUR_JOB: Step 2.G, Set read/write security levels to our characteristic
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.read_perm);
	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&attr_md.write_perm);

	// OUR_JOB: Step 2.C, Configure the characteristic value attribute
	ble_gatts_attr_t attr_char_value;
	memset(&attr_char_value, 0, sizeof(attr_char_value));
	attr_char_value.p_uuid = &char_uuid;
	attr_char_value.p_attr_md = &attr_md;

	// OUR_JOB: Step 2.H, Set characteristic length in number of bytes
	attr_char_value.max_len = sizeof(TransmitCSModel);
	attr_char_value.init_len = 0;
	attr_char_value.p_value = NULL;

	// OUR_JOB: Step 2.E, Add our new characteristic to the service
	err_code = sd_ble_gatts_characteristic_add(p_ravel_service->service_handle,
			&char_md, &attr_char_value, &p_ravel_service->transmit_c_s_model_handles);
	APP_ERROR_CHECK(err_code);

	return NRF_SUCCESS;
}


void ravel_service_init(ble_os_t * p_ravel_service) {
	uint32_t err_code; // Variable to hold return codes from library and softdevice functions

	// FROM_SERVICE_TUTORIAL: Declare 16-bit service and 128-bit base UUIDs and add them to the BLE stack
	ble_uuid_t service_uuid;
	ble_uuid128_t base_uuid = BLE_UUID_RAVEL_BASE_UUID;
	service_uuid.uuid = BLE_UUID_RAVEL;
	err_code = sd_ble_uuid_vs_add(&base_uuid, &service_uuid.type);
	APP_ERROR_CHECK(err_code);

	// OUR_JOB: Step 3.B, Set our service connection handle to default value. I.e. an invalid handle since we are not yet in a connection.
	p_ravel_service->conn_handle = BLE_CONN_HANDLE_INVALID;
	p_ravel_service->evt_handler = m_ravel_evt_handler;

	err_code = sd_ble_gatts_service_add(BLE_GATTS_SRVC_TYPE_PRIMARY,
			&service_uuid, &p_ravel_service->service_handle);

	APP_ERROR_CHECK(err_code);

	// OUR_JOB: Call the function our_char_add() to add our new characteristic to the service.
	 p_phase_model__characteristic_add(p_ravel_service);
	 compressed_sample_model__characteristic_add(p_ravel_service);
	 transmit_c_s_model__characteristic_add(p_ravel_service);
}

void gap_params_init(void) {
	uint32_t err_code;
	ble_gap_conn_params_t gap_conn_params;
	ble_gap_conn_sec_mode_t sec_mode;

	BLE_GAP_CONN_SEC_MODE_SET_OPEN(&sec_mode);

	err_code = sd_ble_gap_device_name_set(&sec_mode,
			(const uint8_t *) DEVICE_NAME, strlen(DEVICE_NAME));
	APP_ERROR_CHECK(err_code);

	memset(&gap_conn_params, 0, sizeof(gap_conn_params));

	gap_conn_params.min_conn_interval = MIN_CONN_INTERVAL;
	gap_conn_params.max_conn_interval = MAX_CONN_INTERVAL;
	gap_conn_params.slave_latency = SLAVE_LATENCY;
	gap_conn_params.conn_sup_timeout = CONN_SUP_TIMEOUT;

	err_code = sd_ble_gap_ppcp_set(&gap_conn_params);
	APP_ERROR_CHECK(err_code);
}


/**@brief Function for initializing services that will be used by the application.
 */
void services_init(void) {
	// FROM_SERVICE_TUTORIAL: Add code to initialize the services used by the application.
	ravel_service_init(&m_ravel_service);
}

static void on_conn_params_evt(ble_conn_params_evt_t * p_evt) {
	uint32_t err_code;

	if (p_evt->evt_type == BLE_CONN_PARAMS_EVT_FAILED) {
		err_code = sd_ble_gap_disconnect(m_conn_handle,
		BLE_HCI_CONN_INTERVAL_UNACCEPTABLE);
		APP_ERROR_CHECK(err_code);
	}
}

static void conn_params_error_handler(uint32_t nrf_error) {
	APP_ERROR_HANDLER(nrf_error);
}

/**@brief Function for initializing the Connection Parameters module.
 */
void conn_params_init(void) {
	uint32_t err_code;
	ble_conn_params_init_t cp_init;

	memset(&cp_init, 0, sizeof(cp_init));

	cp_init.p_conn_params = NULL;
	cp_init.first_conn_params_update_delay = FIRST_CONN_PARAMS_UPDATE_DELAY;
	cp_init.next_conn_params_update_delay = NEXT_CONN_PARAMS_UPDATE_DELAY;
	cp_init.max_conn_params_update_count = MAX_CONN_PARAMS_UPDATE_COUNT;
	cp_init.start_on_notify_cccd_handle = BLE_GATT_HANDLE_INVALID;
	cp_init.disconnect_on_fail = false;
	cp_init.evt_handler = on_conn_params_evt;
	cp_init.error_handler = conn_params_error_handler;

	err_code = ble_conn_params_init(&cp_init);
	APP_ERROR_CHECK(err_code);
}
// *********** END AUTOGENERATED **********//

/*
 * api_ble.h
 *
 *  Created on: 11:44:58 10/12/2016
 *      Author: Ravel
 */

#ifndef API_API_BLE_H_
#define API_API_BLE_H_
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




#define MIN_CONN_INTERVAL                MSEC_TO_UNITS(100, UNIT_1_25_MS)           /**< Minimum acceptable connection interval (0.1 seconds). */
#define MAX_CONN_INTERVAL                MSEC_TO_UNITS(200, UNIT_1_25_MS)           /**< Maximum acceptable connection interval (0.2 second). */
#define SLAVE_LATENCY                    0                                          /**< Slave latency. */
#define CONN_SUP_TIMEOUT                 MSEC_TO_UNITS(4000, UNIT_10_MS)            /**< Connection supervisory timeout (4 seconds). */

#define APP_TIMER_PRESCALER              0

#define FIRST_CONN_PARAMS_UPDATE_DELAY   APP_TIMER_TICKS(5000, APP_TIMER_PRESCALER) /**< Time from initiating event (connect or start of notification) to first time sd_ble_gap_conn_param_update is called (5 seconds). */
#define NEXT_CONN_PARAMS_UPDATE_DELAY    APP_TIMER_TICKS(30000, APP_TIMER_PRESCALER)/**< Time between each call to sd_ble_gap_conn_param_update after the first call (30 seconds). */
#define MAX_CONN_PARAMS_UPDATE_COUNT     3                                          /**< Number of attempts before giving up the connection parameter negotiation. */

#define SEC_PARAM_BOND                   1                                          /**< Perform bonding. */
#define SEC_PARAM_MITM                   0                                          /**< Man In The Middle protection not required. */
#define SEC_PARAM_IO_CAPABILITIES        BLE_GAP_IO_CAPS_NONE                       /**< No I/O capabilities. */
#define SEC_PARAM_OOB                    0                                          /**< Out Of Band data not available. */
#define SEC_PARAM_MIN_KEY_SIZE           7                                          /**< Minimum encryption key size. */
#define SEC_PARAM_MAX_KEY_SIZE           16                                         /**< Maximum encryption key size. */


#define IS_SRVC_CHANGED_CHARACT_PRESENT  1                                          /**< Include or not the service_changed characteristic. if not enabled, the server's database cannot be changed for the lifetime of the device*/

#define CENTRAL_LINK_COUNT               0                                          /**<number of central links used by the application. When changing this number remember to adjust the RAM settings*/
#define PERIPHERAL_LINK_COUNT            1                                          /**<number of peripheral links used by the application. When changing this number remember to adjust the RAM settings*/

#define DEVICE_NAME                      "Ravel"                          /**< Name of device. Will be included in the advertising data. */
#define MANUFACTURER_NAME                "NordicDev"                      /**< Manufacturer. Will be passed to Device Information Service. */
#define APP_ADV_INTERVAL                 300                                        /**< The advertising interval (in units of 0.625 ms. This value corresponds to 25 ms). */
#define APP_ADV_TIMEOUT_IN_SECONDS       180                                        /**< The advertising timeout in units of seconds. */
#define MAX_RM_LEN   (BLE_L2CAP_MTU_DEF - OPCODE_LENGTH - HANDLE_LENGTH)



#define BLE_UUID_RAVEL_BASE_UUID              {{ 0x00, 0x00, 0x00, 0x00, 0x24, 0x90, 0x66, 0x76, 0x42, 0x50, 0x83, 0x72, 0x00, 0x00, 0x10, 0x78 }} // 128-bit base UUID
//1212-efde-1523-785fef13d123 1000-8000-00805f9b34fb
#define BLE_UUID_RAVEL                0x1000 // Just a random, but recognizable value


void sys_evt_dispatch(uint32_t sys_evt);
void sleep_mode_enter(void);

//TODO: works for up to 10
#define BLE_UUID_VIBRATION_MODEL_CHAR_UUID 0x1100
#define BLE_UUID_P_PHASE_MODEL_CHAR_UUID 0x1101
#define BLE_UUID_COMPRESSED_SAMPLE_MODEL_CHAR_UUID 0x1102
#define BLE_UUID_TRANSMIT_C_S_MODEL_CHAR_UUID 0x1103


typedef enum
{    //TODO: fix comma separation
    <<<< ERROR in model type! \>>\>

    //streaming models have notifications
    BLE_P_PHASE_MODEL_NOTIFICATION_ENABLED,
    BLE_P_PHASE_MODEL_NOTIFICATION_DISABLED,

    //streaming models have notifications
    BLE_COMPRESSED_SAMPLE_MODEL_NOTIFICATION_ENABLED,
    BLE_COMPRESSED_SAMPLE_MODEL_NOTIFICATION_DISABLED,

    // replicated model have read/write
    BLE_TRANSMIT_C_S_MODEL_READ,
    BLE_TRANSMIT_C_S_MODEL_WRITE

} ble_rand_mod_evt_type_t;


typedef struct
{
	ble_rand_mod_evt_type_t evt_type;                        /**< Type of event. */
} ble_ravel_evt_t;


/**
 * @brief This structure contains various status information for our service.
 * It only holds one entry now, but will be populated with more items as we go.
 * The name is based on the naming convention used in Nordic's SDKs.
 * 'ble’ indicates that it is a Bluetooth Low Energy relevant structure and
 * ‘os’ is short for Our Service).
 */
typedef struct
{
	ble_ravel_evt_t			    evt_handler;
	uint16_t                    conn_handle;
    uint16_t    				service_handle;     /**< Handle of Our Service (as provided by the BLE stack). */
    //add a handler for each model
    ble_gatts_char_handles_t vibration_model_handles; 
    ble_gatts_char_handles_t p_phase_model_handles; 
    ble_gatts_char_handles_t compressed_sample_model_handles; 
    ble_gatts_char_handles_t transmit_c_s_model_handles; 
}ble_os_t;

ble_os_t m_ravel_service;
ble_ravel_evt_t m_ravel_evt_handler;

//Ravel based functions
void ravel_service_init(ble_os_t * p_service);

//Event form which calls are dispached to arrived stuff
void ble_ravel__on_ble_evt(ble_os_t * p_service, ble_evt_t * p_ble_evt);

//command for writting to the ble
void random_model__sys_write_to_BLE(ble_os_t *p_our_service, RandomModel *model);

//base BLE functions for NRF
void ble_stack_init(void);
void ble_stack_init();
void gap_params_init();
void services_init();
void conn_params_init();
void on_adv_evt(ble_adv_evt_t ble_adv_evt);
void advertising_init(void);
void advertising_start(void);

// *********** END AUTOGENERATED **********//
#endif /* API_API_BLE_H_ */

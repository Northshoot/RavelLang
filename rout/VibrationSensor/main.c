/**
 * Autogenerated main file
 *
 *
 *  Created on: 11:44:58 10/12/2016
 *      Author: Ravel
 */

//standard includes
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include "nordic_common.h"
#include "nrf.h"
#include "app_error.h"
#include "boards.h"
#include "softdevice_handler.h"
#include "app_timer.h"
#include "peer_manager.h"
#include "ble_conn_state.h"
#include "fds.h"
#include "fstorage.h"
#include "bsp.h"
#include "bsp_btn_ble.h"

#define NRF_LOG_MODULE_NAME "APP"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

///Ravel includes
#include "api/api_ble.h"
#include "api/ravel_layer.h"
//include all controllers
#include "node_ctr.h" 

//we put all models to one file
#include "models.h"
//runtime variables
#define USE_APP_CONFIG 1


#define DEAD_BEEF                        0xDEADBEEF                                 /** Value used as error code on stack dump, can be used to identify stack location on stack unwind. */
static uint16_t                          m_conn_handle = BLE_CONN_HANDLE_INVALID;   /** Handle of the current connection. */


void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name)
{
    app_error_handler(DEAD_BEEF, line_num, p_file_name);
}
void sleep_mode_enter(void)
{
   uint32_t err_code = bsp_indication_set(BSP_INDICATE_IDLE);
   APP_ERROR_CHECK(err_code);

   // Prepare wakeup buttons.
   err_code = bsp_btn_ble_sleep_mode_prepare();
   APP_ERROR_CHECK(err_code);

   // Go to system-off mode (this function will not return; wakeup will cause a reset).
   err_code = sd_power_system_off();
   APP_ERROR_CHECK(err_code);
}
/**@brief Function for handling File Data Storage events.
 *
 * @param[in] p_evt  Peer Manager event.
 * @param[in] cmd
 */
static void fds_evt_handler(fds_evt_t const * const p_evt)
{
    if (p_evt->id == FDS_EVT_GC)
    {
        //NRF_LOG_DEBUG("GC completed\n");
    }
}

/**@brief Function for the Power manager.
 */
static void power_manage(void)
{
    uint32_t err_code = sd_app_evt_wait();
    APP_ERROR_CHECK(err_code);
}

/**@brief Function for handling events from the BSP module.
 *
 * @param[in]   event   Event generated by button press.
 */
void bsp_event_handler(bsp_event_t event)
{
    uint32_t err_code;
    switch (event)
    {
        case BSP_EVENT_SLEEP:
            sleep_mode_enter();
            break;

        case BSP_EVENT_DISCONNECT:
            err_code = sd_ble_gap_disconnect(m_conn_handle, BLE_HCI_REMOTE_USER_TERMINATED_CONNECTION);
            if (err_code != NRF_ERROR_INVALID_STATE)
            {
                APP_ERROR_CHECK(err_code);
            }
            break;

        case BSP_EVENT_WHITELIST_OFF:
            err_code = ble_advertising_restart_without_whitelist();
            if (err_code != NRF_ERROR_INVALID_STATE)
            {
                APP_ERROR_CHECK(err_code);
            }
            break;

        default:
            break;
    }
}
void sys_evt_dispatch(uint32_t sys_evt)
{
	 fs_sys_event_handler(sys_evt);
    ble_advertising_on_sys_evt(sys_evt);
}
/**@brief Function for handling Peer Manager events.
 *
 * @param[in] p_evt  Peer Manager event.
 */
static void pm_evt_handler(pm_evt_t const * p_evt)
{
    ret_code_t err_code;

    switch (p_evt->evt_id)
    {
        case PM_EVT_BONDED_PEER_CONNECTED:
            NRF_LOG_DEBUG("Connected to previously bonded device\r\n");
            err_code = pm_peer_rank_highest(p_evt->peer_id);
            if (err_code != NRF_ERROR_BUSY)
            {
                APP_ERROR_CHECK(err_code);
            }
            break; // PM_EVT_BONDED_PEER_CONNECTED

        case PM_EVT_CONN_SEC_START:
            break; // PM_EVT_CONN_SEC_START

        case PM_EVT_CONN_SEC_SUCCEEDED:
            NRF_LOG_DEBUG("Link secured. Role: %d. conn_handle: %d, Procedure: %d\r\n",
                                 ble_conn_state_role(p_evt->conn_handle),
                                 p_evt->conn_handle,
                                 p_evt->params.conn_sec_succeeded.procedure);
            err_code = pm_peer_rank_highest(p_evt->peer_id);
            if (err_code != NRF_ERROR_BUSY)
            {
                APP_ERROR_CHECK(err_code);
            }
            break;  // PM_EVT_CONN_SEC_SUCCEEDED

        case PM_EVT_CONN_SEC_FAILED:

            /** In some cases, when securing fails, it can be restarted directly. Sometimes it can
             *  be restarted, but only after changing some Security Parameters. Sometimes, it cannot
             *  be restarted until the link is disconnected and reconnected. Sometimes it is
             *  impossible, to secure the link, or the peer device does not support it. How to
             *  handle this error is highly application dependent. */
            switch (p_evt->params.conn_sec_failed.error)
            {
                case PM_CONN_SEC_ERROR_PIN_OR_KEY_MISSING:
                    // Rebond if one party has lost its keys.
                    err_code = pm_conn_secure(p_evt->conn_handle, true);
                    if (err_code != NRF_ERROR_INVALID_STATE)
                    {
                        APP_ERROR_CHECK(err_code);
                    }
                    break;

                default:
                    break;
            }
            break; // PM_EVT_CONN_SEC_FAILED

        case PM_EVT_CONN_SEC_CONFIG_REQ:
        {
            // Reject pairing request from an already bonded peer.
            pm_conn_sec_config_t conn_sec_config = {.allow_repairing = false};
            pm_conn_sec_config_reply(p_evt->conn_handle, &conn_sec_config);
        } break; // PM_EVT_CONN_SEC_CONFIG_REQ

        case PM_EVT_STORAGE_FULL:
            // Run garbage collection on the flash.
            err_code = fds_gc();
            if (err_code != NRF_ERROR_BUSY)
            {
                APP_ERROR_CHECK(err_code);
            }
            break; // PM_EVT_STORAGE_FULL

        case PM_EVT_ERROR_UNEXPECTED:
            // A likely fatal error occurred. Assert.
            APP_ERROR_CHECK(p_evt->params.error_unexpected.error);
            break;

        case PM_EVT_PEER_DATA_UPDATE_SUCCEEDED:
            break; // PM_EVT_PEER_DATA_UPDATE_SUCCEEDED

        case PM_EVT_PEER_DATA_UPDATE_FAILED:
            // Assert.
            APP_ERROR_CHECK_BOOL(false);
            break; // PM_EVT_ERROR_UNEXPECTED

        case PM_EVT_PEER_DELETE_SUCCEEDED:
            break; // PM_EVT_PEER_DELETE_SUCCEEDED

        case PM_EVT_PEER_DELETE_FAILED:
            // Assert.
            APP_ERROR_CHECK(p_evt->params.peer_delete_failed.error);
            break; // PM_EVT_PEER_DELETE_FAILED

        case PM_EVT_PEERS_DELETE_SUCCEEDED:
            advertising_start();
            break; // PM_EVT_PEERS_DELETE_SUCCEEDED

        case PM_EVT_PEERS_DELETE_FAILED:
            // Assert.
            APP_ERROR_CHECK(p_evt->params.peers_delete_failed_evt.error);
            break; // PM_EVT_PEERS_DELETE_FAILED

        case PM_EVT_LOCAL_DB_CACHE_APPLIED:
            break; // PM_EVT_LOCAL_DB_CACHE_APPLIED

        case PM_EVT_LOCAL_DB_CACHE_APPLY_FAILED:
            // The local database has likely changed, send service changed indications.
            pm_local_database_has_changed();
            break; // PM_EVT_LOCAL_DB_CACHE_APPLY_FAILED

        case PM_EVT_SERVICE_CHANGED_IND_SENT:
            break; // PM_EVT_SERVICE_CHANGED_IND_SENT

        case PM_EVT_SERVICE_CHANGED_IND_CONFIRMED:
            break; // PM_EVT_SERVICE_CHANGED_IND_SENT

        default:
            // No implementation needed.
            break;
    }
}
/**@brief Function for the Peer Manager initialization.
 *
 * @param[in] erase_bonds  Indicates whether bonding information should be cleared from
 *                         persistent storage during initialization of the Peer Manager.
 */
static void peer_manager_init(bool erase_bonds)
{
    ble_gap_sec_params_t sec_param;
    ret_code_t           err_code;

    err_code = pm_init();
    APP_ERROR_CHECK(err_code);

    if (erase_bonds)
    {
        err_code = pm_peers_delete();
        APP_ERROR_CHECK(err_code);
    }

    memset(&sec_param, 0, sizeof(ble_gap_sec_params_t));

    // Security parameters to be used for all security procedures.
    sec_param.bond           = SEC_PARAM_BOND;
    sec_param.mitm           = SEC_PARAM_MITM;
    sec_param.io_caps        = SEC_PARAM_IO_CAPABILITIES;
    sec_param.oob            = SEC_PARAM_OOB;
    sec_param.min_key_size   = SEC_PARAM_MIN_KEY_SIZE;
    sec_param.max_key_size   = SEC_PARAM_MAX_KEY_SIZE;
    sec_param.kdist_own.enc  = 1;
    sec_param.kdist_own.id   = 1;
    sec_param.kdist_peer.enc = 1;
    sec_param.kdist_peer.id  = 1;

    err_code = pm_sec_params_set(&sec_param);
    APP_ERROR_CHECK(err_code);

    err_code = pm_register(pm_evt_handler);
    APP_ERROR_CHECK(err_code);

    err_code = fds_register(fds_evt_handler);
    APP_ERROR_CHECK(err_code);
}
/**@brief Function for initializing buttons and leds.
 *
 * @param[out] p_erase_bonds  Will be true if the clear bonding button was pressed to wake the application up.
 */
static void buttons_leds_init(bool * p_erase_bonds)
{
    bsp_event_t startup_event;

    uint32_t err_code = bsp_init(BSP_INIT_LED | BSP_INIT_BUTTONS,
                                 APP_TIMER_TICKS(100, APP_TIMER_PRESCALER),
                                 bsp_event_handler);
    APP_ERROR_CHECK(err_code);

    err_code = bsp_btn_ble_init(NULL, &startup_event);
    APP_ERROR_CHECK(err_code);

    *p_erase_bonds = (startup_event == BSP_EVENT_CLEAR_BONDING_DATA);
}


//make the main function
int main(void)
{

    bool erase_bonds;
    uint32_t err_code;
    //timer must be one of first to be initialized, we always need it
    timers_init();

    err_code = NRF_LOG_INIT(NULL);
    APP_ERROR_CHECK(err_code);

    // Initialize.

    buttons_leds_init(&erase_bonds);

    ble_stack_init();
    gap_params_init();
    services_init();
    advertising_init();
    conn_params_init();
    peer_manager_init(erase_bonds);
    if (erase_bonds == true)
    {
        NRF_LOG_INFO("Bonds erased!\r\n");
    }
 // Start advertising.
     advertising_start();

     // from there in we count as booted
     //is there boot event call?
     //init all models
     void vibration_model__innit();

     void p_phase_model__innit();

     void compressed_sample_model__innit();

     void transmit_c_s_model__innit();


     //set model dependencies and variables
     //ConfigModel cm;
     //config_model__first(&cm);
     //random_model_ctr__set_config_model(&cm);

     //init sources
    timers_init();


     //then boot events

     //ex: random_model_ctr__system_booted(&rctx);

     NRF_LOG_INFO("Hello!\r\n");
     // Enter main loop.
     for (;;)
     {
         if (NRF_LOG_PROCESS() == false)
           {
               power_manage();
           };
     }
}
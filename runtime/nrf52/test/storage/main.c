/* Copyright (c) 2014 Nordic Semiconductor. All Rights Reserved.
 *
 * The information contained herein is property of Nordic Semiconductor ASA.
 * Terms and conditions of usage are described in detail in NORDIC
 * SEMICONDUCTOR STANDARD SOFTWARE LICENSE AGREEMENT.
 *
 * Licensees are granted free, non-transferable use of the information. NO
 * WARRANTY of ANY KIND is provided. This heading must NOT be removed from
 * the file.
 *
 */

/** @file
 *
 * @defgroup ble_sdk_app_proximity_main main.c
 * @{
 * @ingroup ble_sdk_app_proximity_eval
 * @brief Proximity Application main file.
 *
 * This file contains is the source code for a sample proximity application using the
 * Immediate Alert, Link Loss and Tx Power services.
 *
 * This application would accept pairing requests from any peer device.
 *
 * It demonstrates the use of fast and slow advertising intervals.
 */

#include <stdbool.h>
#include <stdint.h>
#include <string.h>

#include "nordic_common.h"
#include "nrf.h"
#include "app_error.h"
#include "nrf_gpio.h"
#include "softdevice_handler.h"
#include "boards.h"



#define NRF_LOG_MODULE_NAME "APP"
#define NRF_LOG_LEVEL 4
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#define DEAD_BEEF                           0xDEADBEEF                                   /**< Value used as error code on stack dump, can be used to identify stack location on stack unwind. */

#define SCHED_MAX_EVENT_DATA_SIZE       sizeof(ravel_schedule_event_cntx)
#define SCHED_QUEUE_SIZE                20
typedef struct {
    void *data1;
    void *data2;
    void (*callback) (void*, void*);
} ravel_schedule_event_cntx;



void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name)
{
    app_error_handler(DEAD_BEEF, line_num, p_file_name);
}

static void sys_evt_dispatch(uint32_t sys_evt)
{
    fs_sys_event_handler(sys_evt);
}


int main(void)
{
    uint32_t err_code;
    bool m_read_test = true;

    // Initialize.
    err_code = NRF_LOG_INIT(NULL);
    APP_ERROR_CHECK(err_code);
    NRF_LOG_DEBUG("Starting up \r\n");
    nrf_clock_lf_cfg_t clock_lf_cfg = NRF_CLOCK_LFCLKSRC;

    // Initialize the SoftDevice handler module.
    SOFTDEVICE_HANDLER_INIT(&clock_lf_cfg, NULL);
    err_code = softdevice_sys_evt_handler_set(sys_evt_dispatch);
    APP_SCHED_INIT(SCHED_MAX_EVENT_DATA_SIZE, SCHED_QUEUE_SIZE);

    APP_ERROR_CHECK(err_code);
    err_code =nrf52_ravel_fs_init();
    APP_ERROR_CHECK(err_code);
    if(!m_read_test){
        //TODO: write in batch
        err_code =fds_test_write();
        APP_ERROR_CHECK(err_code);
    } else {
        //TODO: read in batch
    //wait until the write is finished.
    //while (write_flag==0);
        fds_read();
    }
//    err_code = fds_test_find_and_delete();
//    APP_ERROR_CHECK(err_code);



    // Start execution.


    // Enter main loop.
    for (;;)
    {
        app_sched_execute();
    }
}
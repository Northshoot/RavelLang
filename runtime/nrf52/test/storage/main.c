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

#include <stdint.h>
#include <string.h>

#include "nordic_common.h"
#include "nrf.h"
#include "nrf_soc.h"
#include "app_error.h"

#include "softdevice_handler.h"
#include "app_timer.h"
#include "app_util.h"
#define NRF_LOG_MODULE_NAME "FS"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "fds.h"
#include "fstorage.h"



#include "nrf_drv_saadc.h"



#define APP_TIMER_PRESCALER                 0                                            /**< Value of the RTC1 PRESCALER register. */
#define APP_TIMER_OP_QUEUE_SIZE             6                                            /**< Size of timer operation queues. */

#define DEAD_BEEF                           0xDEADBEEF                                   /**< Value used as error code on stack dump, can be used to identify stack location on stack unwind. */


static volatile uint8_t write_flag=0;






void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name)
{
    app_error_handler(DEAD_BEEF, line_num, p_file_name);
}






/**@brief Function for the Power manager.
 */
static void power_manage(void)
{
    uint32_t err_code = sd_app_evt_wait();

    APP_ERROR_CHECK(err_code);
}


static void my_fds_evt_handler(fds_evt_t const * const p_fds_evt)
{
    switch (p_fds_evt->id)
    {
        case FDS_EVT_INIT:
            if (p_fds_evt->result != FDS_SUCCESS)
            {
                // Initialization failed.
            }
            break;
        case FDS_EVT_WRITE:
            if (p_fds_evt->result == FDS_SUCCESS)
            {
                write_flag=1;
            }
            break;
        default:
            break;
    }
}

static ret_code_t fds_test_write(void)
{
#define FILE_ID     0x1111
#define REC_KEY     0x2222
    static uint32_t const m_deadbeef[2] = {0xDEADBEEF,0xBAADF00D};
    fds_record_t        record;
    fds_record_desc_t   record_desc;
    fds_record_chunk_t  record_chunk;
    // Set up data.
    record_chunk.p_data         = m_deadbeef;
    record_chunk.length_words   = 2;
    // Set up record.
    record.file_id              = FILE_ID;
    record.key              		= REC_KEY;
    record.data.p_chunks       = &record_chunk;
    record.data.num_chunks   = 1;

    ret_code_t ret = fds_record_write(&record_desc, &record);
    if (ret != FDS_SUCCESS)
    {
        return ret;
    }
    NRF_LOG_INFO("Writing Record ID = %d \r\n",record_desc.record_id);
    return NRF_SUCCESS;
}

static ret_code_t fds_read(void)
{
#define FILE_ID     0x1111
#define REC_KEY     0x2222
    fds_flash_record_t  flash_record;
    fds_record_desc_t   record_desc;
    fds_find_token_t    ftok ={0};//Important, make sure you zero init the ftok token
    uint32_t *data;
    uint32_t err_code;

    NRF_LOG_INFO("Start searching... \r\n");
    // Loop until all records with the given key and file ID have been found.
    while (fds_record_find(FILE_ID, REC_KEY, &record_desc, &ftok) == FDS_SUCCESS)
    {
        err_code = fds_record_open(&record_desc, &flash_record);
        if ( err_code != FDS_SUCCESS)
        {
            return err_code;
        }

        NRF_LOG_INFO("Found Record ID = %d\r\n",record_desc.record_id);
        NRF_LOG_INFO("Data = ");
        data = (uint32_t *) flash_record.p_data;
        for (uint8_t i=0;i<flash_record.p_header->tl.length_words;i++)
        {
            NRF_LOG_INFO("0x%8x ",data[i]);
        }
        NRF_LOG_INFO("\r\n");
        // Access the record through the flash_record structure.
        // Close the record when done.
        err_code = fds_record_close(&record_desc);
        if (err_code != FDS_SUCCESS)
        {
            return err_code;
        }
    }
    return NRF_SUCCESS;

}


static ret_code_t fds_test_find_and_delete (void)
{
#define FILE_ID     0x1111
#define REC_KEY     0x2222
    fds_record_desc_t   record_desc;
    fds_find_token_t    ftok;

    ftok.page=0;
    ftok.p_addr=NULL;
    // Loop and find records with same ID and rec key and mark them as deleted.
    while (fds_record_find(FILE_ID, REC_KEY, &record_desc, &ftok) == FDS_SUCCESS)
    {
        fds_record_delete(&record_desc);
        NRF_LOG_INFO("Deleted record ID: %d \r\n",record_desc.record_id);
    }
    // call the garbage collector to empty them, don't need to do this all the time, this is just for demonstration
    ret_code_t ret = fds_gc();
    if (ret != FDS_SUCCESS)
    {
        return ret;
    }
    return NRF_SUCCESS;
}

static ret_code_t fds_test_init (void)
{

    ret_code_t ret = fds_register(my_fds_evt_handler);
    if (ret != FDS_SUCCESS)
    {
        return ret;

    }
    ret = fds_init();
    if (ret != FDS_SUCCESS)
    {
        return ret;
    }

    return NRF_SUCCESS;

}


int main(void)
{
    uint32_t err_code;

    // Initialize.
    err_code = NRF_LOG_INIT(NULL);
    APP_ERROR_CHECK(err_code);
    NRF_LOG_INFO("FLASH TEST\r\n");
    err_code =fds_test_init();
    APP_ERROR_CHECK(err_code);
    err_code = fds_test_find_and_delete();
    APP_ERROR_CHECK(err_code);
    err_code =fds_test_write();
    APP_ERROR_CHECK(err_code);
    //wait until the write is finished.
    while (write_flag==0);
    fds_read();


    // Enter main loop.
    for (;;)
    {
        power_manage();
    }
}

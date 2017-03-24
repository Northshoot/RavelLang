//
// Created by lauril on 3/24/17.
//

#include "nrf_ravel_fs.h"
#include <stdbool.h>
#include <stdint.h>
#include <string.h>

#include "nordic_common.h"
#include "nrf.h"
#include "app_error.h"
#include "nrf_gpio.h"
#include "softdevice_handler.h"
#include "boards.h"

#include "fds.h"
#include "fstorage.h"

#define NRF_LOG_MODULE_NAME "RFS"
#define NRF_LOG_LEVEL 4
#include "nrf_log.h"
#include "nrf_log_ctrl.h"


#define FDS_VIRTUAL_PAGE_SIZE 1024

static volatile uint8_t write_flag=0; //generic write flag
static bool m_writing_config = false;
static bool m_queue_empty = true;

void write_done( void );
static void callback_event_handler(void *p_event_data, uint16_t event_size);


typedef struct {
    uint8_t indx;
    uint8_t value;
} data_to_store_t;

#define RAVEL_CONFIG_STORE_FILE_ID 0x2001
typedef struct {
    uint8_t indx;
    uint32_t value;
    uint32_t value1;
    uint8_t value2;
    uint8_t value3;
} ravel_config_store_t;

#define RAVEL_MODEL_STORE_FILE_ID 0x2002


void write_done( void );


static void nrf52_ravel_fs_fds_evt_handler(fds_evt_t const * const p_fds_evt)
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
                if(m_writing_config)
                    app_sched_event_put(NULL,0, write_config_done);
                void write_done( ) ;
                write_flag=1;
            }
            break;
        default:
            break;
    }
}

//save node configuration
void nrf52_ravel_fs_save_node_config(){
    app_sched_event_put(&data,sizeof(ravel_schedule_event_cntx), callback_event_handler);

}
//Record data can consist of multiple chunks.
//The data must be aligned to a 4 byte boundary,
// and because it is not buffered internally,
// it must be kept in memory until the callback
// for the operation has been received.
// The length of the data must not exceed FDS_VIRTUAL_PAGE_SIZE words minus 14 bytes.
uint32_t nrf52_ravel_fs_save (uint32_t *data, size_t length, uint16_t modelID, uint16_t recID)
{
    //TODO: return proper error
    if(length > FDS_VIRTUAL_PAGE_SIZE )
            return NRF_ERROR;
    fds_record_t        record;
    fds_record_desc_t   record_desc;
    fds_record_chunk_t  record_chunk;
    // Set up data.
    record_chunk.p_data         = &data;
    record_chunk.length_words   = length;
    // Set up record.
    record.file_id              = FILE_ID;
    record.key              	= REC_KEY;
    record.data.p_chunks        = &record_chunk;
    record.data.num_chunks      = 1;

    ret_code_t ret = fds_record_write(&record_desc, &record);
    if (ret != FDS_SUCCESS)
    {
        return ret;
    }
    NRF_LOG_DEBUG("Writing Record ID = %d \r\n",record_desc.record_id);
    return NRF_SUCCESS;
}

uint32_t nrf52_ravel_fs_find_and_get (uint32_t *data, uint16_t modelID, uint16_t recID)
{

    fds_flash_record_t  flash_record;
    fds_record_desc_t   record_desc;
    fds_find_token_t    ftok ={0};//Important, make sure you zero init the ftok token
    uint32_t err_code;

    NRF_LOG_DEBUG("Start searching... \r\n");
    // Loop until all records with the given key and file ID have been found.
    while (fds_record_find(modelID, recID, &record_desc, &ftok) == FDS_SUCCESS)
    {
        err_code = fds_record_open(&record_desc, &flash_record);
        if ( err_code != FDS_SUCCESS)
        {
            return err_code;
        }

        NRF_LOG_DEBUG("Found Record ID = %d\r\n",record_desc.record_id);
        NRF_LOG_DEBUG("Data = \r\n");
        memcpy(data, flash_record.p_data, flash_record.p_header->tl.length_words);
        NRF_LOG_HEXDUMP_ERROR(data,flash_record.p_header->tl.length_words );

        // Close the record when done.
        err_code = fds_record_close(&record_desc);
        if (err_code != FDS_SUCCESS)
        {
            return err_code;
        }
    }
    return NRF_SUCCESS;

}

uint32_t nrf52_ravel_fs_find_and_delete (uint16_t modelID, uint16_t recID)
{
    NRF_LOG_DEBUG("fds_test_find_and_delete \r\n");
    fds_record_desc_t   record_desc;
    fds_find_token_t    ftok;

    ftok.page=0;
    ftok.p_addr=NULL;
    // Loop and find records with same ID and rec key and mark them as deleted.
    while (fds_record_find(modelID, recID, &record_desc, &ftok) == FDS_SUCCESS)
    {
        fds_record_delete(&record_desc);
        NRF_LOG_DEBUG("Deleted record ID: %d \r\n",record_desc.record_id);
    }
    // call the garbage collector to empty them, don't need to do this all the time, this is just for demonstration
    ret_code_t ret = fds_gc();
    if (ret != FDS_SUCCESS)
    {
        return ret;
    }
    return NRF_SUCCESS;
}

uint32_t nrf52_ravel_fs_init (void)
{
    NRF_LOG_DEBUG("nrf52_ravel_fs_init \r\n");
    ret_code_t ret = fds_register(nrf52_ravel_fs_fds_evt_handler);
    if (ret != FDS_SUCCESS)
    {
        return ret;

    }
    ret = fds_init();
    if (ret != FDS_SUCCESS)
    {
        return ret;
    }
    NRF_LOG_DEBUG("Init success \r\n");
    return NRF_SUCCESS;

}
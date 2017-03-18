#include "app_error.h"
#include "pstorage.h"
#include "nrf.h"

#include "nrf52_log.h"
#include "nrf52_flash.h"
#include "nrf52_config.h"
#include "nrf52_network.h"
#include "nrf52_queue.h"

#include <string.h>

#define BLOCK_SIZE 256
#define BLOCK_COUNT 2

#define ACKNO_CODE 0
#define SEQNO_CODE 1
#define CONFIG_CODE 2

static void flash_page_erase(uint32_t * page_address)
{
    // Turn on flash erase enable and wait until the NVMC is ready:
    NRF_NVMC->CONFIG = (NVMC_CONFIG_WEN_Een << NVMC_CONFIG_WEN_Pos);

    while (NRF_NVMC->READY == NVMC_READY_READY_Busy)
    {
        // Do nothing.
    }

    // Erase page:
    NRF_NVMC->ERASEPAGE = (uint32_t)page_address;

    while (NRF_NVMC->READY == NVMC_READY_READY_Busy)
    {
        // Do nothing.
    }

    // Turn off flash erase enable and wait until the NVMC is ready:
    NRF_NVMC->CONFIG = (NVMC_CONFIG_WEN_Ren << NVMC_CONFIG_WEN_Pos);

    while (NRF_NVMC->READY == NVMC_READY_READY_Busy)
    {
        // Do nothing.
    }
}


static volatile uint32_t m_pstorage_load_complete;
static volatile uint32_t m_pstorage_update_complete;

static void pstorage_cb(
    pstorage_handle_t * handle,
    uint8_t             op_code,
    uint32_t            result,
    uint8_t           * p_data,
    uint32_t            data_len)
{
    switch(op_code) {
        case PSTORAGE_LOAD_OP_CODE:
            m_pstorage_load_complete = true;
            break;
        case PSTORAGE_STORE_OP_CODE:
            break;
        case PSTORAGE_UPDATE_OP_CODE:
            m_pstorage_update_complete = true;
            break;
        case PSTORAGE_CLEAR_OP_CODE:
        case PSTORAGE_ERROR_OP_CODE:
        default:
            break;
    }
}

static pstorage_handle_t config_block_handle;
static pstorage_handle_t other_block_handle;

void flash_init(void) {

    uint32_t err_code;

    /* initialize the persistant storage module */
    err_code = pstorage_init();
    APP_ERROR_CHECK(err_code);

    /* initialize the config application */
    pstorage_handle_t m_storage_handle;
    pstorage_module_param_t param;
    param.block_size  = BLOCK_SIZE;
    param.block_count = BLOCK_COUNT;
    param.cb          = pstorage_cb;
    err_code = pstorage_register(&param, &m_storage_handle);
    //LOG("flash register %u %u", m_storage_handle.module_id,m_storage_handle.block_id);
    APP_ERROR_CHECK(err_code);

    /* get config flash handles */
    err_code = pstorage_block_identifier_get(
        &m_storage_handle,0, &config_block_handle);
    APP_ERROR_CHECK(err_code);
    //LOG("config %u %u", config_block_handle.module_id, config_block_handle.block_id);

    /* load the config settings into memory
        old_data checks to see if there is already useful data in flash */
    config_init(CONFIG_CODE);
    int old_data = config_load();

    /* get flash handle for network ack/seqno and queue storage*/
    err_code = pstorage_block_identifier_get(
        &m_storage_handle,1, &other_block_handle);
    APP_ERROR_CHECK(err_code);

    if (old_data == 0){
        uint8_t buffer[4];
        memset(buffer,0,4);
        flash_write(ACKNO_CODE,buffer);
        flash_write(SEQNO_CODE,buffer);
    }

    //network_init(ACKNO_CODE,SEQNO_CODE,old_data);
    APP_ERROR_CHECK(err_code);

}

void flash_read(uint8_t code, uint8_t *readdest){

    uint32_t err_code=0;
    m_pstorage_load_complete = false;

    switch(code){
        case CONFIG_CODE:
            err_code = pstorage_load(readdest, &config_block_handle, BLOCK_SIZE, 0);
            break;
        case ACKNO_CODE:
        case SEQNO_CODE:
            err_code = pstorage_load(readdest, &other_block_handle, 4, code*4);
            break;
        default:
            break;
    }

    APP_ERROR_CHECK(err_code);

    while(!m_pstorage_load_complete){
        __WFI();
    }
}

void flash_write(uint8_t code, uint8_t* data){

    uint32_t err_code = 0;
    m_pstorage_update_complete = false;

    switch(code){
        case CONFIG_CODE:
            err_code = pstorage_update(&config_block_handle, data, BLOCK_SIZE, 0);
            break;
        case ACKNO_CODE:
        case SEQNO_CODE:
            err_code = pstorage_update(&other_block_handle, data, 4, code*4);
            break;
        default:
            break;
    }

    APP_ERROR_CHECK(err_code);

    while(!m_pstorage_update_complete){
        __WFI();
    }
}

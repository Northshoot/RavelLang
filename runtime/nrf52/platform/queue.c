#include <string.h>

#include "nrf.h"
#include "app_error.h"
#include "app_util.h"
#include "ble_err.h"
#include "pstorage.h"

#include "network.h"
#include "log.h"

#include "queue.h"
#include "flash.h"

#define BLOCK_SIZE  32
#define BLOCK_COUNT 1024
#define BLOCK_COUNT_RND 1020

static uint32_t m_storage_write_index = 0;
static uint32_t m_storage_read_index  = 0;

static volatile uint32_t m_pstorage_load_complete;
static volatile uint32_t m_pstorage_update_complete;

static
void pstorage_cb(
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

static pstorage_handle_t m_storage_handle;
uint32_t queue_init(uint32_t read_handle,
    uint32_t write_handle) {
    uint32_t err_code;
    pstorage_module_param_t param;

    m_storage_read_index = read_handle;
    m_storage_write_index = write_handle;

    /* Since we need more space than a single page, we need the page size 
     * (usually 1024) to be divisible by the block size (i.e. 
     * 1024 % block_size == 0) */

    /* each block should correspond to a single packet */
    STATIC_ASSERT(sizeof(data_packet_t) <= BLOCK_SIZE);

    param.block_size  = BLOCK_SIZE; 
    param.block_count = BLOCK_COUNT;
    param.cb          = pstorage_cb; /* callback tells us when operations complete */

    err_code = pstorage_register(&param, &m_storage_handle);
    //LOG("queue storage %u %u",m_storage_handle.module_id,m_storage_handle.block_id);
    return err_code;
}

uint32_t queue_enqueue(const data_packet_t *packet, uint32_t write_loc) {
    uint32_t err_code;
    pstorage_handle_t block_handle;
    m_storage_write_index = write_loc;

    /* check to see that there is enough free space in the queue */
    if (m_storage_write_index - m_storage_read_index >= BLOCK_COUNT_RND) {
        LOG("Queue - No more room in packet queue for packet!");
        LOG("write index %u, read index %u", m_storage_write_index, m_storage_read_index);
        return NRF_ERROR_INVALID_STATE;
    }

    /* get the identifier for the next block that can be written to */
    err_code = pstorage_block_identifier_get(
        &m_storage_handle,
        m_storage_write_index % BLOCK_COUNT_RND,
        &block_handle
    );
    APP_ERROR_CHECK(err_code);

    /* store the new packet at this block */
    //LOG("Queue storing %u enqueued at %u",packet->s.seqno,block_handle.block_id);
    m_pstorage_update_complete = false;
    err_code = pstorage_update(
        &block_handle,
        (uint8_t *)packet,
        BLOCK_SIZE,
        0
    );
    APP_ERROR_CHECK(err_code);

    /* wait until the flash is done writing */
    /* XXX this is a sucky hack to get around the stupid flash API... */
    while (!m_pstorage_update_complete) {
        __WFI();
    }
    //LOG("Done storing %u",packet->s.seqno);
    m_storage_write_index += 1;

    return NRF_SUCCESS;
}

void queue_dequeue_n(uint32_t read_loc) {
    /* could try to clear the dequeued block here, but no need */

    /* increment the read index */
    m_storage_read_index = read_loc;
    LOG("Done dequeuing\n");
}

uint32_t queue_read_n(data_packet_t *packet, uint32_t n) {
    uint32_t err_code;
    pstorage_handle_t block_handle;

    /* the flash data needs to be at least word aligned */
    //uint8_t flash_data[BLOCK_SIZE] __attribute__((aligned(4)));

    /* check to see that the index is in bounds */
    if (n >= m_storage_write_index) {
        LOG("Queue - Requested read out of range");
        LOG("Queue - Requested %u, Queue write %u, Queue read %u", n, m_storage_write_index, m_storage_read_index);

        return NRF_ERROR_INVALID_STATE;
    }

    /* get the identifier for the block in question */
    err_code = pstorage_block_identifier_get(
        &m_storage_handle,
        n % BLOCK_COUNT_RND,
        &block_handle
    );
    APP_ERROR_CHECK(err_code);

    /* read the packet from this block */
    //LOG("Queue reading %u %u",block_handle.module_id,block_handle.block_id);
    m_pstorage_load_complete = false;
    err_code = pstorage_load(
        (uint8_t *)packet,
        &block_handle,
        BLOCK_SIZE,
        0
    );

    APP_ERROR_CHECK(err_code);

    /* wait until the flash is done reading */
    while (!m_pstorage_load_complete) {
        __WFI();
    }

    /* copy the data to the packet */
    //LOG("Queue reads SEQUENCE number %u %u",packet->s.seqno,packet->s.payload.data[0].timestamp_lower_bytes);

    return NRF_SUCCESS;
}

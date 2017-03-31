#include <string.h>

#include "nrf.h"
#include "app_error.h"
#include "app_util.h"
#include "ble_err.h"

#include "nrf52_log.h"

#include "nrf52_config.h"
#include "nrf52_link_constants.c"
#include "nrf52_flash.h"

//#define MIN(a,b) ((a)<(b)?(a):(b))
#define BLOCK_SIZE 256

typedef struct {
    config_state_t state;
    uint8_t        encryption_key[16];
    uint32_t       global_time_offset;
    uint8_t        device_name[64];
    uint32_t       sensor_threshold;
} config_t;

static union {
    config_t config;
    uint8_t  buffer[BLOCK_SIZE]; /* buffer for padding */
} m_config __attribute((aligned(4)));

static
void config_init_set_default(void) {
    /* zero out the global config */
    memset(&m_config, 0, sizeof m_config);

    /* set the config state to UNSET */
    memcpy(m_config.config.encryption_key, &DEFAULT_KEY, sizeof m_config.config.encryption_key);
    m_config.config.global_time_offset = (uint32_t)DEFAULT_TIME;
    memcpy(m_config.config.device_name, &DEFAULT_NAME, sizeof m_config.config.device_name);
    m_config.config.sensor_threshold = (uint32_t)DEFAULT_COMPRESSION_THRESHOLD;
    m_config.config.state = CONFIG_STATE_VALID;
}

static uint8_t config_code;
void config_init(uint8_t code) {
    /* set the config to the default state */
    config_code = code;
}

int config_load(void) {
    //returns true if data is loaded from flash, returns false if new data

    /* load the config from flash */
    flash_read(config_code,m_config.buffer);

    if (m_config.config.state != CONFIG_STATE_VALID) {
        LOG("default configs set");
        config_init_set_default();
        config_persist(m_config.config.global_time_offset);
        return 0;
    }
    else {
        LOG("configs read from flash");
        LOG("timestamp %u", m_config.config.global_time_offset);
    }

    return 1;
}

uint32_t config_set_key(const uint8_t *key) {
    memcpy(m_config.config.encryption_key, key, sizeof m_config.config.encryption_key);

    return NRF_SUCCESS;
}

uint32_t config_set_name(const uint8_t *name, size_t len) {
    /* copy the string over byte by byte */
    size_t i;
    for (i = 0; i < MIN(len, sizeof m_config.config.device_name - 1); i++) {
        m_config.config.device_name[i] = name[i];
    }

    /* make sure the string is null terminated */
    m_config.config.device_name[i] = '\0';

    return NRF_SUCCESS;
}

uint32_t config_set_global_time_offset(uint32_t global_time_offset) {
    m_config.config.global_time_offset = global_time_offset;
    return NRF_SUCCESS;
}

uint32_t config_set_sensor_threshold(uint32_t threshold) {
    m_config.config.sensor_threshold = threshold;

    return NRF_SUCCESS;
}

const uint8_t *config_get_key(void) {
    if (strlen((char*)m_config.config.encryption_key) == 0)
        return (const uint8_t *)DEFAULT_KEY;
    return m_config.config.encryption_key;
}

const uint8_t *config_get_name(void) {
    if (strlen((char*)m_config.config.device_name) == 0)
        return (const uint8_t *)DEFAULT_NAME;
    return m_config.config.device_name;
}

uint32_t config_get_global_time_offset(void) {
    if (m_config.config.global_time_offset == 0)
        return (uint32_t)DEFAULT_TIME;
    return m_config.config.global_time_offset;
}

uint32_t config_get_sensor_threshold(void) {
    if (m_config.config.sensor_threshold == 0)
        return (uint32_t)DEFAULT_COMPRESSION_THRESHOLD;
    return m_config.config.sensor_threshold;
}

config_state_t config_get_state(void) {
    return m_config.config.state;
}

void config_persist(uint32_t current_time) {
    /* mark the new config as valid */
    m_config.config.state = CONFIG_STATE_VALID;
    m_config.config.global_time_offset = current_time;
    LOG("writing config to flash");
    flash_write(config_code, m_config.buffer);

}
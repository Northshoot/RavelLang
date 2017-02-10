#ifndef CONFIG_H_
#define CONFIG_H_

#include <stddef.h>

/* The config operation is somewhat subtle.
 *
 * At startup, the flash manager loads the current config from flash into RAM.
 * If the config is valid (denoted by config.state) then operations proceed
 * normally. Otherwise, the device waits to record data until a valid config
 * is persisted and the device is rebooted.
 *
 * Additionally, config parameters cannot be read
 */

typedef enum {
    CONFIG_STATE_INVALID = 0xFFFF,
    CONFIG_STATE_VALID   = 0x1234
} config_state_t;

void config_init(uint8_t);
int config_load(void);

uint32_t config_set_key(const uint8_t *);
uint32_t config_set_name(const uint8_t *, size_t len);
uint32_t config_set_global_time_offset(uint32_t);
uint32_t config_set_sensor_threshold(uint32_t);

const uint8_t *config_get_key(void);
const uint8_t *config_get_name(void);
uint32_t       config_get_global_time_offset(void);
uint32_t       config_get_sensor_threshold(void);
config_state_t config_get_state(void);

void config_persist(uint32_t);

#endif
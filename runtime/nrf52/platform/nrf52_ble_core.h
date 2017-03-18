#ifndef BLE_CORE_H
#define BLE_CORE_H

#include "nrf52_network.h"
 void nrf52_r_core_ble_start(void);

void nrf52_r_core_ble_stack_init(NetworkClb *network)

uint32_t nrf52_send_data(uint8_t * p_data, uint16_t length);

//extern const char device_name[];
//uint8_t service_add(void);
//void advertising_init(uint8_t );
//void advertising_start(void);

#endif
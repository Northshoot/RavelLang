#ifndef BLE_INTERFACE_H
#define BLE_INTERFACE_H

#include <stdint.h>
#include <stddef.h>
#include <stdbool.h>
#include "ble.h"
#include "ble_srv_common.h"

struct BleInterfaceVtable {
  void (*init)(void *);
  void (*ble_bas_on_ble_evt)(ble_evt_t * p_ble_evt);
};

void nrf52_ble_interface_add_handler (void *handler);

#endif /* BLE_INTERFACE_H */

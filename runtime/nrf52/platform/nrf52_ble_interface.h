#ifndef BLE_INTERFACE_H
#define BLE_INTERFACE_H

#include <stdint.h>
#include <stddef.h>
#include <stdbool.h>

struct BleInterfaceVtable {
  void (*init)(void *);
  void (*connected)(void *, bool);
  void (*notify_enabled)(void *, bool);
  void (*packet)(void *, uint8_t *, size_t);
};

void nrf52_ble_interface_add_handler (void *handler);

#endif /* BLE_INTERFACE_H */

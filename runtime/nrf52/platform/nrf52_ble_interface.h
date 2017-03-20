#ifndef BLE_INTERFACE_H
#define BLE_INTERFACE_H

#include <stdint.h>
#include <stddef.h>
#include <stdbool.h>
#include "ble.h"
#include "ble_srv_common.h"
#include "nrf52_network.h"

#define MAX_HANDLERS 5

struct BleInterfaceVtable {
  void (*init)(NetworkClb *network);
  void (*ble_generic_event)(ble_evt_t * p_ble_evt);
  ble_uuid_t p_adv_uuids;
};

//Struct to hold all uuids of services
typedef struct {
    ble_uuid_t m_adv_uuids[MAX_HANDLERS];
    uint8_t m_ble_services_cnt;
} ble_services_uuids_t;

void set_adv_uuid(ble_services_uuids_t * p_adv_uuids);

void nrf52_on_ble_generic_event(ble_evt_t * p_ble_evt);

void nrf52_init_all_ble_services(NetworkClb *network);

void nrf52_ble_interface_add_handler (void *handler);

#endif /* BLE_INTERFACE_H */

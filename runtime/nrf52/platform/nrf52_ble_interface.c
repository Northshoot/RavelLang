#include "nrf52_ble_interface.h"

#define NRF_LOG_MODULE_NAME "BLE_H"
#define NRF_LOG_LEVEL 1
#include "nrf_log.h"
#include "nrf_log_ctrl.h"



static void *handlers[MAX_HANDLERS];

ble_services_uuids_t m_ble_uuids;

static uint8_t m_current_handler = 0;

void nrf52_on_ble_generic_event(ble_evt_t * p_ble_evt)
{
    size_t i;
    for (i = 0; i < m_current_handler; i++) {
        if (handlers[i]) {
            void *handler = handlers[i];
            struct BleInterfaceVtable *vtable = ((struct BleInterfaceVtable*)handler);
            vtable->ble_generic_event( p_ble_evt);
        }
    }
}



void nrf52_init_all_ble_services(NetworkClb *network)
{
    size_t i;
    NRF_LOG_DEBUG("Init all services \r\n");
    for (i = 0; i < m_current_handler; i++) {
        if (handlers[i]) {
            void *handler = handlers[i];
            struct BleInterfaceVtable *vtable = ((struct BleInterfaceVtable*)handler);
            vtable->init(network);
            m_ble_uuids.m_adv_uuids[i] = vtable->p_adv_uuids;
        } else {
            NRF_LOG_DEBUG("no handlers\r\n");
         }
    }
    NRF_LOG_DEBUG("Done\r\n");
}

void set_adv_uuid(ble_services_uuids_t * p_adv_uuids)
{
    //number of current handlers
    NRF_LOG_DEBUG("num handlers %u\r\n", m_current_handler);
    m_ble_uuids.m_ble_services_cnt = m_current_handler;
    p_adv_uuids = &m_ble_uuids;

}

void nrf52_ble_interface_add_handler (void *handler)
{
    NRF_LOG_DEBUG("Adding BLE handler \r\n");
    handlers[m_current_handler] = handler;
    m_current_handler++;
}
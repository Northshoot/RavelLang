/* Copyright (c) 2012 Nordic Semiconductor. All Rights Reserved.
 *
 * The information contained herein is property of Nordic Semiconductor ASA.
 * Terms and conditions of usage are described in detail in NORDIC
 * SEMICONDUCTOR STANDARD SOFTWARE LICENSE AGREEMENT.
 *
 * Licensees are granted free, non-transferable use of the information. NO
 * WARRANTY of ANY KIND is provided. This heading must NOT be removed from
 * the file.
 *
 */

/**@file
 *
 * @defgroup ble_rad Ravel Data Service
 * @{
 * @ingroup  ble_sdk_srv
 * @brief    Ravel Data Service implementation.
 *
 * @details The Ravel Data Service is a simple GATT-based service with TX and RX characteristics.
 *          Data received from the peer is passed to the application, and the data received
 *          from the application of this service is sent to the peer as Handle Value
 *          Notifications. This module demonstrates how to implement a custom GATT-based
 *          service and characteristics using the SoftDevice. The service
 *          is used by the application to send and receive ASCII text strings to and from the
 *          peer.
 *
 * @note The application must propagate SoftDevice events to the Ravel Data Service module
 *       by calling the ble_rad_on_ble_evt() function from the ble_stack_handler callback.
 */

#ifndef BLE_RAD_H__
#define BLE_RAD_H__
#include <stdint.h>
#include <stdbool.h>

#include "ble.h"
#include "ble_srv_common.h"

#include "nrf52_network.h"

#ifdef __cplusplus
extern "C" {
#endif

//Interface to the systems
void ble_rad_init_handler(void);

void ble_rad_init_interface();

void ble_bas_on_ble_evt_interface(ble_evt_t * p_ble_evt);

uint32_t ble_rad_send_data_interface(uint8_t * p_data, uint16_t length);



//END interface to the system

//Below standard implementation of the BLE BAS service


#define BLE_UUID_RAD_SERVICE 0x0001                      /**< The UUID of the Ravel Data Service. */
#define BLE_UUID_RAD_TX_CHARACTERISTIC 0x0002                      /**< The UUID of the TX Characteristic. */
#define BLE_UUID_RAD_RX_CHARACTERISTIC 0x0003                      /**< The UUID of the RX Characteristic. */
#define BLE_RAD_MAX_DATA_LEN  20 /**< Maximum length of data (in bytes) that can be transmitted to the peer by the Ravel Data service module. */

/* Forward declaration of the ble_rad_t type. */
typedef struct ble_rad_s ble_rad_t;


/**@brief Ravel Data Service structure.
 *
 * @details This structure contains status information related to the service.
 */
struct ble_rad_s
{
    uint8_t                  uuid_type;               /**< UUID type for Ravel Data Service Base UUID. */
    uint16_t                 service_handle;          /**< Handle of Ravel Data Service (as provided by the SoftDevice). */
    ble_gatts_char_handles_t tx_handles;              /**< Handles related to the TX characteristic (as provided by the SoftDevice). */
    ble_gatts_char_handles_t rx_handles;              /**< Handles related to the RX characteristic (as provided by the SoftDevice). */
    uint16_t                 conn_handle;             /**< Handle of the current connection (as provided by the SoftDevice). BLE_CONN_HANDLE_INVALID if not in a connection. */
    bool                     is_notification_enabled; /**< Variable to indicate if the peer has enabled notification of the RX characteristic.*/
    NetworkClb               *network;                  /**< Functional structure for event handling. */
};


#ifdef __cplusplus
}
#endif

#endif // BLE_RAD_H__

/** @} */
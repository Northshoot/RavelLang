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

#define BLE_UUID_RAD_SERVICE 0x0001                      /**< The UUID of the Ravel Data Service. */
#define BLE_RAD_MAX_DATA_LEN (GATT_MTU_SIZE_DEFAULT - 3) /**< Maximum length of data (in bytes) that can be transmitted to the peer by the Ravel Data service module. */

/* Forward declaration of the ble_rad_t type. */
typedef struct ble_rad_s ble_rad_t;

/**@brief Ravel Data Service event handler type. */
typedef void (*ble_rad_data_handler_t) (ble_rad_t * p_rad, uint8_t * p_data, uint16_t length);

/**@brief Ravel Data Service initialization structure.
 *
 * @details This structure contains the initialization information for the service. The application
 * must fill this structure and pass it to the service using the @ref ble_rad_init
 *          function.
 */
typedef struct
{
    ble_rad_data_handler_t data_handler; /**< Event handler to be called for handling received data. */
} ble_rad_init_t;

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
    ble_rad_data_handler_t   data_handler;            /**< Event handler to be called for handling received data. */
    NetworkClb               *network;                  /**< Functional structure for event handling. */
};

/**@brief Function for initializing the Ravel Data Service.
 *
 * @param[out] p_rad      Ravel Data Service structure. This structure must be supplied
 *                        by the application. It is initialized by this function and will
 *                        later be used to identify this particular service instance.
 * @param[in] p_rad_init  Information needed to initialize the service.
 *
 * @retval NRF_SUCCESS If the service was successfully initialized. Otherwise, an error code is returned.
 * @retval NRF_ERROR_NULL If either of the pointers p_rad or p_rad_init is NULL.
 */
uint32_t ble_rad_init(ble_rad_t * p_rad, const ble_rad_init_t * p_rad_init);

/**@brief Function for handling the Ravel Data Service's BLE events.
 *
 * @details The Ravel Data Service expects the application to call this function each time an
 * event is received from the SoftDevice. This function processes the event if it
 * is relevant and calls the Ravel Data Service event handler of the
 * application if necessary.
 *
 * @param[in] p_rad       Ravel Data Service structure.
 * @param[in] p_ble_evt   Event received from the SoftDevice.
 */
void ble_rad_on_ble_evt(ble_rad_t * p_rad, ble_evt_t * p_ble_evt);

/**@brief Function for sending a string to the peer.
 *
 * @details This function sends the input byte as an RX characteristic notification to the
 *          peer.
 *
 * @param[in] p_rad       Pointer to the Ravel Data Service structure.
 * @param[in] p_string    Data to be sent.
 * @param[in] length      Length of the Data.
 *
 * @retval NRF_SUCCESS If the string was sent successfully. Otherwise, an error code is returned.
 */
uint32_t ble_rad_send_data(ble_rad_t * p_rad, uint8_t * p_data, uint16_t length);


#ifdef __cplusplus
}
#endif

#endif // BLE_RAD_H__

/** @} */

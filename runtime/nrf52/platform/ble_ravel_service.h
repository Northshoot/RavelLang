//
// Created by lauril on 3/3/17.
//

#ifndef NRF52_BLE_RAD_SERVICE_H
#define NRF52_BLE_RAD_SERVICE_H
#include "ble.h"
#include "ble_srv_common.h"
#include <stdint.h>
#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

//Ravel data service :: rad
#define BLE_UUID_RAD_SERVICE 0x0002
//rad data size
#define BLE_RAD_MAX_DATA_LEN (GATT_MTU_SIZE_DEFAULT - 3)

/* Forward declaration of the ble_rad_t type. */
typedef struct ble_rad_s ble_rad_t;

/**@brief callback to rad service handler */
typedef void (*ble_rad_data_handler_t) (uint8_t * p_data, uint16_t length);


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
    uint8_t                  uuid_type;              
    uint16_t                 service_handle;          
    ble_gatts_char_handles_t tx_handles;              /**< Handles related to the TX characteristic (as provided by the SoftDevice). */
    ble_gatts_char_handles_t rx_handles;              /**< Handles related to the RX characteristic (as provided by the SoftDevice). */
    uint16_t                 conn_handle;             /**< Handle of the current connection (as provided by the SoftDevice). BLE_CONN_HANDLE_INVALID if not in a connection. */
    bool                     is_notification_enabled; /**< Variable to indicate if the peer has enabled notification of the RX characteristic.*/
    ble_rad_data_handler_t   data_handler;            /**< Event handler to be called for handling received data. */
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
uint32_t ble_rad_init();

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
void ble_rad_on_ble_evt(ble_evt_t * p_ble_evt);

/**@brief Function for sending a string to the peer.
 *
 * @details This function sends the input string as an RX characteristic notification to the
 *          peer.
 *
 * @param[in] p_rad       Pointer to the Ravel Data Service structure.
 * @param[in] p_string    String to be sent.
 * @param[in] length      Length of the string.
 *
 * @retval NRF_SUCCESS If the string was sent successfully. Otherwise, an error code is returned.
 */
uint32_t ble_rad_data_send(uint8_t * p_data, uint16_t length);


#ifdef __cplusplus
}
#endif

void ble_rad_service_init();
#endif //NRF52_BLE_RAD_SERVICE_H

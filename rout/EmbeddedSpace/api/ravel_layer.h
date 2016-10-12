 /**
  * Autogenerated main file
  *
  *
  *  Created on: 13:07:23 10/12/2016
  *      Author: Ravel
    */
#ifndef API_RAVEL_LAYER_H_
#define API_RAVEL_LAYER_H_
#include <stdint.h>
#include <string.h>

#include "api_ble.h"


/**
 * System dispatchers
 */

void system_api__device_connected();
void system_api__device_disconnected();

void  app_device_model__data_added();
void  app_config_model__data_added();
void  boot_error_model__data_added();
void  a_d_c_model_model__data_added();
void  device_location_model_model__data_added();
void  measurement_model__data_added();
void  measurement_packet_model__data_added();
void  encrypted_blob_model__data_added();


//model channel connectivity dispatching
bool boot_error_model__is_connected();
void boot_error_model__channel_enabled();
void boot_error_model__channel_disabled();





bool measurement_packet_model__is_connected();
void measurement_packet_model__channel_enabled();
void measurement_packet_model__channel_disabled();



//** end autogenerated files *//
#endif /* API_RAVEL_LAYER_H_ */
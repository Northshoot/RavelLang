 /**
  * Autogenerated main file
  *
  *
  *  Created on: 11:44:58 10/12/2016
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

void  vibration_model__data_added();
void  p_phase_model__data_added();
void  compressed_sample_model__data_added();
void  transmit_c_s_model__data_added();


//model channel connectivity dispatching
bool p_phase_model__is_connected();
void p_phase_model__channel_enabled();
void p_phase_model__channel_disabled();


bool compressed_sample_model__is_connected();
void compressed_sample_model__channel_enabled();
void compressed_sample_model__channel_disabled();



//** end autogenerated files *//
#endif /* API_RAVEL_LAYER_H_ */
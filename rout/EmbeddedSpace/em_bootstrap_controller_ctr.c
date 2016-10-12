/**
 * Autogenerated main file
 *
 *
 *  Created on: 
 *      Author: Ravel
 */
#include "app_timer.h"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "models.h"
#include "api_ble.h"
#include "em_bootstrap_controller_ctr.h"

//implement subscribed model events

 AppDeviceModel repModel; 


//translate actions for the subscribed events
//TODO: check if controller is bubscribed to the particular model event

//void random_model_ctr__set_config_model(ConfigModel *cm){
//TODO: fix controller inner body
	//memset(&localCm, 0, sizeof(ConfigModel));
	//localCm.time = cm->time;
	//localCm.frequency = cm->frequency;
//}


void app_device_model__app_system_ctr__arrived(void *rctx ){
    NRF_LOG_INFO("app_device_model__app_system_ctr__arrived\r\n");
	//if(((RavelContext)rctx)->error == NRF_SUCCESS){
		//}
    	//NRF_LOG_INFO("Arrived frequency: %d \r\n", localCm.frequency);
    	//config_model__sys_write_to_BLE(&m_ravel_service, &localCm);
}

void app_device_model__app_system_ctr__departed(void *rctx){
	//do things when model data arrives
	NRF_LOG_INFO("app_device_model__app_system_ctr__departed\r\n");
}
void app_device_model__app_system_ctr__full(void *rctx){
	//do things when model data arrives
	NRF_LOG_INFO("app_device_model__app_system_ctr__full\r\n");
}

void app_device_model__app_system_ctr__saveDone(void *rctx){
	//do things when model data arrives
	NRF_LOG_INFO("app_device_model__app_system_ctr__arrived\r\n");
}
void app_device_model__app_system_ctr__bufferSaveDone(void *rctx){
	//do things when model data arrives
	NRF_LOG_INFO("app_device_model__app_system_ctr__bufferSaveDone\r\n");
}


//translate actions for the subscribed events
//TODO: check if controller is bubscribed to the particular model event

//void random_model_ctr__set_config_model(ConfigModel *cm){
//TODO: fix controller inner body
	//memset(&localCm, 0, sizeof(ConfigModel));
	//localCm.time = cm->time;
	//localCm.frequency = cm->frequency;
//}


void boot_error_model__em_bootstrap_controller_ctr__arrived(void *rctx ){
    NRF_LOG_INFO("boot_error_model__em_bootstrap_controller_ctr__arrived\r\n");
	//if(((RavelContext)rctx)->error == NRF_SUCCESS){
		//}
    	//NRF_LOG_INFO("Arrived frequency: %d \r\n", localCm.frequency);
    	//config_model__sys_write_to_BLE(&m_ravel_service, &localCm);
}

void boot_error_model__em_bootstrap_controller_ctr__departed(void *rctx){
	//do things when model data arrives
	NRF_LOG_INFO("boot_error_model__em_bootstrap_controller_ctr__departed\r\n");
}
void boot_error_model__em_bootstrap_controller_ctr__full(void *rctx){
	//do things when model data arrives
	NRF_LOG_INFO("boot_error_model__em_bootstrap_controller_ctr__full\r\n");
}

void boot_error_model__em_bootstrap_controller_ctr__saveDone(void *rctx){
	//do things when model data arrives
	NRF_LOG_INFO("boot_error_model__em_bootstrap_controller_ctr__arrived\r\n");
}
void boot_error_model__em_bootstrap_controller_ctr__bufferSaveDone(void *rctx){
	//do things when model data arrives
	NRF_LOG_INFO("boot_error_model__em_bootstrap_controller_ctr__bufferSaveDone\r\n");
}



//generating source callbacks implementations

void em_bootstrap_controller_ctr__system_booted(void *rctx){
	randomizer_ctr__timer__timer_periodic__start(1000);
}


//*** End auto generated file em_bootstrap_controller_ctr.c **//
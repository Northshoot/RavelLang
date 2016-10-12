/**
 * Autogenerated file containing data models
 *  models.c
 *
 *  Created on: 13:07:22 10/12/2016
 *      Author: Ravel
 */
//base includes
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
//default buffers
#include "api/ringbuf.h"

#include "models.h"
#include "em_bootstrap_controller_ctr.h" //controller include
#include "measurement_controller_ctr.h" //controller include
//Model common implementations
// replicated are only one instance of the model, logging makes new model
AppDeviceModel m_app_device_model;

void app_device_model__innit(){
	memset(&m_app_device_model, 0, sizeof(AppDeviceModel));
	//loop through the field to set default values
}
void app_device_model__destroy(){
	//since the model is replicated we dont have any buffer
}
void app_device_model__save(AppDeviceModel *rm) {
	//notify subscribers
   RavelContext rctx;
   rctx.error = NRF_SUCCESS;
   rctx.model = &m_app_device_model;
   app_device_model__app_system_ctr__arrived(&rctx);
}

void app_device_model__get_first(AppDeviceModel *rm){
	memcpy(rm, &m_app_device_model, sizeof(m_app_device_model));
}
void app_device_model__get_last(AppDeviceModel *rm){
	memcpy(rm, &m_app_device_model, sizeof(m_app_device_model));
}
void app_device_model__get(uint32_t position, AppDeviceModel *rm){
     //TODO: raise an error from Ravel
     memcpy(rm, &m_app_device_model, sizeof(m_app_device_model));
}



 


// replicated are only one instance of the model, logging makes new model
AppConfigModel m_app_config_model;

void app_config_model__innit(){
	memset(&m_app_config_model, 0, sizeof(AppConfigModel));
	//loop through the field to set default values
}
void app_config_model__destroy(){
	//since the model is replicated we dont have any buffer
}
void app_config_model__save(AppConfigModel *rm) {
	//notify subscribers
   RavelContext rctx;
   rctx.error = NRF_SUCCESS;
   rctx.model = &m_app_config_model;
   app_config_model__measurement_controller_ctr__arrived(&rctx);
}

void app_config_model__get_first(AppConfigModel *rm){
	memcpy(rm, &m_app_config_model, sizeof(m_app_config_model));
}
void app_config_model__get_last(AppConfigModel *rm){
	memcpy(rm, &m_app_config_model, sizeof(m_app_config_model));
}
void app_config_model__get(uint32_t position, AppConfigModel *rm){
     //TODO: raise an error from Ravel
     memcpy(rm, &m_app_config_model, sizeof(m_app_config_model));
}



 


#define BOOT_ERROR_MODEL__QUEUE_SIZE 100

ghpringbuf *boot_error_model__queue_buf ;
uint32_t boot_error_model__buff_size = BOOT_ERROR_MODEL__QUEUE_SIZE;
bool boot_error_model__buff_full = false;
uint32_t boot_error_model__rec_id=0;
uint32_t boot_error_model__rec_discarded=0;

//generic buffer implementation
ghpringbuf *buf;
//*record functions:*/
//delete buffer
void boot_error_model__destroy(){
    ghpringbuf_destroy(boot_error_model__queue_buf);
}
//is buffer full
inline static bool boot_error_model__is_full(){
	return boot_error_model__queue_buf->capacity == boot_error_model__queue_buf->count;
}

//buffer size is defined via BOOT_ERROR_MODEL__QUEUE_SIZE
//init buffer
void boot_error_model__innit(){
	boot_error_model__queue_buf = ghpringbuf_create(BOOT_ERROR_MODEL__QUEUE_SIZE, sizeof(BootErrorModel), 1, NULL);
}
//create record is in place code snippet
BootErrorModel boot_error_model__create(){
        BootErrorModel m_model;
        //allocate the memory
    	memset(&m_model, 0, sizeof(BootErrorModel));
    	//loop through the field to set default values
        //return newly created model with set fields
    	return m_model;
}
//save record
void boot_error_model__save(BootErrorModel *mr) {
	if(!boot_error_model__buff_full) {
		ghpringbuf_put(boot_error_model__queue_buf, mr);
		boot_error_model__rec_id++;
	} else {
		boot_error_model__rec_discarded++;
	}
	boot_error_model__buff_full = boot_error_model__is_full();
	if(boot_error_model__buff_full){
		boot_error_model__em_bootstrap_controller_ctr__full(mr);
	}
	boot_error_model__em_bootstrap_controller_ctr__bufferSaveDone(mr);
}

//delete
void boot_error_model__delete(int position) {
    //TODO: fix buffer pop at possition
    //ghpringbuf_pop(position);

}

//* record local queries */
void boot_error_model__get(uint32_t position, BootErrorModel *rm){
	BootErrorModel *a_ptr;
	a_ptr = ghpringbuf_at(boot_error_model__queue_buf, position);
	memcpy(&rm, a_ptr, sizeof(BootErrorModel));

}
void boot_error_model__get_first(BootErrorModel *rm){
	BootErrorModel *a_ptr;
	a_ptr = ghpringbuf_at(boot_error_model__queue_buf, 0);
	memcpy(&rm, a_ptr, sizeof(BootErrorModel));

}
void boot_error_model__get_last(BootErrorModel *rm){
	BootErrorModel *a_ptr;
	a_ptr = ghpringbuf_at(boot_error_model__queue_buf, 100-1);
	memcpy(&rm, a_ptr, sizeof(BootErrorModel));
}

 


#define A_D_C_MODEL_MODEL__QUEUE_SIZE 

ghpringbuf *a_d_c_model_model__queue_buf ;
uint32_t a_d_c_model_model__buff_size = A_D_C_MODEL_MODEL__QUEUE_SIZE;
bool a_d_c_model_model__buff_full = false;
uint32_t a_d_c_model_model__rec_id=0;
uint32_t a_d_c_model_model__rec_discarded=0;

//generic buffer implementation
ghpringbuf *buf;
//*record functions:*/
//delete buffer
void a_d_c_model_model__destroy(){
    ghpringbuf_destroy(a_d_c_model_model__queue_buf);
}
//is buffer full
inline static bool a_d_c_model_model__is_full(){
	return a_d_c_model_model__queue_buf->capacity == a_d_c_model_model__queue_buf->count;
}

//buffer size is defined via A_D_C_MODEL_MODEL__QUEUE_SIZE
//init buffer
void a_d_c_model_model__innit(){
	a_d_c_model_model__queue_buf = ghpringbuf_create(A_D_C_MODEL_MODEL__QUEUE_SIZE, sizeof(ADCModelModel), 1, NULL);
}
//create record is in place code snippet
ADCModelModel a_d_c_model_model__create(){
        ADCModelModel m_model;
        //allocate the memory
    	memset(&m_model, 0, sizeof(ADCModelModel));
    	//loop through the field to set default values
        //return newly created model with set fields
    	return m_model;
}
//save record
void a_d_c_model_model__save(ADCModelModel *mr) {
	if(!a_d_c_model_model__buff_full) {
		ghpringbuf_put(a_d_c_model_model__queue_buf, mr);
		a_d_c_model_model__rec_id++;
	} else {
		a_d_c_model_model__rec_discarded++;
	}
	a_d_c_model_model__buff_full = a_d_c_model_model__is_full();
	if(a_d_c_model_model__buff_full){
		a_d_c_model_model__measurement_controller_ctr__full(mr);
	}
	a_d_c_model_model__measurement_controller_ctr__bufferSaveDone(mr);
}

//delete
void a_d_c_model_model__delete(int position) {
    //TODO: fix buffer pop at possition
    //ghpringbuf_pop(position);

}

//* record local queries */
void a_d_c_model_model__get(uint32_t position, ADCModelModel *rm){
	ADCModelModel *a_ptr;
	a_ptr = ghpringbuf_at(a_d_c_model_model__queue_buf, position);
	memcpy(&rm, a_ptr, sizeof(ADCModelModel));

}
void a_d_c_model_model__get_first(ADCModelModel *rm){
	ADCModelModel *a_ptr;
	a_ptr = ghpringbuf_at(a_d_c_model_model__queue_buf, 0);
	memcpy(&rm, a_ptr, sizeof(ADCModelModel));

}
void a_d_c_model_model__get_last(ADCModelModel *rm){
	ADCModelModel *a_ptr;
	a_ptr = ghpringbuf_at(a_d_c_model_model__queue_buf, -1);
	memcpy(&rm, a_ptr, sizeof(ADCModelModel));
}

 


#define DEVICE_LOCATION_MODEL_MODEL__QUEUE_SIZE 

ghpringbuf *device_location_model_model__queue_buf ;
uint32_t device_location_model_model__buff_size = DEVICE_LOCATION_MODEL_MODEL__QUEUE_SIZE;
bool device_location_model_model__buff_full = false;
uint32_t device_location_model_model__rec_id=0;
uint32_t device_location_model_model__rec_discarded=0;

//generic buffer implementation
ghpringbuf *buf;
//*record functions:*/
//delete buffer
void device_location_model_model__destroy(){
    ghpringbuf_destroy(device_location_model_model__queue_buf);
}
//is buffer full
inline static bool device_location_model_model__is_full(){
	return device_location_model_model__queue_buf->capacity == device_location_model_model__queue_buf->count;
}

//buffer size is defined via DEVICE_LOCATION_MODEL_MODEL__QUEUE_SIZE
//init buffer
void device_location_model_model__innit(){
	device_location_model_model__queue_buf = ghpringbuf_create(DEVICE_LOCATION_MODEL_MODEL__QUEUE_SIZE, sizeof(DeviceLocationModelModel), 1, NULL);
}
//create record is in place code snippet
DeviceLocationModelModel device_location_model_model__create(){
        DeviceLocationModelModel m_model;
        //allocate the memory
    	memset(&m_model, 0, sizeof(DeviceLocationModelModel));
    	//loop through the field to set default values
        //return newly created model with set fields
    	return m_model;
}
//save record
void device_location_model_model__save(DeviceLocationModelModel *mr) {
	if(!device_location_model_model__buff_full) {
		ghpringbuf_put(device_location_model_model__queue_buf, mr);
		device_location_model_model__rec_id++;
	} else {
		device_location_model_model__rec_discarded++;
	}
	device_location_model_model__buff_full = device_location_model_model__is_full();
	if(device_location_model_model__buff_full){
		device_location_model_model__app_system_ctr__full(mr);
	}
	device_location_model_model__app_system_ctr__bufferSaveDone(mr);
}

//delete
void device_location_model_model__delete(int position) {
    //TODO: fix buffer pop at possition
    //ghpringbuf_pop(position);

}

//* record local queries */
void device_location_model_model__get(uint32_t position, DeviceLocationModelModel *rm){
	DeviceLocationModelModel *a_ptr;
	a_ptr = ghpringbuf_at(device_location_model_model__queue_buf, position);
	memcpy(&rm, a_ptr, sizeof(DeviceLocationModelModel));

}
void device_location_model_model__get_first(DeviceLocationModelModel *rm){
	DeviceLocationModelModel *a_ptr;
	a_ptr = ghpringbuf_at(device_location_model_model__queue_buf, 0);
	memcpy(&rm, a_ptr, sizeof(DeviceLocationModelModel));

}
void device_location_model_model__get_last(DeviceLocationModelModel *rm){
	DeviceLocationModelModel *a_ptr;
	a_ptr = ghpringbuf_at(device_location_model_model__queue_buf, -1);
	memcpy(&rm, a_ptr, sizeof(DeviceLocationModelModel));
}

 


#define MEASUREMENT_MODEL__QUEUE_SIZE 

ghpringbuf *measurement_model__queue_buf ;
uint32_t measurement_model__buff_size = MEASUREMENT_MODEL__QUEUE_SIZE;
bool measurement_model__buff_full = false;
uint32_t measurement_model__rec_id=0;
uint32_t measurement_model__rec_discarded=0;

//generic buffer implementation
ghpringbuf *buf;
//*record functions:*/
//delete buffer
void measurement_model__destroy(){
    ghpringbuf_destroy(measurement_model__queue_buf);
}
//is buffer full
inline static bool measurement_model__is_full(){
	return measurement_model__queue_buf->capacity == measurement_model__queue_buf->count;
}

//buffer size is defined via MEASUREMENT_MODEL__QUEUE_SIZE
//init buffer
void measurement_model__innit(){
	measurement_model__queue_buf = ghpringbuf_create(MEASUREMENT_MODEL__QUEUE_SIZE, sizeof(MeasurementModel), 1, NULL);
}
//create record is in place code snippet
MeasurementModel measurement_model__create(){
        MeasurementModel m_model;
        //allocate the memory
    	memset(&m_model, 0, sizeof(MeasurementModel));
    	//loop through the field to set default values
        //return newly created model with set fields
    	return m_model;
}
//save record
void measurement_model__save(MeasurementModel *mr) {
	if(!measurement_model__buff_full) {
		ghpringbuf_put(measurement_model__queue_buf, mr);
		measurement_model__rec_id++;
	} else {
		measurement_model__rec_discarded++;
	}
	measurement_model__buff_full = measurement_model__is_full();
	if(measurement_model__buff_full){
		measurement_model__measurement_controller_ctr__full(mr);
	}
	measurement_model__measurement_controller_ctr__bufferSaveDone(mr);
}

//delete
void measurement_model__delete(int position) {
    //TODO: fix buffer pop at possition
    //ghpringbuf_pop(position);

}

//* record local queries */
void measurement_model__get(uint32_t position, MeasurementModel *rm){
	MeasurementModel *a_ptr;
	a_ptr = ghpringbuf_at(measurement_model__queue_buf, position);
	memcpy(&rm, a_ptr, sizeof(MeasurementModel));

}
void measurement_model__get_first(MeasurementModel *rm){
	MeasurementModel *a_ptr;
	a_ptr = ghpringbuf_at(measurement_model__queue_buf, 0);
	memcpy(&rm, a_ptr, sizeof(MeasurementModel));

}
void measurement_model__get_last(MeasurementModel *rm){
	MeasurementModel *a_ptr;
	a_ptr = ghpringbuf_at(measurement_model__queue_buf, -1);
	memcpy(&rm, a_ptr, sizeof(MeasurementModel));
}

 


#define MEASUREMENT_PACKET_MODEL__QUEUE_SIZE 500

ghpringbuf *measurement_packet_model__queue_buf ;
uint32_t measurement_packet_model__buff_size = MEASUREMENT_PACKET_MODEL__QUEUE_SIZE;
bool measurement_packet_model__buff_full = false;
uint32_t measurement_packet_model__rec_id=0;
uint32_t measurement_packet_model__rec_discarded=0;

//generic buffer implementation
ghpringbuf *buf;
//*record functions:*/
//delete buffer
void measurement_packet_model__destroy(){
    ghpringbuf_destroy(measurement_packet_model__queue_buf);
}
//is buffer full
inline static bool measurement_packet_model__is_full(){
	return measurement_packet_model__queue_buf->capacity == measurement_packet_model__queue_buf->count;
}

//buffer size is defined via MEASUREMENT_PACKET_MODEL__QUEUE_SIZE
//init buffer
void measurement_packet_model__innit(){
	measurement_packet_model__queue_buf = ghpringbuf_create(MEASUREMENT_PACKET_MODEL__QUEUE_SIZE, sizeof(MeasurementPacketModel), 1, NULL);
}
//create record is in place code snippet
MeasurementPacketModel measurement_packet_model__create(){
        MeasurementPacketModel m_model;
        //allocate the memory
    	memset(&m_model, 0, sizeof(MeasurementPacketModel));
    	//loop through the field to set default values
        //return newly created model with set fields
    	return m_model;
}
//save record
void measurement_packet_model__save(MeasurementPacketModel *mr) {
	if(!measurement_packet_model__buff_full) {
		ghpringbuf_put(measurement_packet_model__queue_buf, mr);
		measurement_packet_model__rec_id++;
	} else {
		measurement_packet_model__rec_discarded++;
	}
	measurement_packet_model__buff_full = measurement_packet_model__is_full();
	if(measurement_packet_model__buff_full){
		measurement_packet_model__measurement_controller_ctr__full(mr);
	}
	measurement_packet_model__measurement_controller_ctr__bufferSaveDone(mr);
}

//delete
void measurement_packet_model__delete(int position) {
    //TODO: fix buffer pop at possition
    //ghpringbuf_pop(position);

}

//* record local queries */
void measurement_packet_model__get(uint32_t position, MeasurementPacketModel *rm){
	MeasurementPacketModel *a_ptr;
	a_ptr = ghpringbuf_at(measurement_packet_model__queue_buf, position);
	memcpy(&rm, a_ptr, sizeof(MeasurementPacketModel));

}
void measurement_packet_model__get_first(MeasurementPacketModel *rm){
	MeasurementPacketModel *a_ptr;
	a_ptr = ghpringbuf_at(measurement_packet_model__queue_buf, 0);
	memcpy(&rm, a_ptr, sizeof(MeasurementPacketModel));

}
void measurement_packet_model__get_last(MeasurementPacketModel *rm){
	MeasurementPacketModel *a_ptr;
	a_ptr = ghpringbuf_at(measurement_packet_model__queue_buf, 500-1);
	memcpy(&rm, a_ptr, sizeof(MeasurementPacketModel));
}

 


#define ENCRYPTED_BLOB_MODEL__QUEUE_SIZE 

ghpringbuf *encrypted_blob_model__queue_buf ;
uint32_t encrypted_blob_model__buff_size = ENCRYPTED_BLOB_MODEL__QUEUE_SIZE;
bool encrypted_blob_model__buff_full = false;
uint32_t encrypted_blob_model__rec_id=0;
uint32_t encrypted_blob_model__rec_discarded=0;

//generic buffer implementation
ghpringbuf *buf;
//*record functions:*/
//delete buffer
void encrypted_blob_model__destroy(){
    ghpringbuf_destroy(encrypted_blob_model__queue_buf);
}
//is buffer full
inline static bool encrypted_blob_model__is_full(){
	return encrypted_blob_model__queue_buf->capacity == encrypted_blob_model__queue_buf->count;
}

//buffer size is defined via ENCRYPTED_BLOB_MODEL__QUEUE_SIZE
//init buffer
void encrypted_blob_model__innit(){
	encrypted_blob_model__queue_buf = ghpringbuf_create(ENCRYPTED_BLOB_MODEL__QUEUE_SIZE, sizeof(EncryptedBlobModel), 1, NULL);
}
//create record is in place code snippet
EncryptedBlobModel encrypted_blob_model__create(){
        EncryptedBlobModel m_model;
        //allocate the memory
    	memset(&m_model, 0, sizeof(EncryptedBlobModel));
    	//loop through the field to set default values
        //return newly created model with set fields
    	return m_model;
}
//save record
void encrypted_blob_model__save(EncryptedBlobModel *mr) {
	if(!encrypted_blob_model__buff_full) {
		ghpringbuf_put(encrypted_blob_model__queue_buf, mr);
		encrypted_blob_model__rec_id++;
	} else {
		encrypted_blob_model__rec_discarded++;
	}
	encrypted_blob_model__buff_full = encrypted_blob_model__is_full();
	if(encrypted_blob_model__buff_full){
		encrypted_blob_model__measurement_controller_ctr__full(mr);
	}
	encrypted_blob_model__measurement_controller_ctr__bufferSaveDone(mr);
}

//delete
void encrypted_blob_model__delete(int position) {
    //TODO: fix buffer pop at possition
    //ghpringbuf_pop(position);

}

//* record local queries */
void encrypted_blob_model__get(uint32_t position, EncryptedBlobModel *rm){
	EncryptedBlobModel *a_ptr;
	a_ptr = ghpringbuf_at(encrypted_blob_model__queue_buf, position);
	memcpy(&rm, a_ptr, sizeof(EncryptedBlobModel));

}
void encrypted_blob_model__get_first(EncryptedBlobModel *rm){
	EncryptedBlobModel *a_ptr;
	a_ptr = ghpringbuf_at(encrypted_blob_model__queue_buf, 0);
	memcpy(&rm, a_ptr, sizeof(EncryptedBlobModel));

}
void encrypted_blob_model__get_last(EncryptedBlobModel *rm){
	EncryptedBlobModel *a_ptr;
	a_ptr = ghpringbuf_at(encrypted_blob_model__queue_buf, -1);
	memcpy(&rm, a_ptr, sizeof(EncryptedBlobModel));
}

 
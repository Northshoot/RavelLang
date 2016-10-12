/**
 * Autogenerated file containing data models
 *  models.h
 *
 *  Created on: 13:14:31 10/12/2016
 *      Author: Ravel
 */
#ifndef MODELS_H_
#define MODELS_H_
//base includes
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
//default buffers
#include "api/ringbuf.h"


typedef struct _rCntx {
	uint32_t error;
	void* model;
} RavelContext;


//generating structures for the models
//
typedef struct {
    uint32_t m_time ; // AUTOGEN-DOCS: Field[name: time, type: TimeStampField, model: Random] 
    uint32_t m_value ; // AUTOGEN-DOCS: Field[name: value, type: IntegerField, model: Random] 
} RandomModel;


#define RANDOM_MODEL_SIZE sizeof(RandomModel) 

//
typedef struct {
    uint32_t m_frequency ; // AUTOGEN-DOCS: Field[name: frequency, type: IntegerField, model: Frequency] 
} FrequencyModel;


#define FREQUENCY_MODEL_SIZE sizeof(FrequencyModel) 

//
typedef struct {
    uint32_t m_time ; // AUTOGEN-DOCS: Field[name: time, type: TimeStampField, model: BootError] 
    char[] m_occurred ; // AUTOGEN-DOCS: Field[name: occurred, type: StringField, model: BootError] 
     m_context ; // AUTOGEN-DOCS: Field[name: context, type: ContextField, model: BootError] 
} BootErrorModel;


#define BOOT_ERROR_MODEL_SIZE sizeof(BootErrorModel) 

//Create internal model packets
typedef struct _metaRandomModel {
	uint32_t node_UUID;
	uint8_t origin;
	uint8_t rqACK;
	uint8_t size;
	RandomModel model;
}RandomModelPacket;

#define RANDOM_MODEL_PACKET_SIZE sizeof(RandomModelPacket) 

typedef struct _metaFrequencyModel {
	uint32_t node_UUID;
	uint8_t origin;
	uint8_t rqACK;
	uint8_t size;
	FrequencyModel model;
}FrequencyModelPacket;

#define FREQUENCY_MODEL_PACKET_SIZE sizeof(FrequencyModelPacket) 

typedef struct _metaBootErrorModel {
	uint32_t node_UUID;
	uint8_t origin;
	uint8_t rqACK;
	uint8_t size;
	BootErrorModel model;
}BootErrorModelPacket;

#define BOOT_ERROR_MODEL_PACKET_SIZE sizeof(BootErrorModelPacket) 

//define model command
//init model
void random_model__innit();
//destroy buffer
void random_model__destroy();

//*record functions:*/
//create record
RandomModel random_model__create() ;
//save record
void random_model__save(RandomModel *modelP) ;
//delete
void random_model__delete(int position) ;

//* record local queries */
//get record with position position
void random_model__get(uint32_t position, RandomModel *rm);
//get first record
void random_model__get_first(RandomModel *rm);
//get last record
void random_model__get_last(RandomModel *rm);

 

//init model
void frequency_model__innit();
//destroy buffer
void frequency_model__destroy();

//*record functions:*/
//create record
FrequencyModel frequency_model__create() ;
//save record
void frequency_model__save(FrequencyModel *modelP) ;
//delete
void frequency_model__delete(int position) ;

//* record local queries */
//get record with position position
void frequency_model__get(uint32_t position, FrequencyModel *rm);
//get first record
void frequency_model__get_first(FrequencyModel *rm);
//get last record
void frequency_model__get_last(FrequencyModel *rm);

 

//init model
void boot_error_model__innit();
//destroy buffer
void boot_error_model__destroy();

//*record functions:*/
//create record
BootErrorModel boot_error_model__create() ;
//save record
void boot_error_model__save(BootErrorModel *modelP) ;
//delete
void boot_error_model__delete(int position) ;

//* record local queries */
//get record with position position
void boot_error_model__get(uint32_t position, BootErrorModel *rm);
//get first record
void boot_error_model__get_first(BootErrorModel *rm);
//get last record
void boot_error_model__get_last(BootErrorModel *rm);

 

//end of autogenerated code

#endif /* MODELS_H_ */
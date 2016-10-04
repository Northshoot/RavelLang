/*
 * You, just keep on being awesome!
 * 
 *
 *  Autogenerated by Ravel system on: 17:23:06
 *
 *      Any editing will may result in core dump and other nasty stuff
 */

#include <stdint.h> 		 //** Used for uint type */
#include "app_error.h" 		 //** Used to deterime error */
#include "softdevice_handler.h" 		 //** Autogenrated comment for declaration type with name "softdevice_handler.h" */
#include "app_timer.h" 		 //** Autogenrated comment for declaration type with name "app_timer.h" */
#include "api_timer.h" //** obj file header */


/** Function implementations */
  
 /**
 * 
 */
void random_ctr__startTimerPeriodic(uint32_t period){
	uint32_t time =  APP_TIMER_TICKS(period,  APP_TIMER_PRESCALER) ; 		 //pre-scale should a parameter
	app_timer_start(random_ctr__startTimerPeriodic, time,  NULL);
}
  
 void random_ctr__stopTimerPeriodic(uint32_t period){
	app_timer_stop(random_ctr__stopTimerPeriodic); // 
}
  
 /**
 * Initialization method for timers
 */
void timers_init(void){
    // Initialize timer module.
    APP_TIMER_INIT(APP_TIMER_PRESCALER, APP_TIMER_OP_QUEUE_SIZE, false);
    app_timer_create(&, APP_TIMER_MODE_REPEATED, random_ctr__timer_periodic__expired);\n
}
 
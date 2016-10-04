/*
 * You, just keep on being awesome!
 * api_timer.h
 *
 *  Autogenerated by Ravel system on: 12:24:49
 *
 *      Any editing will may result in core dump and other nasty stuff
 */

#ifndef API_TIMER_H
#define API_TIMER_H

//included below
#include <stdint.h> 		 //** Used for uint type */
#include "app_error.h" 		 //** Used to deterime error */
#include "softdevice_handler.h" 		 //** Autogenrated comment for declaration type with name "softdevice_handler.h" */
#include "app_timer.h" 		 //** Autogenrated comment for declaration type with name "app_timer.h" */

// defines below
#define RANDOM_CTR__TIMER_PERIODIC APP_TIMER_TICKS(1000, APP_TIMER_PRESCALER) 		 //** Default value for the timer */
#define APP_TIMER_DEF(random_ctr__timer_periodic) 		 //** Initializing timer */

//function declarations bellow
/**
 * 
 */
void random_ctr__startTimerPeriodic(uint32_t period); 		  		  
/**
 * Stop function for random_ctr__stopTimerPeriodic
 * 
 */
void random_ctr__stopTimerPeriodic(); 		 //
   		  
void timers_init();  		  


#endif /* API_TIMER_H */
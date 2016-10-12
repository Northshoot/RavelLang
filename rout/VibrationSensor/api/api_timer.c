/**
 * Initialization method for timers
 */
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#include "api_timer.h"

#include "ravel_layer.h"
#include "../node_ctr.h"


void timers_init(void){
    // Initialize timer module.
    NRF_LOG_INFO("timers_init running\r\n");
    APP_TIMER_INIT(APP_TIMER_PRESCALER, APP_TIMER_OP_QUEUE_SIZE, false);
    app_timer_create(&m_node_ctr__timer__timer_periodic, APP_TIMER_MODE_REPEATED, node_ctr__timer__timer_periodic__expired);
}

/**
 * 
 */
void node_ctr__timer__timer_periodic__start(uint32_t period){
    NRF_LOG_INFO("timers_init started\r\n");
	uint32_t time =  APP_TIMER_TICKS(period,  APP_TIMER_PRESCALER) ; 		 //pre-scale should a parameter
    NRF_LOG_INFO(" started\r\n");
	app_timer_start(m_node_ctr__timer__timer_periodic, time,  NULL);
}
void node_ctr__timer__timer_periodic__stop(uint32_t period){
    NRF_LOG_INFO("stopping \r\n");
	app_timer_stop(m_node_ctr__timer__timer_periodic); // 
}


//**end autogenerate *//
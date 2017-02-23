#include <stdbool.h>
#include "boards.h"
#include "nrf_drv_gpiote.h"
#include "app_error.h"
#include "app_timer.h"
#include "nrf_drv_clock.h"
#include "app_scheduler.h"
#include "nordic_common.h"
#define NRF_LOG_MODULE_NAME "SYS_T"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"


#include "ravel_error.h"
#include "ravel_timer.h"

/**
 * Generic module providing abstract timer interface
 * We perform internal virtualizaton of the timers rather than use NRF
 * The reason is control of the amount of timmers that are out there
 * in the system
 * Ported to NRF52
 */



APP_TIMER_DEF(m_ravel_system_timer_id);

static uint32_t sys_number_of_timer = 0;

void update_timers_state();

void ravel_system_timer_handler(void *context)
{
    NRF_LOG_INFO("fired\r\n");
}

RAVEL_DRIVER_ERROR init_timer_module()
{
    uint32_t err_code;
    err_code = app_timer_create(&m_ravel_system_timer_id,
                                APP_TIMER_MODE_SINGLE_SHOT,
                                ravel_system_timer_handler);
    APP_ERROR_CHECK(err_code);

    NRF_LOG_INFO("INIT!\r\n");
    return SUCCESS;
}



RAVEL_DRIVER_ERROR create_new_timer(DriverTimer *timer, int id)
{
    NRF_LOG_INFO("NEW TIMER %d\r\n", id);
    if( sys_number_of_timer == MAX_TIMERS )
        return NO_MEMORY;

    timer->__id = id;
    timer->__is_one_shoot = false;
    timer->__is_running = false;
    timer->__reserved  = false;
    //m_timers[number_of_timer] =  &timer;
    sys_number_of_timer++;
    return SUCCESS;
}
/**
 * Internal function to fire of timers
 * @param now
 */
void fire_timers(uint32_t now)
{
    uint16_t num;

    for (num = 0; num < MAX_TIMERS; num++) {
        DriverTimer *timer = &m_timers[num];

        if (timer->__is_running) {
            uint32_t elapsed = now - timer->t0;

            if (elapsed >= timer->dt) {
                if (timer->__is_one_shoot)
                    timer->__is_running = false;
                else // Update timer for next event
                    timer->t0 += timer->dt;

               // app_sched_event_put(&timer->__id, sizeof(timer->__id), ravel_system_timer_handler);
                break;
            }
        }
    }
    //app_sched_event_put(NULL, 0, update_timers_state);
}
/**
 * Internal function that loops through a set of timer
 */
void update_timers_state()
{
    /* This code supports a maximum dt of MAXINT. If min_remaining and
       remaining were switched to uint32_t, and the logic changed a
       little, dt's up to 2^32-1 should work (but at a slightly higher
       runtime cost). */
    uint32_t now = app_timer_cnt_get();
    int32_t min_remaining = (1UL << 31) - 1; /* max int32_t */
    bool min_remaining_isset = false;
    uint16_t num;

    uint32_t err_code = app_timer_stop(m_ravel_system_timer_id);
    APP_ERROR_CHECK(err_code);

    for (num=0; num<MAX_TIMERS; num++)
    {
        DriverTimer* timer = &m_timers[num];

        if (timer->__is_running)
        {
            uint32_t elapsed = now - timer->t0;
            int32_t remaining = timer->dt - elapsed;

            if (remaining < min_remaining)
            {
                min_remaining = remaining;
                min_remaining_isset = true;
            }
        }
    }

    if (min_remaining_isset)
    {
        if (min_remaining <= 0)
            fire_timers(now);
        else
            app_timer_start(m_ravel_system_timer_id,min_remaining, NULL);
    }
}
/**
 * Internal function to start timer
 * @param timer
 * @param time
 * @return SUCCESS
 */
RAVEL_DRIVER_ERROR start_timer(DriverTimer *timer,uint32_t time){

    uint32_t err_code;
    err_code = app_timer_start(m_ravel_system_timer_id, APP_TIMER_TICKS(time, APP_TIMER_PRESCALER), NULL);
    NRF_LOG_INFO("started %d\r\n", err_code);
 APP_ERROR_CHECK(err_code);
    NRF_LOG_INFO("started\r\n");
    return SUCCESS;
}

RAVEL_DRIVER_ERROR timer_start_periodic(DriverTimer *timer,uint32_t time){
    NRF_LOG_INFO("periodic::: %d\r\n", time);
    timer->__is_running = true;
    return start_timer(timer, time);
}
RAVEL_DRIVER_ERROR timer_start_single_shoot(DriverTimer *timer,uint32_t time){
    timer->__is_one_shoot = true;
    timer->__is_running = true;
    return start_timer(timer, time);

}


RAVEL_DRIVER_ERROR timer_cancel(DriverTimer *timer){
    timer->__is_running = false;
    return SUCCESS;
}
RAVEL_DRIVER_ERROR timer_cancel_all(void){
    uint32_t err_code = app_timer_stop(m_ravel_system_timer_id);
    APP_ERROR_CHECK(err_code);

    for(int i = 0; i < sys_number_of_timer; i++){
        m_timers[i].__is_running = false;
    }
    return SUCCESS;
}

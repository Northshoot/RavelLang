#include <stdbool.h>
#include "boards.h"
#include "nrf_drv_gpiote.h"
#include "app_error.h"
#include "app_timer.h"

#include "app_scheduler.h"
#include "nordic_common.h"
#define NRF_LOG_MODULE_NAME "SYS_T"
#define NRF_LOG_LEVEL 4
#include "nrf_log.h"
#include "nrf_log_ctrl.h"
#include "nrf_sdm.h"

#include "nrf52_counter.h"

#include "nrf52_ravel_error.h"
#include "nrf52_ravel_timer.h"

/**
 * Generic module providing abstract timer interface
 * We perform internal virtualizaton of the timers rather than use NRF
 * The reason is control of the amount of timers that are out there
 * in the system
 * Ported to NRF52
 *
 * TODO: a compensation is needed for how many ticks it takes to execute timer
 */

// General application timer settings.
#define APP_TIMER_PRESCALER              0
#define APP_TIMER_OP_QUEUE_SIZE          4

APP_TIMER_DEF(m_ravel_system_timer_id);

static uint32_t sys_number_of_timer = 0;
static bool timer_running = false;

void update_timers_state(void *p_event_data, uint16_t event_size);
void  fire_timers(uint32_t now);

void
ravel_system_timer_handler(void *context)
{
    fire_timers(counter_get());
}

RAVEL_DRIVER_ERROR init_timer_module()
{
    uint32_t err_code=0;
    err_code = app_timer_init();
    APP_ERROR_CHECK(err_code);
    err_code = app_timer_create(&m_ravel_system_timer_id,
                                APP_TIMER_MODE_SINGLE_SHOT,
                                ravel_system_timer_handler);
    APP_ERROR_CHECK(err_code);
    counter_init();
    counter_start();
    NRF_LOG_INFO("INIT! at %u\r\n", counter_get());
    return SUCCESS;
}



RAVEL_DRIVER_ERROR
create_new_timer(DriverTimer *dtimer, int timer_id, fired_callback callback, void *timer)
{
    if( sys_number_of_timer == MAX_TIMERS )
        return NO_MEMORY;

    dtimer->__id = timer_id;
    dtimer->call_back = callback;
    dtimer->__is_one_shoot = false;
    dtimer->__is_running = false;
    dtimer->__reserved  = false;
    dtimer->__timer = timer;
    m_timers[sys_number_of_timer] =  dtimer;
    sys_number_of_timer++;
    return SUCCESS;
}
/**
 * Internal function to fire of timers
 * @param now
 */
void
fire_timers(uint32_t now)
{
    uint16_t num;
    uint32_t err_code;
    for (num = 0; num < sys_number_of_timer; num++) {
        DriverTimer *timer = m_timers[num];
        if (timer->__is_running) {
            uint32_t elapsed = now - timer->t0;
            if (elapsed >= timer->dt) {
                if (timer->__is_one_shoot)
                    timer->__is_running = false;
                else // Update timer for next event
                    timer->t0 += timer->dt;
                //call back to the timer subscriber
                timer->call_back(timer->__timer);
                break;
            }
        }
    }
    err_code = app_sched_event_put(NULL, 0, update_timers_state);
    APP_ERROR_CHECK(err_code);
}
/**
 * Internal function that loops through a set of timer
 * ideas a reused form TinyOS virtual timer
 */
void update_timers_state(void *p_event_data, uint16_t event_size)
{
    /* This code supports a maximum dt of MAXINT. If min_remaining and
       remaining were switched to uint32_t, and the logic changed a
       little, dt's up to 2^32-1 should work (but at a slightly higher
       runtime cost). */
    uint32_t now = counter_get();

    int32_t min_remaining = (1UL << 31) - 1; /* max int32_t */
    bool min_remaining_isset = false;
    uint16_t num;

    uint32_t err_code;
    //first time it is not running, so better not to stop
    if (timer_running) {
        app_timer_stop(m_ravel_system_timer_id);
        APP_ERROR_CHECK(err_code);
    }

    for (num=0; num<sys_number_of_timer; num++)
    {
        DriverTimer* timer = m_timers[num];

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
        //With many timers this need to be compensated or timer end up in fatal state
        //TODO: investigate the reason
        if (min_remaining <= 10)
        {
            fire_timers(now);
        }
        else {
            uint32_t err_code;
            if (!timer_running) {
                err_code = app_timer_start(m_ravel_system_timer_id,min_remaining, NULL);
                APP_ERROR_CHECK(err_code);
            }

        }
    }

}
/**
 * Internal function to start timer
 * @param timer
 * @param time
 * @return SUCCESS
 */
RAVEL_DRIVER_ERROR
start_timer(DriverTimer *timer, uint32_t t0, uint32_t time)
{
    uint32_t err_code;
    timer->__is_running = true;
    timer->t0 = t0;
    timer->dt = APP_TIMER_TICKS(time);
    err_code = app_sched_event_put(NULL, 0,update_timers_state);
    APP_ERROR_CHECK(err_code);
    return SUCCESS;
}

RAVEL_DRIVER_ERROR
timer_start_periodic(DriverTimer *timer,uint32_t time){
    timer->__is_running = true;
    return start_timer(timer, counter_get(), time);
}
RAVEL_DRIVER_ERROR
timer_start_single_shoot(DriverTimer *timer,uint32_t time){
    timer->__is_one_shoot = true;

    return start_timer(timer, counter_get(), time);

}


RAVEL_DRIVER_ERROR
timer_cancel(DriverTimer *timer){
    timer->__is_running = false;
    return SUCCESS;
}
RAVEL_DRIVER_ERROR
timer_cancel_all(void){
    uint32_t err_code = app_timer_stop(m_ravel_system_timer_id);
    APP_ERROR_CHECK(err_code);

    for(int i = 0; i < sys_number_of_timer; i++){
        m_timers[i]->__is_running = false;
    }
    return SUCCESS;
}

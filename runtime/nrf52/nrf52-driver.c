#include <stdlib.h>

/* soft device inlcudes */
#include "app_util.h"
#include "app_error.h"
#include "app_scheduler.h"
#include "nrf_soc.h"
#include "softdevice_handler.h"
#include <stdbool.h>
#include "boards.h"
#include "nrf_drv_gpiote.h"
#include "app_error.h"
#include "app_timer.h"
#include "nrf_drv_clock.h"
/* local includes */
#include "platform/ble_core.h"
#include "platform/network.h"
#include "platform/flash.h"
#include "platform/config.h"
#include "platform/log.h"
#include "boards.h"
#include "AppDispatcher.h"
#include "ravel/nrf52-driver.h"

extern void ravel_generated_app_dispatcher_started(struct AppDispatcher*);


//void app_error_handler(uint32_t error_code, uint32_t line_num, const uint8_t * p_file_name) {
//    #ifdef DEBUG
//        LOG("ERROR - %lu, %lu, %s", error_code, line_num, p_file_name);
//    #endif
//    // On assert, the system can only recover on reset
//    //while (1) {}
//    //NVIC_SystemReset();
//    sd_nvic_SystemReset();
//}

void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name) {
    app_error_handler(0xDEADBEEF, line_num, p_file_name);
}

//static void power_manage(void) {
//    /* execute any scheduled operations */
//    //ravel_nrf52_driver_dispatch_event(&driver);
//    app_sched_execute();
//
//    /* Wait for an interrupt/event */
//    uint32_t err_code = sd_app_evt_wait();
//    APP_ERROR_CHECK(err_code);
//}

void
ravel_nrf52_driver_init(RavelNrf52Driver *self, AppDispatcher *dispatcher)
{
    /* TODO: init any internal systems */
    self->base.dispatcher = dispatcher;
    nrf_clock_lf_cfg_t clock_lf_cfg = NRF_CLOCK_LFCLKSRC;

        // Initialize the SoftDevice handler module.
        SOFTDEVICE_HANDLER_INIT(&clock_lf_cfg, NULL);


}

void
ravel_nrf52_driver_finalize(RavelNrf52Driver *self)
{
    /* Free any context resource here */
}
static void power_manage(void)
{
    uint32_t err_code = sd_app_evt_wait();

    APP_ERROR_CHECK(err_code);
}
//TODO: delete timer and led JUST a test
#define APP_TIMER_PRESCALER             15    // Value of the RTC1 PRESCALER register.
#define APP_TIMER_OP_QUEUE_SIZE         3     // Size of timer operation queues.
APP_TIMER_DEF(m_timer_t);

void
this_fired(void *context)
{
bsp_board_led_invert(3);
}
void
ravel_nrf52__driver_main_loop(RavelNrf52Driver *self)
{
    // TODO
    // Main loop.
    uint32_t err_code = nrf_drv_clock_init();
        APP_ERROR_CHECK(err_code);
        nrf_drv_clock_lfclk_request(NULL);
    APP_TIMER_INIT(APP_TIMER_PRESCALER, APP_TIMER_OP_QUEUE_SIZE, false);
    app_timer_create(&m_timer_t,
                                APP_TIMER_MODE_REPEATED,
                                this_fired);
    app_timer_start(m_timer_t, 500, NULL);

    while (true)
    {
        power_manage();
    }
}

void
ravel_nrf_driver_dispatch_event(RavelNrf52Driver *selft)
{
    /* TODO */
}

void
ravel_nrf52_driver_app_dispatcher_ready(RavelNrf52Driver *self)
{
    ravel_generated_app_dispatcher_started(self->base.dispatcher);
}

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
#define NRF_LOG_MODULE_NAME "Driver"

#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#define APP_TIMER_PRESCALER              0
#define APP_TIMER_OP_QUEUE_SIZE          4
APP_TIMER_DEF(m_driver_timer_periodic);

extern void ravel_generated_app_dispatcher_started(struct AppDispatcher*);


//void app_error_handler(uint32_t error_code, uint32_t line_num, const uint8_t * p_file_name) {
//    LOG("ERROR - %lu, %lu, %s", error_code, line_num, p_file_name);
//    // On assert, the system can only recover on reset
//    //while (1) {}
//    //NVIC_SystemReset();
//    sd_nvic_SystemReset();
//}

void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name) {
    NRF_LOG_INFO("assert_nrf_callback!\r\n");
    app_error_handler(0xDEADBEEF, line_num, p_file_name);
}



void
ravel_nrf52_driver_init(RavelNrf52Driver *self, AppDispatcher *dispatcher)
{
    /* TODO: init any internal systems */
    NRF_LOG_INFO("INIT!\r\n");

    self->base.dispatcher = dispatcher;



}

void
ravel_nrf52_driver_finalize(RavelNrf52Driver *self)
{
    /* Free any context resource here */
    NRF_LOG_INFO("FINALIZE!\r\n");
}




void
ravel_nrf52__driver_main_loop(RavelNrf52Driver *self)
{
    // TODO
    // Main loop.
    NRF_LOG_INFO("LOOP\r\n");
    while (true)
    {
         __WFI();
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

#include <stdlib.h>

/* soft device inlcudes */
#include "app_util.h"
#include "app_error.h"
#include "app_scheduler.h"
#include "nrf_soc.h"
#include "softdevice_handler.h"

/* local includes */
#include "platform/ble_core.h"
#include "platform/network.h"
#include "platform/flash.h"
#include "platform/config.h"
#include "platform/log.h"

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
//    ravel_nrf52_driver_dispatch_event(&driver, ev);
//    app_sched_execute();
//
//    /* Wait for an interrupt/event */
//    uint32_t err_code = sd_app_evt_wait();
//    APP_ERROR_CHECK(err_code);
//}

void
ravel_nrf52_driver_init(RavelNrf52Driver *self, AppDispatcher *dispatcher)
{
    /* TODO */
    self->base.dispatcher = dispatcher;
}

void
ravel_nrf52_driver_finalize(RavelNrf52Driver *self)
{
    /* Free any context resource here */
}
void
ravel_nrf52__driver_main_loop(RavelNrf52Driver *self)
{
    // TODO
     for (;;) {
            // this is where the main execution happens
            //power_manage();
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

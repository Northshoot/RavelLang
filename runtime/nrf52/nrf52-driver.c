#include <stdlib.h>

/* soft device inlcudes */
#include "app_util.h"
#include "app_error.h"
#include "app_scheduler.h"
#include "nrf_soc.h"
#include "softdevice_handler.h"

/* local includes */
#include "platform/ble.h"
#include "platform/network.h"
#include "platform/flash.h"
#include "platform/config.h"
#include "platform/log.h"

void app_error_handler(uint32_t error_code, uint32_t line_num, const uint8_t * p_file_name) {
    #ifdef DEBUG
        LOG("ERROR - %lu, %lu, %s", error_code, line_num, p_file_name);
    #endif
    // On assert, the system can only recover on reset
    //while (1) {}
    //NVIC_SystemReset();
    sd_nvic_SystemReset();
}

void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name) {
    app_error_handler(0xDEADBEEF, line_num, p_file_name);
}



ravel_nrf_driver_init(RavelNRFDriver *self, AppDispatcher *dispatcher)
{
    /* TODO */
    self->base.dispatcher = dispatcher;
}

void
ravel_nrf_driver_finalize(RavelNRFDriver *self)
{
    /* Free any context resource here */
}

void
ravel_nrf_driver_dispatch_event(RavelNRFDriver *self, process_event_t event)
{
    /* TODO */
}

void
ravel_nrf_driver_app_dispatcher_ready(RavelNRFDriver *self)
{
    ravel_generated_app_dispatcher_started(self->base.dispatcher);
}
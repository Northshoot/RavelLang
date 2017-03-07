#include <stdlib.h>
#include <stdbool.h>
#include "app_util.h"
#include "app_error.h"
#include "app_scheduler.h"
#include "nordic_common.h"
#include "nrf_soc.h"
#include "softdevice_handler.h"
#include "boards.h"
#include "nrf_drv_gpiote.h"
#include "app_error.h"
#include "app_timer.h"
#include "nrf_drv_clock.h"

/* local includes */
#include "platform/nrf52_ble_core.h"
#include "platform/nrf52_network.h"
#include "platform/nrf52_flash.h"
#include "platform/nrf52_config.h"
#include "platform/nrf52_log.h"
#include "boards.h"
#include "AppDispatcher.h"
#include "nrf52_ravel_timer.h"
#include "ravel/nrf52-driver.h"
//Implement global driver API
#include "driver.h"
#include "context.h"

#define NRF_LOG_MODULE_NAME "DRV"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#define DEAD_BEEF                       0xDEADBEEF

/**** ****/
RavelEndpoint * const *
ravel_driver_get_endpoints_by_name(RavelDriver *driver, const char *name){
    return NULL;
}

RavelError
ravel_driver_send_data(RavelDriver *driver, RavelPacket *packet, RavelEndpoint *endpoint)
{
    NRF_LOG_INFO("ravel_driver_send_data!\r\n");
    return RAVEL_ERROR_SUCCESS;
}


/**** NRF 52 specific implementations ****/
#define SCHED_MAX_EVENT_DATA_SIZE       sizeof(ravel_schedule_event_cntx)
#define SCHED_QUEUE_SIZE                20

extern void ravel_generated_app_dispatcher_started(struct AppDispatcher*);

void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name) {
    NRF_LOG_INFO("assert_nrf_callback!\r\n");
    app_error_handler(0xDEADBEEF, line_num, p_file_name);
}


void
ravel_nrf52_driver_init(RavelNrf52Driver *self, RavelBaseDispatcher *dispatcher, const char *app_name)
{
    /* TODO: init any internal systems */
    NRF_LOG_INFO("INIT!\r\n");
    nrf_clock_lf_cfg_t clock_lf_cfg = NRF_CLOCK_LFCLKSRC;

    // Initialize the SoftDevice handler module.
    SOFTDEVICE_HANDLER_INIT(&clock_lf_cfg, NULL);

    APP_SCHED_INIT(SCHED_MAX_EVENT_DATA_SIZE, SCHED_QUEUE_SIZE);
    self->base.dispatcher = dispatcher;
    init_timer_module();



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
    // Main loop.

    ble_start();
    NRF_LOG_INFO("LOOP\r\n");
    //
    while (true)
    {
        app_sched_execute();
    }
}

void
ravel_nrf_driver_dispatch_event(RavelNrf52Driver *selft)
{
    NRF_LOG_INFO("driver dispatch evt\r\n");
}

void
ravel_nrf52_driver_app_dispatcher_ready(RavelNrf52Driver *self)
{
    ravel_generated_app_dispatcher_started(self->base.dispatcher);
}

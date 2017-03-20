#include <stdint.h>
#include <string.h>
#include "nordic_common.h"
#include "app_scheduler.h"
#include "nrf.h"
#include "ble_hci.h"
#include "ble_advdata.h"
#include "ble_advertising.h"
#include "ble_conn_params.h"
#include "softdevice_handler.h"
#include "app_timer.h"
#include "app_button.h"
#include "app_uart.h"
#include "app_util_platform.h"
#include "bsp.h"
#include "bsp_btn_ble.h"

#define NRF_LOG_MODULE_NAME "M"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"
#include "nrf52_ble_core.h"
#include "nrf52_ble_rad.h"
#include "ble_bas.h"
/**< The advertising timeout (in units of seconds). */

#define APP_TIMER_PRESCALER             0                                           /**< Value of the RTC1 PRESCALER register. */
#define APP_TIMER_OP_QUEUE_SIZE         4                                           /**< Size of timer operation queues. */

#define DEAD_BEEF                       0xDEADBEEF                                  /**< Value used as error code on stack dump, can be used to identify stack location on stack unwind. */

typedef struct {
    uint32_t __module;
} ravel_schedule_event_cntx;

NetworkClb network;


void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name)
{
    app_error_handler(DEAD_BEEF, line_num, p_file_name);
}


static void power_manage(void)
{
    app_sched_execute();
}

/**** NRF 52 specific implementations ****/
#define SCHED_MAX_EVENT_DATA_SIZE       sizeof(ravel_schedule_event_cntx)
#define SCHED_QUEUE_SIZE                20



int main(void)
{
    uint32_t err_code;
    err_code = NRF_LOG_INIT(NULL);
    APP_ERROR_CHECK(err_code);
    nrf_clock_lf_cfg_t clock_lf_cfg = NRF_CLOCK_LFCLKSRC;

    // Initialize SoftDevice.
    SOFTDEVICE_HANDLER_INIT(&clock_lf_cfg, NULL);
    // Initialize.
    APP_TIMER_INIT(APP_TIMER_PRESCALER, APP_TIMER_OP_QUEUE_SIZE, false);
    APP_SCHED_INIT(SCHED_MAX_EVENT_DATA_SIZE, SCHED_QUEUE_SIZE);



    //TODO: create ble dispatcher
    //TODO: add dispatcher to the ble stack
//    init_timer_module();
//    nrf52_network_init(&network);
    ble_bas_init_handler();
    ble_rad_init_handler();
    nrf52_r_core_ble_stack_init(&network);
    nrf52_r_core_ble_start();

    // Enter main loop.
    for (;;)
    {
        power_manage();
    }
}


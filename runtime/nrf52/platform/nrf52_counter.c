
#include "nrf52_counter.h"
#include "nrf_drv_rtc.h"
#define NRF_LOG_MODULE_NAME "CNTR::"
#define NRF_LOG_LEVEL 1
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

/* RTC driver instance using RTC2.
 * RTC0 is used by the SoftDevice, and RTC1 by the app_timer library. */
static const nrf_drv_rtc_t m_rtc = NRF_DRV_RTC_INSTANCE(2);
static bool m_started = false;
static bool m_init = false;

static void rtc_handler(nrf_drv_rtc_int_type_t int_type)
{
    // Likely a counter overflow.
    APP_ERROR_CHECK(0xFFFFFFFF);
}


void counter_init(void)
{
    ret_code_t err_code;

    // Initialize the RTC instance.
    nrf_drv_rtc_config_t config = NRF_DRV_RTC_DEFAULT_CONFIG;

    //
    config.prescaler = 0;

    err_code = nrf_drv_rtc_init(&m_rtc, &config, rtc_handler);
    APP_ERROR_CHECK(err_code);

    nrf_drv_rtc_tick_disable(&m_rtc);
    m_init = true;
    NRF_LOG_DEBUG("Init\r\n");
}


void counter_start(void)
{
    if(!m_init)
        counter_init();
    nrf_drv_rtc_counter_clear(&m_rtc);
    // Power on!
    nrf_drv_rtc_enable(&m_rtc);
    m_started = true;
    NRF_LOG_DEBUG("Start\r\n");
}


void counter_stop(void)
{
    nrf_drv_rtc_disable(&m_rtc);
    m_started = false;
}



inline static uint32_t __int_counter_get(void)
{
    if(!m_started)
        counter_start();

    return(nrf_drv_rtc_counter_get(&m_rtc));
}

uint32_t counter_get(void)
{
    return( counter_ms() );
}


uint32_t counter_ms(void)
{
    return ( __int_counter_get()) ;

}


/* Copyright (c) 2016 Nordic Semiconductor. All Rights Reserved.
 *
 * The information contained herein is property of Nordic Semiconductor ASA.
 * Terms and conditions of usage are described in detail in NORDIC
 * SEMICONDUCTOR STANDARD SOFTWARE LICENSE AGREEMENT.
 *
 * Licensees are granted free, non-transferable use of the information. NO
 * WARRANTY of ANY KIND is provided. This heading must NOT be removed from
 * the file.
 *
 */

/**@cond To Make Doxygen skip documentation generation for this file.
 * @{
 */

#include "nrf52_counter.h"
#include "nrf_drv_rtc.h"


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

    // 10 ms interval.
    config.prescaler = 15;

    err_code = nrf_drv_rtc_init(&m_rtc, &config, rtc_handler);
    APP_ERROR_CHECK(err_code);

    //nrf_drv_rtc_tick_disable(&m_rtc);
    m_init = true;
}


void counter_start(void)
{
    nrf_drv_rtc_counter_clear(&m_rtc);

    // Power on!
    nrf_drv_rtc_enable(&m_rtc);
    m_started = true;
}


void counter_stop(void)
{
    nrf_drv_rtc_disable(&m_rtc);
}


uint32_t counter_get(void)
{
    if(!m_init)
        counter_init();
    if(!m_started)
        counter_start();

    return(nrf_drv_rtc_counter_get(&m_rtc));
}

/** @}
 *  @endcond
 */

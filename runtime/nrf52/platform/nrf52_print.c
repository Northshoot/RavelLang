#define NRF_LOG_MODULE_NAME "SYS"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"
#include "nrf_sdm.h"

#include "api/system.h"

void
ravel_system_print(RavelSystemAPI *self, const char *msg)
{
    NRF_LOG_DEBUG("%s\r\n", (uint32_t)msg);
}

void
ravel_system_print_number(RavelSystemAPI *self, const char *msg, int32_t number)
{
    NRF_LOG_DEBUG("%s %d\r\n", (uint32_t)msg, number);
}

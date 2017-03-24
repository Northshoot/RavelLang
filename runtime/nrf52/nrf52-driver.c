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
#include "nrf52_ravel_endpoint.h"
#include "nrf52_ble_rad.h"
//Implement global driver API
#include "driver.h"
#include "context.h"
#include "packet.h"
#include "intrinsics.h"

//FIXME: ops static keys :O
#include "temp_keys.h"

#define NRF_LOG_MODULE_NAME "DRV"
#include "nrf_log.h"
#include "nrf_log_ctrl.h"

#define DEAD_BEEF                       0xDEADBEEF

NetworkClb network;

#define MAX_NUMBER_OF_ENDPOINTS 1


#define SCHED_MAX_EVENT_DATA_SIZE       sizeof(ravel_schedule_event_cntx)
#define SCHED_QUEUE_SIZE                20


static RavelEndpoint *endpoints[2] = { NULL, NULL };
static bool m_network_is_busy = false;
/**** ****/
RavelEndpoint * const *
ravel_driver_get_endpoints_by_name(RavelDriver *driver, int32_t name)
{
    return endpoints;
}


void
ravel_nrf52_driver_set_endpoint(RavelNrf52Driver *driver, nrf52_endpoint *endpoint)
{
    if (endpoint != NULL) {
        NRF_LOG_DEBUG("setting endpoint\r\n");
        endpoints[0] = &endpoint->m_ravel_endpoint;
        ravel_base_dispatcher_endpoint_connected(driver->base.dispatcher, endpoints[0]);
    } else {
        endpoints[0] = NULL;
    }
}

RavelPacket pkt_out;
RavelEndpoint *endpoint_out;

void ravel_nrf52_driver_rx_data_from_low(RavelDriver *self, RavelPacket *packet,nrf52_endpoint *endpoint)
{
    NRF_LOG_DEBUG("dispatching packet received model id %u record id %u is ack %u\r\n", packet->model_id, packet->record_id, packet->is_ack);
    ravel_base_dispatcher_data_received(self->dispatcher, packet, &endpoint->m_ravel_endpoint);
}

RavelError
ravel_driver_send_data(RavelDriver *driver, RavelPacket *packet, RavelEndpoint *endpoint)
{
    if (!m_network_is_busy) {
        memcpy(&pkt_out, packet, sizeof(RavelPacket));
        endpoint_out = endpoint;
        m_network_is_busy = true;
        NRF_LOG_DEBUG("Ravel PKT %u\r\n", pkt_out.packet_length);
        if (network_send(packet, endpoint)) {
            return RAVEL_ERROR_IN_TRANSIT;
        } else {
            m_network_is_busy = false;
            return RAVEL_ERROR_NETWORK_ERROR;
        }
    } else {
        ravel_packet_finalize (packet);
        return  RAVEL_ERROR_BUSY;
    }
}

void
ravel_nrf52_driver_send_done_from_low(RavelDriver *self)
{
    NRF_LOG_DEBUG("SIGNAL_UP_SEND_DONE \r\n");
    m_network_is_busy = false;
    RavelPacket copy = pkt_out;

    ravel_base_dispatcher_send_done(self->dispatcher, RAVEL_ERROR_SUCCESS, &copy, endpoint_out);
    ravel_packet_finalize(&copy);
}
void
ravel_driver_save_durably(RavelDriver *driver, RavelPacket *packet)
{
    // TODO implement
    ravel_packet_finalize(packet);
}


/**** NRF 52 specific implementations ****/
//ravel_schedule_event_cntx

extern void ravel_generated_app_dispatcher_started(struct AppDispatcher*);

void assert_nrf_callback(uint16_t line_num, const uint8_t * p_file_name) {
    NRF_LOG_INFO("assert_nrf_callback!\r\n");
    app_error_handler(0xDEADBEEF, line_num, p_file_name);
}


void
ravel_nrf52_driver_init(RavelNrf52Driver *self, RavelBaseDispatcher *dispatcher, const char *app_name, int32_t app_id)
{
    /* TODO: init any internal systems */
    NRF_LOG_INFO("INIT!\r\n");
    nrf_clock_lf_cfg_t clock_lf_cfg = NRF_CLOCK_LFCLKSRC;

    // Initialize the SoftDevice handler module.
    SOFTDEVICE_HANDLER_INIT(&clock_lf_cfg, NULL);

    APP_SCHED_INIT(SCHED_MAX_EVENT_DATA_SIZE, SCHED_QUEUE_SIZE);
    self->base.dispatcher = dispatcher;
    self->network = network;
    init_timer_module();

    ravel_key_provider_init(&self->base.key_provider);


    bool ret = false;
    ret =ravel_key_provider_add_key(&self->base.key_provider, 0, 16, key_0);
    ret =ravel_key_provider_add_key(&self->base.key_provider, 1, 64, key_1);
    ret =ravel_key_provider_add_key(&self->base.key_provider, 2, 16, key_2);
    ret =ravel_key_provider_add_key(&self->base.key_provider, 3, 64, key_3);
}


void
ravel_nrf52_driver_finalize(RavelNrf52Driver *self)
{
    /* Free any context resource here */
    NRF_LOG_INFO("FINALIZE!\r\n");
    ravel_key_provider_finalize(&self->base.key_provider);
}


void
ravel_nrf52__driver_main_loop(RavelNrf52Driver *self)
{
    // Main loop.
     nrf52_network_init(&self->network);
     ble_rad_init_handler();
     nrf52_r_core_ble_stack_init(&self->network);

    //START BLE
    nrf52_r_core_ble_start();
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
    ravel_generated_app_dispatcher_started((struct AppDispatcher*)self->base.dispatcher);
}

static void callback_event_handler(void *p_event_data, uint16_t event_size)
{
    ravel_schedule_event_cntx *data = p_event_data;

    data->callback(data->data1, data->data2);
}

void
ravel_driver_queue_callback(RavelDriver *driver, void (*callback)(void*,void*), void *ptr1, void *ptr2)
{
    ravel_schedule_event_cntx data = { ptr1, ptr2, callback };
    app_sched_event_put(&data,sizeof(ravel_schedule_event_cntx), callback_event_handler);
}

#ifndef NRF52_ENDPOINT_H
#define NRF52_ENDPOINT_H

#include "api/endpoint.h"

typedef struct {
    RavelEndpoint m_ravel_endpoint;
    uint16_t m_tx_uuid;
} nrf52_endpoint;


#endif
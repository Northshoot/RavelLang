//
// Created by lauril on 2/22/17.
//

#ifndef NRF52_MODULE_H
#define NRF52_MODULE_H

enum RAVEL_MODULE {
    RAVEL_TIMER ,
    BLE,
    SCHEDULER,
    STORAGE,
    HMAC,
    LOG,
    QUEUE,
    SENSOR,
    SHA
};

typedef enum RAVEL_MODULE RAVEL_MODULE;

#endif //NRF52_MODULE_H

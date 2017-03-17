#include "nrf52_ble_interface.h"

#define MAX_HANDLERS 5

static void *handlers[MAX_HANDLERS];


void on_connected(void) {
    size_t i;

    for (i = 0; i < MAX_HANDLERS; i++) {
        if (handlers[i]) {
            void *handler = handlers[i];
            struct BleInterfaceVtable *vtable = *((struct BleInterfaceVtable**)handler);

            vtable->connected(handler, true);
        }
    }
}

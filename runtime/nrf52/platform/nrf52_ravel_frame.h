//
// Created by lauril on 2/27/17.
//

#ifndef NRF52_RAVEL_FRAME_H
#define NRF52_RAVEL_FRAME_H

typedef uint8_t rf_ctrf_flags;

typedef struct {
    uint8_t indx; /* packet index */
    rf_ctrf_flags ctrf_flags; /* masked control field flags */
    uint8_t *data;
}__attribute__((packed)) data_packet_t;

#endif //NRF52_RAVEL_FRAME_H

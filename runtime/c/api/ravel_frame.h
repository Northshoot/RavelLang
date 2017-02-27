//
// Created by lauril on 2/27/17.
//

#ifndef C_RAVEL_FRAME_H
#define C_RAVEL_FRAME_H
typedef struct {
    uint8_t dest_addr[8];           /**< Destination address */
    uint8_t src_addr[8];            /**< Source address */
    rf_ctrf fcf;                     /**< Frame control field  */
    uint8_t *payload;               /**< Pointer to ravel payload */
    int payload_len;                /**< Length of payload field 20 bytes in NRF*/
} ravel_frame;

#endif //C_RAVEL_FRAME_H

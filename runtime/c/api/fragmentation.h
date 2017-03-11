//
// Created by lauril on 3/10/17.
//

#ifndef C_FRAGMENTATION_H
#define C_FRAGMENTATION_H

#define BT_MF 0x8000       /* more fragments flag */
#define BT_OFF_MASK 0x7fff /* mask for fragment bits */
#define BT_OFF_BLKSIZE 4   /* offset value of 1 = offset of 8 bytes */
#define BT_OFF_MAX 32767   /* 2^(15)-1 */

#define BT_FRAME_LEN 950   /* maximum without fragmentation is 957 */

#define CCNX_PROTO 0xffff  /* ccnx protocol number */


/***********************************
 * Send CCN packet over bluetooth device
 *  handles fragmentation
 */
int send_ccn_over_bt(const char * bt_dev, const void *data, size_t length, int s);

/***********************************
 * Receives CCN packets over bluetooth device
 *   returns -1 if there is an error
 *   returns 0 if a non-terminating fragment is received
 *   returns length of data if terminating fragment is received
 *   data in packets are written to ccn_packet
 */
int recv_ccn_over_bt(const char * bt_dev, char * ccnx_pkt, int socket);

/***********************************
 * extended bluetooth header
 * containing fragmentation & checksum headers
 */
struct ext_bt_header{
    char dest_mac[18];
    char src_mac[18];
    unsigned char  type;       // 4 bits
    uint16_t protocol;         // 2 bytes

    // bluetooth fragmentation & checksum
    uint32_t length;           // 4 bytes
    uint16_t id;               // 2 bytes
    uint16_t offset;           // 2 bytes

    //not implemented
    uint32_t checksum;         // 4 bytes
} __attribute__((packed));
#endif //C_FRAGMENTATION_H

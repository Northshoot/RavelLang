//
// Created by lauril on 3/24/17.
//

#ifndef NRF52_NRF_RAVEL_FS_H
#define NRF52_NRF_RAVEL_FS_H

/**
 * Initialize ravel flash module
 * @return Ravel Error
 */
uint32_t nrf52_ravel_fs_init (void);

/**
 * find and delete model record with ID
 * @param modelID
 * @param recID
 * @return NER_ERROR
 */
uint32_t nrf52_ravel_fs_find_and_delete (uint16_t modelID, uint16_t recID);

uint32_t nrf52_ravel_fs_find_and_get (uint32_t *data, uint16_t modelID, uint16_t recID);

uint32_t nrf52_ravel_fs_save (uint32_t *data, size_t length, uint16_t modelID, uint16_t recID);

void nrf52_ravel_fds_evt_handler(fds_evt_t const * const p_fds_evt);
#endif //NRF52_NRF_RAVEL_FS_H

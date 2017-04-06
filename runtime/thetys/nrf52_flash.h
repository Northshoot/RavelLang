#ifndef FLASH_H_
#define FLASH_H_

void flash_init(void);

void flash_page_erase(uint32_t * page_address);


void flash_read(uint8_t,uint8_t*);
void flash_write(uint8_t,uint8_t*);

#endif
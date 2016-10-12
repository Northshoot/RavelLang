#ifndef API_TIMER_H_
#define API_TIMER_H_

#include <stdint.h>
#include "app_error.h"
#include "softdevice_handler.h"
#include "app_timer.h"
#include "bsp_btn_ble.h"

#define APP_TIMER_PRESCALER              0                                          /**< Value of the RTC1 PRESCALER register. */
#define APP_TIMER_OP_QUEUE_SIZE          4                                          /**< Size of timer operation queues. */

#define RANDOMIZER_CTR__TIMER__TIMER_PERIODIC  APP_TIMER_TICKS(1000, APP_TIMER_PRESCALER)

APP_TIMER_DEF(m_randomizer_ctr__timer__timer_periodic);


void timers_init(void);

//start timer
void randomizer_ctr__timer__timer_periodic__start(uint32_t period);
//stop timer
void randomizer_ctr__timer__timer_periodic__stop();


//**end autogenerate *//
#endif /* API_TIMER_H_ */
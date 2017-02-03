/* This is a very simple hello_world program.
 * It aims to demonstrate the co-existence of two processes:
 * One of them prints a hello world message and the other blinks the LEDs
 *
 * It is largely based on hello_world in $(CONTIKI)/examples/sensinode
 *
 * Author: George Oikonomou - <oikonomou@users.sourceforge.net>
 */

/**
 * \addtogroup nrf52dk nRF52 Development Kit
 * @{
 *
 * \addtogroup nrf52dk-examples Demo projects for nRF52 DK
 * @{
 *
 * \defgroup nrf52dk-blink-hello Basic sensors and LEDs demo
 * @{
 *
 * This demo demonstrates use of Contiki processes, sensors, and LEDs
 * on nRF52 DK. Pressing a button will start a timer that blinks a
 * respective LED (e.g., button 1 controls LED 1). Each time the button
 * is pressed blinking frequency is doubled. On 4th press the LED is
 * switched off and the sequence can be started from the beginning.
 *
 * \file Main file for Basic sensors and LEDs demo.
 */
#include <stdio.h> /* For printf() */
#include <inttypes.h>
#include "contiki.h"
#include "dev/leds.h"
#include "dev/temperature-sensor.h"
#include "lib/sensors.h"

/*---------------------------------------------------------------------------*/
PROCESS(blink_process_1, "LED1 blink process");

AUTOSTART_PROCESSES(
    &blink_process_1
);

struct blink_process_ctx {
  struct etimer et_blink;
  unsigned char c;
  unsigned char led;
};


/*---------------------------------------------------------------------------*/
PROCESS_THREAD(blink_process_1, ev, data)
{
  static struct blink_process_ctx ctx;

  PROCESS_BEGIN();

  ctx.c = 0;
  ctx.led = LEDS_1;
  etimer_set(&ctx.et_blink, CLOCK_SECOND/4);

  while (1) {
    PROCESS_WAIT_EVENT();
    if (ev == PROCESS_EVENT_TIMER &&  etimer_expired(&ctx.et_blink)){
    	leds_toggle(ctx.led);
    	etimer_reset(&ctx.et_blink);
    }

  }
  PROCESS_END();
}
/**
 * @}
 * @}
 * @}
 */

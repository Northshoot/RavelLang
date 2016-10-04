/**
 * 
 */
void random_ctr__startTimerPeriodic(uint32_t period){
	uint32_t time =  APP_TIMER_TICKS(period,  APP_TIMER_PRESCALER) ; 		 //prescale should a parameter
	app_timer_start(random_ctr__startTimerPeriodic, time,  NULL);
}
/**
 * 
 */
void random_ctr__startTimerPeriodic(uint32_t period){
	uint32_t time =  APP_TIMER_TICKS(period,  APP_TIMER_PRESCALER) ; 		 //prescale should a parameter
	app_timer_start(random_ctr__startTimerPeriodic, time,  NULL);
}
"void (void)
    {
        // Initialize timer module.
        APP_TIMER_INIT(APP_TIMER_PRESCALER, APP_TIMER_OP_QUEUE_SIZE, false);

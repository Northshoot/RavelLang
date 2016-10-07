int main(void)
{

    bool erase_bonds;
    uint32_t err_code;

    err_code = NRF_LOG_INIT(NULL);
    APP_ERROR_CHECK(err_code);

    // Initialize.

    buttons_leds_init(&erase_bonds);
    ble_stack_init();
    gap_params_init();
    services_init();
    advertising_init();
    conn_params_init();
    peer_manager_init(erase_bonds);
    if (erase_bonds == true)
    {
        NRF_LOG_INFO("Bonds erased!\r\n");
    }
 // Start execution.
     advertising_start();
     // from there in we count as booted
     //is there boot event call?
     //create context
     RavelContext rctx;

     //init all models
     void m_random_model__queue_innit_sized(10);


     void m_updatefrequency_model__queue_innit();


     //set model dependencies and variables
     ConfigModel cm;
     config_model__first(&cm);
     random_model_ctr__set_config_model(&cm);

     //init sources
    timers_init();


     //then boot events
     random_model_ctr__system_booted(&rctx);

     NRF_LOG_INFO("Hello!\r\n");
     // Enter main loop.
     for (;;)
     {
         if (NRF_LOG_PROCESS() == false)
           {
               power_manage();
           };
     }
}
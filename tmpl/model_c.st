typedef struct
{
	uint32_t counter;
	uint32_t time;
	int32_t temperature;
	int32_t random;

} model.name;

int32_t random = rand();    // Declare variable holding temperature value
int32_t temperature; // Declare a variable to store current temperature until next measurement.
sd_temp_get(&temperature); // Get temperature
cntr++;
rm.counter = cntr;
rm.time = 0;
rm.temperature = temperature;
rm.random = random;
random_char_update(&m_ravel_service, &rm);

//BLE
attr_char_value.max_len     = sizeof(RandomModel);
attr_char_value.init_len    = sizeof(RandomModel);;
uint8_t value[sizeof(RandomModel)/4] = {0};
attr_char_value.p_value     = value;
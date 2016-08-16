typedef struct
{
<model.schema.fields:{field|<field.type.Ctype> <field.CName> };separator="\n">
}<model.name>;

int32_t random = rand();    // Declare variable holding temperature value
//needs source injection
int32_t temperature; // Declare a variable to store current temperature until next measurement.
sd_temp_get(&temperature); // Get temperature

//incremental variable
cntr++;

<model.schema.fields:{field|<field.CName> = field.source.name };separator="\n">
rm.counter = cntr;
rm.time = 0;
rm.temperature = temperature;
rm.random = random;
random_char_update(&m_ravel_service, &rm);

//BLE
attr_char_value.max_len     = sizeof(<model.name>);
attr_char_value.init_len    = sizeof(RandomModel);;
uint8_t value[sizeof(RandomModel)/4] = {0};
attr_char_value.p_value     = value;
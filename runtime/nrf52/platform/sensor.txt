#include <string.h>
#include <limits.h>

#include "nrf_error.h"
#include "nordic_common.h"
#include "nrf.h"
#include "nrf_gpio.h"
#include "app_timer.h"
#include "app_scheduler.h"
#include "softdevice_handler.h"

#include "ble_core.h"
#include "network.h"
#include "config.h"
#include "log.h"
#include "sensor.h"

#define TIMESTAMP_UPPER_BYTE(time)  ((((uint32_t)time) & 0xFF000000) >> 24)
#define TIMESTAMP_LOWER_BYTES(time) ((((uint32_t)time) & 0x00FFFFFF))
#define ABS(x) ((x) >= 0 ? (x) : -(x))
static
void sensor_encode(
    sensor_encoded_data_t *encoded,
    const sensor_data_t    data[NUM_DATA_PER_PACKET])
{
    /* always use the upper byte of the first data point */
    encoded->timestamp_upper_byte = TIMESTAMP_UPPER_BYTE(data[0].timestamp); 

    /* for each data point, encode it into the packet */
    for (int i = 0; i < NUM_DATA_PER_PACKET; i++) {
        /* if the sensor timings wrap around top byte of the timestamp
         * clamp them to the max time */
        uint32_t timestamp_lower_bytes;
        if (encoded->timestamp_upper_byte < TIMESTAMP_UPPER_BYTE(data[i].timestamp)) {
            timestamp_lower_bytes = TIMESTAMP_LOWER_BYTES(0xFFFFFFFF);
        } else {
            timestamp_lower_bytes = TIMESTAMP_LOWER_BYTES(data[i].timestamp);
        }

        /* setup the rest of the data */
        encoded->data[i].timestamp_lower_bytes = timestamp_lower_bytes;
        encoded->data[i].flow                  = data[i].flow;
        encoded->data[i].temp                  = data[i].temp;
    }
}

/*
 * Compression
 */

#define MAX_DATA_BUFFER_LEN NUM_DATA_PER_PACKET 
uint32_t m_global_time;
static sensor_data_t m_data[MAX_DATA_BUFFER_LEN];
static uint16_t      m_data_len = 0;

uint32_t sensor_get_time(){
    return m_global_time;
}
 
/* add a new set of samples to the data buffer */
static
void sensor_add_data(sensor_data_t *data) {
    const uint32_t threshold = config_get_sensor_threshold();

    /* disguard the sample if it did not reach the desired threshold */
    if (m_data_len > 0 && ABS(data->flow - m_data[m_data_len-1].flow) < threshold && 
        ABS(data->temp - m_data[m_data_len-1].temp) < 2 ){
        //LOG("Sensor - ignoring sample due to thresholding");
        return;
    }
    
    /* if we have space just add the data to the end of the buffer */
    if (m_data_len == MAX_DATA_BUFFER_LEN) {
        sensor_encoded_data_t encoded;

        /* otherwise encode the samples and send it */
        sensor_encode(&encoded, m_data);

        //LOG("Sending sample to network queue");
        network_send((uint8_t *)&encoded, sizeof encoded);

        /* remove the old samples from the buffer */
        m_data_len = 0;
    }

    /* add the data to the buffer */
    m_data[m_data_len++] = *data;
}

/* setup the sensors for reading from the ADC */
#define APP_TIMER_PRESCALER     0                                            /* Value of the RTC1 PRESCALER register. */
#define APP_TIMER_MAX_TIMERS    6                                            /* Maximum number of simultaneously created timers. */
#define APP_TIMER_OP_QUEUE_SIZE 5                                            /* Size of timer operation queues. */

#define ADC_SAMPLE_INTERVAL     APP_TIMER_TICKS(2, APP_TIMER_PRESCALER)   
#define RTC_INTERVAL            APP_TIMER_TICKS(1000, APP_TIMER_PRESCALER)   

#define ADC_NO_VREF_EXTERNAL ADC_CONFIG_EXTREFSEL_None                       /* ADC external reference pin selection (none) */
#define ADC_PSEL_FLOW        ADC_CONFIG_PSEL_AnalogInput7                    /* ADC input to use for flow */
#define ADC_PSEL_TEMP        ADC_CONFIG_PSEL_AnalogInput4                    /* ADC input to use for temp */
#define ADC_PSEL_VDD         ADC_CONFIG_PSEL_Disabled                        /* ADC input disabled */
#define ADC_VREF_INTERNAL    ADC_CONFIG_REFSEL_VBG                           /* ADC internal reference voltage (1.2v) */
#define ADC_PRESCALE_FLOW    ADC_CONFIG_INPSEL_AnalogInputOneThirdPrescaling /* ADC analog input prescaling */
#define ADC_PRESCALE_TEMP    ADC_CONFIG_INPSEL_AnalogInputOneThirdPrescaling /* ADC analog input prescaling */
#define ADC_PRESCALE_VDD     ADC_CONFIG_INPSEL_SupplyOneThirdPrescaling      /* Vdd input prescaling */
#define RESULT_BITS          ADC_CONFIG_RES_8bit                             /* resolution of ADC result (8 bits) */

#define PIN_TEMP_VDD         (4)                                             /* pin to set high for reading thermistor */

static
uint16_t adc_read(uint8_t psel, uint8_t prescale) {
    uint8_t adc_count;
    /* disable adc interrupt */
    NRF_ADC->INTENSET = (ADC_INTENSET_END_Disabled << ADC_INTENSET_END_Pos);  

    /* setup the adc */
    NRF_ADC->CONFIG = (ADC_NO_VREF_EXTERNAL << ADC_CONFIG_EXTREFSEL_Pos)
                    | (psel                 << ADC_CONFIG_PSEL_Pos)
                    | (ADC_VREF_INTERNAL    << ADC_CONFIG_REFSEL_Pos)
                    | (prescale             << ADC_CONFIG_INPSEL_Pos)
                    | (RESULT_BITS          << ADC_CONFIG_RES_Pos)           
                    ;

    /* enable the adc */
    NRF_ADC->ENABLE = ADC_ENABLE_ENABLE_Enabled;

    /* start the conversion */
    NRF_ADC->TASKS_START = 1; 

    /* wait for the result to be ready */
    while (!NRF_ADC->EVENTS_END) { /* loop */ }
    NRF_ADC->EVENTS_END = 0;
    
    /* save the adc result */
    adc_count = NRF_ADC->RESULT;

    /* turn off the adc to save power */
    NRF_ADC->TASKS_STOP = 1;

    return adc_count;
}

/* using the resistance value we can look up the temperature from 
 * http://www.vishay.com/docs/29114/ntcalug3.pdf (part NTCALUG03A103GC)
 *
 * Note the table is in deciOhms
 */
#define THERMISTOR_TABLE_LEN 20
static const uint32_t thermistor_table[THERMISTOR_TABLE_LEN] = {
     /*  0 C */ 326240, /*  5 C */ 253810, 
     /* 10 C */ 198970, /* 15 C */ 157110, 
     /* 20 C */ 124930, /* 25 C */ 100000,
     /* 30 C */  80560, /* 35 C */  65297,
     /* 40 C */  53239, /* 45 C */  43653,
     /* 50 C */  35987, /* 55 C */  29823,
     /* 60 C */  24838, /* 65 C */  20787,
     /* 70 C */  17477, /* 75 C */  14759,
     /* 80 C */  12518, /* 85 C */  10661,
     /* 90 C */   9116, /* 95 C */   7825,
};

/* voltage here is scaled from 0 to ADC_SCALE_FACTOR * 1.2v
 * with 255 being the highest voltage and 0 being grounded
 * (1.2v is the internal reference voltage)
 */
static
uint8_t voltage_to_temp(
    uint16_t adc_vdd,
    uint16_t adc_thermistor)
{
    /* formula to convert a raw voltage to ohms
     * r_thermistor = v_thermistor * 5kOhms / (vdd - v_thermistor)
     *
     * Note 5kOhms is 50000 because we scaled the resistances by 10 in the thermistor table
     */
    const uint32_t r_thermistor = adc_thermistor * 50000 / (adc_vdd - adc_thermistor);

    if (r_thermistor > thermistor_table[0]) {
        return -1;
    }

    for (int i = 0; i < THERMISTOR_TABLE_LEN - 1; i++) {
        const uint32_t upper_bound = thermistor_table[i];
        const uint32_t lower_bound = thermistor_table[i+1];
        if (r_thermistor <= upper_bound && r_thermistor >= lower_bound) {
            /* linear interpolation */
            return (5 * i) + ROUNDED_DIV(5 * (upper_bound - r_thermistor), upper_bound - lower_bound);
        }
    }

    return -1;
}

#define FLOW_BUFFER_LEN 250
#define ERR_MARGIN 1
#define FALSE_POS 1
#define ADC_FLOW_THRESHOLD 1

static int8_t flow_fft_buffer[FLOW_BUFFER_LEN];

static app_timer_id_t m_rtc_timer_id;
static app_timer_id_t m_flow_timer_id;
static bool m_flow_timer_expired;

static
uint8_t calculate_flow(void) {
    bool is_greater = false;
    int8_t last_value = flow_fft_buffer[0];
    int num_crossings = 0;
    for (int i = 1; i < FLOW_BUFFER_LEN; i++) {
        if(is_greater) {
            if (flow_fft_buffer[i] < last_value - ERR_MARGIN) {
                is_greater = false;
            }
        } else {
            if (flow_fft_buffer[i] > last_value - ERR_MARGIN) {
                is_greater = true;
                num_crossings++;
            }
        }

        last_value = flow_fft_buffer[i];
    }

    /* if the number of crossings is less than a threshold, return 0 */
    if (num_crossings <= FALSE_POS) {
        return 0;
    }

    return num_crossings;
}

static
uint8_t measure_temp(void) {
    uint16_t adc_vdd;
    uint16_t adc_thermistor;

    /* set the thermistor pin to high before measuring it */
    nrf_gpio_pin_set(PIN_TEMP_VDD);

    adc_vdd        = adc_read(ADC_PSEL_VDD, ADC_PRESCALE_VDD);
    adc_thermistor = adc_read(ADC_PSEL_TEMP, ADC_PRESCALE_TEMP);

    /* turn off the thermistor to save power */
    nrf_gpio_pin_clear(PIN_TEMP_VDD);

    return voltage_to_temp(adc_vdd, adc_thermistor);
}

static
uint8_t measure_flow(uint8_t flow_len) {
    uint32_t err_code;
    /* if it is, we need to disable ble to read at a fast rate */
    //ble_disable();

    /* start flow sensor timer */
    err_code = app_timer_start(
        m_flow_timer_id,
        ADC_SAMPLE_INTERVAL,
        NULL
    );
    APP_ERROR_CHECK(err_code);

    for (int i = 0; i < flow_len; i++) {
        /* read the actual flow data into the buffer */
        flow_fft_buffer[i] = adc_read(ADC_PSEL_FLOW, ADC_PRESCALE_FLOW);

        /* wait for flow timer to expire */
        m_flow_timer_expired = false;
        while (!m_flow_timer_expired) {
            __WFI();
        }
    }

    app_timer_stop(m_flow_timer_id);

    //ble_enable();

    /* calcuate the overall flow */
    return calculate_flow();
}

static uint8_t prev_flow = 0;
static uint8_t shower_on = 0;

static
void measurement_wrapper(void *p_event, uint16_t event_len) {
    sensor_data_t data;

    memset(&data, 0, sizeof(data));

    //reduce number of ADC measurements if shower is off
    data.timestamp = m_global_time;
    if(shower_on == 1){
        data.flow      = measure_flow(FLOW_BUFFER_LEN);
        if (data.flow == 0 && prev_flow == 0){
            shower_on = 0;
            LOG("Shower off");
        }
    }
    else {
        data.flow = measure_flow(10); //When processing data, should discard first shower data point
        if (data.flow > 0){ 
            shower_on = 1;
            LOG("Shower on %d", data.flow);
        }
    }
    prev_flow = data.flow;
    data.temp      = measure_temp();


    //LOG("time %d flow rate %d temp %d", data.timestamp, data.flow, data.temp);

    /* add the data to the queue of sensor readings */
    sensor_add_data(&data);
}

static
void rtc_timeout_handler(void *p_context) {
    uint32_t err_code;

    m_global_time++;

    /* schedule a measurement */
    err_code = app_sched_event_put(NULL, 0, measurement_wrapper);
    APP_ERROR_CHECK(err_code);
}

static
void flow_timeout_handler(void *p_context) {
    m_flow_timer_expired = true;
}

void sensor_start(void) {
    uint32_t err_code;

    /* turn on the thermistor pin, but keep it low to save power */
    nrf_gpio_pin_dir_set(PIN_TEMP_VDD, NRF_GPIO_PIN_DIR_OUTPUT);
    nrf_gpio_pin_clear(PIN_TEMP_VDD);

    /* start the timer for the clock */
    err_code = app_timer_start(
        m_rtc_timer_id,
        RTC_INTERVAL,
        NULL
    );
    APP_ERROR_CHECK(err_code);

    LOG("Sensors started");
}

#define NUM_TEST_DATA 80
void sensor_init_test(void) {
    uint32_t err_code;

    /* apply the global time offset */
    m_global_time = config_get_global_time_offset();

    /* setup the timer module */
    APP_TIMER_INIT(
        APP_TIMER_PRESCALER,
        APP_TIMER_MAX_TIMERS,
        APP_TIMER_OP_QUEUE_SIZE,
        false
    );

   /* timer for keeping global time */
    err_code = app_timer_create(
        &m_rtc_timer_id,
        APP_TIMER_MODE_REPEATED,
        rtc_timeout_handler
    );
    APP_ERROR_CHECK(err_code);

    /* add test data to the queue */
    for (int i = 0; i < NUM_TEST_DATA; i++) {
        sensor_data_t data = {
            .timestamp = m_global_time,
            .flow      = 0,
            .temp      = 0
        };

        for (int j = 0; j < NUM_DATA_PER_PACKET; j++) {
            sensor_add_data(&data);
        }
    }
}

void sensor_init(void) {
    uint32_t err_code;

    /* apply the global time offset */
    m_global_time += config_get_global_time_offset();

    /* setup the timer module */
    APP_TIMER_INIT(
        APP_TIMER_PRESCALER,
        APP_TIMER_MAX_TIMERS,
        APP_TIMER_OP_QUEUE_SIZE,
        false
    );

   /* timer for keeping global time */
    err_code = app_timer_create(
        &m_rtc_timer_id,
        APP_TIMER_MODE_REPEATED,
        rtc_timeout_handler
    );
    APP_ERROR_CHECK(err_code);

    /* timer for flow measurements */
    err_code = app_timer_create(
        &m_flow_timer_id,
        APP_TIMER_MODE_REPEATED,
        flow_timeout_handler
    );
    APP_ERROR_CHECK(err_code);

    LOG("Sensor initialized");
}

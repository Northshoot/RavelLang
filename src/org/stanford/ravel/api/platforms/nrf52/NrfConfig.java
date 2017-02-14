        package org.stanford.ravel.api.platforms.nrf52;

        /**
        * Created by lauril on 2/13/17.
        */
        public class NrfConfig {

        //==========================================================
        // <q> BLE_ADVERTISING_ENABLED  - ble_advertising - Advertising module


        public int  BLE_ADVERTISING_ENABLED = 1;
        // <q> BLE_DTM_ENABLED  - ble_dtm - Module for testing RF/PHY using DTM commands


        public int  BLE_DTM_ENABLED = 0;
        // <q> BLE_RACP_ENABLED  - ble_racp - Record Access Control Point library


        public int  BLE_RACP_ENABLED = 0;
        // <q> NRF_BLE_QWR_ENABLED  - nrf_ble_qwr - Queued writes support module (prepare/execute write)


        public int  NRF_BLE_QWR_ENABLED = 0;
        // <q> PEER_MANAGER_ENABLED  - peer_manager - Peer Manager


        public int  PEER_MANAGER_ENABLED = 1;
        // </h>
        //==========================================================

        // <h> nRF_BLE_Services

        //==========================================================
        // <q> BLE_ANCS_C_ENABLED  - ble_ancs_c - Apple Notification Service Client


        public int  BLE_ANCS_C_ENABLED = 0;
        // <q> BLE_ANS_C_ENABLED  - ble_ans_c - Alert Notification Service Client


        public int  BLE_ANS_C_ENABLED = 0;
        // <q> BLE_BAS_C_ENABLED  - ble_bas_c - Battery Service Client


        public int  BLE_BAS_C_ENABLED = 0;
        // <q> BLE_BAS_ENABLED  - ble_bas - Battery Service


        public int  BLE_BAS_ENABLED = 1;
        // <q> BLE_CSCS_ENABLED  - ble_cscs - Cycling Speed and Cadence Service


        public int  BLE_CSCS_ENABLED = 0;
        // <q> BLE_CTS_C_ENABLED  - ble_cts_c - Current Time Service Client


        public int  BLE_CTS_C_ENABLED = 0;
        // <q> BLE_DIS_ENABLED  - ble_dis - Device Information Service


        public int  BLE_DIS_ENABLED = 1;
        // <q> BLE_GLS_ENABLED  - ble_gls - Glucose Service


        public int  BLE_GLS_ENABLED = 0;
        // <q> BLE_HIDS_ENABLED  - ble_hids - Human Interface Device Service


        public int  BLE_HIDS_ENABLED = 0;
        // <q> BLE_HRS_C_ENABLED  - ble_hrs_c - Heart Rate Service Client


        public int  BLE_HRS_C_ENABLED = 0;
        // <q> BLE_HRS_ENABLED  - ble_hrs - Heart Rate Service


        public int  BLE_HRS_ENABLED = 1;
        // <q> BLE_HTS_ENABLED  - ble_hts - Health Thermometer Service


        public int  BLE_HTS_ENABLED = 0;
        // <q> BLE_IAS_C_ENABLED  - ble_ias_c - Immediate Alert Service Client


        public int  BLE_IAS_C_ENABLED = 0;
        // <q> BLE_IAS_ENABLED  - ble_ias - Immediate Alert Service


        public int  BLE_IAS_ENABLED = 0;
        // <q> BLE_LBS_C_ENABLED  - ble_lbs_c - Nordic LED Button Service Client


        public int  BLE_LBS_C_ENABLED = 0;
        // <q> BLE_LBS_ENABLED  - ble_lbs - LED Button Service


        public int  BLE_LBS_ENABLED = 0;
        // <q> BLE_LLS_ENABLED  - ble_lls - Link Loss Service


        public int  BLE_LLS_ENABLED = 0;
        // <q> BLE_NUS_C_ENABLED  - ble_nus_c - Nordic UART Central Service


        public int  BLE_NUS_C_ENABLED = 0;
        // <q> BLE_NUS_ENABLED  - ble_nus - Nordic UART Service


        public int  BLE_NUS_ENABLED = 0;
        // <q> BLE_RSCS_C_ENABLED  - ble_rscs_c - Running Speed and Cadence Client


        public int  BLE_RSCS_C_ENABLED = 0;
        // <q> BLE_RSCS_ENABLED  - ble_rscs - Running Speed and Cadence Service


        public int  BLE_RSCS_ENABLED = 0;
        // <q> BLE_TPS_ENABLED  - ble_tps - TX Power Service


        public int  BLE_TPS_ENABLED = 0;
        // </h>
        //==========================================================

        // <h> nRF_Drivers

        //==========================================================
        // <e> ADC_ENABLED - nrf_drv_adc - Driver for ADC peripheral (nRF51)
        //==========================================================
        public int  ADC_ENABLED = 0;

        //#if  ADC_ENABLED
        // <o> ADC_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  ADC_CONFIG_IRQ_PRIORITY = 6;
        //ADC_ENABLED
        // </e>

        // <e> CLOCK_ENABLED - nrf_drv_clock - CLOCK peripheral driver
        //==========================================================
        public int  CLOCK_ENABLED = 1;

        //#if  CLOCK_ENABLED
        // <o> CLOCK_CONFIG_XTAL_FREQ  - HF XTAL Frequency

        // <0=> Default (64 MHz)

        public int  CLOCK_CONFIG_XTAL_FREQ = 0;
        // <o> CLOCK_CONFIG_LF_SRC  - LF Clock Source

        // <0=> RC
        // <1=> XTAL
        // <2=> Synth

        public int  CLOCK_CONFIG_LF_SRC = 1;
        // <o> CLOCK_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  CLOCK_CONFIG_IRQ_PRIORITY = 6;
        //CLOCK_ENABLED
        // </e>

        // <e> COMP_ENABLED - nrf_drv_comp - COMP peripheral driver
        //==========================================================
        public int  COMP_ENABLED = 0;

        //#if  COMP_ENABLED
        // <o> COMP_CONFIG_REF  - Reference voltage

        // <0=> Internal = 1;.2V
        // <1=> Internal = 1;.8V
        // <2=> Internal 2.4V
        // <4=> VDD
        // <7=> ARef

        public int  COMP_CONFIG_REF = 1;
        // <o> COMP_CONFIG_MAIN_MODE  - Main mode

        // <0=> Single ended
        // <1=> Differential

        public int  COMP_CONFIG_MAIN_MODE = 0;
        // <o> COMP_CONFIG_SPEED_MODE  - Speed mode

        // <0=> Low power
        // <1=> Normal
        // <2=> High speed

        public int  COMP_CONFIG_SPEED_MODE =2;
        // <o> COMP_CONFIG_HYST  - Hystheresis

        // <0=> No
        // <1=> 50mV

        public int  COMP_CONFIG_HYST = 0;
        // <o> COMP_CONFIG_ISOURCE  - Current Source

        // <0=> Off
        // <1=> 2.5 uA
        // <2=> 5 uA
        // <3=> = 1;0 uA

        public int  COMP_CONFIG_ISOURCE = 0;
        // <o> COMP_CONFIG_INPUT  - Analog input

        // <0=> = 0;
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  COMP_CONFIG_INPUT = 0;
        // <o> COMP_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  COMP_CONFIG_IRQ_PRIORITY =  6;
        //COMP_ENABLED
        // </e>

        // <q> EGU_ENABLED  - nrf_drv_swi - SWI(EGU) peripheral driver


        public int  EGU_ENABLED = 0;
        // <e> GPIOTE_ENABLED - nrf_drv_gpiote - GPIOTE peripheral driver
        //==========================================================
        public int  GPIOTE_ENABLED = 1;

        //#if  GPIOTE_ENABLED
        // <o> GPIOTE_CONFIG_NUM_OF_LOW_POWER_EVENTS - Number of lower power input pins
        public int  GPIOTE_CONFIG_NUM_OF_LOW_POWER_EVENTS =4;
        // <o> GPIOTE_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  GPIOTE_CONFIG_IRQ_PRIORITY =  6;
        //GPIOTE_ENABLED
        // </e>

        // <e> I2S_ENABLED - nrf_drv_i2s - I2S peripheral driver
        //==========================================================
        public int  I2S_ENABLED = 0;

        //#if  I2S_ENABLED
        // <o> I2S_CONFIG_SCK_PIN - SCK pin  <0-31>


        public int  I2S_CONFIG_SCK_PIN = 31;
        // <o> I2S_CONFIG_LRCK_PIN - LRCK pin  <1-31>


        public int  I2S_CONFIG_LRCK_PIN = 30;
        // <o> I2S_CONFIG_MCK_PIN - MCK pin
        public int  I2S_CONFIG_MCK_PIN = 255;
        // <o> I2S_CONFIG_SDOUT_PIN - SDOUT pin  <0-31>


        public int  I2S_CONFIG_SDOUT_PIN =29;
        // <o> I2S_CONFIG_SDIN_PIN - SDIN pin  <0-31>


        public int  I2S_CONFIG_SDIN_PIN = 28;
        // <o> I2S_CONFIG_MASTER  - Mode

        // <0=> Master
        // <1=> Slave

        public int  I2S_CONFIG_MASTER = 0;
        // <o> I2S_CONFIG_FORMAT  - Format

        // <0=> I2S
        // <1=> Aligned

        public int  I2S_CONFIG_FORMAT = 0;
        // <o> I2S_CONFIG_ALIGN  - Alignment

        // <0=> Left
        // <1=> Right

        public int  I2S_CONFIG_ALIGN = 0;
        // <o> I2S_CONFIG_SWIDTH  - Sample width (bits)

        // <0=> 8
        // <1=> = 1;= 6;
        // <2=> 24

        public int  I2S_CONFIG_SWIDTH = 1;
        // <o> I2S_CONFIG_CHANNELS  - Channels

        // <0=> Stereo
        // <1=> Left
        // <2=> Right

        public int  I2S_CONFIG_CHANNELS = 1;
        // <o> I2S_CONFIG_MCK_SETUP  - MCK behavior

        // <0=> Disabled
        // <2147483648=> 32MHz/2
        // <1342177280=> 32MHz/3
        // <1073741824=> 32MHz/4
        // <805306368=> 32MHz/5
        // <671088640=> 32MHz/= 6;
        // <536870912=> 32MHz/8
        // <402653184=> 32MHz/10
        // <369098752=> 32MHz/11
        // <285212672=> 32MHz/15
        // <268435456=> 32MHz/16
        // <201326592=> 32MHz/21
        // <184549376=> 32MHz/23
        // <142606336=> 32MHz/30
        // <138412032=> 32MHz/31
        // <134217728=> 32MHz/32
        // <100663296=> 32MHz/42
        // <68157440=> 32MHz/63
        // <34340864=> 32MHz/125

        public int  I2S_CONFIG_MCK_SETUP =536870912;
        // <o> I2S_CONFIG_RATIO  - MCK/LRCK ratio

        // <0=> 32x
        // <1=> 48x
        // <2=> 64x
        // <3=> 96x
        // <4=> = 1;28x
        // <5=> = 1;92x
        // <= 6;=> 256x
        // <7=> 384x
        // <8=> 512x

        public int  I2S_CONFIG_RATIO =2000;
        // <o> I2S_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  I2S_CONFIG_IRQ_PRIORITY =  6;
        //I2S_ENABLED
        // </e>

        // <e> LPCOMP_ENABLED - nrf_drv_lpcomp - LPCOMP peripheral driver
        //==========================================================
        public int  LPCOMP_ENABLED = 0;

        ////#if  LPCOMP_ENABLED
        // <o> LPCOMP_CONFIG_REFERENCE  - Reference voltage

        // <0=> Supply = 1;/8
        // <1=> Supply 2/8
        // <2=> Supply 3/8
        // <3=> Supply 4/8
        // <4=> Supply 5/8
        // <5=> Supply = 6;/8
        // <= 6;=> Supply 7/8
        // <8=> Supply = 1;/16 (nRF52)
        // <9=> Supply 3/16 (nRF52)
        // <10=> Supply 5/16 (nRF52)
        // <11=> Supply 7/16 (nRF52)
        // <12=> Supply 9/16 (nRF52)
        // <13=> Supply = 1;1/16 (nRF52)
        // <14=> Supply = 1;3/16 (nRF52)
        // <15=> Supply = 1;5/16 (nRF52)
        // <7=> External Ref = 0;
        // <65543=> External Ref = 1;

        public int  LPCOMP_CONFIG_REFERENCE =3;
        // <o> LPCOMP_CONFIG_DETECTION  - Detection

        // <0=> Crossing
        // <1=> Up
        // <2=> Down

        public int  LPCOMP_CONFIG_DETECTION =2;
        // <o> LPCOMP_CONFIG_INPUT  - Analog input

        // <0=> = 0;
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  LPCOMP_CONFIG_INPUT = 0;
        // <o> LPCOMP_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  LPCOMP_CONFIG_IRQ_PRIORITY = 6;
        //LPCOMP_ENABLED
        // </e>

        // <e> PDM_ENABLED - nrf_drv_pdm - PDM peripheral driver
        //==========================================================
        public int  PDM_ENABLED = 0;

        //#if  PDM_ENABLED
        // <o> PDM_CONFIG_MODE  - Mode

        // <0=> Stereo
        // <1=> Mono

        public int  PDM_CONFIG_MODE = 1;
        // <o> PDM_CONFIG_EDGE  - Edge

        // <0=> Left falling
        // <1=> Left rising

        public int  PDM_CONFIG_EDGE = 0;
        // <o> PDM_CONFIG_CLOCK_FREQ  - Clock frequency

        // <134217728=> = 1;000k
        // <138412032=> = 1;032k (default)
        // <142606336=> = 1;067k

        public int  PDM_CONFIG_CLOCK_FREQ = 138412032;
        // <o> PDM_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  PDM_CONFIG_IRQ_PRIORITY = 6;
        //PDM_ENABLED
        // </e>

        // <q> PERIPHERAL_RESOURCE_SHARING_ENABLED  - nrf_drv_common - Peripheral drivers common module


        public int  PERIPHERAL_RESOURCE_SHARING_ENABLED = 0;
        // <q> PPI_ENABLED  - nrf_drv_ppi - PPI peripheral driver


        public int  PPI_ENABLED = 0;
        // <e> PWM_ENABLED - nrf_drv_pwm - PWM peripheral driver
        //==========================================================
        public int  PWM_ENABLED = 0;

        //#if  PWM_ENABLED
        // <o> PWM_DEFAULT_CONFIG_OUT0_PIN - Out0 pin  <0-31>


        public int  PWM_DEFAULT_CONFIG_OUT0_PIN = 31;
        // <o> PWM_DEFAULT_CONFIG_OUT1_PIN - Out1 pin  <0-31>


        public int  PWM_DEFAULT_CONFIG_OUT1_PIN = 31;
        // <o> PWM_DEFAULT_CONFIG_OUT2_PIN - Out2 pin  <0-31>


        public int  PWM_DEFAULT_CONFIG_OUT2_PIN = 31;
        // <o> PWM_DEFAULT_CONFIG_OUT3_PIN - Out3 pin  <0-31>


        public int  PWM_DEFAULT_CONFIG_OUT3_PIN= 31;
        // <o> PWM_DEFAULT_CONFIG_BASE_CLOCK  - Base clock

        // <0=> = 1;= 6; MHz
        // <1=> 8 MHz
        // <2=> 4 MHz
        // <3=> 2 MHz
        // <4=> = 1; MHz
        // <5=> 500 kHz
        // <= 6;=> 250 kHz
        // <7=> = 1;25 MHz

        public int  PWM_DEFAULT_CONFIG_BASE_CLOCK =4;
        // <o> PWM_DEFAULT_CONFIG_COUNT_MODE  - Count mode

        // <0=> Up
        // <1=> Up and Down

        public int  PWM_DEFAULT_CONFIG_COUNT_MODE = 0;
        // <o> PWM_DEFAULT_CONFIG_TOP_VALUE - Top value
        public int  PWM_DEFAULT_CONFIG_TOP_VALUE = 1000;
        // <o> PWM_DEFAULT_CONFIG_LOAD_MODE  - Load mode

        // <0=> Common
        // <1=> Grouped
        // <2=> Individual
        // <3=> Waveform

        public int  PWM_DEFAULT_CONFIG_LOAD_MODE = 0;
        // <o> PWM_DEFAULT_CONFIG_STEP_MODE  - Step mode

        // <0=> Auto
        // <1=> Triggered

        public int  PWM_DEFAULT_CONFIG_STEP_MODE = 0;
        // <o> PWM_DEFAULT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  PWM_DEFAULT_CONFIG_IRQ_PRIORITY = 6;
        // <q> PWM0_ENABLED  - Enable PWM0 instance


        public int  PWM0_ENABLED = 0;
        // <q> PWM1_ENABLED  - Enable PWM1 instance


        public int  PWM1_ENABLED = 0;
        // <q> PWM2_ENABLED  - Enable PWM2 instance


        public int  PWM2_ENABLED = 0;
        //PWM_ENABLED
        // </e>

        // <e> QDEC_ENABLED - nrf_drv_qdec - QDEC peripheral driver
        //==========================================================
        public int  QDEC_ENABLED = 0;

        //#if  QDEC_ENABLED
        // <o> QDEC_CONFIG_REPORTPER  - Report period

        // <0=> = 1;0 Samples
        // <1=> 40 Samples
        // <2=> 80 Samples
        // <3=> = 1;20 Samples
        // <4=> = 1;60 Samples
        // <5=> 200 Samples
        // <= 6;=> 240 Samples
        // <7=> 280 Samples

        public int  QDEC_CONFIG_REPORTPER = 0;
        // <o> QDEC_CONFIG_SAMPLEPER  - Sample period

        // <0=> = 1;28 us
        // <1=> 256 us
        // <2=> 512 us
        // <3=> = 1;024 us
        // <4=> 2048 us
        // <5=> 4096 us
        // <= 6;=> 8192 us
        // <7=> = 1;6384 us

        public int  QDEC_CONFIG_SAMPLEPER =7;
        // <o> QDEC_CONFIG_PIO_A - A pin  <0-31>


        public int  QDEC_CONFIG_PIO_A =31;
        // <o> QDEC_CONFIG_PIO_B - B pin  <0-31>


        public int  QDEC_CONFIG_PIO_B =31;
        // <o> QDEC_CONFIG_PIO_LED - LED pin  <0-31>


        public int  QDEC_CONFIG_PIO_LED =31;
        // <o> QDEC_CONFIG_LEDPRE - LED pre
        public int  QDEC_CONFIG_LEDPRE =511;
        // <o> QDEC_CONFIG_LEDPOL  - LED polarity

        // <0=> Active low
        // <1=> Active high

        public int  QDEC_CONFIG_LEDPOL = 1;
        // <q> QDEC_CONFIG_DBFEN  - Debouncing enable


        public int  QDEC_CONFIG_DBFEN = 0;
        // <q> QDEC_CONFIG_SAMPLE_INTEN  - Sample ready interrupt enable


        public int  QDEC_CONFIG_SAMPLE_INTEN = 0;
        // <o> QDEC_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  QDEC_CONFIG_IRQ_PRIORITY = 6;
        //QDEC_ENABLED
        // </e>

        // <e> RNG_ENABLED - nrf_drv_rng - RNG peripheral driver
        //==========================================================
        public int  RNG_ENABLED = 0;

        //#if  RNG_ENABLED
        // <q> RNG_CONFIG_ERROR_CORRECTION  - Error correction


        public int  RNG_CONFIG_ERROR_CORRECTION = 0;
        // <o> RNG_CONFIG_POOL_SIZE - Pool size
        public int  RNG_CONFIG_POOL_SIZE =8;
        // <o> RNG_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  RNG_CONFIG_IRQ_PRIORITY = 6;
        //RNG_ENABLED
        // </e>

        // <e> RTC_ENABLED - nrf_drv_rtc - RTC peripheral driver
        //==========================================================
        public int  RTC_ENABLED = 0;

        //#if  RTC_ENABLED
        // <o> RTC_DEFAULT_CONFIG_FREQUENCY - Frequency  <16-32768>


        public int  RTC_DEFAULT_CONFIG_FREQUENCY =32768;
        // <q> RTC_DEFAULT_CONFIG_RELIABLE  - Ensures safe compare event triggering


        public int  RTC_DEFAULT_CONFIG_RELIABLE = 0;
        // <o> RTC_DEFAULT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  RTC_DEFAULT_CONFIG_IRQ_PRIORITY =  6;
        // <q> RTC0_ENABLED  - Enable RTC0 instance


        public int  RTC0_ENABLED = 0;
        // <q> RTC1_ENABLED  - Enable RTC1 instance


        public int  RTC1_ENABLED = 0;
        // <q> RTC2_ENABLED  - Enable RTC2 instance


        public int  RTC2_ENABLED = 0;
        // <o> NRF_MAXIMUM_LATENCY_US - Maximum possible time[us] in highest priority interrupt
        public int  NRF_MAXIMUM_LATENCY_US =2000;
        //RTC_ENABLED
        // </e>

        // <e> SAADC_ENABLED - nrf_drv_saadc - SAADC peripheral driver
        //==========================================================
        public int  SAADC_ENABLED = 0;

        //#if  SAADC_ENABLED
        // <o> SAADC_CONFIG_RESOLUTION  - Resolution

        // <0=> 8 bit
        // <1=> = 1;0 bit
        // <2=> = 1;2 bit
        // <3=> = 1;4 bit

        public int  SAADC_CONFIG_RESOLUTION = 1;
        // <o> SAADC_CONFIG_OVERSAMPLE  - Sample period

        // <0=> Disabled
        // <1=> 2x
        // <2=> 4x
        // <3=> 8x
        // <4=> = 1;6x
        // <5=> 32x
        // <= 6;=> 64x
        // <7=> = 1;28x
        // <8=> 256x

        public int  SAADC_CONFIG_OVERSAMPLE = 0;
        // <q> SAADC_CONFIG_LP_MODE  - Enabling low power mode


        public int  SAADC_CONFIG_LP_MODE = 0;
        // <o> SAADC_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  SAADC_CONFIG_IRQ_PRIORITY = 6;
        //SAADC_ENABLED
        // </e>

        // <e> SPIS_ENABLED - nrf_drv_spis - SPI Slave driver
        //==========================================================
        public int  SPIS_ENABLED = 0;

        //#if  SPIS_ENABLED
        // <o> SPIS_DEFAULT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  SPIS_DEFAULT_CONFIG_IRQ_PRIORITY = 6;
        // <o> SPIS_DEFAULT_MODE  - Mode

        // <0=> MODE_0
        // <1=> MODE_1
        // <2=> MODE_2
        // <3=> MODE_3

        public int  SPIS_DEFAULT_MODE = 0;
        // <o> SPIS_DEFAULT_BIT_ORDER  - SPIS default bit order

        // <0=> MSB first
        // <1=> LSB first

        public int  SPIS_DEFAULT_BIT_ORDER = 0;
        // <o> SPIS_DEFAULT_DEF - SPIS default DEF character  <0-= 255;>


        public int  SPIS_DEFAULT_DEF = 255;
        // <o> SPIS_DEFAULT_ORC - SPIS default ORC character  <0-= 255;>


        public int  SPIS_DEFAULT_ORC = 255;
        // <q> SPIS0_ENABLED  - Enable SPIS0 instance


        public int  SPIS0_ENABLED = 0;
        // <q> SPIS1_ENABLED  - Enable SPIS1 instance


        public int  SPIS1_ENABLED = 0;
        // <q> SPIS2_ENABLED  - Enable SPIS2 instance


        public int  SPIS2_ENABLED = 0;
        //SPIS_ENABLED
        // </e>

        // <e> SPI_ENABLED - nrf_drv_spi - SPI/SPIM peripheral driver
        //==========================================================
        public int  SPI_ENABLED = 0;

        //#if  SPI_ENABLED
        // <e> SPI_CONFIG_LOG_ENABLED - Enables logging in the module.
        //==========================================================
        public int  SPI_CONFIG_LOG_ENABLED = 0;

        //#if  SPI_CONFIG_LOG_ENABLED
        // <o> SPI_CONFIG_LOG_LEVEL  - Default Severity level

        // <0=> Off
        // <1=> Error
        // <2=> Warning
        // <3=> Info
        // <4=> Debug

        public int  SPI_CONFIG_LOG_LEVEL =3;
        // <o> SPI_CONFIG_INFO_COLOR  - ANSI escape code prefix.

        // <0=> Default
        // <1=> Black
        // <2=> Red
        // <3=> Green
        // <4=> Yellow
        // <5=> Blue
        // <= 6;=> Magenta
        // <7=> Cyan
        // <8=> White

        public int  SPI_CONFIG_INFO_COLOR = 0;
        // <o> SPI_CONFIG_DEBUG_COLOR  - ANSI escape code prefix.

        // <0=> Default
        // <1=> Black
        // <2=> Red
        // <3=> Green
        // <4=> Yellow
        // <5=> Blue
        // <= 6;=> Magenta
        // <7=> Cyan
        // <8=> White

        public int  SPI_CONFIG_DEBUG_COLOR = 0;
        //SPI_CONFIG_LOG_ENABLED
        // </e>

        // <o> SPI_DEFAULT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  SPI_DEFAULT_CONFIG_IRQ_PRIORITY = 6;
        // <e> SPI0_ENABLED - Enable SPI0 instance
        //==========================================================
        public int  SPI0_ENABLED = 0;

        //#if  SPI0_ENABLED
        // <q> SPI0_USE_EASY_DMA  - Use EasyDMA


        public int  SPI0_USE_EASY_DMA = 1;
        //SPI0_ENABLED
        // </e>

        // <e> SPI1_ENABLED - Enable SPI1 instance
        //==========================================================
        public int  SPI1_ENABLED = 0;

        //#if  SPI1_ENABLED
        // <q> SPI1_USE_EASY_DMA  - Use EasyDMA


        public int  SPI1_USE_EASY_DMA = 1;
        //SPI1_ENABLED
        // </e>

        // <e> SPI2_ENABLED - Enable SPI2 instance
        //==========================================================
        public int  SPI2_ENABLED = 0;

        //#if  SPI2_ENABLED
        // <q> SPI2_USE_EASY_DMA  - Use EasyDMA


        public int  SPI2_USE_EASY_DMA = 1;
        //SPI2_ENABLED
        // </e>

        //#endif //SPI_ENABLED
        // </e>

        // <e> TIMER_ENABLED - nrf_drv_timer - TIMER periperal driver
        //==========================================================
        public int  TIMER_ENABLED = 0;

        //#if  TIMER_ENABLED
        // <o> TIMER_DEFAULT_CONFIG_FREQUENCY  - Timer frequency if in Timer mode

        // <0=> = 1;= 6; MHz
        // <1=> 8 MHz
        // <2=> 4 MHz
        // <3=> 2 MHz
        // <4=> = 1; MHz
        // <5=> 500 kHz
        // <= 6;=> 250 kHz
        // <7=> = 1;25 kHz
        // <8=> 62.5 kHz
        // <9=> 31.25 kHz

        public int  TIMER_DEFAULT_CONFIG_FREQUENCY = 0;
        // <o> TIMER_DEFAULT_CONFIG_MODE  - Timer mode or operation

        // <0=> Timer
        // <1=> Counter

        public int  TIMER_DEFAULT_CONFIG_MODE = 0;
        // <o> TIMER_DEFAULT_CONFIG_BIT_WIDTH  - Timer counter bit width

        // <0=> = 1;= 6; bit
        // <1=> 8 bit
        // <2=> 24 bit
        // <3=> 32 bit

        public int  TIMER_DEFAULT_CONFIG_BIT_WIDTH = 0;
        // <o> TIMER_DEFAULT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  TIMER_DEFAULT_CONFIG_IRQ_PRIORITY = 6;
        // <q> TIMER0_ENABLED  - Enable TIMER0 instance


        public int  TIMER0_ENABLED = 0;
        // <q> TIMER1_ENABLED  - Enable TIMER1 instance


        public int  TIMER1_ENABLED = 0;
        // <q> TIMER2_ENABLED  - Enable TIMER2 instance


        public int  TIMER2_ENABLED = 0;
        // <q> TIMER3_ENABLED  - Enable TIMER3 instance


        public int  TIMER3_ENABLED = 0;
        // <q> TIMER4_ENABLED  - Enable TIMER4 instance


        public int  TIMER4_ENABLED = 0;
        //TIMER_ENABLED
        // </e>

        // <e> TWIS_ENABLED - nrf_drv_twis - TWIS peripheral driver
        //==========================================================
        public int  TWIS_ENABLED = 0;

        //#if  TWIS_ENABLED
        // <o> TWIS_DEFAULT_CONFIG_ADDR0 - Address0
        public int  TWIS_DEFAULT_CONFIG_ADDR0 = 0;
        // <o> TWIS_DEFAULT_CONFIG_ADDR1 - Address1
        public int  TWIS_DEFAULT_CONFIG_ADDR1 = 0;
        // <o> TWIS_DEFAULT_CONFIG_SCL_PULL  - SCL pin pull configuration

        // <0=> Disabled
        // <1=> Pull down
        // <3=> Pull up

        public int  TWIS_DEFAULT_CONFIG_SCL_PULL = 0;
        // <o> TWIS_DEFAULT_CONFIG_SDA_PULL  - SDA pin pull configuration

        // <0=> Disabled
        // <1=> Pull down
        // <3=> Pull up

        public int  TWIS_DEFAULT_CONFIG_SDA_PULL = 0;
        // <o> TWIS_DEFAULT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  TWIS_DEFAULT_CONFIG_IRQ_PRIORITY = 6;
        // <q> TWIS0_ENABLED  - Enable TWIS0 instance


        public int  TWIS0_ENABLED = 0;
        // <q> TWIS1_ENABLED  - Enable TWIS1 instance


        public int  TWIS1_ENABLED = 0;
        // <q> TWIS_ASSUME_INIT_AFTER_RESET_ONLY  - Assume that any instance would be initialized only once


        // <i> Optimization flag. Registers used by TWIS are shared by other peripherals. Normally, during initialization driver tries to clear all registers to known state before doing the initialization itself. This gives initialization safe procedure, no matter when it would be called. If you activate TWIS only once and do never uninitialize it - set this flag to = 1; what gives more optimal code.

        public int  TWIS_ASSUME_INIT_AFTER_RESET_ONLY = 0;
        // <q> TWIS_NO_SYNC_MODE  - Remove support for synchronous mode


        // <i> Synchronous mode would be used in specific situations. And it uses some additional code and data memory to safely process state machine by polling it in status functions. If this functionality is not required it may be disabled to free some resources.

        public int  TWIS_NO_SYNC_MODE = 0;
        //TWIS_ENABLED
        // </e>

        // <e> TWI_ENABLED - nrf_drv_twi - TWI/TWIM peripheral driver
        //==========================================================
        public int  TWI_ENABLED = 0;

        //#if  TWI_ENABLED
        // <o> TWI_DEFAULT_CONFIG_FREQUENCY  - Frequency

        // <26738688=> = 1;00k
        // <67108864=> 250k
        // <104857600=> 400k

        public int  TWI_DEFAULT_CONFIG_FREQUENCY =26738688;
        // <q> TWI_DEFAULT_CONFIG_CLR_BUS_INIT  - Enables bus clearing procedure during init


        public int  TWI_DEFAULT_CONFIG_CLR_BUS_INIT = 0;
        // <q> TWI_DEFAULT_CONFIG_HOLD_BUS_UNINIT  - Enables bus holding after uninit


        public int  TWI_DEFAULT_CONFIG_HOLD_BUS_UNINIT = 0;
        // <o> TWI_DEFAULT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  TWI_DEFAULT_CONFIG_IRQ_PRIORITY = 6;
        // <e> TWI0_ENABLED - Enable TWI0 instance
        //==========================================================
        public int  TWI0_ENABLED = 0;

        //#if  TWI0_ENABLED
        // <q> TWI0_USE_EASY_DMA  - Use EasyDMA (if present)


        public int  TWI0_USE_EASY_DMA = 0;
        //TWI0_ENABLED
        // </e>

        // <e> TWI1_ENABLED - Enable TWI1 instance
        //==========================================================
        public int  TWI1_ENABLED = 0;

        //#if  TWI1_ENABLED
        // <q> TWI1_USE_EASY_DMA  - Use EasyDMA (if present)


        public int  TWI1_USE_EASY_DMA = 0;
        //TWI1_ENABLED
        // </e>

        //TWI_ENABLED
        // </e>

        // <e> UART_ENABLED - nrf_drv_uart - UART/UARTE peripheral driver
        //==========================================================
        public int  UART_ENABLED = 1;

        //#if  UART_ENABLED
        // <o> UART_DEFAULT_CONFIG_HWFC  - Hardware Flow Control

        // <0=> Disabled
        // <1=> Enabled

        public int  UART_DEFAULT_CONFIG_HWFC = 0;
        // <o> UART_DEFAULT_CONFIG_PARITY  - Parity

        // <0=> Excluded
        // <14=> Included

        public int  UART_DEFAULT_CONFIG_PARITY = 0;
        // <o> UART_DEFAULT_CONFIG_BAUDRATE  - Default Baudrate

        // <323584=> = 1;200 baud
        // <643072=> 2400 baud
        // <1290240=> 4800 baud
        // <2576384=> 9600 baud
        // <3862528=> = 1;4400 baud
        // <5152768=> = 1;9200 baud
        // <7716864=> 28800 baud
        // <10289152=> 38400 baud
        // <15400960=> 57600 baud
        // <20615168=> 76800 baud
        // <30801920=> = 1;15200 baud
        // <61865984=> 230400 baud
        // <67108864=> 250000 baud
        // <121634816=> 460800 baud
        // <251658240=> 921600 baud
        // <268435456=> 57600 baud

        public int  UART_DEFAULT_CONFIG_BAUDRATE =30801920;
        // <o> UART_DEFAULT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  UART_DEFAULT_CONFIG_IRQ_PRIORITY = 6;
        // <q> UART0_CONFIG_USE_EASY_DMA  - Default setting for using EasyDMA


        public int  UART0_CONFIG_USE_EASY_DMA = 1;
        // <q> UART_EASY_DMA_SUPPORT  - Driver supporting EasyDMA


        public int  UART_EASY_DMA_SUPPORT = 1;
        // <q> UART_LEGACY_SUPPORT  - Driver supporting Legacy mode


        public int  UART_LEGACY_SUPPORT = 1;
        //UART_ENABLED
        // </e>

        // <e> WDT_ENABLED - nrf_drv_wdt - WDT peripheral driver
        //==========================================================
        public int  WDT_ENABLED = 0;

        //#if  WDT_ENABLED
        // <o> WDT_CONFIG_BEHAVIOUR  - WDT behavior in CPU SLEEP or HALT mode

        // <1=> Run in SLEEP, Pause in HALT
        // <8=> Pause in SLEEP, Run in HALT
        // <9=> Run in SLEEP and HALT
        // <0=> Pause in SLEEP and HALT

        public int  WDT_CONFIG_BEHAVIOUR = 1;
        // <o> WDT_CONFIG_RELOAD_VALUE - Reload value  <15-4294967295>


        public int  WDT_CONFIG_RELOAD_VALUE =2000;
        // <o> WDT_CONFIG_IRQ_PRIORITY  - Interrupt priority


        // <i> Priorities = 0;,2 (nRF51) and = 0;,1,4,5 (nRF52) are reserved for SoftDevice
        // <0=> = 0; (highest)
        // <1=> = 1;
        // <2=> 2
        // <3=> 3
        // <4=> 4
        // <5=> 5
        // <= 6;=> = 6;
        // <7=> 7

        public int  WDT_CONFIG_IRQ_PRIORITY = 6;
        //WDT_ENABLED
        // </e>

        // </h>
        //==========================================================

        // <h> nRF_Libraries

        //==========================================================
        // <q> APP_FIFO_ENABLED  - app_fifo - Software FIFO implementation


        public int  APP_FIFO_ENABLED = 1;
        // <q> APP_MAILBOX_ENABLED  - app_mailbox - Thread safe mailbox


        public int  APP_MAILBOX_ENABLED = 0;
        // <q> APP_PWM_ENABLED  - app_pwm - PWM functionality


        public int  APP_PWM_ENABLED = 0;
        // <e> APP_SCHEDULER_ENABLED - app_scheduler - Events scheduler
        //==========================================================
        public int  APP_SCHEDULER_ENABLED = 0;

        //#if  APP_SCHEDULER_ENABLED
        // <q> APP_SCHEDULER_WITH_PAUSE  - Enabling pause feature


        public int  APP_SCHEDULER_WITH_PAUSE = 0;
        // <q> APP_SCHEDULER_WITH_PROFILER  - Enabling scheduler profiling


        public int  APP_SCHEDULER_WITH_PROFILER = 0;
        //APP_SCHEDULER_ENABLED
        // </e>

        // <e> APP_TIMER_ENABLED - app_timer - Application timer functionality
        //==========================================================
        public int  APP_TIMER_ENABLED = 1;

        //#if  APP_TIMER_ENABLED
        // <q> APP_TIMER_WITH_PROFILER  - Enable app_timer profiling


        public int  APP_TIMER_WITH_PROFILER = 0;
        // <q> APP_TIMER_KEEPS_RTC_ACTIVE  - Enable RTC always on


        // <i> If option is enabled RTC is kept running even if there is no active timers.
        // <i> This option can be used when app_timer is used for timestamping.

        public int  APP_TIMER_KEEPS_RTC_ACTIVE = 0;
        //APP_TIMER_ENABLED
        // </e>

        // <q> APP_TWI_ENABLED  - app_twi - TWI transaction manager


        public int  APP_TWI_ENABLED = 0;
        // <e> APP_UART_ENABLED - app_uart - UART driver
        //==========================================================
        public int  APP_UART_ENABLED = 0;

        //#if  APP_UART_ENABLED
        // <o> APP_UART_DRIVER_INSTANCE  - UART instance used

        // <0=> = 0;

        public int  APP_UART_DRIVER_INSTANCE = 0;
        //APP_UART_ENABLED
        // </e>

        // <q> BUTTON_ENABLED  - app_button - buttons handling module


        public int  BUTTON_ENABLED = 1;
        // <q> CRC16_ENABLED  - crc16 - CRC16 calculation routines


        public int  CRC16_ENABLED = 1;
        // <q> CRC32_ENABLED  - crc32 - CRC32 calculation routines


        public int  CRC32_ENABLED = 0;
        // <q> ECC_ENABLED  - ecc - Elliptic Curve Cryptography Library


        public int  ECC_ENABLED = 0;
        // <e> FDS_ENABLED - fds - Flash data storage module
        //==========================================================
        public int  FDS_ENABLED = 1;

        //#if  FDS_ENABLED
        // <o> FDS_OP_QUEUE_SIZE - Size of the internal queue.
        public int  FDS_OP_QUEUE_SIZE =4;
        // <o> FDS_CHUNK_QUEUE_SIZE - Determines how many @ref fds_record_chunk_t structures can be buffered at any time.
        public int  FDS_CHUNK_QUEUE_SIZE =8;
        // <o> FDS_MAX_USERS - Maximum number of callbacks that can be registered.
        public int  FDS_MAX_USERS =8;
        // <o> FDS_VIRTUAL_PAGES - Number of virtual flash pages to use.
        // <i> One of the virtual pages is reserved by the system for garbage collection.
        // <i> Therefore, the minimum is two virtual pages: one page to store data and
        // <i> one page to be used by the system for garbage collection. The total amount
        // <i> of flash memory that is used by FDS amounts to @ref FDS_VIRTUAL_PAGES
        // <i> @ref FDS_VIRTUAL_PAGE_SIZE * 4 bytes.

        public int  FDS_VIRTUAL_PAGES =3;
        // <o> FDS_VIRTUAL_PAGE_SIZE  - The size of a virtual page of flash memory, expressed in number of 4-byte words.


        // <i> By default, a virtual page is the same size as a physical page.
        // <i> The size of a virtual page must be a multiple of the size of a physical page.
        // <1024=> = 1;024
        // <2048=> 2048

        public int  FDS_VIRTUAL_PAGE_SIZE = 1024;
        //FDS_ENABLED
        // </e>

        // <e> FSTORAGE_ENABLED - fstorage - Flash storage module
        //==========================================================
        public int  FSTORAGE_ENABLED = 1;

        //#if  FSTORAGE_ENABLED
        // <o> FS_QUEUE_SIZE - Configures the size of the internal queue.
        // <i> Increase this if there are many users, or if it is likely that many
        // <i> operation will be queued at once without waiting for the previous operations
        // <i> to complete. In general, increase the queue size if you frequently receive
        // <i> @ref FS_ERR_QUEUE_FULL errors when calling @ref fs_store or @ref fs_erase.

        public int  FS_QUEUE_SIZE =4;
        // <o> FS_OP_MAX_RETRIES - Number attempts to execute an operation if the SoftDevice fails.
        // <i> Increase this value if events return the @ref FS_ERR_OPERATION_TIMEOUT
        // <i> error often. The SoftDevice may fail to schedule flash access due to high BLE activity.

        public int  FS_OP_MAX_RETRIES =3;
        // <o> FS_MAX_WRITE_SIZE_WORDS - Maximum number of words to be written to flash in a single operation.
        // <i> Tweaking this value can increase the chances of the SoftDevice being
        // <i> able to fit flash operations in between radio activity. This value is bound by the
        // <i> maximum number of words which the SoftDevice can write to flash in a single call to
        // <i> @ref sd_flash_write, which is 256 words for nRF51 ICs and = 1;024 words for nRF52 ICs.

        public int  FS_MAX_WRITE_SIZE_WORDS = 1024;
        //FSTORAGE_ENABLED
        // </e>

        // <q> HARDFAULT_HANDLER_ENABLED  - hardfault_default - HardFault default handler for debugging and release


        public int  HARDFAULT_HANDLER_ENABLED = 0;
        // <e> HCI_MEM_POOL_ENABLED - hci_mem_pool - memory pool implementation used by HCI
        //==========================================================
        public int  HCI_MEM_POOL_ENABLED = 0;

        //#if  HCI_MEM_POOL_ENABLED
        // <o> HCI_TX_BUF_SIZE - TX buffer size in bytes.
        public int  HCI_TX_BUF_SIZE =600;
        // <o> HCI_RX_BUF_SIZE - RX buffer size in bytes.
        public int  HCI_RX_BUF_SIZE =600;
        // <o> HCI_RX_BUF_QUEUE_SIZE - RX buffer queue size.
        public int  HCI_RX_BUF_QUEUE_SIZE =4;
        //HCI_MEM_POOL_ENABLED
        // </e>

        // <e> HCI_SLIP_ENABLED - hci_slip - SLIP protocol implementation used by HCI
        //==========================================================
        public int  HCI_SLIP_ENABLED = 0;

        //#if  HCI_SLIP_ENABLED
        // <o> HCI_UART_BAUDRATE  - Default Baudrate

        // <323584=> = 1;200 baud
        // <643072=> 2400 baud
        // <1290240=> 4800 baud
        // <2576384=> 9600 baud
        // <3862528=> = 1;4400 baud
        // <5152768=> = 1;9200 baud
        // <7716864=> 28800 baud
        // <10289152=> 38400 baud
        // <15400960=> 57600 baud
        // <20615168=> 76800 baud
        // <30801920=> = 1;15200 baud
        // <61865984=> 230400 baud
        // <67108864=> 250000 baud
        // <121634816=> 460800 baud
        // <251658240=> 921600 baud
        // <268435456=> 57600 baud

        public int  HCI_UART_BAUDRATE = 30801920;
        // <o> HCI_UART_FLOW_CONTROL  - Hardware Flow Control

        // <0=> Disabled
        // <1=> Enabled

        public int  HCI_UART_FLOW_CONTROL = 0;
        // <o> HCI_UART_RX_PIN - UART RX pin
        public int  HCI_UART_RX_PIN= 8;
        // <o> HCI_UART_TX_PIN - UART TX pin
        public int  HCI_UART_TX_PIN = 6;
        // <o> HCI_UART_RTS_PIN - UART RTS pin
        public int  HCI_UART_RTS_PIN =5;
        // <o> HCI_UART_CTS_PIN - UART CTS pin
        public int  HCI_UART_CTS_PIN =7;
        //HCI_SLIP_ENABLED
        // </e>

        // <e> HCI_TRANSPORT_ENABLED - hci_transport - HCI transport
        //==========================================================
        public int  HCI_TRANSPORT_ENABLED = 0;

        //#if  HCI_TRANSPORT_ENABLED
        // <o> HCI_MAX_PACKET_SIZE_IN_BITS - Maximum size of a single application packet in bits.
        public int  HCI_MAX_PACKET_SIZE_IN_BITS =8000;
        //HCI_TRANSPORT_ENABLED
        // </e>

        // <q> LED_SOFTBLINK_ENABLED  - led_softblink - led_softblink module


        public int  LED_SOFTBLINK_ENABLED = 0;
        // <q> LOW_POWER_PWM_ENABLED  - low_power_pwm - low_power_pwm module


        public int  LOW_POWER_PWM_ENABLED = 0;
        // <e> MEM_MANAGER_ENABLED - mem_manager - Dynamic memory allocator
        //==========================================================
        public int  MEM_MANAGER_ENABLED = 0;

        //#if  MEM_MANAGER_ENABLED
        // <o> MEMORY_MANAGER_SMALL_BLOCK_COUNT - Size of each memory blocks identified as 'small' block.  <0-= 255;>


        public int  MEMORY_MANAGER_SMALL_BLOCK_COUNT = 1;
        // <o> MEMORY_MANAGER_SMALL_BLOCK_SIZE -  Size of each memory blocks identified as 'small' block.
        // <i>  Size of each memory blocks identified as 'small' block. Memory block are recommended to be word-sized.

        public int  MEMORY_MANAGER_SMALL_BLOCK_SIZE =32;
        // <o> MEMORY_MANAGER_MEDIUM_BLOCK_COUNT - Size of each memory blocks identified as 'medium' block.  <0-= 255;>


        public int  MEMORY_MANAGER_MEDIUM_BLOCK_COUNT = 0;
        // <o> MEMORY_MANAGER_MEDIUM_BLOCK_SIZE -  Size of each memory blocks identified as 'medium' block.
        // <i>  Size of each memory blocks identified as 'medium' block. Memory block are recommended to be word-sized.

        public int  MEMORY_MANAGER_MEDIUM_BLOCK_SIZE =256;
        // <o> MEMORY_MANAGER_LARGE_BLOCK_COUNT - Size of each memory blocks identified as 'large' block.  <0-= 255;>


        public int  MEMORY_MANAGER_LARGE_BLOCK_COUNT = 0;
        // <o> MEMORY_MANAGER_LARGE_BLOCK_SIZE -  Size of each memory blocks identified as 'large' block.
        // <i>  Size of each memory blocks identified as 'large' block. Memory block are recommended to be word-sized.

        public int  MEMORY_MANAGER_LARGE_BLOCK_SIZE =256;
        // <q> MEM_MANAGER_ENABLE_LOGS  - Enable debug trace in the module.


        public int  MEM_MANAGER_ENABLE_LOGS = 0;
        // <q> MEM_MANAGER_DISABLE_API_PARAM_CHECK  - Disable API parameter checks in the module.


        public int  MEM_MANAGER_DISABLE_API_PARAM_CHECK = 0;
        //MEM_MANAGER_ENABLED
        // </e>

        // <e> NRF_CSENSE_ENABLED - nrf_csense - nrf_csense module
        //==========================================================
        public int  NRF_CSENSE_ENABLED = 0;

        //#if  NRF_CSENSE_ENABLED
        // <o> NRF_CSENSE_PAD_HYSTERESIS - Minimal value of change to decide that pad was touched.
        public int  NRF_CSENSE_PAD_HYSTERESIS = 15;
        // <o> NRF_CSENSE_PAD_DEVIATION - Minimal value measured on pad to take its value while calculating step.
        public int  NRF_CSENSE_PAD_DEVIATION = 70;
        // <o> NRF_CSENSE_MIN_PAD_VALUE - Minimum normalized value on pad to take its value into account.
        public int  NRF_CSENSE_MIN_PAD_VALUE =20;
        // <o> NRF_CSENSE_MAX_PADS_NUMBER - Maximum number of pads used for one instance.
        public int  NRF_CSENSE_MAX_PADS_NUMBER =20;
        // <o> NRF_CSENSE_MAX_VALUE - Maximum normalized value got from measurement.
        public int  NRF_CSENSE_MAX_VALUE = 1000;
        // <o> NRF_CSENSE_OUTPUT_PIN - Output pin used by lower module.
        // <i> This is only used when running on NRF51.

        public int  NRF_CSENSE_OUTPUT_PIN = 30;
        //NRF_CSENSE_ENABLED
        // </e>

        // <e> NRF_DRV_CSENSE_ENABLED - nrf_drv_csense - Capacitive sensor module
        //==========================================================
        public int  NRF_DRV_CSENSE_ENABLED = 0;

        //#if  NRF_DRV_CSENSE_ENABLED
        // <o> TIMER0_FOR_CSENSE - First TIMER instance used by the driver (except nRF51)
        public int  TIMER0_FOR_CSENSE = 1;
        // <o> TIMER1_FOR_CSENSE - Second TIMER instance used by the driver (except nRF51)
        public int  TIMER1_FOR_CSENSE =2;
        // <o> MEASUREMENT_PERIOD - Single measurement period.
        // <i> Time of single measurement can be calculated as T = (1/2)*MEASUREMENT_PERIOD*(1/f_OSC) where f_OSC = I_SOURCE / (2C*(VUP-VDOWN) ). I_SOURCE, VUP and VDOWN are values used to initialize COMP and C is capacitance of used pad.

        public int  MEASUREMENT_PERIOD = 20;
        //NRF_DRV_CSENSE_ENABLED
        // </e>

        // <q> SLIP_ENABLED  - slip - SLIP encoding decoding


        public int  SLIP_ENABLED = 0;
        // </h>
        //==========================================================

        // <h> nRF_Log

        //==========================================================
        // <e> NRF_LOG_ENABLED - nrf_log - Logging
        //==========================================================
        public int  NRF_LOG_ENABLED = 0;

        //#if  NRF_LOG_ENABLED
        // <e> NRF_LOG_USES_COLORS - If enabled then ANSI escape code for colors is prefixed to every string
        //==========================================================
        public int  NRF_LOG_USES_COLORS = 0;

        //#if  NRF_LOG_USES_COLORS
        // <o> NRF_LOG_COLOR_DEFAULT  - ANSI escape code prefix.

        // <0=> Default
        // <1=> Black
        // <2=> Red
        // <3=> Green
        // <4=> Yellow
        // <5=> Blue
        // <= 6;=> Magenta
        // <7=> Cyan
        // <8=> White

        public int  NRF_LOG_COLOR_DEFAULT = 0;
        // <o> NRF_LOG_ERROR_COLOR  - ANSI escape code prefix.

        // <0=> Default
        // <1=> Black
        // <2=> Red
        // <3=> Green
        // <4=> Yellow
        // <5=> Blue
        // <= 6;=> Magenta
        // <7=> Cyan
        // <8=> White

        public int  NRF_LOG_ERROR_COLOR = 0;
        // <o> NRF_LOG_WARNING_COLOR  - ANSI escape code prefix.

        // <0=> Default
        // <1=> Black
        // <2=> Red
        // <3=> Green
        // <4=> Yellow
        // <5=> Blue
        // <= 6;=> Magenta
        // <7=> Cyan
        // <8=> White

        public int  NRF_LOG_WARNING_COLOR = 0;
        //NRF_LOG_USES_COLORS
        // </e>

        // <o> NRF_LOG_DEFAULT_LEVEL  - Default Severity level

        // <0=> Off
        // <1=> Error
        // <2=> Warning
        // <3=> Info
        // <4=> Debug

        public int  NRF_LOG_DEFAULT_LEVEL = 3;
        // <e> NRF_LOG_DEFERRED - Enable deffered logger.

        // <i> Log data is buffered and can be processed in idle.
        //==========================================================
        public int  NRF_LOG_DEFERRED = 1;

        //#if  NRF_LOG_DEFERRED
        // <o> NRF_LOG_DEFERRED_BUFSIZE - Size of the buffer for logs in words.
        // <i> Must be power of 2

        public int  NRF_LOG_DEFERRED_BUFSIZE = 256;
        //NRF_LOG_DEFERRED
        // </e>

        // <q> NRF_LOG_USES_TIMESTAMP  - Enable timestamping


        // <i> Function for getting the timestamp is provided by the user

        public int  NRF_LOG_USES_TIMESTAMP = 0;
        //NRF_LOG_ENABLED
        // </e>

        // <h> nrf_log_backend - Logging sink

        //==========================================================
        // <o> NRF_LOG_BACKEND_MAX_STRING_LENGTH - Buffer for storing single output string
        // <i> Logger backend RAM usage is determined by this value.

        public int  NRF_LOG_BACKEND_MAX_STRING_LENGTH = 256;
        // <o> NRF_LOG_TIMESTAMP_DIGITS - Number of digits for timestamp
        // <i> If higher resolution timestamp source is used it might be needed to increase that

        public int  NRF_LOG_TIMESTAMP_DIGITS = 8;
        // <e> NRF_LOG_BACKEND_SERIAL_USES_UART - If enabled data is printed over UART
        //==========================================================
        public int  NRF_LOG_BACKEND_SERIAL_USES_UART = 1;

        //#if  NRF_LOG_BACKEND_SERIAL_USES_UART
        // <o> NRF_LOG_BACKEND_SERIAL_UART_BAUDRATE  - Default Baudrate

        // <323584=> = 1;200 baud
        // <643072=> 2400 baud
        // <1290240=> 4800 baud
        // <2576384=> 9600 baud
        // <3862528=> = 1;4400 baud
        // <5152768=> = 1;9200 baud
        // <7716864=> 28800 baud
        // <10289152=> 38400 baud
        // <15400960=> 57600 baud
        // <20615168=> 76800 baud
        // <30801920=> = 1;15200 baud
        // <61865984=> 230400 baud
        // <67108864=> 250000 baud
        // <121634816=> 460800 baud
        // <251658240=> 921600 baud
        // <268435456=> 57600 baud

        public int  NRF_LOG_BACKEND_SERIAL_UART_BAUDRATE = 30801920;
        // <o> NRF_LOG_BACKEND_SERIAL_UART_TX_PIN - UART TX pin
        public int  NRF_LOG_BACKEND_SERIAL_UART_TX_PIN = 6;
        // <o> NRF_LOG_BACKEND_SERIAL_UART_RX_PIN - UART RX pin
        public int  NRF_LOG_BACKEND_SERIAL_UART_RX_PIN = 8;
        // <o> NRF_LOG_BACKEND_SERIAL_UART_RTS_PIN - UART RTS pin
        public int  NRF_LOG_BACKEND_SERIAL_UART_RTS_PIN = 5;
        // <o> NRF_LOG_BACKEND_SERIAL_UART_CTS_PIN - UART CTS pin
        public int  NRF_LOG_BACKEND_SERIAL_UART_CTS_PIN = 7;
        // <o> NRF_LOG_BACKEND_SERIAL_UART_FLOW_CONTROL  - Hardware Flow Control

        // <0=> Disabled
        // <1=> Enabled

        public int  NRF_LOG_BACKEND_SERIAL_UART_FLOW_CONTROL = 0;
        // <o> NRF_LOG_BACKEND_UART_INSTANCE  - UART instance used

        // <0=> = 0;

        public int  NRF_LOG_BACKEND_UART_INSTANCE = 0;
        //NRF_LOG_BACKEND_SERIAL_USES_UART
        // </e>

        // <q> NRF_LOG_BACKEND_SERIAL_USES_RTT  - If enabled data is printed using RTT
        public int  NRF_LOG_BACKEND_SERIAL_USES_RTT = 0;
        // </h>


        // <<< end of configuration section >>>


        }

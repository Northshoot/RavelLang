package ai.harmony.api.platforms.nrf52.obj;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.platforms.RavelAPIObject;
import ai.harmony.api.platforms.RavelObjectInterface;
import ai.harmony.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

import static ai.harmony.api.platforms.nrf52Platform.BASE_PALTFORM_TMPL_PATH;

/**
 * Created by lauril on 10/4/16.
 */
public class MainApp extends RavelAPIObject implements RavelObjectInterface {

    private boolean debug_nrf_log = true;
    private boolean debug_segger_rtt = true;
    public boolean ble_enable = true;
    private boolean nrf_uart = true;

    private String fileName = "main.c";
    private Space mSpace;
    private STGroup mMainTmlp;
    private STGroup mCnfgTmlp;



    public MainApp(String bp, Space s) {
        mBuildPath = bp;
        mSpace = s;
        mMainTmlp = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/main.stg");
        mCnfgTmlp = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/config.stg");
        //create main app
        obj.setPath(mBuildPath);
        obj.setFileName(fileName);
        if(ble_enable) makeBLE(mBuildPath);
        //Base stuff that is needed by nrf
        addLogging();
        addBaseMake();
        //this app
        addMainAppMake();
        //make config section
        //TODO: add dynamic app config based on params
        makeCnf();

    }

    private void makeBLE(String mBuildPath){
        STGroup ble_tmpl = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/ble.stg");
        ST tmpl_h = ble_tmpl.getInstanceOf("ble_header");
        tmpl_h.add("space", mSpace);
        ST tmpl_o = ble_tmpl.getInstanceOf("ble_obj");
        tmpl_o.add("space", mSpace);

        addToMakeObjSDK("api/api_ble.c");
        FileObject ble_header = new FileObject();
        ble_header.setFileName("api_ble.h");
        ble_header.setPath(mBuildPath+"/api/");
        ble_header.setContent(tmpl_h.render());

        FileObject ble_obj= new FileObject();
        ble_obj.setFileName("api_ble.c");
        ble_obj.setPath(mBuildPath+"/api/");
        ble_obj.setContent(tmpl_o.render());
        mFileList.add(ble_header);
        mFileList.add(ble_obj);
    }
    public List<FileObject> getFiles() {
        //add assert
//        ST t_obj = mMainTmlp.getInstanceOf("assert_nrf_callback");
//        obj.appendContent(t_obj.render());
//        // Function for handling File Data Storage events.
//        t_obj = mMainTmlp.getInstanceOf("fds_evt_handler");
//        obj.appendContent(t_obj.render());
//        //Function for handling Peer Manager events.
//        t_obj = mMainTmlp.getInstanceOf("pm_evt_handler");
//        obj.appendContent(t_obj.render());
//        //  Function for the Peer Manager initialization
//        t_obj = mMainTmlp.getInstanceOf("assert_nrf_callback");
//        obj.appendContent(t_obj.render());
//        //vFunction for the Power manager.
//        t_obj = mMainTmlp.getInstanceOf("power_manage");
//        obj.appendContent(t_obj.render());
        //main
        ST t_obj = mMainTmlp.getInstanceOf("main_obj_c");
        t_obj.add("space", mSpace);
        t_obj.add("components", this);
        obj.appendContent(t_obj.render());

        obj.setContent(t_obj.render());
        mFileList.add(obj);
        return mFileList;
    }

    @Override
    public String getHeaderDefName() {
        return null;
    }

    private void makeCnf(){
        ST t_obj = mCnfgTmlp.getInstanceOf("ravel_gcc_nrf52_ld");
        addCnfFile("ravel_gcc_nrf52_ld.ld",t_obj.render());

        t_obj = mCnfgTmlp.getInstanceOf("app_cnfg");
        addCnfFile("app_config.h",t_obj.render());

        t_obj = mCnfgTmlp.getInstanceOf("device_manager_cnfg");
        addCnfFile("device_manager_cnfg.h",t_obj.render());

        t_obj = mCnfgTmlp.getInstanceOf("nrf_drv_config");
        addCnfFile("nrf_drv_config.h",t_obj.render());

        t_obj = mCnfgTmlp.getInstanceOf("sdk_config");
        addCnfFile("sdk_config.h",t_obj.render());

    }

    private void addCnfFile(String name, String content){
        FileObject fo = new FileObject();
        fo.setPath(mBuildPath + "/config/");
        fo.setFileName(name);
        fo.setContent(content);
        mFileList.add(fo);


    }
    private void addMainAppMake(){
        /**
         * Fix includes for this project
         */
        addToMakeIncludePath("api/");
        addToMakeIncludePath("config/");
        //include "this" directory too
        addToMakeIncludePath("");
        addToDefines(new Declaration("#define DEAD_BEEF                        0xDEADBEEF", "/**< Value used as error code on stack dump, can be used to identify stack location on stack unwind. */"));
        addToIncludes(new Declaration("USE_APP_CONFIG 1"));
        addToDeclarations(new Declaration("static uint16_t                          m_conn_handle = BLE_CONN_HANDLE_INVALID;", "/**< Handle of the current connection. */"));
        /**
         * Add objects for this project
         */
        addToMakeObj(fileName);
    }

    private void addLogging(){
        if(debug_nrf_log){
            addToMakeIncludePathSDK("/components/libraries/log");
            addToMakeIncludePathSDK("/components/libraries/log/src");
            addToMakeObjSDK("/components/libraries/log/src/nrf_log_backend_serial.c");
            addToMakeObjSDK("/components/libraries/log/src/nrf_log_frontend.c");
            addToDefines(new Declaration("NRF_LOG_MODULE_NAME \"APP\""));
            addToIncludes(new Declaration("nrf_log.h"));
            addToIncludes(new Declaration("nrf_log_ctrl.h"));
        }
        if(debug_segger_rtt){
            addToMakeIncludePathSDK("/external/segger_rtt");
            addToMakeObjSDK("/external/segger_rtt/RTT_Syscalls_GCC.c");
            addToMakeObjSDK("/external/segger_rtt/SEGGER_RTT.c");
            addToMakeObjSDK("/external/segger_rtt/SEGGER_RTT_printf.c");
        }

    }
    private void addBaseMake(){

        addToMakeIncludePathSDK("/components");

        addToMakeIncludePathSDK("/components/drivers_nrf/comp");
        addToMakeIncludePathSDK("/components/drivers_nrf/twi_master");
        addToMakeIncludePathSDK("/components/drivers_nrf/rng");
        addToMakeIncludePathSDK("/components/drivers_nrf/spi_slave");
        addToMakeIncludePathSDK("/components/drivers_nrf/lpcomp");
        addToMakeIncludePathSDK("/components/drivers_nrf/qdec");
        addToMakeIncludePathSDK("/components/drivers_nrf/spi_master");
        addToMakeIncludePathSDK("/components/drivers_nrf/pdm");
        addToMakeIncludePathSDK("/components/drivers_nrf/saadc");
        addToMakeIncludePathSDK("/components/drivers_nrf/swi");
        addToMakeIncludePathSDK("/components/drivers_nrf/clock");
        addToMakeIncludePathSDK("/components/drivers_nrf/ppi");
        addToMakeIncludePathSDK("/components/drivers_nrf/twis_slave");
        addToMakeIncludePathSDK("/components/drivers_nrf/delay");
        addToMakeIncludePathSDK("/components/drivers_nrf/hal");
        addToMakeIncludePathSDK("/components/drivers_nrf/i2s");
        addToMakeIncludePathSDK("/components/drivers_nrf/common");
        addToMakeIncludePathSDK("/components/drivers_nrf/adc");
        addToMakeIncludePathSDK("/components/drivers_nrf/uart");
        addToMakeIncludePathSDK("/components/drivers_nrf/pwm");
        addToMakeIncludePathSDK("/components/drivers_nrf/wdt");

        addToMakeIncludePathSDK("/components/libraries/scheduler");
        addToMakeIncludePathSDK("/components/libraries/util");
        addToMakeIncludePathSDK("/components/libraries/crc32");
        addToMakeIncludePathSDK("/components/libraries/csense_drv");
        addToMakeIncludePathSDK("/components/libraries/csense");
        addToMakeIncludePathSDK("/components/libraries/low_power_pwm");
        addToMakeIncludePathSDK("/components/libraries/uart");
        addToMakeIncludePathSDK("/components/libraries/hci");
        addToMakeIncludePathSDK("/components/libraries/fifo");
        addToMakeIncludePathSDK("/components/libraries/led_softblink");
        addToMakeIncludePathSDK("/components/libraries/button");
        addToMakeIncludePathSDK("/components/libraries/fds");
        addToMakeIncludePathSDK("/components/libraries/twi");
        addToMakeIncludePathSDK("/components/libraries/pwm");
        addToMakeIncludePathSDK("/components/libraries/mailbox");
        addToMakeIncludePathSDK("/components/libraries/fstorage");
        addToMakeIncludePathSDK("/components/libraries/experimental_section_vars");
        addToMakeIncludePathSDK("/components/libraries/slip");
        addToMakeIncludePathSDK("/components/libraries/mem_manager");

        addToMakeIncludePathSDK("/components/device");

        addToMakeObjSDK("/components/libraries/util/app_error.c");
        addToMakeObjSDK("/components/libraries/util/app_error_weak.c");
        addToMakeIncludePathSDK("/components/libraries/hardfault");
        addToMakeObjSDK("/components/libraries/hardfault/hardfault_implementation.c");
        addToMakeObjSDK("/components/libraries/util/nrf_assert.c");

        addToMakeObjSDK("/components/libraries/fifo/app_fifo.c");
        addToMakeObjSDK("/components/libraries/util/app_util_platform.c");

        addToMakeIncludePathSDK("/components/libraries/crc16");
        addToMakeObjSDK("/components/libraries/crc16/crc16.c");

        addToMakeObjSDK("/components/libraries/fds/fds.c");
        addToMakeObjSDK("/components/libraries/fstorage/fstorage.c");


        addToMakeObjSDK("/components/libraries/util/sdk_mapped_flags.c");

        addToMakeIncludePathSDK("/components/drivers_nrf/rtc");
        addToMakeObjSDK("/components/drivers_nrf/clock/nrf_drv_clock.c");
        addToMakeObjSDK("/components/drivers_nrf/common/nrf_drv_common.c");

        addToMakeIncludePathSDK("/components/drivers_nrf/gpiote");
        addToMakeObjSDK("/components/drivers_nrf/gpiote/nrf_drv_gpiote.c");


        addToMakeObjSDK("/components/libraries/button/app_button.c");

        addToMakeIncludePathSDK("/examples/bsp");
        addToMakeObjSDK("/examples/bsp/bsp.c");
        addToMakeObjSDK("/examples/bsp/bsp_btn_ble.c");

        /**
         * Needed for tool chain
         */
        addToMakeIncludePathSDK("/components/toolchain/gcc");
        addToMakeIncludePathSDK("/components/toolchain");
        addToMakeIncludePathSDK("/components/toolchain/cmsis/include");
        addToMakeObjSDK("/components/toolchain/gcc/gcc_startup_nrf52.S");
        addToMakeObjSDK("/components/toolchain/system_nrf52.c");

        /**
         * Softdevice manager
         */
        addToMakeIncludePathSDK("/components/softdevice/s132/headers");
        addToMakeIncludePathSDK("/components/softdevice/s132/headers/nrf52");
        addToMakeIncludePathSDK("/components/softdevice/common/softdevice_handler");
        addToMakeObjSDK("/components/softdevice/common/softdevice_handler/softdevice_handler.c");


        if(nrf_uart){
            addToMakeIncludePathSDK("");
            addToMakeObjSDK("/components/drivers_nrf/uart/nrf_drv_uart.c");
        }


        if(ble_enable){
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_lbs_c");
            addToMakeIncludePathSDK("/components/ble/ble_racp");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_cscs");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_nus");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_hids");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_lbs");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_hts");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_tps");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_dis");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_nus_c");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_dfu");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_ans_c");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_ancs_c");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_ias_c");
            addToMakeIncludePathSDK("/components/ble/common");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_lls");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_cts_c");
            addToMakeIncludePathSDK("/components/ble/nrf_ble_qwr");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_gls");
            addToMakeIncludePathSDK("/components/ble/ble_advertising");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_bas_c");
            addToMakeIncludePathSDK("/components/ble/ble_dtm");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_rscs_c");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_bas");
            addToMakeIncludePathSDK("/components/ble/ble_services/ble_ias");

            addToMakeObjSDK("/components/ble/common/ble_advdata.c");
            addToMakeObjSDK("/components/ble/ble_advertising/ble_advertising.c");

            addToMakeObjSDK("/components/ble/common/ble_conn_params.c");
            addToMakeObjSDK("/components/ble/common/ble_conn_state.c");
            addToMakeObjSDK("/components/ble/common/ble_srv_common.c");

            addToMakeIncludePathSDK("/components/ble/peer_manager");
            addToMakeObjSDK("/components/ble/peer_manager/gatt_cache_manager.c");
            addToMakeObjSDK("/components/ble/peer_manager/gatts_cache_manager.c");
            addToMakeObjSDK("/components/ble/peer_manager/id_manager.c");
            addToMakeObjSDK("/components/ble/peer_manager/peer_data.c");
            addToMakeObjSDK("/components/ble/peer_manager/peer_data_storage.c");
            addToMakeObjSDK("/components/ble/peer_manager/peer_database.c");
            addToMakeObjSDK("/components/ble/peer_manager/peer_id.c");
            addToMakeObjSDK("/components/ble/peer_manager/peer_manager.c");
            addToMakeObjSDK("/components/ble/peer_manager/pm_buffer.c");
            addToMakeObjSDK("/components/ble/peer_manager/pm_mutex.c");
            addToMakeObjSDK("/components/ble/peer_manager/security_dispatcher.c");
            addToMakeObjSDK("/components/ble/peer_manager/security_manager.c");

        }

    }

}

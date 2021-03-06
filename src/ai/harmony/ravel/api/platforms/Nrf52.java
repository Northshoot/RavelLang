package ai.harmony.ravel.api.platforms;

import ai.harmony.ravel.RavelProperties;
import ai.harmony.ravel.api.builder.FileObject;
import ai.harmony.ravel.api.platforms.nrf52.NrfConfig;
import ai.harmony.ravel.api.platforms.nrf52.NrfPlatformOptions;
import ai.harmony.ravel.misc.TemplatePair;
import ai.harmony.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

//TODO:
//TODO: platforms should be separated form the compiler


/**
 * The NRF52 runtime
 *
 * Created by lauril on 2/13/17.
 */
public class Nrf52 extends BaseCPlatform {
    //TODO: platform path should be configurable
    private final static String BASE_LANG_TMPL_PATH = RavelProperties.getInstance().get_nrf52_tmpl_dir();
    private STGroup ldGroup;
    private STGroup configGroup;

    public Nrf52() {
        super(
                new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg"),
                new STGroupFile(BASE_LANG_TMPL_PATH + "/makefile.stg")
        );
    }
    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        //TODO: need a better way to handle C build system with adding components
        TemplatePair tmpl_extra = new TemplatePair("linker_script", s.getName());
        TemplatePair tmpl_extra1 = new TemplatePair("sdk_config", "sdk_config");
        List<TemplatePair> make_addons = new ArrayList<>();
        make_addons.add(tmpl_extra);
        //make_addons.add(tmpl_extra1);
        List<FileObject> all_files = new ArrayList<>(super.createBuildSystem(s,files, NrfPlatformOptions.getInstance(), make_addons));

        all_files.add(makeLinkerScript(tmpl_extra));
        //all_files.add(makeConfigFile(tmpl_extra1, build_nrf_config_file(s)));

        return all_files;
    }

    private STGroup getConfigGroup(){
        configGroup = new STGroupFile(BASE_LANG_TMPL_PATH + getAPILevel()+"/sdk_config.stg");
        return configGroup;
    }
    private STGroup getLDGroup(){
        ldGroup = new STGroupFile(BASE_LANG_TMPL_PATH + getAPILevel()+"/linker.stg");
        return ldGroup;
    }
    private NrfConfig build_nrf_config_file(Space s) {
        
        NrfConfig nc = new NrfConfig();
        //TODO: add to interface
        //NOT used right now
        //define = {val: key}
        nc.TIMER_ENABLED = 0;
        nc.RTC2_ENABLED =1 ;
        nc.RTC_ENABLED =1 ;
        nc.CLOCK_ENABLED =1;
        nc.CLOCK_CONFIG_XTAL_FREQ =1;
        nc.CLOCK_CONFIG_LF_SRC =1;
        nc.GPIOTE_ENABLED =1;
        nc.NRF_LOG_ENABLED =1;
        nc.NRF_LOG_USES_COLORS =1;
        nc.NRF_LOG_COLOR_DEFAULT = 3;
        nc.NRF_LOG_WARNING_COLOR = 4;
        nc.NRF_LOG_DEFAULT_LEVEL =4;
        nc.NRF_LOG_DEFERRED =0; //no buffering
        nc.NRF_LOG_DEFAULT_LEVEL =4; //Debug level 4
        nc.APP_SCHEDULER_ENABLED = 1;

//        Collection<ConcreteInterface> interfaces = s.getInterfaces();
//        Iterator<ConcreteInterface> interfaceIterator = interfaces.iterator();
//        while (interfaceIterator.hasNext()){
//            ConcreteInterface next = interfaceIterator.next();
//            Object define = next.getConfiguration().get("DEFINE");
//        }
        return nc;
    }

    private FileObject makeLinkerScript(TemplatePair p){
        //TODO: values could come from config file
        //TODO: we need to increase the memory with each BLE service

        /**
         *
         MEMORY
         {
         FLASH (rx) : ORIGIN = 0x1f000, LENGTH = 0x61000
         RAM (rwx) :  ORIGIN = 0x20002128, LENGTH = 0xded8
         }
         */
        ST tmpl_ld = getLDGroup().getInstanceOf(p.getKeyword());
        FileObject file_ld = new FileObject();
        file_ld.setFileName( p.getValue()+".ld");
        file_ld.setContent(tmpl_ld.render());
        return file_ld;


    }

    private FileObject makeConfigFile(TemplatePair p, NrfConfig configObj){
        //TODO: these could come from config
        ST tmpl_sd = getConfigGroup().getInstanceOf("sdk_config");
        tmpl_sd.add("config", configObj);
        FileObject sdk_config_file = new FileObject();
        sdk_config_file.setSubPath("config/");
        sdk_config_file.setFileName( p.getValue()+".h");
        sdk_config_file.setContent(tmpl_sd.render());
        return sdk_config_file;
    }



    @Override
    public PlatformOptions getOptions() {
        return NrfPlatformOptions.getInstance();
    }




}

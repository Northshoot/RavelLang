package org.stanford.ravel.api.platforms;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.platforms.nrf52.NrfConfig;
import org.stanford.ravel.api.platforms.nrf52.NrfPlatformOptions;
import org.stanford.ravel.misc.TemplatePair;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;
//TODO: platforms should be separated form the compiler
import static org.stanford.ravel.api.Settings.BASE_TMPL_PATH;

/**
 * The NRF52 runtime
 *
 * Created by lauril on 2/13/17.
 */
public class Nrf52 extends BaseCPlatform {
    //TODO: platform path should be configurable
    private final static String BASE_LANG_TMPL_PATH = BASE_TMPL_PATH +"/platforms/nrf52/tmpl";
    private final STGroup ldGroup;
    private final STGroup configGroup;

    public Nrf52() {
        super(
                new STGroupFile(BASE_LANG_TMPL_PATH + "/main.stg"),
                new STGroupFile(BASE_LANG_TMPL_PATH + "/makefile.stg")
        );
        ldGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/linker.stg");
        configGroup = new STGroupFile(BASE_LANG_TMPL_PATH + "/sdk_config.stg");
    }
    @Override
    public List<FileObject> createBuildSystem(Space s, List<FileObject> files) {
        //TODO: need a better way to handle C build system with adding components
        TemplatePair tmpl_extra = new TemplatePair("linker_script", s.getName());
        TemplatePair tmpl_extra1 = new TemplatePair("sdk_config", "sdk_config");
        List<TemplatePair> make_addons = new ArrayList<>();
        make_addons.add(tmpl_extra);
        //make_addons.add(tmpl_extra1);

        makeLinkerScript(tmpl_extra);
       makeConfigFile(tmpl_extra1, new NrfConfig());
        return super.createBuildSystem(s,files, NrfPlatformOptions.getInstance(), make_addons);
    }

    private void makeLinkerScript(TemplatePair p){
        //TODO: values could come from config file
        /**
         *
         MEMORY
         {
         FLASH (rx) : ORIGIN = 0x1f000, LENGTH = 0x61000
         RAM (rwx) :  ORIGIN = 0x20002128, LENGTH = 0xded8
         }
         */
        ST tmpl_ld = ldGroup.getInstanceOf(p.getKeyword());
        FileObject file_ld = new FileObject();
        file_ld.setFileName( p.getValue()+".ld");
        file_ld.setContent(tmpl_ld.render());
        super.addFile(file_ld);


    }

    private void makeConfigFile(TemplatePair p, NrfConfig configObj){
        //TODO: these could come from config file
        ST tmpl_sd = configGroup.getInstanceOf("sdk_config");
        tmpl_sd.add("config", new NrfConfig());
        FileObject sdk_config_file = new FileObject();
        sdk_config_file.setSubPath("config/");
        sdk_config_file.setFileName( p.getValue()+".h");
        sdk_config_file.setContent(tmpl_sd.render());
        super.addFile(sdk_config_file);
    }



    @Override
    public PlatformOptions getOptions() {
        return NrfPlatformOptions.getInstance();
    }




}

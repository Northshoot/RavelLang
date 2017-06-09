package edu.stanford.ravel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by larry on 4/19/17.
 */
public class RavelProperties {
    private static final String BUNDLE_NAME = "edu.stanford.resources.config";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
    private final static String FILE_OUT_DIR="fileOutDir";
    private final static String BASE_TMPL_DIR="baseTmplDir";
    private final static String API_C_TMPL_DIR="apiCTmplDir";
    private final static String API_JAVA_TMPL_DIR="apiJavaDir";
    private final static String API_PYTHON_TMPL_DIR="apiPythonDir";
    private final static String API_NRF52_TMPL_DIR="apiNRF52Dir";
    private final static String API_CONTIKI_TMPL_DIR="apiContikiDir";
    private final static String API_ANDROID_TMPL_DIR="apiAndroidDir";
    private final static String API_POSIX_TMPL_DIR="apiPosixDir";
    private final static String API_J2SE_TMPL_DIR="apiJ2SEDir";

    public final static String API_LANG = "apiLangPackage";
    public final static String API_PLATFORM = "apiPlatformPackage";
    public final static String RUNTIME = "apiRuntimePackage";
    private static final String API_RIE_J2SE_TMPL_DIR = "apiRPIEDir";



    public static String get_fileOutDir(){
        return RESOURCE_BUNDLE.getString(FILE_OUT_DIR)+"/";
    }

    public static String get_c_tmpl_dir(){
        return get_base_tmpl_dir()+"/" +RESOURCE_BUNDLE.getString(API_C_TMPL_DIR)+"/";
    }
    public static String get_java_tmpl_dir(){
        return get_base_tmpl_dir()+"/" +RESOURCE_BUNDLE.getString(API_JAVA_TMPL_DIR)+"/";
    }
    public static String get_python_tmpl_dir(){
        return get_base_tmpl_dir()+"/" +RESOURCE_BUNDLE.getString(API_PYTHON_TMPL_DIR)+"/";
    }
    public static String get_j2se_tmpl_dir(){
        return get_base_tmpl_dir()+"/" +RESOURCE_BUNDLE.getString(API_J2SE_TMPL_DIR)+"/";
    }
    public static String get_android_tmpl_dir(){
        return get_base_tmpl_dir()+"/" +RESOURCE_BUNDLE.getString(API_ANDROID_TMPL_DIR)+"/";
    }
    public static String get_posix_tmpl_dir(){
        return get_base_tmpl_dir()+"/" +RESOURCE_BUNDLE.getString(API_POSIX_TMPL_DIR)+"/";
    }
    public static String get_contiki_tmpl_dir(){
        return get_base_tmpl_dir()+"/" +RESOURCE_BUNDLE.getString(API_CONTIKI_TMPL_DIR)+"/";
    }

    public static String get_nrf52_tmpl_dir(){
        return get_base_tmpl_dir()+"/" + RESOURCE_BUNDLE.getString(API_NRF52_TMPL_DIR)+"/";
    }

    public static String get_base_tmpl_dir(){
        return RESOURCE_BUNDLE.getString(BASE_TMPL_DIR);
    }
    public static String get_language_package(){
        return RESOURCE_BUNDLE.getString(API_LANG);
    }
    public static String get_platform_package(){
        return RESOURCE_BUNDLE.getString(API_PLATFORM);
    }
    public static String get_runtime_package(){
        return RESOURCE_BUNDLE.getString(RUNTIME);
    }

    public static String get_rpiej2se_tmpl_dir() {
        return get_base_tmpl_dir() +RESOURCE_BUNDLE.getString(API_RIE_J2SE_TMPL_DIR);
    }


    private static RavelProperties mRavelProperties = new RavelProperties( );
    private final Properties properties = new Properties();


    private RavelProperties(){ propInit(); }

    public static RavelProperties getInstance() {
        return mRavelProperties;
    }
    private void propInit()  {
        InputStream input = null;

        try {
            String propFileName = System.getProperty("user.dir")+"/default.properties";
            properties.load(new FileInputStream(propFileName ));

        } catch (Exception e) {
            System.out.println("Exception opening: " + e);
        }

    }
}

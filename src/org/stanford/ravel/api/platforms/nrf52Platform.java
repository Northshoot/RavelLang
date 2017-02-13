//package org.stanford.ravel.api.platforms;
//
//import org.stanford.ravel.api.builder.FileObject;
//import org.stanford.ravel.api.lang.CLang;
//import org.stanford.ravel.api.lang.ConcreteLanguage;
//import org.stanford.ravel.api.lang.JLang;
//import org.stanford.ravel.primitives.Interface;
//import org.stanford.ravel.primitives.Space;
//import org.stanford.ravel.api.Settings;
//import org.stanford.ravel.api.platforms.nrf52.obj.*;
//import org.stringtemplate.v4.ST;
//import org.stringtemplate.v4.STGroup;
//import org.stringtemplate.v4.STGroupFile;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Logger;
//
///**
// * Created by lauril on 9/21/16.
// */
//public class nrf52Platform extends BasePlatform {
//    private static Logger LOGGER = Logger.getLogger(nrf52Platform.class.getName());
//    public final static String BASE_PALTFORM_TMPL_PATH = Settings.BASE_TMPL_PATH +"/platforms/nrf52/tmpl";
//    public final static String MAKE_SDK_PREFIX = "$(SDK_ROOT)";
//    public final static String MAKE_PRJ_PREFIX = "$(PROJ_DIR)";
//
//    public final static String VERSION = "nrf52Platform.v12";
//    //provided API objects
//    private TimerQueue tQueue=null;
//    private Random mRandom=null;
//    private Boot mBoot=null;
//
//    //default make data
//    private MainApp mMainApp;
//
//    //build files
//    List<FileObject> mFiles;
//    private String mBuildPath="";
//    private String mBuildPathApi="";
//
//    private Space mSpace;
//
//    public nrf52Platform (String path){
//        this();
//        this.setPath(path);
//    }
//
//    public nrf52Platform() {
//        super();
//        mFiles = new ArrayList<>();
//    }
//
//    @Override
//    public boolean allowsLanguage(ConcreteLanguage lang) {
//        return lang instanceof CLang;
//    }
//
//    public void setPath(String path) {
//        mBuildPath = path;
//        mBuildPathApi += mBuildPath + "/api/";
//
//    }
//    public void addSourceBoot(String name, Interface src){
//        if(mBoot == null) {
//            this.mBoot = new Boot(mBuildPathApi);
//        }
//        //set boot callback function
//        mFiles.addAll(mBoot.getFiles());
//    }
//
//    /*
//    public void addSourceTimer(String timer_name, Source src){
//        if(tQueue == null) {
//            this.tQueue = new TimerQueue(mBuildPathApi, src.getController());
//
//        }
//        src.setInitCallName(tQueue.innit_name);
//        tQueue.addTimer(src, true);
//    }
//    */
//
//    public void addSourceVibration(String name, Interface src){
//        //TODO: implement sources
//        LOGGER.severe("<<<<< not implemented addSourceVibration >>>>>");
//    }
//    public void addSourceTemperature(String name, Interface src){
//        //TODO: implement sources
//        LOGGER.severe("<<<<< not implemented addSourceTemperature >>>>>");
//    }
//    public void addSourceVoltageIO(String name, Interface src){
//        //TODO: implement sources
//        LOGGER.severe("<<<<< not implemented addSourceVoltageIO >>>>>");
//    }
//    public void addSourceRandom(String name, Interface src) {
//
//    }
//    /*
//    public void  addSourcePeriodicTimer(String name, Source src) {
//        if(tQueue == null) {
//            this.tQueue = new TimerQueue(mBuildPathApi, src.getController());
//        }
//        src.setInitCallName(tQueue.innit_name);
//        tQueue.addTimer(src, true);
//    }
//    */
//    public static boolean providesAPI(String v){
//        return VERSION.contentEquals(v);
//    }
//
//    public Timer getTimer(String timer_name){
//        return tQueue.getTimer(timer_name);
//    }
//    public Boot getBoot(){ return mBoot; }
//
//    public Random getRandom() { return mRandom; }
//
//    @Override
//    public List<FileObject> build(Space s) {
//        mSpace = s;
//        mMainApp = new MainApp(mBuildPath, mSpace);
//        //TODO: add a check first that api provides methods
//        /*
//        for (Source src: s.getSources()){
//            // get the method
//            String n = src.getSinkReference().replace("platform.system.", "");
//            String name = "addSource" + n.substring(0, 1).toUpperCase() + n.substring(1);
//            //also we need to push puch the init methods
//            try {
//                Method addSourceMethod  = nrf52Platform.class.getMethod(name, n.getClass(), src.getClass());
//                addSourceMethod.invoke(this, n, src);
//            } catch (NoSuchMethodException e) {
//                LOGGER.severe("NoSuchMethodException: Can not find method: " + name);
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                LOGGER.severe("InvocationTargetException: Wrong parameters to invoke: " + name);
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                LOGGER.severe("IllegalAccessException: Can not access: " + name);
//                e.printStackTrace();
//            }
//        }
//        */
//        return getFiles();
//    }
//
//    public List<FileObject> getFiles(){
//        mFiles.addAll(mMainApp.getFiles());
//        if(tQueue != null)
//            mFiles.addAll(tQueue.getFiles());
//        mFiles.add(getMakeFile());
//        return mFiles;
//    }
//
//    private FileObject getMakeFile(){
//        //for each created API object
//        //Aggregate all makes to one obj
//        //for make file we need to distinguish local files and SDK files to to
//        //different prefix in the build process
//        //TODO: create a nice API list to iterate over
//        RavelAPIObject obj = new RavelAPIObject();
//        if(mMainApp != null){
//            obj.addToMakeIncludePath(mMainApp.getMakeIncludePath());
//            obj.addToMakeObj(mMainApp.getMakeObjects());
//        }
//        if(tQueue != null){
//            obj.addToMakeIncludePath(tQueue.getMakeIncludePath());
//            obj.addToMakeObj(tQueue.getMakeObjects());
//        }
//        if(mRandom != null){
//            obj.addToMakeIncludePath(mRandom.getMakeIncludePath());
//            obj.addToMakeObj(mRandom.getMakeObjects());
//        }
//        if(mBoot != null){
//            obj.addToMakeIncludePath(mBoot.getMakeIncludePath());
//            obj.addToMakeObj(mBoot.getMakeObjects());
//        }
//        //get the make file dependencies
//        STGroup make_tmpl = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/makefile.stg");
//        ST r = make_tmpl.getInstanceOf("make");
//        r.add("components", obj);
//        r.add("space", mSpace);
//        //create and populate file object
//        FileObject fo = new FileObject();
//        fo.setBasePath(mBuildPath);
//        fo.setFileName("Makefile");
//        fo.setContent(r.render());
//        return fo;
//    }
//}

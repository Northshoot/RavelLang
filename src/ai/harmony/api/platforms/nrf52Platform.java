package ai.harmony.api.platforms;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.platforms.RavelAPIObject;
import ai.harmony.api.platforms.nrf52.obj.*;
import ai.harmony.ravel.primitives.Controller;
import ai.harmony.ravel.primitives.Source;
import ai.harmony.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static ai.harmony.api.Settings.BASE_TMPL_PATH;

/**
 * Created by lauril on 9/21/16.
 */
public class nrf52Platform extends ConcretePlatform{
    public final static String BASE_PALTFORM_TMPL_PATH = BASE_TMPL_PATH +"/platforms/nrf52/tmpl";
    public final static String MAKE_SDK_PREFIX = "$(SDK_ROOT)";
    public final static String MAKE_PRJ_PREFIX = "$(PROJ_DIR)";

    public final static String VERSION = "nrf52Platform.v12";
    //provided API objects
    private TimerQueue tQueue=null;
    private Random mRandom=null;
    private Boot mBoot=null;

    //default make data
    private MainApp mMainApp;

    //build files
    List<FileObject> mFiles;
    private String mBuildPath="";
    private String mBuildPathApi="";

    private Space mSpace;

    public nrf52Platform (String path){
        this();
        this.setPath(path);
    }

    public nrf52Platform() {
        super();
        mFiles = new ArrayList<>();
    }


    public void setPath(String path) {
        mBuildPath = path;
        mBuildPathApi += "api/";

    }
    public void addSourceBoot(String name, Source src){
        if(mBoot == null) {
            this.mBoot = new Boot(mBuildPathApi);
        }
        //set boot callback function
        mFiles.addAll(mBoot.getFiles());
    }

    public void addSourceTimer(String timer_name, Source src){
        if(tQueue == null) {
            this.tQueue = new TimerQueue(mBuildPathApi);

        }
        src.setInitCallName(tQueue.innit_name);
        tQueue.addTimer(timer_name, true);
    }

    public void addSourceRandom(String name, Source src) {

    }

    public static boolean providesAPI(String v){
        return VERSION.contentEquals(v);
    }

    public Timer getTimer(String timer_name){
        return tQueue.getTimer(timer_name);
    }
    public Boot getBoot(){ return mBoot; }

    public Random getRandom() { return mRandom; }

    public List<FileObject> build(Space s){
        mSpace = s;
        setPath(mSpace.getBuildPath());
        mMainApp = new MainApp(mBuildPath, mSpace);
        //TODO: add a check first that api provides methods
        for(Source src: s.getSources()){
            // get the method
            String n = src.getSinkReference().replace("platform.system.", "");
            String name = "addSource" + n.substring(0, 1).toUpperCase() + n.substring(1);
            //also we need to push puch the init methods
            try {
                Method addSourceMethod  = nrf52Platform.class.getMethod(name, name.getClass(), src.getClass());
                addSourceMethod.invoke(this, name, src);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return getFiles();
    }

    public List<FileObject> getFiles(){
        mFiles.addAll(mMainApp.getFiles());
        mFiles.addAll(this.tQueue.getFiles());
        mFiles.add(getMakeFile());
        return mFiles;
    }

    private FileObject getMakeFile(){
        //for each created API object
        //Aggregate all makes to one obj
        //for make file we need to distinguish local files and SDK files to to
        //different prefix in the build process
        //TODO: create a nice API list to iterate over
        RavelAPIObject obj = new RavelAPIObject();
        if(mMainApp != null){
            obj.addToMakeIncludePath(mMainApp.getMakeIncludePath());
            obj.addToMakeObj(mMainApp.getMakeObjects());
        }
        if(tQueue != null){
            obj.addToMakeIncludePath(tQueue.getMakeIncludePath());
            obj.addToMakeObj(tQueue.getMakeObjects());
        }
        if(mRandom != null){
            obj.addToMakeIncludePath(mRandom.getMakeIncludePath());
            obj.addToMakeObj(mRandom.getMakeObjects());
        }
        if(mBoot != null){
            obj.addToMakeIncludePath(mBoot.getMakeIncludePath());
            obj.addToMakeObj(mBoot.getMakeObjects());
        }
        //get the make file dependencies
        STGroup make_tmpl = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/makefile.stg");
        ST r = make_tmpl.getInstanceOf("make");
        r.add("components", obj);
        r.add("space", mSpace);
        //create and populate file object
        FileObject fo = new FileObject();
        fo.setPath(mBuildPath);
        fo.setFileName("Makefile");
        fo.setContent(r.render());
        return fo;
    }


    @Override
    public void setAPILevel(String name) {

    }
}

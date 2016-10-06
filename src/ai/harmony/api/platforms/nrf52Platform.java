package ai.harmony.api.platforms;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.platforms.RavelAPIObject;
import ai.harmony.api.platforms.nrf52.obj.*;
import ai.harmony.ravel.primitives.Controller;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 9/21/16.
 */
public class nrf52Platform implements SystemApi{
    public final static String BASE_PALTFORM_TMPL_PATH = "src/ai/harmony/api/platforms/nrf52/tmpl";
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
    private Controller ctr;


    public nrf52Platform (Controller ctr, String path){
        this();
        this.setPath(path);
        this.setController( ctr );
    }

    public nrf52Platform() {
        mFiles = new ArrayList<>();
    }

    public void setController(Controller ctrl){
        this.ctr = ctrl;
    }
    public void setPath(String path) {
        mBuildPath = path;
        mBuildPathApi = mBuildPath+"api/";
        mMainApp = new MainApp(mBuildPath);
    }
    public void addBoot(String name){
        if(mBoot == null) {
            this.mBoot = new Boot(mBuildPathApi);
        }
        mFiles.addAll(mBoot.getFiles());
    }
    public void addTimer(String timer_name){
        if(tQueue == null) {
            this.tQueue = new TimerQueue(mBuildPathApi);

        }
        tQueue.addTimer(timer_name, true);
    }

    public static boolean providesAPI(String v){
        return VERSION.contentEquals(v);
    }

    public Timer getTimer(String timer_name){
        return tQueue.getTimer(timer_name);
    }
    public Boot getBoot(){ return mBoot; }

    public Random getRandom() { return mRandom; }


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

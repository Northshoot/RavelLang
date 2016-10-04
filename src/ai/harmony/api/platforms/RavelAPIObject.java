package ai.harmony.api.platforms;

import ai.harmony.api.builder.FileObject;
import ai.harmony.api.lang.c.Declaration;
import ai.harmony.api.lang.c.FuncDeclaration;
import ai.harmony.api.platforms.nrf52.obj.MainApp;
import ai.harmony.ravel.error.NotImplementedException;

import java.text.SimpleDateFormat;
import java.util.*;

import static ai.harmony.api.platforms.nrf52.nrf52Platform.MAKE_PRJ_PREFIX;
import static ai.harmony.api.platforms.nrf52.nrf52Platform.MAKE_SDK_PREFIX;

/**
 * Created by lauril on 9/21/16.
 */
public class RavelAPIObject {
    protected String docs = "Object documentation is not set";
    protected boolean isStandAlone = false;
    protected String mMethodName = "";
    protected String mInitMethodName = "";
    protected String mBuildPath;
    protected FileObject header = new FileObject();
    protected FileObject obj = new FileObject();
    protected List<FileObject> mFileList;
    private MainApp mMainApp = null;
    private Map<String, List<Declaration>> depenencies = new LinkedHashMap();

    public RavelAPIObject(){
        mFileList = new ArrayList<>();
        this.depenencies.put("imports", new ArrayList<>());
        this.depenencies.put("make_object", new ArrayList<>());
        this.depenencies.put("make_include_path", new ArrayList<>());
        this.depenencies.put("defines", new ArrayList<>());
        this.depenencies.put("declarations", new ArrayList<>());
        this.depenencies.put("functions", new ArrayList<>());

    }


    /**
     * Add all the necessary thingies
     * TODO: add to the list only if it does not exist
     * TODO: lists need sorting and clensing
     * right not it can crete multiple addon's  due to cross over dependencies
     * @param val
     */
    public void addToIncludes(Declaration val){
        this.depenencies.get("imports").add(val);
    }
    public void addToDefines(Declaration val){
        this.depenencies.get("defines").add(val);
    }
    public void addToDeclarations(Declaration val) {this.depenencies.get("declarations").add(val);}
    public void addFuncDeclaration(FuncDeclaration obj){
        this.depenencies.get("functions").add(obj);
    }

    public void addToMakeIncludePath(Declaration val){
        this.depenencies.get("make_include_path").add(val);
    }
    public void addToMakeObj(Declaration obj){ this.depenencies.get("make_object").add(obj); }
    //Overload for string and format for project
    public void addToMakeIncludePath(String val){

        this.depenencies.get("make_include_path")
                .add(new Declaration(MAKE_PRJ_PREFIX +val));
    }
    public void addToMakeObj(String obj){ this.depenencies.get("make_object")
            .add(new Declaration(MAKE_PRJ_PREFIX +obj));
    }

    //Overloading with lists
    public void addToMakeIncludePath(List<Declaration> val){
        this.depenencies.get("make_include_path").addAll(val);
    }
    public void addToMakeObj(List<Declaration> obj){ this.depenencies.get("make_object").addAll(obj); }

    //Pass on to the SDK make
    public void addToMakeIncludePathSDK(String val){
        this.addToMakeIncludePath(new Declaration((MAKE_SDK_PREFIX+val)));
    }
    public void addToMakeObjSDK(String obj){
        this.addToMakeObj(new Declaration(MAKE_SDK_PREFIX +obj));
    }

    /**
     * get all the thingies
     * @return
     */
    //TODO: for Makefile fun this need to be aggregated
    public List<Declaration> getMakeObjects(){
        return this.depenencies.get("make_object");
    }
    public List<Declaration> getMakeIncludePath(){
        return this.depenencies.get("make_include_path");
    }

    //This file specific
    public List<Declaration> getImports(){
        return this.depenencies.get("imports");
    }
    public List<Declaration> getDefines(){
        return this.depenencies.get("defines");
    }
    public List<Declaration> getDeclarations(){
        return this.depenencies.get("declarations");
    }
    //trix to fix the casting
    public List<FuncDeclaration> getFuncDeclaration(){
        return  (List<FuncDeclaration>) (Object) this.depenencies.get("functions");
    }

    /**
     * get time current
     * @return
     */
    public String getTimeDate(){
         Date t = Calendar.getInstance().getTime();
        return new SimpleDateFormat("HH:mm:ss").format(t).toString();
    }

    public String getFileName() {
        return null;
    }

    public boolean isStandAlone() {
        return isStandAlone;
    }
    /**
     * kind of platform specific
     * @return
     */
    public String getInitMethodCall(){
        return mInitMethodName+"();";
    }

    public String getHeaderFileName() {
        return null;
    }

    public String getReadMethod() {
        return null;
    }

    public String getReturnType(String functionName) {
        List<FuncDeclaration> flist = (List<FuncDeclaration>) (Object) this.depenencies.get("functions");
        for(FuncDeclaration f: flist) {
            if (f.getName().equals(functionName))
                return f.getReturnType();
        }
        //TODO: should probably through and error here
        return "void";
    }
    public String getInitImplementation() {
        return "";
    }

    public String getDocs(){
        return this.docs;
    }

    public List<FileObject> getFiles() {
        mFileList.add(header);
        mFileList.add(obj);
        return mFileList;
    }

}

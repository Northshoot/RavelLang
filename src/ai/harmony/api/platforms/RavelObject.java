package ai.harmony.api.platforms;

import ai.harmony.api.lang.c.Declaration;
import ai.harmony.ravel.error.NotImplementedException;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lauril on 9/21/16.
 */
public class RavelObject  {
    protected String docs = "Object documentation is not set";
    protected boolean isStandAlone = false;
    protected String mReturnType = "void";
    protected String mMethodName = "";
    protected String mInitMethodName = "";

    private Map<String, List<Declaration>> depenencies = new LinkedHashMap();

    public RavelObject(){
        this.depenencies.put("imports", new ArrayList<>());
        this.depenencies.put("make_object", new ArrayList<>());
        this.depenencies.put("make_include_path", new ArrayList<>());
        this.depenencies.put("defines", new ArrayList<>());
        this.depenencies.put("declarations", new ArrayList<>());
    }

    /**
     * Add all the necessery thingies
     * @param val
     */
    protected void addToInclues(Declaration val){
        this.depenencies.get("includes").add(val);
    }
    protected void addToDefines(Declaration val){
        this.depenencies.get("defines").add(val);
    }
    protected void addToMakeIncludePath(Declaration val){
        this.depenencies.get("make_include_path").add(val);
    }
    protected void addToMakeObj(Declaration obj){ this.depenencies.get("make_object").add(obj); }
    protected void addFuncDeclaration(Declaration obj){
        this.depenencies.get("declarations").add(obj);
    }

    /**
     * get all the thingies
     * @return
     */
    public List<Declaration> getMakeObjects(){
        return this.depenencies.get("make_object");
    }
    public List<Declaration> getMakeIncludePath(){
        return this.depenencies.get("make_include_path");
    }
    public List<Declaration> getImports(){
        return this.depenencies.get("imports");
    }
    public List<Declaration> getDefines(){
        return this.depenencies.get("defines");
    }
    public List<Declaration> getFuncDeclaration(){
        return this.depenencies.get("declarations");
    }

    public String getTimeDate(){
         Date t = Calendar.getInstance().getTime();
        return new SimpleDateFormat("HH:mm:ss").format(t).toString();
    }

    public String getFileName() {
        return null;
    }

    public String getInitMethodName(){
        return mInitMethodName;
    }
    public String getHeaderFileName() {
        return null;
    }

    public String getReadMethod() {
        return null;
    }
    public boolean isStandAlone() {
        return isStandAlone;
    }
    public String getReturnType() {
        return mReturnType;
    }
    public String getInitImplementation() {
        return "";
    }

    public String getDocs(){
        return this.docs;
    }
}

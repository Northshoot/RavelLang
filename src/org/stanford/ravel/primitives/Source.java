package org.stanford.ravel.primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lauril on 8/16/16.
 */
public class Source extends Primitive {

    private String identifier;
    private String resolve;
    private String mInitCallName = null;
    private List<String> mCallBacks = new ArrayList<>();

    public Source(String name, String res) {
        super(name);
        identifier = name;
        resolve = res;
    }

    public InstantiatedSource instantiate(Space space, Map<String, Object> parameters, String varName) {
        InstantiatedSource instantiated = new InstantiatedSource(space, this, varName);
        instantiated.setManyParam(parameters);
        // TODO: check types of parameters
        // TODO: check that all parameters are set
        return instantiated;
    }

    public boolean hasInit(){
        if (mInitCallName == null ){
            return false;
        } else {
            return true;
        }
    }
     public void setInitCallName(String s){
         mInitCallName = s;
     }


    public String getInitMethodCall(){
        return mInitCallName;
    }
        public String getSinkIdentifier() {return this.identifier; }
        public String getSinkReference() {return this.resolve; }

    public void addCallBack(String callbackname){
        mCallBacks.add(callbackname);
    }
    public List<String>  getCallBacks(){
        return mCallBacks;
    }
}

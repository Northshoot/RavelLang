package org.stanford.ravel.primitives;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 8/16/16.
 */
public class Source extends Primitive{

    private String identifier;
    private String resolve;
    private String mInitCallName = null;
    private Controller mController = null;
    private List<String> mCallBacks = new ArrayList<>();

    public Source(String name, String res) {
        super(name);
        identifier = name;
        resolve = res;
    }

    public void source(String name, String resolve){
            this.identifier = name;
            this.resolve = resolve;
        }

    public boolean hasInit(){
        if(mInitCallName == null ){
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

    public void addController(Controller controller) {
        mController = controller;
        mName = controller.getCName()+"__"+mName;
    }

    public Controller getController() {
        return mController;
    }

    public void addCallBack(String callbackname){
        mCallBacks.add(callbackname);
    }
    public List<String>  getCallBacks(){
        return mCallBacks;
    }
}

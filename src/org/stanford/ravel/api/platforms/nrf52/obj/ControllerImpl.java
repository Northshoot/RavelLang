package org.stanford.ravel.api.platforms.nrf52.obj;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.platforms.RavelAPIObject;
import org.stanford.ravel.api.platforms.RavelObjectInterface;
import org.stanford.ravel.primitives.InstantiatedController;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

import static org.stanford.ravel.api.platforms.nrf52Platform.BASE_PALTFORM_TMPL_PATH;

/**
 * Created by lauril on 10/10/16.
 */
public class ControllerImpl extends RavelAPIObject implements RavelObjectInterface {

    private String fileName = "NOT SET";
    private Space mSpace;
    private STGroup mControllerTmlp;
    private InstantiatedController mCtr;
    private MainApp mApp;


    public ControllerImpl(){
        super();
        mControllerTmlp = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/controller.stg");
    }

    public ControllerImpl(InstantiatedController c, MainApp mMainApp) {
        this();
        this.mApp = mMainApp;
        this.mCtr = c;
        fileName = c.getCName();
    }


    public void build(){
        obj.setFileName(getObjFileName());
        obj.setPath(mApp.getBuildPath());
        ST ctrObjTmpl = mControllerTmlp.getInstanceOf("controller_obj_c");
        ctrObjTmpl.add("controller", mCtr);
        obj.setContent(ctrObjTmpl.render());

        ST hrdTmpl = mControllerTmlp.getInstanceOf("controller_header");
        hrdTmpl.add("controller", mCtr);
        header.setPath(mApp.getBuildPath());
        header.setFileName(getHeaderFileName());
        header.setContent(hrdTmpl.render());
        mApp.addToMakeObj(this.getObjFileName());

    }

    @Override
    public List<FileObject> getFiles(){
        this.build();
        return super.getFiles();
    }

    public String getHeaderFileName(){
        return fileName + ".h";
    }
    public String getObjFileName(){
        return fileName + ".c";
    }
    @Override
    public String getHeaderDefName() {
        return null;
    }
}

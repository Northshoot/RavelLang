package org.stanford.ravel.api.platforms.nrf52.obj;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.api.lang.c.Declaration;
import org.stanford.ravel.api.platforms.RavelAPIObject;
import org.stanford.ravel.api.platforms.RavelObjectInterface;
import org.stanford.ravel.primitives.Space;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

import static org.stanford.ravel.api.platforms.nrf52Platform.BASE_PALTFORM_TMPL_PATH;

/**
 * Created by lauril on 10/10/16.
 */
public class RavelLayer extends RavelAPIObject implements RavelObjectInterface {

    private String fileName = "ravel_layer";
    private Space mSpace;
    private STGroup mRavelLayerTmpl;

    public RavelLayer(String bp, Space s){
        super();
        mBuildPath = bp+"/api/";
        mSpace = s;
        mRavelLayerTmpl = new STGroupFile(BASE_PALTFORM_TMPL_PATH+"/ravel_layer.stg");
        addToMakeObj(new Declaration(fileName+".c"));

    }
    public String getHeaderDefName() {
        return null;
    }

    public String getMakeObjName(){return "api/"+fileName+".c";}
    @Override
    public List<FileObject> getFiles() {
        ST t_obj = mRavelLayerTmpl.getInstanceOf("ravel_layer_obj_c");
        ST t_h = mRavelLayerTmpl.getInstanceOf("ravel_layer_header");
        t_obj.add("space", mSpace);
        t_h.add("space", mSpace);
        obj.setBasePath(mBuildPath);
        obj.setFileName(fileName+".c");
        obj.setContent(t_obj.render());

        header.setBasePath(mBuildPath);
        header.setFileName(fileName+".h");
        header.setContent(t_h.render());
        return super.getFiles();
    }
}

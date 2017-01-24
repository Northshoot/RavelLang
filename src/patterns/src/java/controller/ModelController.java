package patterns.src.java.controller;

import patterns.src.java.model.Model;
import patterns.src.java.rrt.Context;

/**
 * Created by lauril on 1/23/17.
 */
public class ModelController extends ModelCtrl {

    private Model mModel;


    public ModelController(Model model){
        this.mModel = model;
        name = "ModelController";
        mModel.setController(this);
    }

    public void arrived(Context ctx){
        System.out.println(ctx);
    }

    public void departed(Context ctx){
        System.out.println(ctx);

    }

    public void full(Context ctx){
        System.out.println(ctx);

    }

    public void save_done(Context ctx){
        System.out.println(ctx);
    }

}

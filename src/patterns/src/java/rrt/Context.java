package patterns.src.java.rrt;

import patterns.src.java.tiers.Error;
import patterns.src.java.model.Model;

import java.time.LocalDateTime;

/**
 * Created by lauril on 1/23/17.
 */
public class Context {

    public Model.Record mRecord;
    public Error mError;
    public LocalDateTime createTime;
    public Model mModel;

    public Context(Model model, Model.Record record, Error error){
        this(model);
        this.mRecord = record;
        this.mError = error;

    }

    public Context(Model model) {
        createTime = LocalDateTime.now();
        this.mModel = model;
    }


}

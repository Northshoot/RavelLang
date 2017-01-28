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


    public Context() {

    }

    @Override
    public String toString() {
        return "[CTX: " + getError() +"]";
    }

    private String getError(){
        switch (this.mError){
            case SUCCESS:
                return "SUCCESS";
            case NETWORK_ERROR:
                return "NETWORK_ERROR";
                
            case WAITING_FOR_NETWORK:
                return "WAITING_FOR_NETWORK";
                
            case WRITE_ERROR:
                return "WRITE_ERROR";
                
            case READ_ERROR:
                return "READ_ERROR";
                
            case OUT_OF_STORAGE:
                return "OUT_OF_STORAGE";
                
            case SYSTEM_ERROR:
                return "SYSTEM_ERROR";
                
            case IN_TRANSIT:
                return "IN_TRANSIT";
                
        }
        return "UNREACHABLE RETURN";
    }
}

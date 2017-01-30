package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.model.ModelCommandAPI;
import org.stanford.ravel.rrt.tiers.Error;
import java.time.LocalDateTime;

/**
 * The runtime context object, for Java-based platforms
 *
 * Created by gcampagn on 1/29/17.
 */
public class Context<RecordType> {
    public RecordType mRecord;
    public Error mError;
    public LocalDateTime createTime;
    public ModelCommandAPI mModel;

    public Context(ModelCommandAPI model, RecordType record, Error error){
        this(model);
        this.mRecord = record;
        this.mError = error;
    }

    public Context(ModelCommandAPI model) {
        createTime = LocalDateTime.now();
        this.mModel = model;
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

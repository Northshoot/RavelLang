package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.model.ModelCommandAPI;
import org.stanford.ravel.rrt.model.ModelRecord;
import org.stanford.ravel.rrt.tiers.Error;
import java.time.LocalDateTime;

/**
 * The runtime context object, for Java-based platforms
 *
 * Created by gcampagn on 1/29/17.
 */
public class Context<RecordType extends ModelRecord> {
    // These fields are accessed directly by Ravel-generated controller code
    // so they must not be renamed
    public RecordType record;
    public Error error;

    // These fields are private and for future use
    private LocalDateTime createTime;
    private ModelCommandAPI mModel;

    public Context(ModelCommandAPI<RecordType> model, RecordType record){
        this(model);
        this.record = record;
    }

    public Context(ModelCommandAPI<RecordType> model, Error error) {
        this(model);
        this.error = error;
    }

    public Context(ModelCommandAPI<RecordType> model) {
        createTime = LocalDateTime.now();
        this.mModel = model;
        this.error = Error.SUCCESS;
    }

    public boolean hasError() {
        return this.error != Error.SUCCESS;
    }

    @Override
    public String toString() {
        return "[CTX: " + getError() +"]";
    }

    private String getError(){
        switch (this.error) {
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

package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.model.ModelCommandAPI;
import org.stanford.ravel.rrt.model.ModelRecord;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

import java.util.Date;

/**
 * The runtime context object, for Java-based platforms
 *
 * Created by gcampagn on 1/29/17.
 */
public class Context<RecordType extends ModelRecord> {
    // These fields are accessed directly by Ravel-generated controller code
    // so they must not be renamed
    public final RecordType record;
    public Endpoint endpoint;
    public final Error error;
    public final ModelCommandAPI model;

    // These fields are private and for future use
    private Date createTime;

    public Context(ModelCommandAPI<RecordType> model, RecordType record){
        createTime = new Date();
        this.model = model;
        this.record = record;
        this.error = Error.SUCCESS;
    }

    public Context(ModelCommandAPI<RecordType> model, RecordType record, Error error) {
        createTime = new Date();
        this.model = model;
        this.record = record;
        this.error = error;
    }

    public Context(ModelCommandAPI<RecordType> model, RecordType record, Error error, Endpoint endpoint) {
        createTime = new Date();
        this.model = model;
        this.record = record;
        this.error = error;
        this.endpoint = endpoint;
    }

    public Context(ModelCommandAPI<RecordType> model, Error error) {
        createTime = new Date();
        this.model = model;
        this.record = null;
        this.error = error;
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

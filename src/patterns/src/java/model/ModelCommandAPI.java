package patterns.src.java.model;

import patterns.src.java.rrt.Context;

/**
 * Created by lauril on 1/23/17.
 */
public interface ModelCommandAPI {

    /**
     * Saves the record and initiates transmission
     * @param rec
     * @return
     */
    Context save(Model.Record rec);

    /**
     * Creates an empty Record
     * @return empty record
     */
    Model.Record create();

    /**
     * Returns deleted record
     * @param recordPos
     * @return context with deleted record
     */
    Context delete(int recordPos);


}

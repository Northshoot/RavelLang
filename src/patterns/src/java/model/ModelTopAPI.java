package patterns.src.java.model;

import patterns.src.java.rrt.Context;

/**
 * Created by lauril on 1/23/17.
 */

/**
 * API that gets implemented by the controller using model
 *
 */
public interface ModelTopAPI {

    void arrived(Context ctx);

    void departed(Context ctx);

    void full(Context ctx);

    void save_done(Context ctx);


}

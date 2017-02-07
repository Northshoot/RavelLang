package org.stanford.ravel.rrt.events;

import org.stanford.ravel.rrt.Context;
import org.stanford.ravel.rrt.model.ModelRecord;

/**
 * Created by lauril on 1/31/17.
 */
public class ModelEvent extends Event {

    public final Event.Type type;
    public final Context<? extends ModelRecord> ctx;

    public ModelEvent(Context<? extends ModelRecord> ctx, Event.Type type){
        this.ctx = ctx;
        this.type = type;
    }

    @Override
    public Type getType() {
        return this.type;
    }
}

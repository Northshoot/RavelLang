package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.model.ModelRecord;

/**
 * Created by lauril on 1/31/17.
 */
public class ModelEvent extends Event {

    public Event.Type type;
    public Context<? extends ModelRecord> ctx;

    public ModelEvent(Context<? extends ModelRecord> ctx, Event.Type type){
        this.ctx = ctx;
        this.type = type;
    }

    @Override
    public Type getType() {
        return this.type;
    }
}

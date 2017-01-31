package org.stanford.ravel.rrt;

import org.stanford.ravel.compiler.types.ModelType;

/**
 * Created by lauril on 1/31/17.
 */
public class ModelEvent extends Event {

    public Event.Type type;
    public Context<ModelType.RecordType> ctx;

    public ModelEvent(Context<ModelType.RecordType> ctx, Event.Type type){
        this.ctx = ctx;
        this.type = type;
    }

    @Override
    public Type getType() {
        return this.type;
    }
}

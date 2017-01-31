package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by lauril on 1/31/17.
 */
public class NetworkEvent extends Event {


    public byte[] data;
    public Endpoint endpoint;
    public Event.Type eventType;
    public Error error;

    public NetworkEvent(byte[] data, Endpoint ed, Event.Type eventType){
        super();
        this.data = data;
        this.endpoint = ed;
        this.eventType = eventType;
    }
    public NetworkEvent(byte[] data, Endpoint ed, Error err, Event.Type eventType){
        this(data, ed, eventType);
        this.error = err;
    }

    @Override
    public Type getType() {
        return this.eventType;
    }
}

package org.stanford.ravel.rrt;

import org.stanford.ravel.rrt.tiers.Endpoint;

/**
 * Created by lauril on 1/31/17.
 */
public class NetworkEvent extends Event {


    public byte[] data;
    public Endpoint endpoint;
    public Event.Type eventType;

    public NetworkEvent(byte[] data, Endpoint ed, Event.Type eventType){
        super();
        this.data = data;
        this.endpoint = ed;
        this.eventType = eventType;
    }

    @Override
    public Type getType() {
        return this.eventType;
    }
}

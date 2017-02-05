package org.stanford.ravel.rrt.events;

import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by lauril on 1/31/17.
 */
public class NetworkEvent extends Event {


    public byte[] data = null;
    public Endpoint endpoint;
    public Event.Type eventType;
    public Error error;
    public RavelPacket pkt = null;

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

    public NetworkEvent(byte[] data, Endpoint ed, Error err, Event.Type eventType, RavelPacket pkt){
        this(data, ed, eventType);
        this.error = err;
        this.pkt = pkt;
    }

    public NetworkEvent(byte[] data, Endpoint ed, Event.Type eventType, RavelPacket pkt){
        this(data, ed, eventType);
        this.pkt = pkt;
    }

    public boolean hasPacket(){
        return this.pkt != null;
    }

    public boolean hasData(){
        return this.data != null;
    }
    @Override
    public Type getType() {
        return this.eventType;
    }
}

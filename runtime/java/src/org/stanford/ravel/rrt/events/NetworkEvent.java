package org.stanford.ravel.rrt.events;

import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.Endpoint;
import org.stanford.ravel.rrt.tiers.Error;

/**
 * Created by lauril on 1/31/17.
 */
public class NetworkEvent extends Event {
    private final Event.Type eventType;
    public final RavelPacket data;
    public final Endpoint endpoint;
    public final Error error;

    public NetworkEvent(RavelPacket data, Endpoint ed, Event.Type eventType) {
        this(data, ed, Error.SUCCESS, eventType);
    }
    public NetworkEvent(RavelPacket data, Endpoint ed, Error err, Event.Type eventType) {
        this.data = data;
        this.endpoint = ed;
        this.eventType = eventType;
        this.error = err;
    }

    @Override
    public Type getType() {
        return this.eventType;
    }
}

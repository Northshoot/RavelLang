package org.stanford.ravel.rrt.tiers;

import org.stanford.ravel.rrt.RavelPacket;

import java.io.Closeable;

/**
 * A something that can send RavelPackets
 *
 * Created by gcampagn on 2/8/17.
 */
public interface RavelSocket extends Closeable {
    void write(RavelPacket pkt) throws RavelIOException;
}

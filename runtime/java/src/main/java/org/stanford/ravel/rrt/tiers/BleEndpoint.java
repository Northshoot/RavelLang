package org.stanford.ravel.rrt.tiers;

/**
 * A BLE endpoint, as seen from the device
 *
 * Created by gcampagn on 2/8/17.
 */
import org.stanford.ravel.rrt.tiers.RavelIdentity;

public class BleEndpoint extends Endpoint {
    public BleEndpoint(int id) {
        super(TYPE.BLE, RavelIdentity.makeRemoteIdentity(id));
    }
}

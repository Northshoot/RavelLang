package org.stanford.ravel.rrt.tiers;

/**
 * Created by lauril on 1/23/17.
 */
public enum Error {
    SUCCESS,
    NETWORK_ERROR,
    WAITING_FOR_NETWORK,
    WRITE_ERROR,
    READ_ERROR,
    OUT_OF_STORAGE,
    SYSTEM_ERROR, IN_TRANSIT, ENDPOINT_UNREACHABLE, SECURITY_ERROR;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

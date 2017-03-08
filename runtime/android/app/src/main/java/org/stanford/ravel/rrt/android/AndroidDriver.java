package org.stanford.ravel.rrt.android;

import android.content.Context;
import org.stanford.ravel.rrt.DispatcherAPI;
import org.stanford.ravel.rrt.RavelPacket;
import org.stanford.ravel.rrt.tiers.*;

/**
 * Created by gcampagn on 2/6/17.
 */
public class AndroidDriver extends JavaDriver {
    private final Context ctx;
    public AndroidDriver(DispatcherAPI dispatcher, Context ctx) {
        super(dispatcher);
        this.ctx = ctx;
    }

    protected JavaDurableStorage createStorage() {
        return new JavaDurableStorage(ctx.getDir("storage", Context.MODE_PRIVATE));
    }

    @Override
    protected void sendDataThread(RavelPacket data, Endpoint endpoint) throws RavelIOException {
        switch (endpoint.getType()) {
            case BLE:
                // send to a ble device
                // TODO
                break;
            case GCM:
                // send to the cloud using GCM
                // TODO
                break;

            default:
                super.sendDataThread(data, endpoint);
        }
    }

    @Override
    protected Error startLocalEndpoint(Endpoint endpoint) {
        switch (endpoint.getType()) {
            case BLE:
                // open ble listening socket
                // TODO
                break;
            case GCM:
                // prepare connection from GCM
                // TODO
                break;

            default:
                return super.startLocalEndpoint(endpoint);
        }

        return Error.SUCCESS;
    }

    @Override
    protected Error startRemoteEndpoint(Endpoint endpoint) {
        switch (endpoint.getType()) {
            case BLE:
                // prepare connection to ble device
                // TODO
                break;
            case GCM:
                // prepare connection to GCM
                // TODO
                break;

            default:
                return super.startRemoteEndpoint(endpoint);
        }

        return Error.SUCCESS;
    }
}

package org.stanford.ravel.rrt.android.ble;

import java.util.UUID;

/**
 * Most of the defines here are static and used by Ravel application to enable data sync
 *
 * Created by lauril on 10/30/15.
 */
public class BleDefines {

    /**
     * BLE control defines
     */

    public final static long SCAN_PERIOD = 1000; //scanning for 10 seconds
    public final static long BLE_RECONNECT_TIME = 10000;
    public final static String SPACE_NAME = "RavelRAD";
    /**
     * BLE intent action filters
     */

    public final static int ACTION_GATT_CONNECTED =81;
    public final static int ACTION_GATT_DISCONNECTED =82;
    public final static int ACTION_GATT_SERVICES_DISCOVERED =83;
    public final static int ACTION_DATA_AVAILABLE =84;

    public final static int DEVICE_DOES_NOT_SUPPORT_RAD =85;

    public static final String INTENT_BLE_FILTER =
            "org.stanford.ravel.rrt.android.ble.RavelBelService.BLE_ACTION";

    public final static String EXTRA_DATA = "DATA";
    public final static String COMMAND = "COMMAND";

    public static final UUID CLIENT_CHARACTERISTIC_CONFIG =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
}

package edu.stanford.ravel.model.ledstatus;

import android.net.Uri;
import android.provider.BaseColumns;

import edu.stanford.ravel.model.LedModelContent;
import edu.stanford.ravel.model.ledstatus.LedstatusColumns;

/**
 * Reliable LedStatus.
 */
public class LedstatusColumns implements BaseColumns {
    public static final String TABLE_NAME = "ledstatus";
    public static final Uri CONTENT_URI = Uri.parse(LedModelContent.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String LED_STATUS = "led_status";

    public static final String IOT_DEVICE = "iot_device";

    public static final String ORIGIN = "origin";

    public static final String TIME_STAMP_RX_GATEWAY = "time_stamp_rx_gateway";

    public static final String ACK_M = "ACK_M";

    public static final String ACK_G = "ACK_G";

    public static final String ACK_C = "ACK_C";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            LED_STATUS,
            IOT_DEVICE,
            ORIGIN,
            TIME_STAMP_RX_GATEWAY,
            ACK_M,
            ACK_G,
            ACK_C
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(LED_STATUS) || c.contains("." + LED_STATUS)) return true;
            if (c.equals(IOT_DEVICE) || c.contains("." + IOT_DEVICE)) return true;
            if (c.equals(ORIGIN) || c.contains("." + ORIGIN)) return true;
            if (c.equals(TIME_STAMP_RX_GATEWAY) || c.contains("." + TIME_STAMP_RX_GATEWAY)) return true;
            if (c.equals(ACK_M) || c.contains("." + ACK_M)) return true;
            if (c.equals(ACK_G) || c.contains("." + ACK_G)) return true;
            if (c.equals(ACK_C) || c.contains("." + ACK_C)) return true;
        }
        return false;
    }

}

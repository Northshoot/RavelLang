package edu.stanford.ravel.model.ledstatus;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import edu.stanford.ravel.model.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code ledstatus} table.
 */
public class LedstatusCursor extends AbstractCursor implements LedstatusModel {
    public LedstatusCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(LedstatusColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code led_status} value.
     * Can be {@code null}.
     */
    @Nullable
    public Integer getLedStatus() {
        Integer res = getIntegerOrNull(LedstatusColumns.LED_STATUS);
        return res;
    }

    /**
     * Get the {@code iot_device} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getIotDevice() {
        String res = getStringOrNull(LedstatusColumns.IOT_DEVICE);
        return res;
    }

    /**
     * Get the {@code origin} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getOrigin() {
        String res = getStringOrNull(LedstatusColumns.ORIGIN);
        return res;
    }

    /**
     * Get the {@code time_stamp_rx_gateway} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getTimeStampRxGateway() {
        String res = getStringOrNull(LedstatusColumns.TIME_STAMP_RX_GATEWAY);
        return res;
    }

    /**
     * Get the {@code ack_m} value.
     * Can be {@code null}.
     */
    @Nullable
    public Boolean getAckM() {
        Boolean res = getBooleanOrNull(LedstatusColumns.ACK_M);
        return res;
    }

    /**
     * Get the {@code ack_g} value.
     * Can be {@code null}.
     */
    @Nullable
    public Boolean getAckG() {
        Boolean res = getBooleanOrNull(LedstatusColumns.ACK_G);
        return res;
    }

    /**
     * Get the {@code ack_c} value.
     * Can be {@code null}.
     */
    @Nullable
    public Boolean getAckC() {
        Boolean res = getBooleanOrNull(LedstatusColumns.ACK_C);
        return res;
    }
}

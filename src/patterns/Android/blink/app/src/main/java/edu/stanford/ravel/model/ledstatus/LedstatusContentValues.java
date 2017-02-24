package edu.stanford.ravel.model.ledstatus;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import edu.stanford.ravel.model.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code ledstatus} table.
 */
public class LedstatusContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return LedstatusColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable LedstatusSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable LedstatusSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public LedstatusContentValues putLedStatus(@Nullable Integer value) {
        mContentValues.put(LedstatusColumns.LED_STATUS, value);
        return this;
    }

    public LedstatusContentValues putLedStatusNull() {
        mContentValues.putNull(LedstatusColumns.LED_STATUS);
        return this;
    }

    public LedstatusContentValues putIotDevice(@Nullable String value) {
        mContentValues.put(LedstatusColumns.IOT_DEVICE, value);
        return this;
    }

    public LedstatusContentValues putIotDeviceNull() {
        mContentValues.putNull(LedstatusColumns.IOT_DEVICE);
        return this;
    }

    public LedstatusContentValues putOrigin(@Nullable String value) {
        mContentValues.put(LedstatusColumns.ORIGIN, value);
        return this;
    }

    public LedstatusContentValues putOriginNull() {
        mContentValues.putNull(LedstatusColumns.ORIGIN);
        return this;
    }

    public LedstatusContentValues putTimeStampRxGateway(@Nullable String value) {
        mContentValues.put(LedstatusColumns.TIME_STAMP_RX_GATEWAY, value);
        return this;
    }

    public LedstatusContentValues putTimeStampRxGatewayNull() {
        mContentValues.putNull(LedstatusColumns.TIME_STAMP_RX_GATEWAY);
        return this;
    }

    public LedstatusContentValues putAckM(@Nullable Boolean value) {
        mContentValues.put(LedstatusColumns.ACK_M, value);
        return this;
    }

    public LedstatusContentValues putAckMNull() {
        mContentValues.putNull(LedstatusColumns.ACK_M);
        return this;
    }

    public LedstatusContentValues putAckG(@Nullable Boolean value) {
        mContentValues.put(LedstatusColumns.ACK_G, value);
        return this;
    }

    public LedstatusContentValues putAckGNull() {
        mContentValues.putNull(LedstatusColumns.ACK_G);
        return this;
    }

    public LedstatusContentValues putAckC(@Nullable Boolean value) {
        mContentValues.put(LedstatusColumns.ACK_C, value);
        return this;
    }

    public LedstatusContentValues putAckCNull() {
        mContentValues.putNull(LedstatusColumns.ACK_C);
        return this;
    }
}

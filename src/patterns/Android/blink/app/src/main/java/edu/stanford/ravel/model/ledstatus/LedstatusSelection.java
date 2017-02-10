package edu.stanford.ravel.model.ledstatus;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import edu.stanford.ravel.model.base.AbstractSelection;

/**
 * Selection for the {@code ledstatus} table.
 */
public class LedstatusSelection extends AbstractSelection<LedstatusSelection> {
    @Override
    protected Uri baseUri() {
        return LedstatusColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code LedstatusCursor} object, which is positioned before the first entry, or null.
     */
    public LedstatusCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new LedstatusCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public LedstatusCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code LedstatusCursor} object, which is positioned before the first entry, or null.
     */
    public LedstatusCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new LedstatusCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public LedstatusCursor query(Context context) {
        return query(context, null);
    }


    public LedstatusSelection id(long... value) {
        addEquals("ledstatus." + LedstatusColumns._ID, toObjectArray(value));
        return this;
    }

    public LedstatusSelection idNot(long... value) {
        addNotEquals("ledstatus." + LedstatusColumns._ID, toObjectArray(value));
        return this;
    }

    public LedstatusSelection orderById(boolean desc) {
        orderBy("ledstatus." + LedstatusColumns._ID, desc);
        return this;
    }

    public LedstatusSelection orderById() {
        return orderById(false);
    }

    public LedstatusSelection ledStatus(Integer... value) {
        addEquals(LedstatusColumns.LED_STATUS, value);
        return this;
    }

    public LedstatusSelection ledStatusNot(Integer... value) {
        addNotEquals(LedstatusColumns.LED_STATUS, value);
        return this;
    }

    public LedstatusSelection ledStatusGt(int value) {
        addGreaterThan(LedstatusColumns.LED_STATUS, value);
        return this;
    }

    public LedstatusSelection ledStatusGtEq(int value) {
        addGreaterThanOrEquals(LedstatusColumns.LED_STATUS, value);
        return this;
    }

    public LedstatusSelection ledStatusLt(int value) {
        addLessThan(LedstatusColumns.LED_STATUS, value);
        return this;
    }

    public LedstatusSelection ledStatusLtEq(int value) {
        addLessThanOrEquals(LedstatusColumns.LED_STATUS, value);
        return this;
    }

    public LedstatusSelection orderByLedStatus(boolean desc) {
        orderBy(LedstatusColumns.LED_STATUS, desc);
        return this;
    }

    public LedstatusSelection orderByLedStatus() {
        orderBy(LedstatusColumns.LED_STATUS, false);
        return this;
    }

    public LedstatusSelection iotDevice(String... value) {
        addEquals(LedstatusColumns.IOT_DEVICE, value);
        return this;
    }

    public LedstatusSelection iotDeviceNot(String... value) {
        addNotEquals(LedstatusColumns.IOT_DEVICE, value);
        return this;
    }

    public LedstatusSelection iotDeviceLike(String... value) {
        addLike(LedstatusColumns.IOT_DEVICE, value);
        return this;
    }

    public LedstatusSelection iotDeviceContains(String... value) {
        addContains(LedstatusColumns.IOT_DEVICE, value);
        return this;
    }

    public LedstatusSelection iotDeviceStartsWith(String... value) {
        addStartsWith(LedstatusColumns.IOT_DEVICE, value);
        return this;
    }

    public LedstatusSelection iotDeviceEndsWith(String... value) {
        addEndsWith(LedstatusColumns.IOT_DEVICE, value);
        return this;
    }

    public LedstatusSelection orderByIotDevice(boolean desc) {
        orderBy(LedstatusColumns.IOT_DEVICE, desc);
        return this;
    }

    public LedstatusSelection orderByIotDevice() {
        orderBy(LedstatusColumns.IOT_DEVICE, false);
        return this;
    }

    public LedstatusSelection origin(String... value) {
        addEquals(LedstatusColumns.ORIGIN, value);
        return this;
    }

    public LedstatusSelection originNot(String... value) {
        addNotEquals(LedstatusColumns.ORIGIN, value);
        return this;
    }

    public LedstatusSelection originLike(String... value) {
        addLike(LedstatusColumns.ORIGIN, value);
        return this;
    }

    public LedstatusSelection originContains(String... value) {
        addContains(LedstatusColumns.ORIGIN, value);
        return this;
    }

    public LedstatusSelection originStartsWith(String... value) {
        addStartsWith(LedstatusColumns.ORIGIN, value);
        return this;
    }

    public LedstatusSelection originEndsWith(String... value) {
        addEndsWith(LedstatusColumns.ORIGIN, value);
        return this;
    }

    public LedstatusSelection orderByOrigin(boolean desc) {
        orderBy(LedstatusColumns.ORIGIN, desc);
        return this;
    }

    public LedstatusSelection orderByOrigin() {
        orderBy(LedstatusColumns.ORIGIN, false);
        return this;
    }

    public LedstatusSelection timeStampRxGateway(String... value) {
        addEquals(LedstatusColumns.TIME_STAMP_RX_GATEWAY, value);
        return this;
    }

    public LedstatusSelection timeStampRxGatewayNot(String... value) {
        addNotEquals(LedstatusColumns.TIME_STAMP_RX_GATEWAY, value);
        return this;
    }

    public LedstatusSelection timeStampRxGatewayLike(String... value) {
        addLike(LedstatusColumns.TIME_STAMP_RX_GATEWAY, value);
        return this;
    }

    public LedstatusSelection timeStampRxGatewayContains(String... value) {
        addContains(LedstatusColumns.TIME_STAMP_RX_GATEWAY, value);
        return this;
    }

    public LedstatusSelection timeStampRxGatewayStartsWith(String... value) {
        addStartsWith(LedstatusColumns.TIME_STAMP_RX_GATEWAY, value);
        return this;
    }

    public LedstatusSelection timeStampRxGatewayEndsWith(String... value) {
        addEndsWith(LedstatusColumns.TIME_STAMP_RX_GATEWAY, value);
        return this;
    }

    public LedstatusSelection orderByTimeStampRxGateway(boolean desc) {
        orderBy(LedstatusColumns.TIME_STAMP_RX_GATEWAY, desc);
        return this;
    }

    public LedstatusSelection orderByTimeStampRxGateway() {
        orderBy(LedstatusColumns.TIME_STAMP_RX_GATEWAY, false);
        return this;
    }

    public LedstatusSelection ackM(Boolean value) {
        addEquals(LedstatusColumns.ACK_M, toObjectArray(value));
        return this;
    }

    public LedstatusSelection orderByAckM(boolean desc) {
        orderBy(LedstatusColumns.ACK_M, desc);
        return this;
    }

    public LedstatusSelection orderByAckM() {
        orderBy(LedstatusColumns.ACK_M, false);
        return this;
    }

    public LedstatusSelection ackG(Boolean value) {
        addEquals(LedstatusColumns.ACK_G, toObjectArray(value));
        return this;
    }

    public LedstatusSelection orderByAckG(boolean desc) {
        orderBy(LedstatusColumns.ACK_G, desc);
        return this;
    }

    public LedstatusSelection orderByAckG() {
        orderBy(LedstatusColumns.ACK_G, false);
        return this;
    }

    public LedstatusSelection ackC(Boolean value) {
        addEquals(LedstatusColumns.ACK_C, toObjectArray(value));
        return this;
    }

    public LedstatusSelection orderByAckC(boolean desc) {
        orderBy(LedstatusColumns.ACK_C, desc);
        return this;
    }

    public LedstatusSelection orderByAckC() {
        orderBy(LedstatusColumns.ACK_C, false);
        return this;
    }
}

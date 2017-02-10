package edu.stanford.ravel.model.ledstatus;

import edu.stanford.ravel.model.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Reliable LedStatus.
 */
public interface LedstatusModel extends BaseModel {

    /**
     * Get the {@code led_status} value.
     * Can be {@code null}.
     */
    @Nullable
    Integer getLedStatus();

    /**
     * Get the {@code iot_device} value.
     * Can be {@code null}.
     */
    @Nullable
    String getIotDevice();

    /**
     * Get the {@code origin} value.
     * Can be {@code null}.
     */
    @Nullable
    String getOrigin();

    /**
     * Get the {@code time_stamp_rx_gateway} value.
     * Can be {@code null}.
     */
    @Nullable
    String getTimeStampRxGateway();

    /**
     * Get the {@code ack_m} value.
     * Can be {@code null}.
     */
    @Nullable
    Boolean getAckM();

    /**
     * Get the {@code ack_g} value.
     * Can be {@code null}.
     */
    @Nullable
    Boolean getAckG();

    /**
     * Get the {@code ack_c} value.
     * Can be {@code null}.
     */
    @Nullable
    Boolean getAckC();
}

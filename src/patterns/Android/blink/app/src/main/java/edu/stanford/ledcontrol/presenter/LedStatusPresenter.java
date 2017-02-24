package edu.stanford.ledcontrol.presenter;

import android.content.ContentUris;
import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.util.Log;

import edu.stanford.ledcontrol.model.LedStatusRepresentation;
import edu.stanford.ravel.model.base.AbstractCursor;
import edu.stanford.ravel.model.ledstatus.LedstatusColumns;
import edu.stanford.ravel.model.ledstatus.LedstatusContentValues;
import edu.stanford.ravel.model.ledstatus.LedstatusCursor;
import edu.stanford.ravel.model.ledstatus.LedstatusSelection;

/**
 * Created by lauril on 2/8/16.
 */
public class LedStatusPresenter  {
    private final static String TAG = LedStatusPresenter.class.getSimpleName();
    private Context context;

    public LedStatusPresenter(Context context){
        this.context = context;
    }

    public long insertLedStatus(LedStatusRepresentation ledStatusRepresentation){
        Log.d(TAG, ledStatusRepresentation.toString());
        LedstatusContentValues lscv = new LedstatusContentValues();
        lscv.putLedStatus(ledStatusRepresentation.led_status);
        lscv.putOrigin(ledStatusRepresentation.origin);
        lscv.putTimeStampRxGateway(ledStatusRepresentation.time);
        lscv.putAckG(true);

        Uri uri = lscv.insert(context.getContentResolver());
        long tmp = ContentUris.parseId(uri);
        queryLedStatus();
        return tmp;
    }

    public LedStatusRepresentation getLastLedStatus(){
        LedStatusRepresentation ledStatusRepresentation;
        LedstatusSelection lss = new LedstatusSelection();
        lss.orderByTimeStampRxGateway();
//        LedstatusContentValues lscv = new LedstatusContentValues();
//        lscv.putAckG(true);
//        lscv.update(context.getContentResolver(), lss);

        LedstatusCursor c = lss.query(context.getContentResolver(), LedstatusColumns.ALL_COLUMNS);
        c.moveToLast();
        ledStatusRepresentation = cursorToObject(c);
//        while (c.moveToNext()) {
//            Log.d(TAG, "Device:" + c.getIotDevice() + " origin: " + c.getOrigin() +
//                    " status: " + c.getLedStatus()+ ", time:" + c.getTimeStampRxGateway()) ;
//        }

        Log.d(TAG, "GET LAST: " + ledStatusRepresentation.toString());
        return ledStatusRepresentation;
    }

    private LedStatusRepresentation cursorToObject(AbstractCursor c){
        LedStatusRepresentation l = null;
        try {
            l = new LedStatusRepresentation(
                    ((LedstatusCursor) c).getLedStatus(),
                    ((LedstatusCursor) c).getIotDevice(),
                    ((LedstatusCursor) c).getOrigin()
            );
            c.close();
        }catch (CursorIndexOutOfBoundsException e) {
            Log.d(TAG, "New db nothing to return");
        }
        return l;

    }

    public void queryLedStatus(){
        LedstatusSelection lscv = new LedstatusSelection();

        LedstatusCursor c = lscv.query(context.getContentResolver(), LedstatusColumns.ALL_COLUMNS);
        while (c.moveToNext()) {
            Log.d(TAG, "Device:" + c.getIotDevice() + " origin: " + c.getOrigin() +
                    " status: " + c.getLedStatus()+ ", time:" + c.getTimeStampRxGateway()) ;
        }
        c.close();
    }


}

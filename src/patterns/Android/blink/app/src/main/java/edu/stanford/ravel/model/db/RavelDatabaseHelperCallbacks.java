package edu.stanford.ravel.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.stanford.ledcontrol.BuildConfig;

/**
 * Implement your custom database creation or upgrade code here.
 *
 * This file will not be overwritten if you re-run the content provider generator.
 */
public class RavelDatabaseHelperCallbacks {
    private static final String TAG = RavelDatabaseHelperCallbacks.class.getSimpleName();
    Handler.Callback mCallbackonPostCreate = null;

    public void onOpen(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onOpen");
        // Insert your db open code here.
    }

    public void onPreCreate(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onPreCreate");
        // Insert your db creation code here. This is called before your tables are created.
    }

    public void onPostCreate(final Context context, final SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onPostCreate");
        if (mCallbackonPostCreate != null){
            sendCallback(mCallbackonPostCreate, null);
        }
        // Insert your db creation code here. This is called after your tables are created.
    }

    public void onUpgrade(final Context context, final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // Insert your upgrading code here.
    }

    public void setCallBackForDone(Handler.Callback callback) {
        this.mCallbackonPostCreate = callback;
    }

    private void sendCallback(Handler.Callback callback, Message message){
        Handler handler = new Handler(callback);
        handler.sendMessage(message);
    }
}

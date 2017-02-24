package edu.stanford.ravel.model.db;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import edu.stanford.ledcontrol.BuildConfig;
import edu.stanford.ravel.model.ledstatus.LedstatusColumns;

public class RavelDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = RavelDatabaseHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "ravel.db";
    private static final int DATABASE_VERSION = 1;
    private static RavelDatabaseHelper sInstance;
    private final Context mContext;
    private final RavelDatabaseHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_LEDSTATUS = "CREATE TABLE IF NOT EXISTS "
            + LedstatusColumns.TABLE_NAME + " ( "
            + LedstatusColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LedstatusColumns.LED_STATUS + " INTEGER, "
            + LedstatusColumns.IOT_DEVICE + " TEXT, "
            + LedstatusColumns.ORIGIN + " TEXT, "
            + LedstatusColumns.TIME_STAMP_RX_GATEWAY + " TEXT, "
            + LedstatusColumns.ACK_M + " INTEGER, "
            + LedstatusColumns.ACK_G + " INTEGER, "
            + LedstatusColumns.ACK_C + " INTEGER "
            + " );";

    // @formatter:on

    public static RavelDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static RavelDatabaseHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static RavelDatabaseHelper newInstancePreHoneycomb(Context context) {
        return new RavelDatabaseHelper(context);
    }

    private RavelDatabaseHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new RavelDatabaseHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static RavelDatabaseHelper newInstancePostHoneycomb(Context context) {
        return new RavelDatabaseHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private RavelDatabaseHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new RavelDatabaseHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_LEDSTATUS);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }


}

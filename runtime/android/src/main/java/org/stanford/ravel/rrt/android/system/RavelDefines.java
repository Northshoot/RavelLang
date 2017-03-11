package org.stanford.ravel.rrt.android.system;


import org.stanford.ravel.rrt.android.utils.PhoneID;

/**
 * Created by lauril on 10/30/15.
 */
public class RavelDefines {


    public final static String PHONE_ID = PhoneID.getUniquePseudoID();
    public static final int CONNECTION_MGC = 21000;
    public static final int CONNECTION_MG = 21001;
    public static final int CONNECTION_MC = 21002;
    public static final int CONNECTION_M = 21003;
    public static final int CONNECTION_C = 21004;
    public static final int CONNECTION_G = 21005;
    public static final int CONNECTION_GC = 21006;

    public static final String EMBEDDED = "M";
    public static final String GATEWAY = "G";
    public static final String CLOUD = "C";

    public static final int SEND_DONE_SUCCESS = 780001;
    public static final int SEND_DONE_ERROR = 780002;

    //needed for requesting permissions, used as callbacks

    public static final int RAVEL_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH =31003;
    public static final int RAVEL_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH_ADMIN=31004;
    public static final int RAVEL_PERMISSIONS_REQUEST_ACCESS_INTERNET=31005;
    public static final int RAVEL_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =31000;
    public static final int RAVEL_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION =31001;
    public static final int RAVEL_PERMISSIONS_REQUEST_ACCESS_bluetooth_le =31002;
    public static final int RAVEL_PERMISSIONS_REQUEST_ACCESS_READ_PHONE_STATE=31006;


}

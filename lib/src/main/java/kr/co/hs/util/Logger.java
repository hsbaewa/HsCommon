package kr.co.hs.util;

import android.util.Log;

/**
 * Created by Bae on 2016-12-21.
 */
public class Logger {
    private static final String COMMON_LOG_TAG = "HsCommonLogging";



    public static final void d(String message, Throwable t){
        d(COMMON_LOG_TAG, message, t);
    }
    public static final void d(String message){
        d(COMMON_LOG_TAG, message, null);
    }
    public static final void d(String tag, String message){
        d(tag, message, null);
    }
    public static final void d(String tag, String message, Throwable t){
        if(t == null)
            Log.d(tag, message);
        else
            Log.d(tag, message, t);
    }
}

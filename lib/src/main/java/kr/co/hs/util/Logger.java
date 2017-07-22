package kr.co.hs.util;

import android.util.Log;

/**
 * Created by Bae on 2016-12-21.
 */
public class Logger {
    private static final String COMMON_LOG_TAG = "HsCommonLogging";



    public static final void d(String message, Throwable t){
        d(COMMON_LOG_TAG, t, message, null);
    }
    public static final void d(String message){
        d(COMMON_LOG_TAG, null, message, null);
    }
    public static final void d(String tag, String message){
        d(tag, null, message, null);
    }
    public static final void d(String tag, String format, Object... objects){
        d(tag, null, format, objects);
    }
    public static final void d(String tag, String message, Throwable t){
        d(tag, t, message, null);
    }
    public static final void d(String tag, Throwable t, String format, Object... objects){
        String message;
        if(objects == null){
            message = format;
        }else{
            message = String.format(format, objects);
        }
        if(t == null)
            Log.d(tag, message);
        else
            Log.d(tag, message, t);
    }
}

package kr.co.hs.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.PermissionChecker;

import kr.co.hs.app.HsActivity;
import kr.co.hs.app.HsFragment;
import kr.co.hs.app.OnRequestPermissionResult;
import kr.co.hs.app.OnRequestResult;


/**
 * Created by Bae on 2016-11-02.
 */
public class HsPermissionChecker {

    public static final int PERMISSION_GRANTED = PermissionChecker.PERMISSION_GRANTED;
    public static final int PERMISSION_DENIED = PermissionChecker.PERMISSION_DENIED;
    public static final int PERMISSION_DENIED_APP_OP = PermissionChecker.PERMISSION_DENIED_APP_OP;


    public static int checkSelfPermission(Context context, String permission){
        return PermissionChecker.checkSelfPermission(context, permission);
    }

    public static int checkCallingOrSelfPermission(Context context, String permission){
        return PermissionChecker.checkCallingOrSelfPermission(context, permission);
    }

    public static int checkCallingPermission(Context context, String permission, String packageName){
        return PermissionChecker.checkCallingPermission(context, permission, packageName);
    }

    public static int checkPermission(Context context, String permission, int pid, int uid, String packageName){
        return PermissionChecker.checkPermission(context, permission, pid, uid, packageName);
    }


    public static boolean isGranted(Context context, String permission){
        int result = checkSelfPermission(context, permission);
        if(PERMISSION_GRANTED == result)
            return true;
        else
            return false;
    }

    public static boolean isGranted(Context context, String[] permissions){
        for(String permission : permissions){
            if(!isGranted(context, permission)){
                return false;
            }
        }
        return true;
    }

    public static boolean isGrantedNotificationListenerService(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            boolean weHaveNotificationListenerPermission = false;
            for (String service : NotificationManagerCompat.getEnabledListenerPackages(context)) {
                if (service.equals(context.getPackageName()))
                    weHaveNotificationListenerPermission = true;
            }
            return weHaveNotificationListenerPermission;
        }else{
            return false;
        }
    }


    public static void requestPermissions(HsActivity activity, String[] permissions, int requestCode, OnRequestPermissionResult onRequestPermissionResult){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isGranted(activity, permissions)){
            activity.setOnRequestPermissionResult(onRequestPermissionResult);
            activity.requestPermissions(permissions, requestCode);
        }else{
            int[] grantResults = new int[permissions.length];
            for(int i=0;i<grantResults.length;i++){
                grantResults[i] = PERMISSION_GRANTED;
            }
            onRequestPermissionResult.onResult(requestCode, permissions, grantResults, true);
        }
    }
    public static void requestPermissions(HsFragment fragment, String[] permissions, int requestCode, OnRequestPermissionResult onRequestPermissionResult){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isGranted(fragment.getContext(), permissions)){
            fragment.setOnRequestPermissionResult(onRequestPermissionResult);
            fragment.requestPermissions(permissions, requestCode);
        }else{
            int[] grantResults = new int[permissions.length];
            for(int i=0;i<grantResults.length;i++){
                grantResults[i] = PERMISSION_GRANTED;
            }
            onRequestPermissionResult.onResult(requestCode, permissions, grantResults, true);
        }
    }

    public static void requestResult(HsFragment fragment, Intent intent, int requestCode, OnRequestResult onRequestResult){
        fragment.setOnRequestResult(onRequestResult);
        fragment.startActivityForResult(intent, requestCode);
    }
    public static void requestResult(HsActivity activity, Intent intent, int requestCode, OnRequestResult onRequestResult){
        activity.setOnRequestResult(onRequestResult);
        activity.startActivityForResult(intent, requestCode);
    }
}

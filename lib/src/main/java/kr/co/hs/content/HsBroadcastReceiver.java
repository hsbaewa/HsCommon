package kr.co.hs.content;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import kr.co.hs.app.HsActivity;
import kr.co.hs.app.HsApplication;

/**
 * Created by Bae on 2016-12-06.
 */
public abstract class HsBroadcastReceiver extends BroadcastReceiver implements IHsBroadcastReceiver{
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;

        String action;
        if(intent == null || (action = intent.getAction())==null)
            return;

        onActionReceive(context, action, intent);
    }

    protected boolean sendPendingBroadcast(Context context, int requestCode, Intent intent, int flags){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags);
        try {
            pendingIntent.send();
            return true;
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof HsBroadcastReceiver){
            HsBroadcastReceiver receiver = (HsBroadcastReceiver) obj;
            String targetClsName = receiver.getClass().getName();
            String currentClsName = getClass().getName();
            if(targetClsName.equals(currentClsName)){
                return true;
            }else{
                return false;
            }
        }
        return super.equals(obj);
    }

    public Context getContext() {
        return mContext;
    }

    public HsApplication getHsApplication(){
        return (HsApplication) getContext().getApplicationContext();
    }

    @Override
    public HsPreferences getDefaultPreference() {
        HsApplication application = getHsApplication();
        if(application != null)
            return application.getDefaultPreference();
        return null;
    }

    @Override
    public String getDeviceId() {
        HsApplication application = getHsApplication();
        if(application != null)
            return application.getDeviceId();
        return null;
    }

    @Override
    public ArrayList<HsActivity.ActivityStatus> getActivityStatusList() {
        HsApplication application = getHsApplication();
        if(application != null)
            return application.getActivityStatusList();
        return null;
    }

    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent, int flags) {
        HsApplication application = getHsApplication();
        if(application != null)
            return application.sendPendingBroadcast(requestCode, intent, flags);
        return false;
    }

    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent) {
        HsApplication application = getHsApplication();
        if(application != null)
            return application.sendPendingBroadcast(requestCode, intent);
        return false;
    }

    @Override
    public boolean sendPendingBroadcast(Intent intent) {
        HsApplication application = getHsApplication();
        if(application != null)
            return application.sendPendingBroadcast(intent);
        return false;
    }

    @Override
    public String getTopActivity() {
        HsApplication application = getHsApplication();
        if(application != null)
            return application.getTopActivity();
        return null;
    }

    public abstract void onActionReceive(Context context, String action, Intent intent);
}

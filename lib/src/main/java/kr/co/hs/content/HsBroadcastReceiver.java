package kr.co.hs.content;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import kr.co.hs.app.HsActivity;
import kr.co.hs.app.IHsApplication;

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

    public Context getApplicationContext(){
        return getContext().getApplicationContext();
    }

    public IHsApplication getHsApplication(){
        IHsApplication application = (IHsApplication) getApplicationContext();
        return application;
    }

    @Override
    public String getDeviceId() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getDeviceId();
        return null;
    }

    @Override
    public ArrayList<HsActivity.ActivityStatus> getActivityStatusList() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getActivityStatusList();
        return null;
    }

    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent, int flags) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.sendPendingBroadcast(requestCode, intent, flags);
        return false;
    }

    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.sendPendingBroadcast(requestCode, intent);
        return false;
    }

    @Override
    public boolean sendPendingBroadcast(Intent intent) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.sendPendingBroadcast(intent);
        return false;
    }

    @Override
    public String getTopActivity() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getTopActivity();
        return null;
    }

    @Override
    public List<String> getRunningServiceClassName() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getRunningServiceClassName();
        return null;
    }

    @Override
    public boolean isRunningService(Class<?> service) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.isRunningService(service);
        return false;
    }

    @Override
    public int getColorCompat(int resourceId) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getColorCompat(resourceId);
        else
            return 0;
    }

    @Override
    public Drawable getDrawableCompat(int resourceId) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getDrawableCompat(resourceId);
        else
            return null;
    }

    public abstract void onActionReceive(Context context, String action, Intent intent);
}

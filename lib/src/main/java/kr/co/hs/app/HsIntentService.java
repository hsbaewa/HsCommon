package kr.co.hs.app;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import kr.co.hs.content.HsPreferences;

/**
 * Created by Bae on 2016-12-06.
 */
public abstract class HsIntentService extends IntentService implements IHsService{
    private static final String TAG = "HsIntentService";

    private final HsIntentServiceBinder mBinder = new HsIntentServiceBinder();

    public HsIntentService() {
        super(TAG);
    }

    public class HsIntentServiceBinder extends Binder {
        public HsIntentService getService(){
            return HsIntentService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getBinder();
    }

    public HsIntentServiceBinder getBinder(){
        return mBinder;
    }

    public Context getContext(){
        return this;
    }


    protected boolean sendPendingBroadcast(int requestCode, Intent intent, int flags){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, flags);
        try {
            pendingIntent.send();
            return true;
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IHsApplication getHsApplication() {
        IHsApplication application = (IHsApplication) getApplicationContext();
        return application;
    }

    @Override
    public HsPreferences getDefaultPreference() {
        if(getHsApplication() == null){
            try {
                throw new Exception("상위 Application 컴포넌트가 HsApplication이어야 합니다.");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return getHsApplication().getDefaultPreference();
    }

    @Override
    public String getDeviceId() {
        if(getHsApplication() == null){
            try {
                throw new Exception("상위 Application 컴포넌트가 HsApplication이어야 합니다.");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return getHsApplication().getDeviceId();
    }
}

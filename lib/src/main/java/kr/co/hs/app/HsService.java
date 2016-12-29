package kr.co.hs.app;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import kr.co.hs.content.HsPreferences;

/**
 * Created by Bae on 2016-11-23.
 */
public abstract class HsService extends Service implements IHsService{

    private final HsBinder mBinder = new HsBinder();

    public class HsBinder extends Binder{
        HsService getService(){
            return HsService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getBinder();
    }

    public HsBinder getBinder(){
        return mBinder;
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

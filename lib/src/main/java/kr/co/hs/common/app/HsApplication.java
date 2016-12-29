package kr.co.hs.common.app;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import kr.co.hs.common.content.HsPreferences;

/**
 * Created by Bae on 2016-12-23.
 */
public class HsApplication extends Application implements IHsApplication {

    private static final String PREFERENCE_KEY_DEVICE_ID = "HsDeviceID";

    private HsPreferences mPreference;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }


    @Override
    public void init() {
        //default 프리퍼런스 초기화
        mPreference = new HsPreferences(PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    public HsPreferences getDefaultPreference() {
        return mPreference;
    }


    @Override
    public String getDeviceId() {
        String strDeviceID = "";
        try
        {
            TelephonyManager mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            strDeviceID = mTelephonyMgr.getDeviceId();
        }
        catch(Exception e)
        {
        }

        if(strDeviceID != null && !"".equalsIgnoreCase(strDeviceID))
        {
            return strDeviceID;
        }

        strDeviceID = getDefaultPreference().getString(PREFERENCE_KEY_DEVICE_ID, null);

        if(strDeviceID != null && !"".equalsIgnoreCase(strDeviceID))
        {
            return strDeviceID;
        }

        strDeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        getDefaultPreference().set(PREFERENCE_KEY_DEVICE_ID, strDeviceID);

        return strDeviceID;
    }
}

package kr.co.hs.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.List;

import kr.co.hs.content.HsPreferences;

/**
 * Created by Bae on 2016-12-23.
 */
public class HsApplication extends Application implements IHsApplication{

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

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param packageName
     * @param flags
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getPackageManager().getApplicationInfo(packageName, flags);
    }

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param packageName
     * @param flags
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getPackageManager().getPackageInfo(packageName, flags);
    }

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param flags
     * @return
     */
    public List<ApplicationInfo> getInstalledApplications(int flags){
        return getPackageManager().getInstalledApplications(flags);
    }

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param flags
     * @return
     */
    public List<PackageInfo> getInstalledPackages(int flags){
        return getPackageManager().getInstalledPackages(flags);
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

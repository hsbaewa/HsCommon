package kr.co.hs.app;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

import kr.co.hs.content.HsPreferences;

/**
 * Created by Bae on 2016-12-23.
 */
public abstract class HsApplication extends Application implements IHsApplication, IHsPackageManager{

    private static final String PREFERENCE_KEY_DEVICE_ID = "HsDeviceID";

    private HsPreferences mPreference;

    private final ArrayList<HsActivity.ActivityStatus> mActivityStatusList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
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

    public Drawable loadIcon(String packageName) throws PackageManager.NameNotFoundException {
        return getApplicationInfo(packageName, PackageManager.GET_META_DATA).loadIcon(getPackageManager());
    }

    public CharSequence loadLabel(String packageName) throws PackageManager.NameNotFoundException {
        return getApplicationInfo(packageName, PackageManager.GET_META_DATA).loadLabel(getPackageManager());
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

    private Context getContext(){
        return getApplicationContext();
    }

    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent, int flags){
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
    public boolean sendPendingBroadcast(int requestCode, Intent intent){
        return sendPendingBroadcast(requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public boolean sendPendingBroadcast(Intent intent) {
        return sendPendingBroadcast(0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public ArrayList<HsActivity.ActivityStatus> getActivityStatusList() {
        ArrayList<HsActivity.ActivityStatus> result = new ArrayList<>();
        result.addAll(mActivityStatusList);
        return result;
    }

    @Override
    public String getTopActivity() {
        ArrayList<HsActivity.ActivityStatus> list = getActivityStatusList();
        if(list != null && list.size()>0)
            return list.get(0).getClassName();
        return null;
    }

    public int addActivityStatus(HsActivity.ActivityStatus status) {
        synchronized (mActivityStatusList){
            int existIdx = mActivityStatusList.indexOf(status);
            if(existIdx >= 0){
                mActivityStatusList.remove(existIdx);
                mActivityStatusList.add(0, status);
            }else{
                mActivityStatusList.add(0, status);
            }
        }
        return 0;
    }

    public int removeActivityStatus(HsActivity.ActivityStatus status) {
        synchronized (mActivityStatusList){
            int existIdx = mActivityStatusList.indexOf(status);
            if(existIdx >= 0){
                mActivityStatusList.remove(existIdx);
            }
        }
        return 0;
    }
}

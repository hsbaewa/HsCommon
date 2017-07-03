package kr.co.hs.app;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;



/**
 * 생성된 시간 2017-02-20, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.app
 */

public class HsMultiDexApplication extends MultiDexApplication implements IHsApplication, IHsPackageManager{
    private SharedPreferences mPreference;
    private final ArrayList<HsActivity.ActivityStatus> mActivityStatusList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //default 프리퍼런스 초기화
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private SharedPreferences getPreference(){
        return mPreference;
    }

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param packageName
     * @param flags
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getPackageManager().getApplicationInfo(packageName, flags);
    }

    @Override
    public Drawable loadIcon(String packageName) throws PackageManager.NameNotFoundException {
        return getApplicationInfo(packageName, PackageManager.GET_META_DATA).loadIcon(getPackageManager());
    }

    @Override
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
    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getPackageManager().getPackageInfo(packageName, flags);
    }

    @Override
    public PackageInfo getPackageInfo(int flags) throws PackageManager.NameNotFoundException {
        return getPackageInfo(getPackageName(), flags);
    }

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param flags
     * @return
     */
    @Override
    public List<ApplicationInfo> getInstalledApplications(int flags){
        return getPackageManager().getInstalledApplications(flags);
    }

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param flags
     * @return
     */
    @Override
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

        strDeviceID = getPreference().getString(PREFERENCE_KEY_DEVICE_ID, null);

        if(strDeviceID != null && !"".equalsIgnoreCase(strDeviceID))
        {
            return strDeviceID;
        }

        strDeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(PREFERENCE_KEY_DEVICE_ID, strDeviceID);
        editor.commit();

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

    int addActivityStatus(HsActivity.ActivityStatus status) {
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

    int removeActivityStatus(HsActivity.ActivityStatus status) {
        synchronized (mActivityStatusList){
            int existIdx = mActivityStatusList.indexOf(status);
            if(existIdx >= 0){
                mActivityStatusList.remove(existIdx);
            }
        }
        return 0;
    }

    @Override
    public boolean isRunningService(Class<?> service) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo serviceInfo : serviceInfos) {
            if(serviceInfo.service.getClassName().equals(service.getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getRunningServiceClassName() {
        List<String> result = new ArrayList<>();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : serviceInfos) {
            result.add(service.service.getClassName());
        }
        return result;
    }

    @Override
    public int getColorCompat(int resourceId) {
        return ContextCompat.getColor(getContext(), resourceId);
    }

    @Override
    public Drawable getDrawableCompat(int resourceId) {
        return ContextCompat.getDrawable(getContext(), resourceId);
    }

    @Override
    public String getVersionName() {
        try {
            PackageInfo packageInfo = getPackageInfo(PackageManager.GET_META_DATA);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getVersionCode() {
        try {
            PackageInfo packageInfo = getPackageInfo(PackageManager.GET_META_DATA);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean isForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}

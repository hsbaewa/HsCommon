package kr.co.hs.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import kr.co.hs.content.HsBroadcastReceiver;

/**
 * Created by Bae on 2016-12-06.
 */
public abstract class HsIntentService extends IntentService implements IHsService, IHsPackageManager, IHsRegisterBroadcastReceiver{
    private static final String TAG = "HsIntentService";

    private final HsIntentServiceBinder mBinder = new HsIntentServiceBinder();

    //BroadcastReceiver 등록되있는건지 확인 가능한 구조 만들자
    private final ArrayList<HsBroadcastReceiver> mBroadcastReceiverList = new ArrayList<>();

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

    public IHsApplication getHsApplication() {
        IHsApplication application = (IHsApplication) getApplicationContext();
        return application;
    }

    public IHsPackageManager getHsPackageManager(){
        IHsPackageManager packageManager = (IHsPackageManager) getApplicationContext();
        return packageManager;
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

    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getHsPackageManager().getApplicationInfo(packageName, flags);
    }

    @Override
    public Drawable loadIcon(String packageName) throws PackageManager.NameNotFoundException {
        return getHsPackageManager().loadIcon(packageName);
    }

    @Override
    public CharSequence loadLabel(String packageName) throws PackageManager.NameNotFoundException {
        return getHsPackageManager().loadLabel(packageName);
    }

    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getHsPackageManager().getPackageInfo(packageName, flags);
    }

    @Override
    public PackageInfo getPackageInfo(int flags) throws PackageManager.NameNotFoundException {
        return getPackageInfo(getPackageName(), flags);
    }

    @Override
    public List<ApplicationInfo> getInstalledApplications(int flags) {
        return getHsPackageManager().getInstalledApplications(flags);
    }

    @Override
    public List<PackageInfo> getInstalledPackages(int flags) {
        return getHsPackageManager().getInstalledPackages(flags);
    }

    @Override
    public Intent registerReceiver(HsBroadcastReceiver broadcastReceiver, IntentFilter filter) {
        if(mBroadcastReceiverList != null && !mBroadcastReceiverList.contains(broadcastReceiver)){
            mBroadcastReceiverList.add(broadcastReceiver);
        }
        return super.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void unregisterReceiver(HsBroadcastReceiver receiver) {
        if(mBroadcastReceiverList != null && mBroadcastReceiverList.contains(receiver)){
            mBroadcastReceiverList.remove(receiver);
        }
        super.unregisterReceiver(receiver);
    }

    @Override
    public boolean isRegisteredReceiver(HsBroadcastReceiver broadcastReceiver) {
        if(mBroadcastReceiverList != null && mBroadcastReceiverList.contains(broadcastReceiver))
            return true;
        else
            return false;
    }

    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent, int flags){
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.sendPendingBroadcast(requestCode, intent, flags);
        else
            return false;
    }
    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent){
        IHsApplication application = getHsApplication();
        if(application != null){
            return application.sendPendingBroadcast(requestCode, intent);
        }else{
            return false;
        }
    }

    @Override
    public boolean sendPendingBroadcast(Intent intent) {
        IHsApplication application = getHsApplication();
        if(application != null){
            return application.sendPendingBroadcast(intent);
        }else{
            return false;
        }
    }

    @Override
    public String getRemoteClassName(Intent intent) {
        return intent.getStringExtra(EXTRA_REMOTE_CLASS);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra(EXTRA_REMOTE_CLASS, getClass().getName());
        super.startActivity(intent);
    }
    @Override
    public void startActivity(Intent intent, Bundle options) {
        intent.putExtra(EXTRA_REMOTE_CLASS, getClass().getName());
        super.startActivity(intent, options);
    }

    @Override
    public ArrayList<HsActivity.ActivityStatus> getActivityStatusList() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getActivityStatusList();
        return null;
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
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return getResources().getColor(resourceId, getTheme());
            }else{
                return getResources().getColor(resourceId);
            }
        }
    }

    @Override
    public Drawable getDrawableCompat(int resourceId) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getDrawableCompat(resourceId);
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return getResources().getDrawable(resourceId, getTheme());
            }else{
                return getResources().getDrawable(resourceId);
            }
        }
    }

    @Override
    public String getVersionName() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getVersionName();
        else
            return null;
    }

    @Override
    public int getVersionCode() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getVersionCode();
        else
            return -1;
    }

    @Override
    public boolean isForeground() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.isForeground();
        else
            return false;
    }

    @Override
    public String getPlayStoreUrl() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getPlayStoreUrl();
        else
            return null;
    }

    @Override
    public String getPlayStoreUrl(String packageName) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getPlayStoreUrl(packageName);
        else
            return null;
    }

    @Override
    public Uri getPlayStoreUri() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getPlayStoreUri();
        else
            return null;
    }

    @Override
    public Uri getPlayStoreUri(String packageName) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getPlayStoreUri(packageName);
        else
            return null;
    }
}

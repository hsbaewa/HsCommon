package kr.co.hs.app;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import kr.co.hs.content.HsPreferences;

/**
 * Created by Bae on 2016-11-23.
 */
public abstract class HsService extends Service implements IHs, IHsPackageManager{

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

    public HsApplication getHsApplication() {
        HsApplication application = (HsApplication) getApplicationContext();
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

    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getHsApplication().getApplicationInfo(packageName, flags);
    }

    @Override
    public Drawable loadIcon(String packageName) throws PackageManager.NameNotFoundException {
        return getHsApplication().loadIcon(packageName);
    }

    @Override
    public CharSequence loadLabel(String packageName) throws PackageManager.NameNotFoundException {
        return getHsApplication().loadLabel(packageName);
    }

    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getHsApplication().getPackageInfo(packageName, flags);
    }

    @Override
    public List<ApplicationInfo> getInstalledApplications(int flags) {
        return getHsApplication().getInstalledApplications(flags);
    }

    @Override
    public List<PackageInfo> getInstalledPackages(int flags) {
        return getHsApplication().getInstalledPackages(flags);
    }
}

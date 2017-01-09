package kr.co.hs.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * 생성된 시간 2017-01-09, Bae 에 의해 생성됨
 * 프로젝트 이름 : MobileFilter4
 * 패키지명 : kr.co.hs.app
 */

public interface IHsPackageManager {
    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param packageName
     * @param flags
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException;

    /**
     * 패키지의 아이콘
     * @param packageName
     * @return
     */
    Drawable loadIcon(String packageName) throws PackageManager.NameNotFoundException;

    /**
     * 패키지의 라벨
     * @param packageName
     * @return
     */
    CharSequence loadLabel(String packageName) throws PackageManager.NameNotFoundException;

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param packageName
     * @param flags
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException;

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param flags
     * @return
     */
    List<ApplicationInfo> getInstalledApplications(int flags);

    /**
     * 패키지 매니저 관련 depth 줄이기 위하여 추가
     * @param flags
     * @return
     */
    List<PackageInfo> getInstalledPackages(int flags);
}

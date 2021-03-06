package kr.co.hs.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * 생성된 시간 2017-01-12, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.app
 */

public interface IHsComponent extends IHs{
    String EXTRA_REMOTE_CLASS = "kr.co.hs.EXTRA_REMOTE_CLASS";

    String getDeviceId();
    ArrayList<HsActivity.ActivityStatus> getActivityStatusList();
    String getTopActivity();

    boolean sendPendingBroadcast(int requestCode, Intent intent, int flags);
    boolean sendPendingBroadcast(int requestCode, Intent intent);
    boolean sendPendingBroadcast(Intent intent);

    List<String> getRunningServiceClassName();
    boolean isRunningService(Class<?> service);

    //리소스 getter 추가
    int getColorCompat(int resourceId);
    Drawable getDrawableCompat(int resourceId);

    //앱 버전 정보 추가
    String getVersionName();
    int getVersionCode();

    //자신이 포어그라운드인지 백그라운드인지 확인
    boolean isForeground();
}

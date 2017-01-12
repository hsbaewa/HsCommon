package kr.co.hs.app;

import android.content.Intent;

import kr.co.hs.content.HsPreferences;

/**
 * 생성된 시간 2017-01-12, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.app
 */

public interface IHsComponent extends IHs{
    String EXTRA_REMOTE_CLASS = "kr.co.hs.EXTRA_REMOTE_CLASS";

    HsPreferences getDefaultPreference();
    String getDeviceId();

    boolean sendPendingBroadcast(int requestCode, Intent intent, int flags);
    boolean sendPendingBroadcast(int requestCode, Intent intent);
    boolean sendPendingBroadcast(Intent intent);
}

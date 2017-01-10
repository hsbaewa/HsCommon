package kr.co.hs.app;

import android.content.Intent;
import android.content.IntentFilter;

import kr.co.hs.content.HsBroadcastReceiver;

/**
 * 생성된 시간 2017-01-10, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.app
 */

public interface IHsRegisterBroadcastReceiver {
    Intent registerReceiver(HsBroadcastReceiver broadcastReceiver, IntentFilter filter);
    void unregisterReceiver(HsBroadcastReceiver receiver);
    boolean isRegisteredReceiver(HsBroadcastReceiver broadcastReceiver);
}

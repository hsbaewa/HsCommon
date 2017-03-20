package kr.co.hs.app;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;

import kr.co.hs.content.HsPreferences;

/**
 * Created by Bae on 2016-12-23.
 */
public interface IHsFragment {
    HsPreferences getDefaultPreference();
    String getDeviceId();

    Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter);
    void unregisterReceiver(BroadcastReceiver receiver);

    //리소스 getter 추가
    int getColorCompat(int resourceId);
    Drawable getDrawableCompat(int resourceId);
}

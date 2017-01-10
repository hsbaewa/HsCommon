package kr.co.hs.app;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Bae on 2016-12-23.
 */
public interface IHsFragment extends IHs{
    Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter);
    void unregisterReceiver(BroadcastReceiver receiver);
}

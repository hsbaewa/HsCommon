package kr.co.hs.app;

import android.content.Intent;
import android.content.IntentFilter;

import kr.co.hs.content.HsBroadcastReceiver;

/**
 * Created by Bae on 2016-12-23.
 */
public interface IHsFragment extends IHs{
    Intent registerReceiver(HsBroadcastReceiver receiver, IntentFilter filter);
}

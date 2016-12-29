package kr.co.hs.common.content;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Bae on 2016-12-06.
 */
public abstract class HsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action;
        if(intent == null || (action = intent.getAction())==null)
            return;

        onActionReceive(context, action, intent);
    }

    protected boolean sendPendingBroadcast(Context context, int requestCode, Intent intent, int flags){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags);
        try {
            pendingIntent.send();
            return true;
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            return false;
        }
    }

    public abstract void onActionReceive(Context context, String action, Intent intent);
}

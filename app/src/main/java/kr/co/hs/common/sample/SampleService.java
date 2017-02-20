package kr.co.hs.common.sample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import kr.co.hs.app.HsIntentService;
import kr.co.hs.app.HsService;
import kr.co.hs.content.HsBroadcastReceiver;
import kr.co.hs.util.Logger;

/**
 * 생성된 시간 2017-02-16, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.common.sample
 */

public class SampleService extends HsService {
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        for(int i=0;i<10;i++){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Logger.d("asda");
//        }
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        for(int i=0;i<3;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.d("asda");
        }
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("action1");
        filter.addAction("action2");
        registerReceiver(mReceiver, filter);

    }

    private HsBroadcastReceiver mReceiver = new HsBroadcastReceiver() {
        @Override
        public void onActionReceive(Context context, String action, Intent intent) {
            switch (action){

            }
        }
    };
}

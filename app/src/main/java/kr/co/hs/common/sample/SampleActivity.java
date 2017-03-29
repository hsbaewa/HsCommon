package kr.co.hs.common.sample;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.hs.app.HsActivity;
import kr.co.hs.app.HsApplication;
import kr.co.hs.app.OnRequestPermissionResult;
import kr.co.hs.content.HsBroadcastReceiver;
import kr.co.hs.content.HsPermissionChecker;
import kr.co.hs.util.Logger;
import kr.co.hs.util.MmsHelper;
import kr.co.hs.widget.HsFloatingActionButton;

/**
 * Created by Bae on 2016-12-29.
 */
public class SampleActivity extends HsActivity implements View.OnClickListener{

    RecyclerView mRecyclerView;
    HsFloatingActionButton mFloatingActionButton;
    RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mRecyclerAdapter = new RecyclerAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mFloatingActionButton = (HsFloatingActionButton) findViewById(R.id.FloatingActionButton);
        mFloatingActionButton.setBehavior(new HsFloatingActionButton.ScrollAwareFABBehavior());
        mFloatingActionButton.setOnClickListener(this);

        mRecyclerView.setAdapter(mRecyclerAdapter);


        Intent serIntent = new Intent(getContext(), SampleService.class);
        startService(serIntent);

        boolean isrunning = isRunningService(SampleService.class);

//        if(!isrunning){
//            Intent serIntent = new Intent(getContext(), SampleService.class);
//            startService(serIntent);
//        }

//        isrunning = isRunningService(SampleService.class);
//        Logger.d("a");

        String[] permissions = {
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS
        };
        HsPermissionChecker.requestPermissions(this, permissions, 10, new OnRequestPermissionResult() {
            @Override
            public void onResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, boolean isAllGranted) {
                if(isAllGranted){
                    Cursor cursor = getContentResolver().query(Telephony.Mms.CONTENT_URI, null, null, null, null);
                    while(cursor.moveToNext()){
                        long idx = cursor.getLong(cursor.getColumnIndex(Telephony.Mms._ID));
                        int cs = cursor.getInt(cursor.getColumnIndex(Telephony.Mms.SUBJECT_CHARSET));
                        String sub = cursor.getString(cursor.getColumnIndex(Telephony.Mms.SUBJECT));

                        String subject = MmsHelper.getInstance().decodeSubject(sub, cs);
                        String body = MmsHelper.getInstance().getPartText(getContext(), idx);
                        String from = MmsHelper.getInstance().getFromAddress(getContext(), idx);
                        String contact = MmsHelper.getInstance().getFromContact(getContext(), from);

                        Logger.d("a");
                    }
                }
            }
        });



        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_HOME);

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo info : list)
        {
            String strPackageName = info.activityInfo.packageName;
            Logger.d("a");
//            arrStr.add(strPackageName);
        }
        Logger.d("a");
//        for(ResolveInfo info : list)
//        {
//            String strPackageName = info.activityInfo.packageName;
//
//            arrStr.add(strPackageName);
//        }
    }

    IntentFilter filter = new IntentFilter();

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ActivityStatus> list = getActivityStatusList();
        Logger.d("a");

        filter.addAction("action1");
//        registerReceiver(eventReceiver, filter);

        Intent broadcast = new Intent();
        broadcast.setAction("action1");
        sendPendingBroadcast(0, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private HsBroadcastReceiver eventReceiver = new HsBroadcastReceiver() {
        @Override
        public void onActionReceive(Context context, String action, Intent intent) {
            switch (action){
            }
        }
    };

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(getContext(), SecondActivity.class);
//        startActivity(intent);
        boolean isrunning = isRunningService(SampleService.class);
        Logger.d("a");

        String top = getTopActivity();

        filter.addAction("action2");
        Intent broadcast = new Intent();
        broadcast.setAction("action2");
        sendPendingBroadcast(0, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);

    }


    class RecyclerAdapter extends RecyclerView.Adapter<Holder>{

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.viewhloder, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.onBind("asdsadsad"+position);
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public Holder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.TextView);
        }

        public void onBind(String text){
            mTextView.setText(text);
        }
    }
}

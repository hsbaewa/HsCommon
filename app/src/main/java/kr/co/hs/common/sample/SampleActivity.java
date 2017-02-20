package kr.co.hs.common.sample;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.hs.app.HsActivity;
import kr.co.hs.content.HsBroadcastReceiver;
import kr.co.hs.util.Logger;
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

package kr.co.hs.common.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.co.hs.app.HsActivity;
import kr.co.hs.common.sample.R;
import kr.co.hs.widget.HsFloatingActionButton;

/**
 * Created by Bae on 2016-12-29.
 */
public class SampleActivity extends HsActivity {

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

        mRecyclerView.setAdapter(mRecyclerAdapter);
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

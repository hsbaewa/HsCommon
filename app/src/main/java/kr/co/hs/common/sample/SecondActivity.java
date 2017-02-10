package kr.co.hs.common.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import java.util.ArrayList;

import kr.co.hs.app.HsActivity;
import kr.co.hs.util.Logger;

/**
 * 생성된 시간 2017-01-12, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.common.sample
 */

public class SecondActivity extends HsActivity implements View.OnClickListener{

    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.FloatingActionButton);

//        SampleApplication application = (SampleApplication) getApplicationContext();
//        List<HsTaskInfo> list = application.getHsTaskInfo(10);
//        Logger.d("a");

        mFloatingActionButton.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ActivityStatus> list = getActivityStatusList();
        Logger.d("a");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), SampleActivity.class);
        startActivity(intent);
    }
}

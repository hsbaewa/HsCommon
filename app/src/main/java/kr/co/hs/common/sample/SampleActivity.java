package kr.co.hs.common.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;

import kr.co.hs.app.HsActivity;
import kr.co.hs.common.sample.R;

/**
 * Created by Bae on 2016-12-29.
 */
public class SampleActivity extends HsActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }
}

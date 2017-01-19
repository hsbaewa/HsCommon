package kr.co.hs.common.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;

import kr.co.hs.app.HsActivity;
import kr.co.hs.content.HsIntent;
import kr.co.hs.util.Logger;

/**
 * 생성된 시간 2017-01-12, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.common.sample
 */

public class SecondActivity extends HsActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String remoteClsName = getRemoteClassName();

        Logger.d("a");
    }
}

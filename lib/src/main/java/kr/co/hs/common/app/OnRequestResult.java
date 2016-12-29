package kr.co.hs.common.app;

import android.content.Intent;

/**
 * Created by Bae on 2016-12-02.
 */
public interface OnRequestResult {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}

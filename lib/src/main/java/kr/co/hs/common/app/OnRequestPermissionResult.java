package kr.co.hs.common.app;

import android.support.annotation.NonNull;

/**
 * Created by Bae on 2016-11-02.
 */
public interface OnRequestPermissionResult {
    void onResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, boolean isAllGranted);
}

package kr.co.hs.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Bae on 2016-12-02.
 */
public class HsView extends View {
    public HsView(Context context) {
        super(context);
    }

    public HsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}

package kr.co.hs.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Created by privacydev on 2017. 7. 24..
 */

public class KeyboardEventLayout extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener{

    private boolean isKeyboardOpened;
    OnKeyboardStateListener mOnKeyboardStateListener;

    public KeyboardEventLayout(Context context) {
        super(context);

        isKeyboardOpened = isKeyboardOpened();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public KeyboardEventLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        isKeyboardOpened = isKeyboardOpened();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public KeyboardEventLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        isKeyboardOpened = isKeyboardOpened();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        boolean currentKeyboradStatus = isKeyboardOpened();
        if(isKeyboardOpened != currentKeyboradStatus){
            isKeyboardOpened = currentKeyboradStatus;
            onKeyboard(isKeyboardOpened);
        }
    }

    private boolean isKeyboardOpened(){
        Rect r = new Rect();
        getWindowVisibleDisplayFrame(r);

        int screenHeight = getRootView().getHeight();

        // r.bottom is the position above soft keypad or device button.
        // if keypad is shown, the r.bottom is smaller than that before.
        int keypadHeight = screenHeight - r.bottom;

        // 0.15 ratio is perhaps enough to determine keypad height.
        if (keypadHeight > screenHeight * 0.15) {
            // keyboard is opened
            return true;
        }
        else {
            // keyboard is closed
            return false;
        }
    }


    protected void onKeyboard(boolean isOpened){
        if(mOnKeyboardStateListener != null)
            mOnKeyboardStateListener.onKeyboardState(isOpened);
    }

    public void setOnKeyboardStateListener(OnKeyboardStateListener onKeyboardStateListener) {
        mOnKeyboardStateListener = onKeyboardStateListener;
    }

    public interface OnKeyboardStateListener{
        void onKeyboardState(boolean visible);
    }
}

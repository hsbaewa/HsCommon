package kr.co.hs;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Bae on 2016-11-21.
 */
public class HsHandler extends Handler{

    private OnHandleMessage onHandleMessage;

    public HsHandler(OnHandleMessage onHandleMessage) {
        this.onHandleMessage = onHandleMessage;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(onHandleMessage != null)
            onHandleMessage.handleMessage(msg);
    }


    public interface OnHandleMessage{
        boolean handleMessage(Message msg);
    }
}

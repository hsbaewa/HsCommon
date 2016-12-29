package kr.co.hs.common.content;

import android.content.DialogInterface;

import java.io.Serializable;

/**
 * Created by Bae on 2016-12-02.
 */
public interface HsDialogInterface extends DialogInterface, Serializable {

    interface OnClickListener extends DialogInterface.OnClickListener, Serializable{
    }
}

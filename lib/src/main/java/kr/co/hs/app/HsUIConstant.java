package kr.co.hs.app;

import android.os.Bundle;

import kr.co.hs.HsConstant;
import kr.co.hs.content.HsDialogInterface;


/**
 * Created by Bae on 2016-11-24.
 */
public interface HsUIConstant extends HsConstant {

    int HD_DISMISS_DIALOG = 1000;
    int HD_SHOW_ALERT_DIALOG = 1001;
    int HD_SHOW_PROGRESS_DIALOG = 1002;
    String DIALOG_TITLE = "DialogTitle";
    String DIALOG_MESSAGE = "DialogMessage";
    String DIALOG_POSITIVE_CAPTION = "DialogPositiveCaption";
    String DIALOG_NEGATIVE_CAPTION = "DialogNegativeCaption";
    String DIALOG_NEUTRAL_CAPTION = "DialogNeutralCaption";
    String DIALOG_LISTENER = "DialogListener";

    void showAlertDialog(int resTitle, int resMsg, int resPositiveCaption, int resNeutralCaption, int resNegativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int resTitle, int resMessage, int resPositiveCaption, int resNegativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int resTitle, int resMessage, int resPositiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int resMessage, int resPositiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String message, String positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message);
    void showAlertDialog(String message);
    void showProgressDialog(String title, String message);
    void showProgressDialog(String message);
    void dismissDialog();


    /**
     * 핸들러 메시지 관련
     */
    void sendMessage(int what);
    void sendMessage(int what, Object obj);
    void sendMessage(int what, Bundle data);
    void sendMessage(int what, Object obj, Bundle data);

    void sendMessageDelayed(int what, long delayMillis);
}
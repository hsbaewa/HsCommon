package kr.co.hs.app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import kr.co.hs.HsConstant;
import kr.co.hs.content.HsDialogInterface;


/**
 * Created by Bae on 2016-11-24.
 */
public interface HsUIConstant extends HsConstant {

    int HD_DISMISS_DIALOG = Integer.MAX_VALUE;
    int HD_SHOW_ALERT_DIALOG = Integer.MAX_VALUE-1;
    int HD_SHOW_PROGRESS_DIALOG = Integer.MAX_VALUE-2;
    int HD_SHOW_TOAST = Integer.MAX_VALUE-3;

    String DIALOG_TITLE = "DialogTitle";
    String DIALOG_MESSAGE = "DialogMessage";
    String DIALOG_POSITIVE_CAPTION = "DialogPositiveCaption";
    String DIALOG_NEGATIVE_CAPTION = "DialogNegativeCaption";
    String DIALOG_NEUTRAL_CAPTION = "DialogNeutralCaption";
    String DIALOG_LISTENER = "DialogListener";

    String TOAST_MESSAGE = "ToastMessage";
    String TOAST_DURATION = "ToastDuration";

    void showAlertDialog(String message);
    void showAlertDialog(int messageId);

    void showAlertDialog(int titleId, String message);
    void showAlertDialog(String title, int messageId);
    void showAlertDialog(int titleId, int messageId);
    void showAlertDialog(String title, String message);

    void showAlertDialog(int messageId, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String message, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(int messageId, int positiveCaptionId, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int messageId, String positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String message, int positiveCaptionId, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String message, String positiveCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(int title, int message, int positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, String positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, int positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, String positiveCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(String title, int message, int positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, String positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, int positiveCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(int title, int message, int positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, int positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, String positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(int title, String message, int positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, int positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, String positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(String title, int message, int positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, int positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, String positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(String title, String message, int positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, int positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(String title, String message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, String positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(String title, String message, int positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, int positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, int positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, String message, int positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(String title, int message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, String positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, String positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, String positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(int title, String message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, String positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, String positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, String positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(String title, int message, int positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, int positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, int positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(String title, int message, int positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(int title, int message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, String positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, String positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, String positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(int title, String message, int positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, int positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, int positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, String message, int positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);

    void showAlertDialog(int title, int message, int positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, int positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, int positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener);
    void showAlertDialog(int title, int message, int positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener);



    void showProgressDialog(String message);
    void showProgressDialog(int messageId);

    void showProgressDialog(String title, String message);
    void showProgressDialog(int titleId, int messageId);
    void showProgressDialog(int titleId, String message);
    void showProgressDialog(String title, int messageId);

    void dismissDialog();
    void showToast(int resID, int duration);
    void showToast(String message, int duration);


    /**
     * 핸들러 메시지 관련
     */
    void sendMessage(int what);
    void sendMessage(int what, Object obj);
    void sendMessage(int what, Bundle data);
    void sendMessage(int what, Object obj, Bundle data);

    void sendMessageDelayed(int what, long delayMillis);
    void sendMessageDelayed(int what, Object obj, long delayMillis);
    void sendMessageDelayed(int what, Bundle data, long delayMillis);
    void sendMessageDelayed(int what, Object obj, Bundle data, long delayMillis);

    void removeMessage(int what);
    void removeMessage(int what, Object object);
    void removeCallbacks(Runnable r);
    void removeCallbacks(Runnable r, Object token);
    void removeCallbacksAndMessages(Object token);


    //화면 픽셀 사이즈
    int getScreenWidthPixels();
    int getScreenHeightPixels();
    float getDensity();
    int getDensityDpi();
    float getXDpi();
    float getYDpi();
}

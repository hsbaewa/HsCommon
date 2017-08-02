package kr.co.hs.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import kr.co.hs.HsHandler;
import kr.co.hs.R;
import kr.co.hs.content.HsBroadcastReceiver;
import kr.co.hs.content.HsDialogInterface;
import kr.co.hs.content.HsPermissionChecker;


/**
 * Created by Bae on 2016-11-21.
 */
abstract public class HsActivity extends AppCompatActivity implements HsHandler.OnHandleMessage, HsUIConstant, DialogInterface.OnDismissListener, IHsActivity, IHsPackageManager, IHsRegisterBroadcastReceiver{

    private HsHandler mHandler;
    private OnRequestPermissionResult mOnRequestPermissionResult;
    private OnRequestResult mOnRequestResult;

    private Dialog mDialog;

    //BroadcastReceiver 등록되있는건지 확인 가능한 구조 만들자
    private final ArrayList<HsBroadcastReceiver> mBroadcastReceiverList = new ArrayList<>();

    //Activity 상태 이벤트 리스너
    private OnActivityLifeCycleListener mOnActivityLifeCycleListener = null;

    private ActivityStatus mActivityStatus = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //액티비티 상태값 구조체 저장
        if(mActivityStatus == null){
            mActivityStatus = new ActivityStatus(getClass().getName(), STATUS_CREATE, hashCode());
            addActivityStatus(mActivityStatus);
        }
        else
            mActivityStatus.setStatus(STATUS_CREATE);

        super.onCreate(savedInstanceState);

        if(mHandler == null)
            mHandler = new HsHandler(this);

        if(this.mOnActivityLifeCycleListener != null)
            this.mOnActivityLifeCycleListener.onActivityCreateStatus();
    }

    public HsHandler getHandler() {
        if(mHandler == null)
            mHandler = new HsHandler(this);
        return mHandler;
    }

    public Dialog getDialog(){
        return mDialog;
    }

    public void setDialog(Dialog dialog){
        this.mDialog = dialog;
    }

    public boolean isDialogShowing(){
        if(mDialog != null && mDialog.isShowing())
            return true;
        else
            return false;
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case HD_DISMISS_DIALOG:{
                if(isDialogShowing()){
                    mDialog.dismiss();
                    mDialog = null;
                }
                return true;
            }
            case HD_SHOW_ALERT_DIALOG:{
                if(isDialogShowing()){
                    mDialog.dismiss();
                    mDialog = null;
                }
                Bundle data = msg.getData();
                String title = data.getString(DIALOG_TITLE);
                String message = data.getString(DIALOG_MESSAGE);
                String posCaption = data.getString(DIALOG_POSITIVE_CAPTION);
                String negCaption = data.getString(DIALOG_NEGATIVE_CAPTION);
                String neuCaption = data.getString(DIALOG_NEUTRAL_CAPTION);
                final HsDialogInterface.OnClickListener listener = (HsDialogInterface.OnClickListener) data.getSerializable(DIALOG_LISTENER);
                final HsDialogInterface.OnClickListener onClickListener = new HsDialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onClick(dialogInterface, i);
                        mDialog.setOnDismissListener(null);
                        mDialog = null;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                if(title != null)
                    builder.setTitle(title);
                if(message != null)
                    builder.setMessage(message);

                if(posCaption == null)
                    posCaption = getString(R.string.common_ok);

                builder.setPositiveButton(posCaption, (listener != null)?onClickListener:null);

                if(negCaption != null){
                    builder.setNegativeButton(negCaption, onClickListener);
                }

                if(neuCaption != null){
                    builder.setNeutralButton(neuCaption, onClickListener);
                }

                mDialog = builder.create();
                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
//                        mDialog = null;
                        if(mDialog != null){
                            if(listener != null)
                                listener.onClick(dialogInterface, DialogInterface.BUTTON_NEGATIVE);
                            mDialog.setOnDismissListener(null);
                            mDialog = null;
                        }
                    }
                });
                mDialog.show();
                return true;
            }
            case HD_SHOW_PROGRESS_DIALOG:{
                if(isDialogShowing()){
                    mDialog.dismiss();
                    mDialog = null;
                }
                Bundle data = msg.getData();
                String title = data.getString(DIALOG_TITLE);
                String message = data.getString(DIALOG_MESSAGE);
                mDialog = ProgressDialog.show(getContext(), title, message);
                return true;
            }
            case HD_SHOW_TOAST:{
                Bundle data = msg.getData();
                String message = data.getString(TOAST_MESSAGE);
                int duration = data.getInt(TOAST_DURATION, Toast.LENGTH_SHORT);
                if(duration == Toast.LENGTH_LONG)
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                else if(duration == Toast.LENGTH_SHORT)
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void setOnRequestPermissionResult(OnRequestPermissionResult onRequestPermissionResult){
        this.mOnRequestPermissionResult = onRequestPermissionResult;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(this.mOnRequestPermissionResult != null){
            boolean isAllGranted = true;
            for(int grantResult : grantResults){
                if(grantResult != HsPermissionChecker.PERMISSION_GRANTED)
                {
                    isAllGranted = false;
                    break;
                }
            }
            this.mOnRequestPermissionResult.onResult(requestCode, permissions, grantResults, isAllGranted);
        }
    }

    protected Context getContext(){
        return this;
    }


    @Override
    public void sendMessage(int what) {getHandler().sendMessage(getHandler().obtainMessage(what));}

    @Override
    public void sendMessage(int what, Object obj) {getHandler().sendMessage(getHandler().obtainMessage(what, obj));}

    @Override
    public void sendMessage(int what, Bundle data) {
        Message msg = getHandler().obtainMessage(what);
        msg.setData(data);
        getHandler().sendMessage(msg);
    }

    @Override
    public void sendMessage(int what, Object obj, Bundle data) {
        Message msg = getHandler().obtainMessage(what, obj);
        msg.setData(data);
        getHandler().sendMessage(msg);
    }

    @Override
    public void sendMessageDelayed(int what, long delayMillis) {
        getHandler().sendMessageDelayed(getHandler().obtainMessage(what), delayMillis);
    }

    @Override
    public void sendMessageDelayed(int what, Object obj, long delayMillis) {
        getHandler().sendMessageDelayed(getHandler().obtainMessage(what, obj), delayMillis);
    }

    @Override
    public void sendMessageDelayed(int what, Bundle data, long delayMillis) {
        Message msg = getHandler().obtainMessage(what);
        msg.setData(data);
        getHandler().sendMessageDelayed(msg, delayMillis);
    }

    @Override
    public void sendMessageDelayed(int what, Object obj, Bundle data, long delayMillis) {
        Message msg = getHandler().obtainMessage(what, obj);
        msg.setData(data);
        getHandler().sendMessageDelayed(msg, delayMillis);
    }

    @Override
    public void removeMessage(int what) {
        getHandler().removeMessages(what);
    }

    @Override
    public void removeMessage(int what, Object object) {
        getHandler().removeMessages(what, object);
    }


    @Override
    public void removeCallbacks(Runnable r) {
        getHandler().removeCallbacks(r);
    }

    @Override
    public void removeCallbacks(Runnable r, Object token) {
        getHandler().removeCallbacks(r, token);
    }

    @Override
    public void removeCallbacksAndMessages(Object token) {
        getHandler().removeCallbacksAndMessages(token);
    }
    @Override
    public void showAlertDialog(String message) {
        Bundle data = new Bundle();
        data.putString(DIALOG_MESSAGE, message);
        sendMessage(HD_SHOW_ALERT_DIALOG, data);
    }

    @Override
    public void showAlertDialog(int messageId) {
        showAlertDialog(getString(messageId));
    }

    @Override
    public void showAlertDialog(String title, String message) {
        Bundle data = new Bundle();
        data.putString(DIALOG_TITLE, title);
        data.putString(DIALOG_MESSAGE, message);
        sendMessage(HD_SHOW_ALERT_DIALOG, data);
    }

    @Override
    public void showAlertDialog(int titleId, String message) {
        showAlertDialog(getString(titleId), message);
    }

    @Override
    public void showAlertDialog(String title, int messageId) {
        showAlertDialog(title, getString(messageId));
    }

    @Override
    public void showAlertDialog(int titleId, int messageId) {
        showAlertDialog(getString(titleId), getString(messageId));
    }

    @Override
    public void showAlertDialog(int messageId, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(messageId), getString(R.string.common_ok), listener);
    }

    @Override
    public void showAlertDialog(String message, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(message, getString(R.string.common_ok), listener);
    }

    @Override
    public void showAlertDialog(int resTitle, int resMsg, int resPositiveCaption, int resNeutralCaption, int resNegativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(resTitle), getString(resMsg), getString(resPositiveCaption), getString(resNeutralCaption), getString(resNegativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, String message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        Bundle data = new Bundle();
        data.putString(DIALOG_TITLE, title);
        data.putString(DIALOG_MESSAGE, message);
        data.putString(DIALOG_POSITIVE_CAPTION, positiveCaption);
        data.putString(DIALOG_NEUTRAL_CAPTION, neutralCaption);
        data.putString(DIALOG_NEGATIVE_CAPTION, negativeCaption);
        data.putSerializable(DIALOG_LISTENER, listener);
        sendMessage(HD_SHOW_ALERT_DIALOG, data);
    }

    @Override
    public void showAlertDialog(String title, String message, String positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, positiveCaption, neutralCaption, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, String message, String positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, positiveCaption, getString(neutralCaption), negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, String message, String positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, positiveCaption, getString(neutralCaption), getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, String message, int positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, getString(positiveCaption), neutralCaption, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, String message, int positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, getString(positiveCaption), neutralCaption, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, String message, int positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, getString(positiveCaption), getString(neutralCaption), negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, String message, int positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, getString(positiveCaption), getString(neutralCaption), getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, int message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), positiveCaption, neutralCaption, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, int message, String positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), positiveCaption, neutralCaption, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, int message, String positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), positiveCaption, getString(neutralCaption), negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, int message, String positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), positiveCaption, getString(neutralCaption), getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, String message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, positiveCaption, neutralCaption, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, String message, String positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, positiveCaption, neutralCaption, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, String message, String positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, positiveCaption, getString(neutralCaption), negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, String message, String positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, positiveCaption, getString(neutralCaption), getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, int message, int positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), getString(positiveCaption), neutralCaption, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, int message, int positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), getString(positiveCaption), neutralCaption, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, int message, int positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), getString(positiveCaption), getString(neutralCaption), negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, int message, int positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), getString(positiveCaption), getString(neutralCaption), getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, int message, String positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), positiveCaption, neutralCaption, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, int message, String positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), positiveCaption, neutralCaption, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, int message, String positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), positiveCaption, getString(neutralCaption), negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, int message, String positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), positiveCaption, getString(neutralCaption), getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, String message, int positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, getString(positiveCaption), neutralCaption, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, String message, int positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, getString(positiveCaption), neutralCaption, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, String message, int positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, getString(positiveCaption), getString(neutralCaption), negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, String message, int positiveCaption, int neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, getString(positiveCaption), getString(neutralCaption), getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, int message, int positiveCaption, String neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), getString(positiveCaption), neutralCaption, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, int message, int positiveCaption, String neutralCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), getString(positiveCaption), neutralCaption, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, int message, int positiveCaption, int neutralCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), getString(positiveCaption), getString(neutralCaption), negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int resTitle, int resMessage, int resPositiveCaption, int resNegativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(resTitle), getString(resMessage), getString(resPositiveCaption), getString(resNegativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, int message, int positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), getString(positiveCaption), null, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, int message, String positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), positiveCaption, null, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, int message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), positiveCaption, null, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, String message, int positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, getString(positiveCaption), null, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, String message, int positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, getString(positiveCaption), null, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int title, String message, String positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, positiveCaption, null, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, String message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, positiveCaption, null, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, int message, int positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), getString(positiveCaption), null, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, int message, int positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), getString(positiveCaption), null, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, int message, String positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), positiveCaption, null, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, int message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), positiveCaption, null, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, String message, int positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, getString(positiveCaption), null, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, String message, int positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, getString(positiveCaption), null, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(String title, String message, String positiveCaption, int negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, positiveCaption, null, getString(negativeCaption), listener);
    }

    @Override
    public void showAlertDialog(String title, String message, String positiveCaption, String negativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, positiveCaption, null, negativeCaption, listener);
    }

    @Override
    public void showAlertDialog(int resTitle, int resMessage, int resPositiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(resTitle), getString(resMessage), getString(resPositiveCaption), listener);
    }

    @Override
    public void showAlertDialog(int title, int message, String positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), getString(message), positiveCaption, null, null, listener);
    }

    @Override
    public void showAlertDialog(int title, String message, int positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, getString(positiveCaption), null, null, listener);
    }

    @Override
    public void showAlertDialog(int title, String message, String positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(title), message, positiveCaption, null, null, listener);
    }

    @Override
    public void showAlertDialog(String title, int message, int positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), getString(positiveCaption), null, null, listener);
    }

    @Override
    public void showAlertDialog(String title, int message, String positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, getString(message), positiveCaption, null, null, listener);
    }

    @Override
    public void showAlertDialog(String title, String message, int positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, getString(positiveCaption), null, null, listener);
    }

    @Override
    public void showAlertDialog(String title, String message, String positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, positiveCaption, null, null, listener);
    }

    @Override
    public void showAlertDialog(int resMessage, int resPositiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(resMessage), getString(resPositiveCaption), listener);
    }

    @Override
    public void showAlertDialog(int messageId, String positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(null, getString(messageId), positiveCaption, null, null, listener);
    }

    @Override
    public void showAlertDialog(String message, int positiveCaptionId, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(null, message, getString(positiveCaptionId), null, null ,listener);
    }

    @Override
    public void showAlertDialog(String message, String positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(null, message, positiveCaption, null, null, listener);
    }

    @Override
    public void showProgressDialog(String title, String message) {
        Bundle data = new Bundle();
        data.putString(DIALOG_TITLE, title);
        data.putString(DIALOG_MESSAGE, message);
        sendMessage(HD_SHOW_PROGRESS_DIALOG, data);
    }

    @Override
    public void showProgressDialog(int titleId, int messageId) {
        showProgressDialog(getString(titleId), getString(messageId));
    }

    @Override
    public void showProgressDialog(int titleId, String message) {
        showProgressDialog(getString(titleId), message);
    }

    @Override
    public void showProgressDialog(String title, int messageId) {
        showProgressDialog(title, getString(messageId));
    }

    @Override
    public void showProgressDialog(String message) {
        Bundle data = new Bundle();
        data.putString(DIALOG_MESSAGE, message);
        sendMessage(HD_SHOW_PROGRESS_DIALOG, data);
    }

    @Override
    public void showProgressDialog(int messageId) {
        showProgressDialog(getString(messageId));
    }

    @Override
    public void dismissDialog() {
        sendMessage(HD_DISMISS_DIALOG);
    }

    @Override
    public void showToast(int resID, int duration) {
        String strMessage = getString(resID);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST_MESSAGE, strMessage);
        bundle.putInt(TOAST_DURATION, duration);
        sendMessage(HD_SHOW_TOAST, bundle);
    }

    @Override
    public void showToast(String message, int duration) {
        Bundle bundle = new Bundle();
        bundle.putString(TOAST_MESSAGE, message);
        bundle.putInt(TOAST_DURATION, duration);
        sendMessage(HD_SHOW_TOAST, bundle);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mDialog = null;
    }


    public void setOnRequestResult(OnRequestResult onRequestResult) {
        this.mOnRequestResult = onRequestResult;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(this.mOnRequestResult != null){
            this.mOnRequestResult.onActivityResult(requestCode, resultCode, data);
        }
    }

    public IHsApplication getHsApplication() {
        try{
            IHsApplication application = (IHsApplication) getApplicationContext();
            return application;
        }catch (ClassCastException e){
            e.printStackTrace();
            return null;
        }
    }

    public IHsPackageManager getHsPackageManager(){
        IHsPackageManager packageManager = (IHsPackageManager) getApplicationContext();
        return packageManager;
    }

    @Override
    public String getDeviceId() {
        if(getHsApplication() == null){
            try {
                throw new Exception("상위 Application 컴포넌트가 HsApplication이어야 합니다.");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return getHsApplication().getDeviceId();
    }

    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getHsPackageManager().getApplicationInfo(packageName, flags);
    }

    @Override
    public Drawable loadIcon(String packageName) throws PackageManager.NameNotFoundException {
        return getHsPackageManager().loadIcon(packageName);
    }

    @Override
    public CharSequence loadLabel(String packageName) throws PackageManager.NameNotFoundException {
        return getHsPackageManager().loadLabel(packageName);
    }

    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getHsPackageManager().getPackageInfo(packageName, flags);
    }

    @Override
    public PackageInfo getPackageInfo(int flags) throws PackageManager.NameNotFoundException {
        return getPackageInfo(getPackageName(), flags);
    }

    @Override
    public List<ApplicationInfo> getInstalledApplications(int flags) {
        return getHsPackageManager().getInstalledApplications(flags);
    }

    @Override
    public List<PackageInfo> getInstalledPackages(int flags) {
        return getHsPackageManager().getInstalledPackages(flags);
    }

    @Override
    public Intent registerReceiver(HsBroadcastReceiver broadcastReceiver, IntentFilter filter) {
        if(mBroadcastReceiverList != null && !mBroadcastReceiverList.contains(broadcastReceiver)){
            mBroadcastReceiverList.add(broadcastReceiver);
        }
        return super.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void unregisterReceiver(HsBroadcastReceiver receiver) {
        if(mBroadcastReceiverList != null && mBroadcastReceiverList.contains(receiver)){
            mBroadcastReceiverList.remove(receiver);
        }
        super.unregisterReceiver(receiver);
    }

    @Override
    public boolean isRegisteredReceiver(HsBroadcastReceiver broadcastReceiver) {
        if(mBroadcastReceiverList != null && mBroadcastReceiverList.contains(broadcastReceiver))
            return true;
        else
            return false;
    }

    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent, int flags){
        IHsApplication application = getHsApplication();
        if(application != null){
            return application.sendPendingBroadcast(requestCode, intent, flags);
        }else
            return false;
    }
    @Override
    public boolean sendPendingBroadcast(int requestCode, Intent intent){
        IHsApplication application = getHsApplication();
        if(application != null){
            return application.sendPendingBroadcast(requestCode, intent);
        }else{
            return false;
        }
    }

    @Override
    public boolean sendPendingBroadcast(Intent intent) {
        IHsApplication application = getHsApplication();
        if(application != null){
            return application.sendPendingBroadcast(intent);
        }else{
            return false;
        }
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        intent.putExtra(EXTRA_REMOTE_CLASS, getClass().getName());
        super.startActivityFromFragment(fragment, intent, requestCode, options);
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        intent.putExtra(EXTRA_REMOTE_CLASS, getClass().getName());
        super.startActivityFromFragment(fragment, intent, requestCode);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra(EXTRA_REMOTE_CLASS, getClass().getName());
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        intent.putExtra(EXTRA_REMOTE_CLASS, getClass().getName());
        super.startActivity(intent, options);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(EXTRA_REMOTE_CLASS, getClass().getName());
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        intent.putExtra(EXTRA_REMOTE_CLASS, getClass().getName());
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public String getRemoteClassName() {
        return getIntent().getStringExtra(EXTRA_REMOTE_CLASS);
    }

    @Override
    public void onBackPressed() {
        boolean isAllowBackPressed = true;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager != null){
            List<Fragment> fragmentList = fragmentManager.getFragments();
            if(fragmentList != null){
                for(Fragment fragment : fragmentList){
                    if(fragment != null && fragment instanceof HsFragment){
                        HsFragment hsFragment = (HsFragment) fragment;
                        boolean isOnBackPressed = hsFragment.onBackPressed();
                        if(isAllowBackPressed){
                            isAllowBackPressed = isOnBackPressed;
                        }
                    }
                }
            }

            /*
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            if(backStackEntryCount > 0){
                for(int i=backStackEntryCount-1;i>=0;i--){
                    FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
                    Fragment fragment = fragmentManager.findFragmentById(backStackEntry.getId());
                    if(fragment != null && fragment instanceof HsFragment){
                        HsFragment hsFragment = (HsFragment) fragment;
                        boolean isOnBackPressed = hsFragment.onBackPressed();
                        if(isAllowBackPressed){
                            isAllowBackPressed = isOnBackPressed;
                        }
                    }
                }

            }
            */
        }
        if(isAllowBackPressed)
            super.onBackPressed();
    }

    public final void  onBackPressedForce(){
        super.onBackPressed();
    }

    public void setOnActivityLifeCycleListener(OnActivityLifeCycleListener onActivityLifeCycleListener) {
        mOnActivityLifeCycleListener = onActivityLifeCycleListener;
    }

    public OnActivityLifeCycleListener getOnActivityLifeCycleListener() {
        return mOnActivityLifeCycleListener;
    }

    @Override
    protected void onResume() {
        //액티비티 상태값 구조체 저장
        if(mActivityStatus == null){
            mActivityStatus = new ActivityStatus(getClass().getName(), STATUS_RESUME, hashCode());
            addActivityStatus(mActivityStatus);
        }
        else{
            mActivityStatus.setStatus(STATUS_RESUME);
            addActivityStatus(mActivityStatus);
        }

        super.onResume();
        if(this.mOnActivityLifeCycleListener != null)
            this.mOnActivityLifeCycleListener.onActivityResumeStatus();
    }

    @Override
    protected void onPause() {
        //액티비티 상태값 구조체 저장
        if(mActivityStatus == null){
            mActivityStatus = new ActivityStatus(getClass().getName(), STATUS_PAUSE, hashCode());
            addActivityStatus(mActivityStatus);
        }
        else{
            mActivityStatus.setStatus(STATUS_PAUSE);
        }

        super.onPause();
        if(this.mOnActivityLifeCycleListener != null)
            this.mOnActivityLifeCycleListener.onActivityPauseStatus();
    }

    @Override
    protected void onStart() {
        //액티비티 상태값 구조체 저장
        if(mActivityStatus == null){
            mActivityStatus = new ActivityStatus(getClass().getName(), STATUS_START,hashCode());
            addActivityStatus(mActivityStatus);
        }
        else{
            mActivityStatus.setStatus(STATUS_START);
        }

        super.onStart();
        if(this.mOnActivityLifeCycleListener != null)
            this.mOnActivityLifeCycleListener.onActivityStartStatus();
    }

    @Override
    protected void onStop() {
        //액티비티 상태값 구조체 저장
        if(mActivityStatus == null){
            mActivityStatus = new ActivityStatus(getClass().getName(), STATUS_STOP, hashCode());
        }
        else
            mActivityStatus.setStatus(STATUS_STOP);

        super.onStop();
        if(this.mOnActivityLifeCycleListener != null)
            this.mOnActivityLifeCycleListener.onActivityStopStatus();
    }

    @Override
    protected void onDestroy() {
        //액티비티 상태값 구조체 저장
        if(mActivityStatus == null){
            mActivityStatus = new ActivityStatus(getClass().getName(), STATUS_DESTROY, hashCode());
            addActivityStatus(mActivityStatus);
        }
        else{
            mActivityStatus.setStatus(STATUS_DESTROY);
            removeActivityStatus(mActivityStatus);
        }

        super.onDestroy();
        if(this.mOnActivityLifeCycleListener != null)
            this.mOnActivityLifeCycleListener.onActivityDestryStatus();
    }

    @Override
    public ActivityStatus getActivityStatus() {
        return mActivityStatus;
    }

    @Override
    public ArrayList<ActivityStatus> getActivityStatusList() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getActivityStatusList();
        return null;
    }

    @Override
    public int addActivityStatus(ActivityStatus status) {
        IHsApplication application = getHsApplication();
        if(application != null){
            if(application instanceof HsApplication){
                return ((HsApplication) application).addActivityStatus(status);
            }else if(application instanceof HsMultiDexApplication){
                return ((HsMultiDexApplication) application).addActivityStatus(status);
            }else
                return 0;
        }
        return 0;
    }

    @Override
    public int removeActivityStatus(ActivityStatus status) {
        IHsApplication application = getHsApplication();
        if(application != null){
            if(application instanceof HsApplication){
                return ((HsApplication) application).removeActivityStatus(status);
            }else if(application instanceof HsMultiDexApplication){
                return ((HsMultiDexApplication) application).removeActivityStatus(status);
            }else{
                return 0;
            }
        }
        return 0;
    }

    @Override
    public String getTopActivity() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getTopActivity();
        return null;
    }

    @Override
    public List<String> getRunningServiceClassName() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getRunningServiceClassName();
        return null;
    }

    @Override
    public boolean isRunningService(Class<?> service) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.isRunningService(service);
        return false;
    }

    public static class ActivityStatus{
        private int mStatus;
        private final int mHashCode;
        private final String mClassName;

        public ActivityStatus(String className, int status, int hashCode) {
            mClassName = className;
            mStatus = status;
            this.mHashCode = hashCode;
        }

        public int getStatus() {
            return mStatus;
        }

        public void setStatus(int status) {
            mStatus = status;
        }

        public String getClassName() {
            return mClassName;
        }

        public int getHashCode() {
            return mHashCode;
        }

        @Override
        public String toString() {
            return getClassName();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ActivityStatus){
                if(getHashCode() == ((ActivityStatus) obj).getHashCode())
                    return true;
                else
                    return false;
            }
            else if(obj instanceof String){
                String paramClsName = (String) obj;
                if(getClassName() != null && getClassName().equals(paramClsName))
                    return true;
                else
                    return false;
            }
            return super.equals(obj);
        }
    }

    @Override
    public int getColorCompat(int resourceId) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getColorCompat(resourceId);
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return getResources().getColor(resourceId, getTheme());
            }else{
                return getResources().getColor(resourceId);
            }
        }
    }

    @Override
    public Drawable getDrawableCompat(int resourceId) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getDrawableCompat(resourceId);
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return getResources().getDrawable(resourceId, getTheme());
            }else{
                return getResources().getDrawable(resourceId);
            }
        }
    }

    @Override
    public String getVersionName() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getVersionName();
        else
            return null;
    }

    @Override
    public int getVersionCode() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getVersionCode();
        else
            return -1;
    }

    @Override
    public boolean isForeground() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.isForeground();
        else
            return false;
    }

    @Override
    public String getPlayStoreUrl() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getPlayStoreUrl();
        else
            return null;
    }

    @Override
    public String getPlayStoreUrl(String packageName) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getPlayStoreUrl(packageName);
        else
            return null;
    }

    @Override
    public Uri getPlayStoreUri() {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getPlayStoreUri();
        else
            return null;
    }

    @Override
    public Uri getPlayStoreUri(String packageName) {
        IHsApplication application = getHsApplication();
        if(application != null)
            return application.getPlayStoreUri(packageName);
        else
            return null;
    }

    @Override
    public void startPlayStore(String packageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, getPlayStoreUri(packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getPlayStoreUrl(packageName))));
        }
    }

    @Override
    public void startPlayStore() {
        startPlayStore(getPackageName());
    }
}

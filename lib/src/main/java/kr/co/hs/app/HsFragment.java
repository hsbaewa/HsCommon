package kr.co.hs.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.hs.HsHandler;
import kr.co.hs.R;
import kr.co.hs.content.HsBroadcastReceiver;
import kr.co.hs.content.HsDialogInterface;
import kr.co.hs.content.HsPermissionChecker;
import kr.co.hs.content.HsPreferences;


/**
 * Created by Bae on 2016-11-21.
 */
public abstract class HsFragment extends Fragment implements HsUIConstant, HsHandler.OnHandleMessage, DialogInterface.OnDismissListener, IHsPackageManager, IHsFragment, IHs, IHsRegisterBroadcastReceiver{

    private HsHandler handler;
    private View contentView;
    private ViewGroup container;
    private LayoutInflater mLayoutInflater;
    private OnRequestPermissionResult mOnRequestPermissionResult;
    private OnRequestResult mOnRequestResult;

    private Dialog mDialog;

    private int mPagerAdapterPosition;

    //BroadcastReceiver 등록되있는건지 확인 가능한 구조 만들자
    private final ArrayList<HsBroadcastReceiver> mBroadcastReceiverList = new ArrayList<>();

    public HsHandler getHandler() {
        if(handler == null)
            handler = new HsHandler(this);
        return handler;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mLayoutInflater = inflater;
        this.container = container;
        if(this.handler == null)
            this.handler = new HsHandler(this);
        onCreateView(container, savedInstanceState);
        if(contentView == null){
            return super.onCreateView(inflater, container, savedInstanceState);
        }else{
            return contentView;
        }
    }


    protected HsActivity getHsActivity(){
        try{
            HsActivity activity = (HsActivity) getActivity();
            return activity;
        }catch (Exception e){
            return null;
        }
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
                Toast.makeText(getContext(), message, duration).show();
                return true;
            }
        }
        return false;
    }


    @Override
    public void sendMessage(int what, Object obj, Bundle data) {
        Message msg = getHandler().obtainMessage(what, obj);
        msg.setData(data);
        getHandler().sendMessage(msg);
    }

    @Override
    public void sendMessage(int what, Bundle data) {
        Message msg = getHandler().obtainMessage(what);
        msg.setData(data);
        getHandler().sendMessage(msg);
    }

    @Override
    public void sendMessage(int what, Object obj) {
        getHandler().sendMessage(getHandler().obtainMessage(what, obj));
    }

    @Override
    public void sendMessage(int what) {
        getHandler().sendMessage(getHandler().obtainMessage(what));
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
    public void showAlertDialog(String title, String message) {
        Bundle data = new Bundle();
        data.putString(DIALOG_TITLE, title);
        data.putString(DIALOG_MESSAGE, message);
        sendMessage(HD_SHOW_ALERT_DIALOG, data);
    }

    @Override
    public void showAlertDialog(String message) {
        Bundle data = new Bundle();
        data.putString(DIALOG_MESSAGE, message);
        sendMessage(HD_SHOW_ALERT_DIALOG, data);
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
    public void showAlertDialog(int resTitle, int resMessage, int resPositiveCaption, int resNegativeCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(resTitle), getString(resMessage), getString(resPositiveCaption), getString(resNegativeCaption), listener);
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
    public void showAlertDialog(String title, String message, String positiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(title, message, positiveCaption, null, null, listener);
    }

    @Override
    public void showAlertDialog(int resMessage, int resPositiveCaption, HsDialogInterface.OnClickListener listener) {
        showAlertDialog(getString(resMessage), getString(resPositiveCaption), listener);
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
    public void showProgressDialog(String message) {
        Bundle data = new Bundle();
        data.putString(DIALOG_MESSAGE, message);
        sendMessage(HD_SHOW_PROGRESS_DIALOG, data);
    }

    @Override
    public void dismissDialog() {
        sendMessage(HD_DISMISS_DIALOG);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mDialog = null;
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

    public void setOnRequestResult(OnRequestResult onRequestResult){
        this.mOnRequestResult = onRequestResult;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(this.mOnRequestResult != null){
            this.mOnRequestResult.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void setContentView(int layoutId){
        contentView = mLayoutInflater.inflate(layoutId, container, false);
    }

    protected View findViewById(int id){
        if(contentView != null){
            return contentView.findViewById(id);
        }else
            return null;
    }

    public HsApplication getHsApplication() {
        HsApplication application = (HsApplication) getContext().getApplicationContext();
        return application;
    }

    @Override
    public HsPreferences getDefaultPreference() {
        if(getHsApplication() == null){
            try {
                throw new Exception("상위 Application 컴포넌트가 HsApplication이어야 합니다.");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return getHsApplication().getDefaultPreference();
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

    public abstract void onCreateView(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    void setPagerAdapterPosition(int position){
        this.mPagerAdapterPosition = position;
    }
    int getPagerAdapterPosition(){
        return this.mPagerAdapterPosition;
    }

    public PackageManager getPackageManager(){
        return getContext().getPackageManager();
    }

    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getHsApplication().getApplicationInfo(packageName, flags);
    }

    @Override
    public Drawable loadIcon(String packageName) throws PackageManager.NameNotFoundException {
        return getHsApplication().loadIcon(packageName);
    }

    @Override
    public CharSequence loadLabel(String packageName) throws PackageManager.NameNotFoundException {
        return getHsApplication().loadLabel(packageName);
    }

    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return getHsApplication().getPackageInfo(packageName, flags);
    }

    @Override
    public List<ApplicationInfo> getInstalledApplications(int flags) {
        return getHsApplication().getInstalledApplications(flags);
    }

    @Override
    public List<PackageInfo> getInstalledPackages(int flags) {
        return getHsApplication().getInstalledPackages(flags);
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return getContext().registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        getContext().unregisterReceiver(receiver);
    }

    @Override
    public Intent registerReceiver(HsBroadcastReceiver broadcastReceiver, IntentFilter filter) {
        if(mBroadcastReceiverList != null && !mBroadcastReceiverList.contains(broadcastReceiver)){
            mBroadcastReceiverList.add(broadcastReceiver);
        }
        return registerReceiver((BroadcastReceiver) broadcastReceiver, filter);
    }

    @Override
    public void unregisterReceiver(HsBroadcastReceiver receiver) {
        if(mBroadcastReceiverList != null && mBroadcastReceiverList.contains(receiver)){
            mBroadcastReceiverList.remove(receiver);
        }
        unregisterReceiver((BroadcastReceiver) receiver);
    }

    @Override
    public boolean isRegisteredReceiver(HsBroadcastReceiver broadcastReceiver) {
        if(mBroadcastReceiverList != null && mBroadcastReceiverList.contains(broadcastReceiver))
            return true;
        else
            return false;
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

    /**
     * backPress 허용하려면 true, backPress 비허용 하려면 false
     * @return
     */
    public boolean onBackPressed(){
        //기본적으로 true
        return true;
    }
}

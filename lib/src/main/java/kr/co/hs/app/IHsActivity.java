package kr.co.hs.app;

/**
 * Created by Bae on 2016-12-23.
 */
public interface IHsActivity extends IHsComponent{
    int STATUS_CREATE = Integer.MAX_VALUE;
    int STATUS_RESUME = Integer.MAX_VALUE-1;
    int STATUS_START = Integer.MAX_VALUE-2;
    int STATUS_PAUSE = (Integer.MAX_VALUE/2);
    int STATUS_STOP = (Integer.MAX_VALUE/2)-1;
    int STATUS_DESTROY = (Integer.MAX_VALUE/2)-2;

    String getRemoteClassName();
    HsActivity.ActivityStatus getActivityStatus();
    int addActivityStatus(HsActivity.ActivityStatus status);
    int removeActivityStatus(HsActivity.ActivityStatus status);

    void startPlayStore(String packageName);
    void startPlayStore();
}

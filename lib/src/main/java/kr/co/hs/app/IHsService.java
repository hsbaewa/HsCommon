package kr.co.hs.app;

import android.content.Intent;

/**
 * Created by Bae on 2016-12-23.
 */
public interface IHsService extends IHsComponent{
    String getRemoteClassName(Intent intent);
}

package kr.co.hs.common.app;

import kr.co.hs.common.content.HsPreferences;

/**
 * Created by Bae on 2016-12-23.
 */
public interface IHs {
    HsPreferences getDefaultPreference();
    String getDeviceId();
}

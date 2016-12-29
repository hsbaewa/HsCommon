package kr.co.hs.app;

import kr.co.hs.content.HsPreferences;

/**
 * Created by Bae on 2016-12-23.
 */
public interface IHs {
    HsPreferences getDefaultPreference();
    String getDeviceId();
}

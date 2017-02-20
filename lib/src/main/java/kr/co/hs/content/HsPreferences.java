package kr.co.hs.content;

import android.content.SharedPreferences;

import kr.co.hs.content.advancedpreference.AdvancedPreference;

/**
 * Created by Bae on 2016-11-24.
 */
@Deprecated
public class HsPreferences extends AdvancedPreference{
    public HsPreferences(SharedPreferences mSharedPreferences) {
        super(mSharedPreferences);
    }
}

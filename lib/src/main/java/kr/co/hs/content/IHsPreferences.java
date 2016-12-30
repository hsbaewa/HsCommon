package kr.co.hs.content;

import java.util.Map;
import java.util.Set;

/**
 * Created by Bae on 2016-11-24.
 */
public interface IHsPreferences {
    void set(String key, String value);
    void set(String key, int value);
    void set(String key, long value);
    void set(String key, boolean value);
    void set(String key, float value);
    void set(String key, Set<String> value);

    boolean commit();
    String getString(String key, String def);
    int getInt(String key, int def);
    long getLong(String key, long def);
    boolean getBoolean(String key, boolean def);
    float getFloat(String key, float def);
    Set<String> getStringSet(String key, Set<String> def);

    Map<String,?> getAll();
    void syncCache();
    void clearCache();
    Map<String,Object> getCacheDataMap();
}

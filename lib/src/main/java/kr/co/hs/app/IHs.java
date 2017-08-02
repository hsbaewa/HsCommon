package kr.co.hs.app;


import android.net.Uri;

/**
 * Created by Bae on 2016-12-23.
 */
public interface IHs {
    String URI_PLAY_STORE_FORMAT = "market://details?id=%s";
    String URL_PLAY_STORE_FORMAT = "https://play.google.com/store/apps/details?id=%s";

    Uri getPlayStoreUri();
    Uri getPlayStoreUri(String packageName);
    String getPlayStoreUrl();
    String getPlayStoreUrl(String packageName);
}

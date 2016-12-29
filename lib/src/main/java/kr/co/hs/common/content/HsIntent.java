package kr.co.hs.common.content;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Bae on 2016-12-25.
 */

public class HsIntent extends Intent{
    public static final String ACTION_HS_PACKAGE_ADDED = "kr.co.hs.common.content.ACTION_HS_PACKAGE_ADDED";
    public static final String ACTION_HS_PACKAGE_REMOVED = "kr.co.hs.common.content.ACTION_HS_PACKAGE_REMOVED";
    public static final String ACTION_HS_PACKAGE_REPLACED = "kr.co.hs.common.content.ACTION_HS_PACKAGE_REPLACED";

    public static final String EXTRA_HS_PACKAGE_NAME = "kr.co.hs.common.content.EXTRA_HS_PACKAGE_NAME";

    public HsIntent() {
    }

    public HsIntent(Intent o) {
        super(o);
    }

    public HsIntent(String action) {
        super(action);
    }

    public HsIntent(String action, Uri uri) {
        super(action, uri);
    }

    public HsIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public HsIntent(String action, Uri uri, Context packageContext, Class<?> cls) {
        super(action, uri, packageContext, cls);
    }
}

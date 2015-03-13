package it.sephiroth.android.library.floatingmenu;

import android.os.Build;

/**
 * Created by crugnola on 12/6/14.
 */
class ApiHelper {
    static final boolean AT_LEAST_21 = Build.VERSION.SDK_INT >= 21;
    static final boolean AT_LEAST_11 = Build.VERSION.SDK_INT >= 11;
}

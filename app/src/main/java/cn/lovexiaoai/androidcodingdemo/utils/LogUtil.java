package cn.lovexiaoai.androidcodingdemo.utils;

import android.text.TextUtils;
import android.util.Log;


/**
 * log接口类
 *
 * @author guozhiqing
 */
public final class LogUtil {

    private static final String TAG = LogUtil.class.getSimpleName();

    public static boolean mDebug = false;

    public static void d(String tag, String str) {
        if (mDebug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            if (str == null) {
                str = "[[null!]]";
            }
            Log.d(tag, str);
        }
    }

    public static void e(String tag, String str) {
        if (mDebug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            if (str == null) {
                str = "[[null!]]";
            }
            Log.e(tag, str);
        }
    }

    public static void i(String tag, String str) {
        if (mDebug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            if (str == null) {
                str = "[[null!]]";
            }
            Log.i(tag, str);
        }
    }

    public static void v(String tag, String str) {
        if (mDebug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            if (str == null) {
                str = "[[null!]]";
            }
            Log.v(tag, str);
        }
    }

    public static void w(String tag, String str) {
        if (mDebug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            if (str == null) {
                str = "[[null!]]";
            }
            Log.w(tag, str);
        }
    }


    public static void d(String str) {
        if (mDebug) {

            if (str == null) {
                str = "[[null!]]";
            }
            Log.d(TAG, str);
        }
    }

    public static void e(String str) {
        if (mDebug) {

            if (str == null) {
                str = "[[null!]]";
            }
            Log.e(TAG, str);
        }
    }

    public static void i(String str) {
        if (mDebug) {

            if (str == null) {
                str = "[[null!]]";
            }
            Log.i(TAG, str);
        }
    }

    public static void v(String str) {
        if (mDebug) {

            if (str == null) {
                str = "[[null!]]";
            }
            Log.v(TAG, str);
        }
    }

    public static void w(String str) {
        if (mDebug) {

            if (str == null) {
                str = "[[null!]]";
            }
            Log.w(TAG, str);
        }
    }


}

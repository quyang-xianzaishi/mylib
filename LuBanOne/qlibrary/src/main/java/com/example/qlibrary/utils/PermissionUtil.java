package com.example.qlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017/6/5.
 */

public class PermissionUtil {


    /**
     * 判断api级别是否是6.0以上
     * @return
     */
    public static boolean bigThan23() {
        if (Build.VERSION.SDK_INT > 23) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否有权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(Context context, String... permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return false;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     */
    public static void requestPermissions(Activity activity, int code, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, code);
    }
}

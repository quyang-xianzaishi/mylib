package com.example.qlibrary.utils;

import android.os.Build;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/5/29.
 */

public class PhoneUtil {

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneType() {
        return Build.MODEL;
    }

    /**
     * 获取手机系统版本
     *
     * @return
     */
    public static String getPhoneSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static boolean isPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        if (number.length() == 11 && TextUtils.isDigitsOnly(number)) {
            return true;
        }
        return false;
    }

    public static boolean isPhone(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        if (TextUtils.isDigitsOnly(number)) {
            return true;
        }
        return false;
    }


}

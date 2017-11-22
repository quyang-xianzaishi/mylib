package com.example.qlibrary.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/5/31.
 */

public class NumberUtil {

    public static boolean isAllNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            int cp = Character.codePointAt(str, i);
            if (!Character.isDigit(cp)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsNumber(String source, String target) {
        if (TextUtils.isEmpty(source)) {
            return false;
        }
        if (source.contains(target)) {
            return true;
        }
        return false;
    }
}

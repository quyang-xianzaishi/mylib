package com.example.qlibrary.utils;

/**
 * Created by Administrator on 2017/5/24.
 */
public class ConvertUtil {

    public static int convert2Int(String target, int defaultValue) {
        if (null == target || "null".equals(target) || "".equals(target)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(target);
        } catch (Exception e) {
            try {
                return Double.valueOf(target).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public static long convert2Long(String target, long defaultValue) {
        if (null == target || "null".equals(target) || "".equals(target)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(target);
        } catch (Exception e) {
            return defaultValue;
        }
    }



    public static String convert2String(Integer target,String defaultValue) {
        if (null == target) {
            return defaultValue;
        }
        return  String.valueOf(target);
    }



    public static String convert2String(Long target,String defaultValue) {
        if (null == target) {
            return defaultValue;
        }
        return  String.valueOf(target);
    }

    public static String convert2String(Double target,String defaultValue) {
        if (null == target) {
            return defaultValue;
        }
        return  String.valueOf(target);
    }
}

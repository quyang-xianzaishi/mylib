package com.example.qlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/7/5.
 */

public class SPUtils {

    //获取版本名
    public static String getVersionName(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //包名没有找到的
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取boolean的值
     *
     * @param con      Context
     * @param fileName xml文件名
     * @param key      String 键名
     * @param defValue boolean 默认值
     * @return boolean 获取到的value
     */
    public static boolean getBooleanValue(Context con, String fileName, String key, boolean defValue) {
        SharedPreferences sp = con.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    /**
     * @param con      Context
     * @param fileName xml文件名
     * @param key      键名
     * @param value    值
     */
    public static void putBooleanValue(Context con, String fileName, String key, boolean value) {
        SharedPreferences sp = con.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }


    public static String getStringValue(Context con, String fileName, String key, String defValue) {
        SharedPreferences sp = con.getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }


    public static void putStringValue(Context con, String fileName, String key, String value) {
        SharedPreferences sp = con.getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static int getIntValue(Context con, String fileName, String key, int defValue) {
        SharedPreferences sp = con.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }


    public static void putIntValue(Context con, String fileName, String key, int value) {
        SharedPreferences sp = con.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static void removeData(Context con, String fileName, String key) {
        SharedPreferences sp = con.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void  clearAllDate() {

    }


}

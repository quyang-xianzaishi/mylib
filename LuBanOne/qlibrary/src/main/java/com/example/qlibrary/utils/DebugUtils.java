package com.example.qlibrary.utils;

import android.text.TextUtils;
import android.widget.TextView;
import com.example.qlibrary.MyConfig;

/**
 * Created by Administrator on 2017/7/12.
 */

public class DebugUtils {

  public static String isDebug(String target, String debug) {
    if (MyConfig.isDebug) {
      return debug;
    }
    return target;
  }

  public static String convert(String target, String debug) {
    if (TextUtils.isEmpty(target)) {
      return debug;
    }
    return target;
  }

  public static Integer convert(Integer target, Integer debug) {
    if (null == target) {
      return debug;
    }
    return target;
  }

  public static Integer isDebug(Integer target, Integer debug) {
    if (MyConfig.isDebug) {
      return debug;
    }
    return target;
  }

  public static void setIntValues(Integer target, int defValue, TextView textView) {
    if (null == textView) {
      return;
    }
    try {
      if (null == target) {
        textView.setText(String.valueOf(defValue));
      } else {
        textView.setText(String.valueOf(target));
      }
    } catch (Exception e) {
      textView.setText(String.valueOf(defValue));
    }
  }

  public static void setStringValue(String target, String defValue, TextView textView) {
    if (null == textView) {
      return;
    }
    try {
      if (TextUtils.isEmpty(target)) {
        textView.setText(defValue);
      } else {
        textView.setText(target);
      }
    } catch (Exception e) {
      textView.setText(defValue);
    }
  }

}

package com.example.qlibrary.utils;

import android.text.TextUtils;

/**
 * Created by admistrator on 2017/7/27.
 */

public class TextUitl {

  public static boolean isEmpty(String s) {
    if (TextUtils.isEmpty(s)) {
      return true;
    }
    return false;
  }

  public static boolean isNotEmpty(String s) {
    if (!TextUtils.isEmpty(s)) {
      return true;
    }
    return false;
  }


}

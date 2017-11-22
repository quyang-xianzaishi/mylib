package com.example.administrator.lubanone.utils;

import android.content.Context;
import com.example.administrator.lubanone.Config;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;

/**
 * Created by admistrator on 2017/8/12.
 */

public class TokenUtil {

  public static String getToken(Context context) {
    return SPUtils.getStringValue(context, Config.USER_INFO, Config.TOKEN, "");
  }

  public static void putToken(Context context, String tokne) {
    SPUtils.putStringValue(context, Config.USER_INFO, Config.TOKEN, tokne);
  }

  public static boolean exist(Context context) {
    if (TextUitl.isNotEmpty(SPUtils.getStringValue(context, Config.USER_INFO, Config.TOKEN, ""))) {
      return true;
    } else {
      return false;
    }
  }
}

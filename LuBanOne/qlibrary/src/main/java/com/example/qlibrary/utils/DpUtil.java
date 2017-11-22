package com.example.qlibrary.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/6/18.
 */

public class DpUtil {

  /**
   * 将px值转换为dp
   */
  public static int px2dp(Context context, int pxValue) {
    final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * 将dp值转换为px值
   */
  public static int dp2px(Context context, int dipValue) {
    final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }
}

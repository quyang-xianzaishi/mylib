package com.example.qlibrary.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import com.example.qlibrary.R;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ColorUtil {


  public static int getColor(int resColorId, Context context) {
    return ContextCompat.getColor(context, resColorId);
  }

  /**
   * 文字颜色选择器
   */
  public static ColorStateList getColorList(Context context, int colorId) {
    Resources resource = (Resources) context.getResources();
    ColorStateList csl = (ColorStateList) resource.getColorStateList(colorId);
    return csl;
  }


  /**
   * 获取color
   */
  public static int getColor(Context context, int colorId) {
    return ContextCompat.getColor(context, colorId);
  }


}

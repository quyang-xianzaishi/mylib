package com.example.qlibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017/6/20.
 */

public class DrableUtil {

  /**
   * 获取shape（圆角，颜色）
   *
   * @param cornerSize 单位像素，shape圆角大小
   */
  public static GradientDrawable getShape(Context context, int solidColor, int cornerSize,
      int lineSize, int lineColor) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(cornerSize);
    drawable.setStroke(lineSize, lineColor);
    drawable.setColor(ColorUtil.getColor(solidColor, context));
    return drawable;
  }


  public static GradientDrawable getShape(Context context, int solidColor, int cornerSize) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(cornerSize);
    drawable.setColor(ColorUtil.getColor(solidColor, context));
    return drawable;
  }

  public static GradientDrawable getShapeNew(Context context, int solidColor, int cornerSize) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(cornerSize);
    drawable.setColor(solidColor);
    return drawable;
  }


  /**
   * 状体选择器
   */
  public static StateListDrawable getStateListDrawable(Context context, int pressId, int normalId) {
    StateListDrawable bg = new StateListDrawable();
    Drawable pressed = ContextCompat.getDrawable(context, pressId);
    Drawable normal = ContextCompat.getDrawable(context, normalId);
    bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
    bg.addState(new int[]{}, normal);
    return bg;
  }

  /**
   * 将drawable目录下的转为Drawable
   *
   * @param resDrawableId drawable文件夹下的文件
   */
  public static Drawable getDrawable(Context context, int resDrawableId) {
    return ContextCompat.getDrawable(context, resDrawableId);
  }


}

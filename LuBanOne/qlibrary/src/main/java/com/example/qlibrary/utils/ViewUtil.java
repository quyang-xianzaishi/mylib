package com.example.qlibrary.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by admistrator on 2017/7/28.
 */

public class ViewUtil {

  public static void setBackground(View view, Drawable drawable) {

    //大于4.04 api16
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      view.setBackground(drawable);
    } else {
      view.setBackgroundDrawable(drawable);
    }
  }

}

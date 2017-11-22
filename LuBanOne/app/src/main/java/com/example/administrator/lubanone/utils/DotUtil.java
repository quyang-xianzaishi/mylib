package com.example.administrator.lubanone.utils;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/6/20.
 */

public class DotUtil {

  /**
   * 获取一个textview类型的shape
   *
   * @param size 直径
   */
  public static TextView getOval(Context context, int size, int resIdOfShape) {
    TextView textView = new TextView(context.getApplicationContext());
    textView.setHeight(size);
    textView.setWidth(size);
    textView.setBackgroundResource(resIdOfShape);
    return textView;
  }
}

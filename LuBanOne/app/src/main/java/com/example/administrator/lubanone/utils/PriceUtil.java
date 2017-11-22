package com.example.administrator.lubanone.utils;

import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.StringUtil;

/**
 * Created by admistrator on 2017/7/27.
 */

public class PriceUtil {


  /**
   * 设置价格 例如 12颗（24，000，000）
   */
  public static void setPrice(String count, String price, String danwei, TextView textView) {

    textView
        .setText(StringUtil.getBufferString(DebugUtils.convert(count, "0"),
            danwei, "(", StringUtil.getThreeString(
                DemicalUtil.multile(DebugUtils.convert(count, "0"),
                    DebugUtils.convert(price, "0"))),
            ")"));

  }


  /**
   * 获取价格 例如 12颗（24，000，000）
   */
  public static String getPrice(String count, String price, String danwei) {

    return StringUtil.getBufferString(DebugUtils.convert(count, "0"),
        danwei, "(", StringUtil.getThreeString(
            DemicalUtil.multile(DebugUtils.convert(count, "0"),
                DebugUtils.convert(price, "0"))),
        ")");
  }


  /**
   * 设置数量 例如：246个
   */
  public static void setCount(String count, String danwei, TextView textView) {
    textView.setText(StringUtil.getBufferString(DebugUtils.convert(count, "0"), danwei));
  }


  /**
   * 获取数量 例如：246个
   */
  public static String getCount(String count, String danwei) {
    return StringUtil.getBufferString(DebugUtils.convert(count, "0"), danwei);
  }


}

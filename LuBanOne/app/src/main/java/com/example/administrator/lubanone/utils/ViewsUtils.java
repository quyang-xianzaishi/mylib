package com.example.administrator.lubanone.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.ViewUtil;

/**
 * Created by admistrator on 2017/8/26.
 */

public class ViewsUtils {

  /**
   * 类似联系会员的按钮背景和文字颜色
   */
  public static void grayBgLineGrayTextRound4(TextView tvCui, Context mContext) {
    ViewUtil.setBackground(tvCui,
        DrableUtil.getDrawable(mContext, R.drawable.gray_shape_4));
    tvCui.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
  }


  /**
   * 类似催一催灰色边框背景 白色文字样式
   */
  public static void grayBgWhiteTextRound4(TextView tvCui, Context mContext) {
    ViewUtil.setBackground(tvCui,
        DrableUtil.getDrawable(mContext, R.drawable.gray_4));
    tvCui.setTextColor(ColorUtil.getColor(R.color.white, mContext));
  }

  /**
   * 类似我要投诉 橘黄色边框背景 橘黄色文字样式
   */
  public static void organeBorderBgOrganeTextRound4(TextView tvCui, Context mContext) {
    ViewUtil.setBackground(tvCui,
        DrableUtil.getDrawable(mContext, R.drawable.round_line_orange_shape));
    tvCui.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
  }


  /**
   * 白色文字 灰色背景  无圆角  不可点击
   */
  public static void setTextView(TextView view, String string, Context context) {
    view.setVisibility(View.VISIBLE);
    view.setText(string);
    view.setTextColor(ColorUtil.getColor(R.color.white, context));
    view.setBackgroundColor(ColorUtil.getColor(R.color.cBBB, context));
    view.setEnabled(false);
  }


}

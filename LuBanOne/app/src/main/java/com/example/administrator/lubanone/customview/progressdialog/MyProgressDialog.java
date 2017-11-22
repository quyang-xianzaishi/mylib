package com.example.administrator.lubanone.customview.progressdialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by hou on 2017/7/18.
 */

public class MyProgressDialog extends BaseProgressDialog {

  private static final float ANIMATE_SPEED = 1;
  private SpinView mIndeterminateView;

  public MyProgressDialog(@NonNull Context context) {
    super(context);
    setCanceledOnTouchOutside(false);
    setCancelable(true);
  }

  @Override
  protected View setContentView() {
    mIndeterminateView = new SpinView(getContext());
    mIndeterminateView.setAnimationSpeed(ANIMATE_SPEED);
    int widthMeasureSpec  = Helper.dpToPixel(40, getContext());
    int heightMeasureSpec = Helper.dpToPixel(40, getContext());
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthMeasureSpec, heightMeasureSpec);
    mIndeterminateView.setLayoutParams(lp);
    return mIndeterminateView;
  }

  /**
   * 改变动画的速度
   * @param scale 默认 1, 2 速度提升2倍, 0.5 速度减少半倍..等
   */
  public void setAnimationSpeed(int scale) {
    mIndeterminateView.setAnimationSpeed(scale);
  }
}

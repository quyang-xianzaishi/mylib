package com.example.administrator.lubanone.customview.progressdialog;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.example.administrator.lubanone.R;

/**
 * Created by hou on 2017/7/18.
 */

public class ProgressDialogBgLayout extends LinearLayout {

  private float mCornerRadius = 10;
  private int mBackgroundColor;

  public ProgressDialogBgLayout(Context context) {
    super(context);
    init();
  }

  public ProgressDialogBgLayout(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ProgressDialogBgLayout(Context context,
      @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    int color = getContext().getResources().getColor(R.color.kprogresshud_default_color);
    initBackground(color, mCornerRadius);
  }

  private void initBackground(int color, float cornerRadius) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setShape(GradientDrawable.RECTANGLE);
    drawable.setColor(color);
    drawable.setCornerRadius(cornerRadius);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      setBackground(drawable);
    } else {
      //noinspection deprecation
      setBackgroundDrawable(drawable);
    }
  }

  public void setCornerRadius(float radius) {
    mCornerRadius = Helper.dpToPixel(radius, getContext());
    initBackground(mBackgroundColor, mCornerRadius);
  }

  public void setBaseColor(int color) {
    mBackgroundColor = color;
    initBackground(mBackgroundColor, mCornerRadius);
  }
}

package com.example.administrator.lubanone.customview.progressdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;

/**
 * Created by hou on 2017/7/18.
 */

public abstract class BaseProgressDialog extends Dialog {

  private static final float DIM_AMOUNT      = 0;//黑暗度 0 - 1.0
  private static final int BACKGROUND_WIDTH  = 0;
  private static final int BACKGROUND_HEIGHT = 0;

  private View mRootVew;
  private TextView mLabelText;
  private TextView mDetailsText;
  private FrameLayout mCustomViewContainer;
  private ProgressDialogBgLayout mBackgroundLayout;

  public BaseProgressDialog(@NonNull Context context) {
    super(context);
    mRootVew = getLayoutInflater().inflate(R.layout.progress_dialog_layout, null, false);
    setWindowDimAmount(DIM_AMOUNT);
    initViews();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(mRootVew);
  }
  private void setWindowDimAmount(float dimAmount) {
    Window window = getWindow();
    window.setBackgroundDrawable(new ColorDrawable(0));
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    WindowManager.LayoutParams layoutParams = window.getAttributes();
    layoutParams.dimAmount = dimAmount;//设置黑暗度
    window.setAttributes(layoutParams);
  }

  private void initViews() {
    mBackgroundLayout    = (ProgressDialogBgLayout) mRootVew.findViewById(R.id.background);
    mCustomViewContainer = (FrameLayout) mRootVew.findViewById(R.id.container);
    mLabelText   = (TextView) mRootVew.findViewById(R.id.label);
    mDetailsText = (TextView) mRootVew.findViewById(R.id.details_label);

    updateBackgroundSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

    addViewToFrame(setContentView());

  }

  private void addViewToFrame(View view) {
    if (view == null) return;
    mCustomViewContainer.addView(view);
  }

  protected abstract View setContentView();

  public void updateBackgroundSize(int width, int height) {
    if (width > 0 && height > 0){
      ViewGroup.LayoutParams params = mBackgroundLayout.getLayoutParams();
      params.width = Helper.dpToPixel(width, getContext());
      params.height = Helper.dpToPixel(height, getContext());
      mBackgroundLayout.setLayoutParams(params);
    }
  }

  /**
   * 指定Window的透明度
   *
   * @param dimAmount 透明度从 0 到 1.
   */
  public BaseProgressDialog setDimAmount(float dimAmount) {
    if (dimAmount >= 0 && dimAmount <= 1) {
      setWindowDimAmount(dimAmount);
    }
    return this;
  }


  /**
   * dialog的背景颜色
   *
   * @param color ARGB color
   */
  public BaseProgressDialog setWindowColor(int color) {
    mBackgroundLayout.setBaseColor(color);
    return this;
  }

  /**
   * 设置dialog背景的圆角 (默认是 10)
   *
   * @param radius 圆角的半径
   */
  public BaseProgressDialog setCornerRadius(float radius) {
    mBackgroundLayout.setCornerRadius(radius);
    return this;
  }

  /**
   * 提供一个方法,可现实自定的view.
   *
   * @param view 必须不能为null
   */
  public BaseProgressDialog setCustomView(View view) {
    if (view != null) {
      setView(view);
    } else {
      throw new RuntimeException("Custom view must not be null!");
    }
    return this;
  }

  public void setView(View view) {
    if (view != null) {
      if (isShowing()) {
        mCustomViewContainer.removeAllViews();
        addViewToFrame(view);
      }
    }
  }

  public BaseProgressDialog setLabel(String label) {
    if (label != null) {
      mLabelText.setText(label);
      mLabelText.setVisibility(View.VISIBLE);
    } else {
      mLabelText.setVisibility(View.GONE);
    }
    return this;
  }

  public BaseProgressDialog setDetailsLabel(String detailsLabel) {
    if (detailsLabel != null) {
      mDetailsText.setText(detailsLabel);
      mDetailsText.setVisibility(View.VISIBLE);
    } else {
      mDetailsText.setVisibility(View.GONE);
    }
    return this;
  }

  public void setSize(int width, int height) {
    if (mBackgroundLayout != null) {
      updateBackgroundSize(width, height);
    }
  }


}

package com.example.administrator.lubanone.customview.progressdialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by hou on 2017/8/16.
 */

public class ProgressLoadingDialog extends BaseProgressDialog {

  private int mMaxProgress;
  private AnnularView mDeterminateView;
  private boolean mIsAutoDismiss = true;//加载完是否自动dismiss

  public ProgressLoadingDialog(@NonNull Context context) {
    super(context);
  }

  @Override
  protected View setContentView() {
    mDeterminateView = new AnnularView(getContext());
    return mDeterminateView;
  }

  /**
   * 进度条的最大值
   */
  public ProgressLoadingDialog setMaxProgress(int maxProgress) {
    mMaxProgress = maxProgress;
    mDeterminateView.setMax(maxProgress);
    return this;
  }

  /**
   * 进度条到最大值时,dialog是否自动关闭
   */
  public ProgressLoadingDialog setAutoDismiss(boolean isAutoDismiss) {
    mIsAutoDismiss = isAutoDismiss;
    return this;
  }

  public void setProgress(int progress) {
    if (mDeterminateView != null) {
      mDeterminateView.setProgress(progress);
      if (mIsAutoDismiss && progress >= mMaxProgress) {
        dismiss();
      }
    }
  }
}

package com.example.qlibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.example.qlibrary.R;
import com.example.qlibrary.dialog.DialogManager;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.ToastUtil;


public abstract class QBaseActivity extends AppCompatActivity implements View.OnClickListener {

  private Long mLastClickedTime = 0L;
  protected Context mContext;
  private DialogManager mDialogManager;
  private DialogManager mDialogManagerCommit;
  private DialogManager mDialogUploadManager;

  private static String upload_img = "头像上传中. . .";
  private static String landing = "数据加载中. . .";
  private static String commit = "数据提交中. . .";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      //透明状态栏
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      }
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      beforeSetContentView();
      setContentView(getContentViewId());
      mContext = getApplicationContext();
      initLandIng();
      initCommit();
      initUpload();
      initView();
//      loadData();
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this.getApplicationContext());
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    loadData();
  }

  private void initLandIng() {
    mDialogManager = new DialogManager(this, "数据加载中...");
  }


  private void initUpload() {
    mDialogUploadManager = new DialogManager(this, "头像上传中...");
  }

  public void showUploadIconDialog() {
    mDialogUploadManager.show();
  }

  public void hideUploadIconDialog() {
    mDialogUploadManager.dismiss();
  }


  private void initCommit() {
    mDialogManagerCommit = new DialogManager(this, commit);
  }


  public void showCommitDialog() {
    mDialogManagerCommit.show();
  }

  public void hideCommitDialog() {
    mDialogManagerCommit.dismiss();
  }


  public void show(String msg) {
    ToastUtil.showShort(msg, this);
  }


  /**
   * 向下滑动回调
   */
  private void downSlip() {
    System.out.println("quyang, down");
  }

  /**
   * 左右滑动回调
   */
  private void rightLeftSlip() {
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
  }

  protected abstract void beforeSetContentView();

  protected abstract int getContentViewId();

  /**
   * find view
   */
  public abstract void initView();


  /**
   * 加载一些数据，如，一进入界面就要请求数据
   */
  public abstract void loadData();

  //全屏
   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }*/

  /**
   * 限制连续点击，比如点击按钮发起多次请求
   */
  public boolean isCanClicked() {
    long currentTime = SystemClock.elapsedRealtime();
    if (null != mLastClickedTime && currentTime - mLastClickedTime > 500) {
      mLastClickedTime = currentTime;
      return true;
    }
    return false;
  }


  public void startNewActivityThenFinish(Activity src, Class targetClazz) {
    Intent intent = new Intent(src, targetClazz);
    startActivity(intent);
    finish();
  }

  public void startNewActivity(Activity src, Class targetClazz) {
    Intent intent = new Intent(src, targetClazz);
    startActivity(intent);
  }


  public String getInfo(int resId) {
    return getResources().getString(resId);
  }

  public Drawable getMyDrawable(int id) {
    return getResources().getDrawable(id);
  }

  public boolean isEmpty(String string) {
    if (TextUtils.isEmpty(string)) {
      return true;
    }
    return false;
  }

  public void judgeNet() {
    if (!NetUtil.isConnected(this)) {
      throw new DefineException(getInfo(R.string.NET_ERROR));
    }
  }

  public void showLandingDialog() {
    mDialogManager.show();
  }


  public void hideLandingDialog() {
    mDialogManager.dismiss();
  }


}

package com.example.administrator.lubanone.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.customview.progressdialog.BaseProgressDialog;
import com.example.administrator.lubanone.customview.progressdialog.MyProgressDialog;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.qlibrary.dialog.DialogManager;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.ToastUtil;

/**
 * Created by quyang on 2017/6/24.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

  public static final String TAG = "TAG";


  private DialogManager mDialogManager;
  private DialogManager mCommitDialog;
  private Long mLastClickedTime = 0L;
  protected Context mContext;
  private Unbinder mBind;
  private BaseProgressDialog loadingDialog;
  private BaseProgressDialog uploadingDialog;
  private BaseProgressDialog commitDialog;
  protected MyApplication myApp;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      //透明状态栏
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      }
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

      beforeSetContentView();
      setContentView(getContentViewId());
      if (Config.test_memery) {
        MyApplication.getWatcher().watch(this);
      }
      mContext = getApplicationContext();
      mBind = ButterKnife.bind(this);
      HouLog.d(this.getClass().getSimpleName());
      loadingDialog = new MyProgressDialog(this).setLabel(getString(R.string.loadings));
      uploadingDialog = new MyProgressDialog(this).setLabel(getString(R.string.uploading));
      commitDialog = new MyProgressDialog(this).setLabel(setTip());
      uploadingDialog.setCancelable(false);
      myApp = (MyApplication) getApplication();
      myApp.addActivity(this);
      initLandIng();
      commitLandIng();
      initView();
//      loadData();
      loadDate();

    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this.getApplicationContext());
    }
  }

  public void loadDate() {

  }


  public String setTip() {

    return null;
  }

  @Override
  protected void onResume() {
    super.onResume();
    loadData();
  }

  public void showUploadingDialog() {
    uploadingDialog.show();
  }

  public void dismissUploadingDialog() {
    uploadingDialog.dismiss();
  }

  public void showCommitDataDialog() {
    if (null != commitDialog && !commitDialog.isShowing()) {
      commitDialog.show();
    }
  }

  public void hideCommitDataDialog() {
    if (null != commitDialog && commitDialog.isShowing()) {
      commitDialog.dismiss();
    }
  }

  public void showLoadingDialog() {
    loadingDialog.show();
  }

  public void dismissLoadingDialog() {
    loadingDialog.dismiss();
  }

  private void initLandIng() {
    mDialogManager = new DialogManager(this, getInfo(com.example.qlibrary.R.string.loading));
  }

  private void commitLandIng() {
    mCommitDialog = new DialogManager(this, getInfo(R.string.committing));
  }

  public void showCommitDialog() {
    mCommitDialog.show();
  }


  public void hideCommitDialog() {
    mCommitDialog.dismiss();
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
    return DrableUtil.getDrawable(this, id);
  }

  public boolean isEmpty(String string) {
    if (TextUtils.isEmpty(string)) {
      return true;
    }
    return false;
  }

  public void isEmptyWithException(String string, String errorTip) {
    if (TextUtils.isEmpty(string)) {
      throw new DefineException(errorTip);
    }
  }


  public String[] getStringArray(int id) {
    String[] stringArray = getResources().getStringArray(id);
    return stringArray;
  }

  public void judgeNet() {
    if (!NetUtil.isConnected(this)) {
      throw new DefineException(getInfo(com.example.qlibrary.R.string.NET_ERROR));
    }
  }

  public boolean hasNet() {
    if (!NetUtil.isConnected(this)) {
      return false;
    }
    return true;
  }

  public void showLandingDialog() {
    mDialogManager.show();
  }

  public void hideLandingDialog() {
    mDialogManager.dismiss();
  }

  public void show(String msg) {
    if (Config.isOffLine) {
      ToastUtil.showShort(msg, this);
    }
  }

  public void showMsg(String msg) {
    ToastUtil.showShort(msg, getApplicationContext());
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (null != mBind) {
      mBind.unbind();
    }
    myApp.removeActivity(this);
  }

  public int getResColor(int clolorId) {
    int color = ContextCompat.getColor(getApplicationContext(), clolorId);
    return color;
  }

  public boolean isResultNull(Result result) {
    if (null == result.getResult()) {
      return true;
    }
    return false;
  }

  public void isResultNullWithException(Result result, String tips) {
    if (null == result.getResult()) {
      throw new DefineException(tips);
    }
  }


  public void isFalseWithException(boolean b, String s) {
    if (b) {
      throw new DefineException(s);
    }
  }

}

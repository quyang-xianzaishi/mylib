package com.example.administrator.lubanone.activity.land;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.bean.landregister.UrlBean;
import com.example.administrator.lubanone.net.UpdateService;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SDUtil;
import com.example.qlibrary.utils.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

public class StartActivity extends BaseActivity {


  private ImageView mIv;

  @Override
  protected void beforeSetContentView() {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_start;
  }

  @Override
  public void initView() {
    mIv = (ImageView) findViewById(R.id.iv);
  }

  @Override
  public void loadData() {
    initAnimation(mIv);
  }

  @Override
  public void onClick(View v) {

  }

  private void initAnimation(View view) {

    AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
    alphaAnimation.setDuration(4000);
    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        if (NetUtil.isConnected(getApplicationContext())) {
          requestNet();
        } else {
          showMsg(getString(R.string.NET_ERROR));
        }
      }

      @Override
      public void onAnimationEnd(Animation animation) {
      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });

    view.startAnimation(alphaAnimation);

  }

  private void requestNet() {

    OkHttpUtils.get().url(Urls.APP_URL)
        .build()
        .execute(new StringCallback() {
          @Override
          public void onError(Call call, Exception e, int id) {
            enterLoginActivity();
          }

          @Override
          public void onResponse(String response, int id) {
            parseJson(response);
          }
        });
  }

  //parse json
  private void parseJson(String json) {

    Log.e("StartActivity", "parseJson=" + json);

    if (TextUtils.isEmpty(json)) {
      enterLoginActivity();
      return;
    }

    final Result<UrlBean> urlBeanResult = GsonUtil.processJson(json, UrlBean.class);

    if (urlBeanResult == null || urlBeanResult.getResult() == null) {
      enterLoginActivity();
      return;
    }

    if (TextUtils.isEmpty(urlBeanResult.getResult().getVersioncode())) {
      enterLoginActivity();
      return;
    }
    try {
      int versionCode = Integer.parseInt(urlBeanResult.getResult().getVersioncode());
      if (versionCode > 0 && getVersionCode() < versionCode) {
        downLoadNewApp(urlBeanResult.getResult());
      } else {
        enterLoginActivity();
      }
    } catch (Exception e) {
      enterLoginActivity();
    }
  }

  public int getVersionCode() {
    PackageManager manager = getPackageManager();
    try {
      PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    return -1;
  }

  private void downLoadNewApp(final UrlBean version) {

    if (TextUtils.isEmpty(version.getUrl())) {
      enterLoginActivity();
      return;
    }

    StytledDialog
        .showIosAlert(this, getString(R.string.find_new_version),
            DebugUtils.convert(version.getDes(), getString(R.string.version_tip)),
            getString(R.string.no_now), getString(R.string.download_now), "", false, false,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                enterLoginActivity();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                down(version);
              }
            });
  }

  private void down(UrlBean bean) {
//    final ProgressDialog dialog = getProgressDialog(bean);
    if (!NetUtil.isConnected(getApplicationContext())) {
      showMsg(getString(R.string.check_net));
      return;
    }
    if (TextUtils.isEmpty(bean.getUrl())) {
      enterLoginActivity();
      return;
    }

    if (!SDUtil.existSDCard()) {
      show(getString(R.string.sd_card_tips));
      return;
    }

    System.out.println("download=" + bean.getUrl());
    Log.e(TAG, "down: " + bean.getUrl());

    Intent intent = new Intent(this, UpdateService.class);
    intent.putExtra("apkUrl", bean.getUrl());
    startService(intent);

    enterLoginActivity();
  }


  public void enterLoginActivity() {
    String account = SPUtils.getStringValue(this, Config.USER_INFO, Config.USER_ACCOUNT, null);
    if (TextUtils.isEmpty(account)) {
      Intent intent = new Intent(this, LandActivity.class);
      startActivity(intent);
    } else {
//      Intent intent = new Intent(this, LandActivity.class);
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
    }

    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1) {
      Log.e(TAG, "onActivityResult: ");
      enterLoginActivity();
    }
  }


}

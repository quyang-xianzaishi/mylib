package com.example.administrator.lubanone.activity.us;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.model.UsIntroduceBean;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.administrator.lubanone.utils.InitWebView;
import com.example.qlibrary.utils.SPUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import java.util.HashMap;
import java.util.Map;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/7/11.
 */

public class CommonListDetailsActivity extends BaseActivity {

  private WebView mWebView;
  private String if_id;
  private LinearLayout backBtn;
  private RelativeLayout shareBtn;
  private ProgressBar mProgressBar;
  private boolean isDownload = false;
  private boolean isHideShare = false;
  //分享需要
  private String url = "";
  private String theme = "";
  private String content = "";
  MyOrientoinListener myOrientoinListener;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_us_common_list_details;

  }

  @Override
  public void initView() {
    mWebView = (WebView) findViewById(R.id.us_common_list_details_webview);
    backBtn = (LinearLayout) findViewById(R.id.us_common_list_details_back);
    shareBtn = (RelativeLayout) findViewById(R.id.us_common_list_details_share_btn);
    mProgressBar = (ProgressBar) findViewById(R.id.us_common_list_details_progressBar);
    shareBtn.setOnClickListener(this);
    backBtn.setOnClickListener(this);
    InitWebView.initWebView(mWebView);
    Intent intent = getIntent();
    if_id = intent.getStringExtra("if_id");
    theme = intent.getStringExtra("theme");
    content = intent.getStringExtra("content");
    isHideShare = intent.getBooleanExtra("is_hide_share", false);
    isDownload = intent.getBooleanExtra("is_download", false);
    if (isHideShare) {
      shareBtn.setVisibility(View.GONE);
    }

    mWebView.setDownloadListener(new DownloadListener() {
      @Override
      public void onDownloadStart(String url, String userAgent, String contentDisposition,
          String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
      }
    });
    mWebView.setWebChromeClient(new MyWebChromeClient());
//    hideBottomUIMenu();
    myOrientoinListener = new MyOrientoinListener(this);
    boolean autoRotateOn = (android.provider.Settings.System
        .getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
    //检查系统是否开启自动旋转
    if (autoRotateOn) {
      myOrientoinListener.enable();
    }
  }

  class MyOrientoinListener extends OrientationEventListener {

    public MyOrientoinListener(Context context) {
      super(context);
    }

    public MyOrientoinListener(Context context, int rate) {
      super(context, rate);
    }

    @Override
    public void onOrientationChanged(int orientation) {
      HouLog.d(TAG, "orention" + orientation);
      int screenOrientation = getResources().getConfiguration().orientation;
      if (((orientation >= 0) && (orientation < 45)) || (orientation > 315)) {//设置竖屏
        if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
          HouLog.d(TAG, "设置竖屏");
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
      } else if (orientation > 225 && orientation < 315) { //设置横屏
        HouLog.d(TAG, "设置横屏");
        if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
      } else if (orientation > 45 && orientation < 135) {// 设置反向横屏
        HouLog.d(TAG, "反向横屏");
        if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
      } else if (orientation > 135 && orientation < 225) {
        HouLog.d(TAG, "反向竖屏");
        if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        }
      }
    }
  }

  /**
   * 隐藏虚拟按键，并且设置成全屏
   */
  protected void hideBottomUIMenu() {
    if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
      View v = this.getWindow().getDecorView();
      v.setSystemUiVisibility(View.GONE);
    } else if (Build.VERSION.SDK_INT >= 19) {
      //for new api versions.
      View decorView = getWindow().getDecorView();
      int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//          | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
          | View.SYSTEM_UI_FLAG_IMMERSIVE;
      decorView.setSystemUiVisibility(uiOptions);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }

  /**
   * 视频全屏参数
   */
  protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
  private View customView;
  private FrameLayout fullscreenContainer;
  private WebChromeClient.CustomViewCallback customViewCallback;

  class MyWebChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
      HouLog.d("onProgressChanged----" + newProgress);
      super.onProgressChanged(view, newProgress);
      if (newProgress == 100) {
        mProgressBar.setVisibility(View.GONE);
      } else {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(newProgress);
      }
    }

    @Override
    public View getVideoLoadingProgressView() {
      FrameLayout frameLayout = new FrameLayout(CommonListDetailsActivity.this);
      frameLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT));
      return frameLayout;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
      hideBottomUIMenu();
      showCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
      hideCustomView();
    }
  }

  /**
   * 视频播放全屏
   **/
  private void showCustomView(View view, CustomViewCallback callback) {
    if (customView != null) {
      callback.onCustomViewHidden();
      return;
    }
    CommonListDetailsActivity.this.getWindow().getDecorView();

    FrameLayout decor = (FrameLayout) getWindow().getDecorView();
    fullscreenContainer = new FullscreenHolder(CommonListDetailsActivity.this);
    fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
    decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
    customView = view;
    setStatusBarVisibility(false);
    customViewCallback = callback;
  }

  /**
   * 隐藏视频全屏
   */
  private void hideCustomView() {
    if (customView == null) {
      return;
    }

    setStatusBarVisibility(true);
    FrameLayout decor = (FrameLayout) getWindow().getDecorView();
    decor.removeView(fullscreenContainer);
    fullscreenContainer = null;
    customView = null;
    customViewCallback.onCustomViewHidden();
    mWebView.setVisibility(View.VISIBLE);
  }


  /**
   * 全屏容器界面
   */
  static class FullscreenHolder extends FrameLayout {

    public FullscreenHolder(Context ctx) {
      super(ctx);
      setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
      return true;
    }
  }

  private void setStatusBarVisibility(boolean visible) {
    int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
    getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
  }


  @Override
  public void loadData() {
    if (isDownload) {
      getDownloadDetails();
    } else {
      getDataRetrofit();
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.us_common_list_details_back:
        finish();
        break;
      case R.id.us_common_list_details_share_btn:
        //分享的url地址
        UMWeb web = new UMWeb(url);
        //分享需要一个标题，和一个内容简介
        web.setTitle(theme);
        web.setDescription(content);
        new ShareAction(CommonListDetailsActivity.this).withMedia(web)
            .setDisplayList(SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE)
            .setCallback(umShareListener).open();
//        hideBottomUIMenu();
        break;
    }
  }

  UMShareListener umShareListener = new UMShareListener() {
    @Override
    public void onStart(SHARE_MEDIA share_media) {
//      hideBottomUIMenu();
      HouLog.d("分享onStart 开始");
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
//      hideBottomUIMenu();
      HouToast.showLongToast(CommonListDetailsActivity.this, "分享成功");
      HouLog.d("分享onResult 成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//      hideBottomUIMenu();
      HouToast.showLongToast(CommonListDetailsActivity.this, "分享失败");
      HouLog.d("分享onError 错误");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
      HouLog.d("分享onCancel 取消");
    }
  };

  private void getDownloadDetails() {
    showLoadingDialog();
    Subscriber getDataByPost = new MySubscriber<UsIntroduceBean>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        Log.d("us列表详情error", e.toString());
        HouToast.showLongToast(CommonListDetailsActivity.this, e.getMessage());
      }

      @Override
      public void onNext(UsIntroduceBean usIntroduceBean) {
        dismissLoadingDialog();
        if (usIntroduceBean == null) {
          Log.d("us列表详情result", "onNext");
        } else if (usIntroduceBean != null) {
          url = usIntroduceBean.getUrl();
          mWebView.loadUrl(url);
        }
      }
    };
    Map<String, String> map = new HashMap<>();
    map.put("token", SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    map.put("if_id", if_id);
    Log.d("us列表详情请求参数", map.toString());
    //subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
    //observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
    MyApplication.rxNetUtils.getUsIntroduceService().getDownloadContent(map)
        .map(new BaseModelFunc<UsIntroduceBean>())
        .subscribeOn(Schedulers.io())//指定subscribe()发生在io线程。
        .observeOn(AndroidSchedulers.mainThread())//目的是让subscribe回调部分的代码在主线程被调用。
        .subscribe(getDataByPost);
  }


  //请求列表详情
  private void getDataRetrofit() {
    showLoadingDialog();
    Subscriber getDataByPost = new MySubscriber<UsIntroduceBean>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        Log.d("us列表详情error", e.toString());
        HouToast.showLongToast(CommonListDetailsActivity.this, e.getMessage());
      }

      @Override
      public void onNext(UsIntroduceBean usIntroduceBean) {
        dismissLoadingDialog();
        if (usIntroduceBean == null) {
          Log.d("us列表详情result", "onNext");
        } else if (usIntroduceBean != null) {
          url = usIntroduceBean.getUrl();
          mWebView.loadUrl(url);
        }
      }
    };
    Map<String, String> map = new HashMap<>();
    map.put("token", SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    map.put("if_id", if_id);
    Log.d("us列表详情请求参数", map.toString());
    //subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
    //observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
    MyApplication.rxNetUtils.getUsIntroduceService().getIntroduceContent(map)
        .map(new BaseModelFunc<UsIntroduceBean>())
        .subscribeOn(Schedulers.io())//指定subscribe()发生在io线程。
        .observeOn(AndroidSchedulers.mainThread())//目的是让subscribe回调部分的代码在主线程被调用。
        .subscribe(getDataByPost);
  }

  @Override
  public void onDestroy() {
    UMShareAPI.get(mContext).release();
    InitWebView.destoryWebView(mWebView);
    //销毁时取消监听
    myOrientoinListener.disable();
    super.onDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
  }
}

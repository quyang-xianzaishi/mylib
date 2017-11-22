package com.example.administrator.lubanone.activity.training.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.model.TrainDetailsModel;
import com.example.administrator.lubanone.bean.model.UploadPicture;
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
 * Created by hou on 2017/8/4.
 */

public class TrainDetailsActivity extends BaseActivity {

  private final static String TAG = "TrainDetailsActivity";
  private String trainId = "";
  private String theme = "";
  private String imageUrl = "";
  private String videoUrl = "";
  private String url;
  private boolean collectStatus = false;//未收藏

  private TextView trainTheme;
  private TextView trainUploader;
  private TextView trainUploadTime;
  private TextView trainScore;
  private TextView trainStatus;
  private WebView mWebView;

  private LinearLayout backBtn;
  private RelativeLayout shareBtn;
  private RelativeLayout collectBtn;
  private ImageView collectImage;

  private ImageView playImage;
  private ImageView playBtn;

  private ProgressBar mProgressBar;
  MyOrientoinListener myOrientoinListener;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_train_details;
  }

  @Override
  public void initView() {
//    AndroidBug54971Workaround.assistActivity(findViewById(R.id.train_details));
    mProgressBar = (ProgressBar) findViewById(R.id.train_list_details_progressBar);
    backBtn = (LinearLayout) findViewById(R.id.train_details_back_icon);
    shareBtn = (RelativeLayout) findViewById(R.id.train_details_share_btn);
    collectBtn = (RelativeLayout) findViewById(R.id.train_details_collect_btn);
    trainTheme = (TextView) findViewById(R.id.train_details_theme);
    trainUploader = (TextView) findViewById(R.id.train_details_uploader);
    trainUploadTime = (TextView) findViewById(R.id.train_details_upload_time);
    trainScore = (TextView) findViewById(R.id.train_details_score);
    trainStatus = (TextView) findViewById(R.id.train_details_status);
//    mWebView = (WebView) findViewById(R.id.train_details_webview);
    mWebView = (WebView) findViewById(R.id.train_details_only_webView);
    playImage = (ImageView) findViewById(R.id.train_details_play_image);
    playBtn = (ImageView) findViewById(R.id.train_details_play_icon);
    collectImage = (ImageView) findViewById(R.id.train_details_collect_image);

    backBtn.setOnClickListener(this);
    shareBtn.setOnClickListener(this);
    collectBtn.setOnClickListener(this);
    playBtn.setOnClickListener(this);

    Intent intent = getIntent();
    trainId = intent.getStringExtra("trainId");
    theme = intent.getStringExtra("theme");
    imageUrl = intent.getStringExtra("imageUrl");
    InitWebView.initWebView(mWebView);
    mWebView.setWebChromeClient(new MyWebChromeClient());
    getData();
//    hideBottomUIMenu();
    myOrientoinListener = new MyOrientoinListener(this);
    boolean autoRotateOn = (android.provider.Settings.System
        .getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
    //检查系统是否开启自动旋转
    if (autoRotateOn) {
      myOrientoinListener.enable();
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
      if (mProgressBar != null) {
        if (newProgress == 100) {
          mProgressBar.setVisibility(View.GONE);
        } else {
          mProgressBar.setVisibility(View.VISIBLE);
          mProgressBar.setProgress(newProgress);
        }
      } else {
        HouLog.d("mProgressBar is null");
      }
    }

    @Override
    public View getVideoLoadingProgressView() {
      FrameLayout frameLayout = new FrameLayout(TrainDetailsActivity.this);
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
    TrainDetailsActivity.this.getWindow().getDecorView();

    FrameLayout decor = (FrameLayout) getWindow().getDecorView();
    fullscreenContainer = new FullscreenHolder(TrainDetailsActivity.this);
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


  //获取详情数据
  private void getData() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<TrainDetailsModel>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        HouLog.d(TAG, "培训详情onError：" + e.toString());
      }

      @Override
      public void onNext(TrainDetailsModel trainDetailsModel) {
        dismissLoadingDialog();
        if (trainDetailsModel != null) {
          HouLog.d(TAG, trainDetailsModel.toString());

          trainTheme.setText(theme);
          trainUploader.setText("上传者:" + trainDetailsModel.getUploader());
          trainUploadTime.setText(trainDetailsModel.getUploadtime());
          trainScore.setText("培训积分指数:" + trainDetailsModel.getCode() + "分");
          trainStatus.setText(trainDetailsModel.getFinishstatus());

          videoUrl = trainDetailsModel.getVideourl();
          url = trainDetailsModel.getUrl();//返回的h5地址
          String sessionUrl = url + "/sessionid/" + trainDetailsModel.getSessionid();
          HouLog.d(TAG, "H5请求地址:" + sessionUrl);
          mWebView.loadUrl(sessionUrl);
          Glide.with(TrainDetailsActivity.this)
              .load(imageUrl)
              .into(playImage);
          if (trainDetailsModel.getStatus().equals("1")) {
            collectImage.setImageResource(R.drawable.task_collect_selected);
            collectStatus = true;
          }

        } else {

        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("trainid", trainId);
//    params.put("format", "android");
    HouLog.d(TAG + "培训详情参数:", params.toString());
    myApp.rxNetUtils.getTrainService().getTrainDetails(params)
        .map(new BaseModelFunc<TrainDetailsModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);

  }

  @Override
  public void loadData() {

  }

  //收藏按钮
  private void clickCollectBtn() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<UploadPicture>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        HouToast.showLongToast(TrainDetailsActivity.this, e.toString());

      }

      @Override
      public void onNext(UploadPicture uploadPicture) {
        dismissLoadingDialog();
        if (collectStatus) {
          collectImage.setImageResource(R.drawable.task_collect_normal);
          collectStatus = false;
          HouToast.showLongToast(TrainDetailsActivity.this, uploadPicture.getShow());
        } else {
          collectImage.setImageResource(R.drawable.task_collect_selected);
          collectStatus = true;
          HouToast.showLongToast(TrainDetailsActivity.this, uploadPicture.getShow());
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("trainid", trainId);
    myApp.rxNetUtils.getTrainService().getCollectInfo(params)
        .map(new BaseModelFunc<UploadPicture>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.train_details_back_icon:
        finish();
        break;
      case R.id.train_details_share_btn:
        UMWeb web = new UMWeb(url);
        web.setTitle(theme);
        web.setDescription("内容简介");
        new ShareAction(TrainDetailsActivity.this).withMedia(web)
            .setDisplayList(SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE)
            .setCallback(umShareListener).open();
//        hideBottomUIMenu();
        break;
      case R.id.train_details_collect_btn:
        clickCollectBtn();
        break;
      case R.id.train_details_play_icon:
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("theme", theme);
        intent.putExtra("videoUrl", videoUrl);
        intent.putExtra("imageUrl", imageUrl);
        startActivity(intent);
        break;
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    UMShareAPI.get(mContext).release();
    InitWebView.destoryWebView(mWebView);
    //销毁时取消监听
    myOrientoinListener.disable();
    super.onDestroy();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      case KeyEvent.KEYCODE_BACK:
        /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
//        else if (mWebView.canGoBack()) {
//        mWebView.goBack();
//      }
        if (customView != null) {
          hideCustomView();
        } else {
          finish();
        }
        return true;
      default:
        return super.onKeyUp(keyCode, event);
    }
  }

  /**
   * umShareListener为回调监听，构建如下，
   * 其中分享成功会回调onComplete，
   * 取消分享回调onCancel，
   * 分享错误回调onError，对应的错误信息可以用过onError的Throwable参数来打印
   */
  UMShareListener umShareListener = new UMShareListener() {
    @Override
    public void onStart(SHARE_MEDIA share_media) {
//      hideBottomUIMenu();
      //分享开始的回调
      HouLog.d("=======>>>>>", "分享开始");
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
//      hideBottomUIMenu();
      HouLog.d("=======>>>>>", "分享成功");
      HouToast.showLongToast(TrainDetailsActivity.this, "分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//      hideBottomUIMenu();
      HouToast.showLongToast(TrainDetailsActivity.this, "分享失败");
      HouLog.d("=======>>>>>", "分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
      HouLog.d("=======>>>>>", "分享取消");
    }
  };

}

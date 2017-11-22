package com.example.administrator.lubanone.activity.task;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.TasksDetailsBean;
import com.example.administrator.lubanone.bean.model.UploadPicture;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.administrator.lubanone.utils.InitWebView;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.utils.SPUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/6/24.
 */

public class TaskDetailsActivity extends BaseActivity {

  private final static String TAG = "TaskDetailsActivity";
  private final static int REQUEST_UPLOAD_CODE = 111;
  private static final int IMAGE_CODE = 11;

  private ScrollView mScrollView;
  private WebView mWebView;

  private LinearLayout backBtn;
  private RelativeLayout collectBtn;
  private RelativeLayout shareBtn;
  private TextView uploadImgBtn;
  private TextView contentTitle;
  private TextView mReward;
  private TextView mDate;
  private ImageView playVideoBtn;
  private ImageView playVideoImage;
  private ImageView collectImage;

  private String taskId = "";//详情页面的任务id
  private String title = "";//详情页面的任务名
  private String content = "";//任务内容
  private String thumbImgUrl = "";//缩略图地址
  private String videoUrl = "";//视频地址
  private String url = "https://www.baidu.com/";//详情页链接
  private boolean isCollected = false;
  private boolean isNew = false;

  private ProgressBar mProgressBar;
  private FrameLayout mFrameLayout;
  MyOrientoinListener myOrientoinListener;

  //选取单张
  private String imagePath;
  private File imageFile;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_task_details;
  }

  @Override
  public void initView() {
    mScrollView = (ScrollView) findViewById(R.id.task_details_scroll_view);
//    mWebView = (WebView) findViewById(R.id.task_details_web_view);
    mWebView = (WebView) findViewById(R.id.task_details_only_webView);
    mFrameLayout = (FrameLayout) findViewById(R.id.task_details_webView_frameLayout);
    mProgressBar = (ProgressBar) findViewById(R.id.task_details_progressBar);
    backBtn = (LinearLayout) findViewById(R.id.task_details_back_icon);
    collectBtn = (RelativeLayout) findViewById(R.id.task_details_collect_btn);
    shareBtn = (RelativeLayout) findViewById(R.id.task_details_share_btn);
    uploadImgBtn = (TextView) findViewById(R.id.task_upload_image_btn);
    contentTitle = (TextView) findViewById(R.id.task_details_content_title);
    mReward = (TextView) findViewById(R.id.task_details_reward);
    mDate = (TextView) findViewById(R.id.task_details_date);
    playVideoBtn = (ImageView) findViewById(R.id.task_details_play_icon);
    playVideoImage = (ImageView) findViewById(R.id.task_details_play_image);
    collectImage = (ImageView) findViewById(R.id.task_details_collect_image);

    backBtn.setOnClickListener(this);
    collectBtn.setOnClickListener(this);
    shareBtn.setOnClickListener(this);
    uploadImgBtn.setOnClickListener(this);
    playVideoBtn.setOnClickListener(this);

    Intent intent = getIntent();
    taskId = intent.getStringExtra("if_id");

    uploadImgBtn.setVisibility(View.GONE);
    InitWebView.initWebView(mWebView);//初始化webview
    mWebView.setWebChromeClient(new MyWebChromeClient());
    getTaskDetailsData();
//    hideBottomUIMenu();
    myOrientoinListener = new MyOrientoinListener(this);
    boolean autoRotateOn = (android.provider.Settings.System
        .getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
    //检查系统是否开启自动旋转
    if (autoRotateOn) {
      myOrientoinListener.enable();
    }
  }

  //-------------------------视频全屏方法----------------------------------
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
      FrameLayout frameLayout = new FrameLayout(TaskDetailsActivity.this);
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
    TaskDetailsActivity.this.getWindow().getDecorView();

    FrameLayout decor = (FrameLayout) getWindow().getDecorView();
    fullscreenContainer = new FullscreenHolder(TaskDetailsActivity.this);
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
//-----------------------------------------------------------------------------------------


  //获取详情
  private void getTaskDetailsData() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<TasksDetailsBean>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        HouLog.d(TAG, "任务详情onError：" + e.toString());
      }

      @Override
      public void onNext(TasksDetailsBean usIntroduceBean) {
        HouLog.d(TAG, "onNext");
        dismissLoadingDialog();
        if (usIntroduceBean != null) {
          title = usIntroduceBean.getTitle();
          content = usIntroduceBean.getContent();
          thumbImgUrl = usIntroduceBean.getThumimg();
          videoUrl = usIntroduceBean.getVideolink();
          url = usIntroduceBean.getUrl();

          contentTitle.setText(title);
          mDate.setText(usIntroduceBean.getCreatdate());
          mReward.setText(
              getInfo(R.string.task_list_item_reward1) + usIntroduceBean.getCatalyst() + getInfo(
                  R.string.task_list_item_reward2));
          Glide.with(TaskDetailsActivity.this).load(thumbImgUrl).into(playVideoImage);

//          mWebView.loadUrl(url + "/format/1");
          mWebView.loadUrl(url);
//          mWebView.loadUrl(
//              "http://103.210.239.20/api.php/Home/Traindetail/index/trainid/422/sessionid/pbm7gm48m8gndejb61sl2vrnh5");
          HouLog.d("h5地址：" + url + "/format/1");

          if (usIntroduceBean.getCollect_status().equals("1")) {
            collectImage.setImageResource(R.drawable.task_collect_selected);
            isCollected = true;
          }
          if (usIntroduceBean.getShare_status().equals("1")) {
            shareBtn.setVisibility(View.GONE);
          }
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("if_id", taskId);
    params.put("format", "android");
    HouLog.d(TAG, "任务详情参数:" + params.toString());
    myApp.rxNetUtils.getTaskService().getTaskDetails(params)
        .map(new BaseModelFunc<TasksDetailsBean>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void loadData() {

  }

  //点击收藏按钮
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
        HouLog.d(TAG, "收藏按钮onError:" + e.toString());
      }

      @Override
      public void onNext(UploadPicture uploadPicture) {
        dismissLoadingDialog();
        if (isCollected) {
          HouToast.showLongToast(TaskDetailsActivity.this, uploadPicture.getShow());
          collectImage.setImageResource(R.drawable.task_collect_normal);
          isCollected = false;
        } else {
          HouToast.showLongToast(TaskDetailsActivity.this, uploadPicture.getShow());
          collectImage.setImageResource(R.drawable.task_collect_selected);
          isCollected = true;
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("if_id", taskId);
    HouLog.d(TAG, "点击收藏参数:" + params.toString());
    myApp.rxNetUtils.getTaskService().getTaskCollectStatus(params)
        .map(new BaseModelFunc<UploadPicture>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //隐藏分享按钮
  private void hideShareBtn() {
    showLoadingDialog();
    Subscriber subscriber = new Subscriber<List<String>>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        dismissLoadingDialog();
        HouLog.d("onError: 隐藏失败" + e.getMessage());
      }

      @Override
      public void onNext(List<String> baseModel) {
        dismissLoadingDialog();
        HouLog.d("onNext: 隐藏成功");
        shareBtn.setVisibility(View.GONE);
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("if_id", taskId);
    HouLog.d("隐藏分享参数:", params.toString());
    myApp.rxNetUtils.getTaskService().hideShareBtn(params)
        .map(new BaseModelFunc<List<String>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //上传单个文件
  private void uploadImage() {
    showUploadingDialog();
    Subscriber subscriber = new MySubscriber<List<String>>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissUploadingDialog();
        Toast.makeText(TaskDetailsActivity.this, "上传失败", Toast.LENGTH_LONG).show();
        HouLog.d(TAG, "上传图片onError：" + e.getMessage());
      }

      @Override
      public void onNext(List<String> strings) {
        dismissUploadingDialog();
        uploadImgBtn.setVisibility(View.GONE);
        shareBtn.setVisibility(View.GONE);
        Toast.makeText(TaskDetailsActivity.this, "上传成功", Toast.LENGTH_LONG).show();
      }
    };
    HouLog.d(TAG, "上传图片参数:" + " 任务id " + taskId + "图片路径 " + imagePath);

    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
    MultipartBody.Part part = MultipartBody.Part
        .createFormData("file", imageFile.getName(), requestBody);
    RequestBody token = RequestBody
        .create(MediaType.parse("text/plain"), SPUtils
            .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestBody ifId = RequestBody.create(MediaType.parse("text/plain"), taskId);
    myApp.rxNetUtils.getTaskService().uploadImage(token, ifId, part)
        .map(new BaseModelFunc<List<String>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.task_details_collect_btn:
        clickCollectBtn();
        break;
      case R.id.task_details_back_icon:
        finish();
        break;
      case R.id.task_details_share_btn:
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setDescription(content);
        web.setThumb(new UMImage(mContext, thumbImgUrl));
        new ShareAction(TaskDetailsActivity.this).withMedia(web)
            .setDisplayList(SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE)
            .setCallback(umShareListener).open();
//        hideBottomUIMenu();
        break;
      case R.id.task_upload_image_btn:
        choseImage();
        break;
      default:
        break;
    }
  }

  /**
   * 选择图片
   */
  private void choseImage() {
    Matisse.from(this)
        .choose(MimeType.ofImage())
        .countable(false)
        .theme(R.style.Matisse_Dracula)
        .maxSelectable(1)
        .forResult(IMAGE_CODE);
  }

  /**
   * 裁剪图片
   */
  private void startCrop(Uri uri) {
    Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));
    UCrop.Options options = new UCrop.Options();
    options.setMaxScaleMultiplier(5);
    options.setCompressionQuality(70);
    options.setCompressionFormat(CompressFormat.JPEG);
//    options.setToolbarColor(getResources().getColor(R.color.gray_b6b6b6));

    UCrop.of(uri, destinationUri).withOptions(options).withAspectRatio(1, 1)
        .withMaxResultSize(500, 500)
        .start(this);
  }

  /**
   * 改写物理按键，返回的逻辑
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
      mWebView.goBack();
      return true;//返回上一页面
    }
    return super.onKeyDown(keyCode, event);
  }

  /**
   * 该处的处理尤为重要:
   * 应该在内置缩放控件消失以后,再执行mWebView.destroy()
   * 否则报错WindowLeaked
   */
  @Override
  protected void onDestroy() {
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
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        //选择图片
        case IMAGE_CODE:
          List<Uri> uris = Matisse.obtainResult(data);
          imagePath = VideoUtils.getPath(this, uris.get(0));//添加图片路径
          HouLog.d("得到的图片", imagePath);
          //上传图片
          imageFile = new File(imagePath);
          uploadImage();
//          if (TextUitl.isNotEmpty(imagePath) && new File(imagePath).exists()) {
//            startCrop(Uri.fromFile(new File(imagePath)));
//          }
          break;
        case UCrop.REQUEST_CROP:
          HouLog.d(TAG + "裁剪成功！");
          Uri croppedFileUri = UCrop.getOutput(data);
          //获取默认的下载目录
          String downloadsDirectoryPath = Environment
              .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
          String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(),
              croppedFileUri.getLastPathSegment());
          imageFile = new File(downloadsDirectoryPath, filename);//实例化图片文件
          HouLog.d(TAG, "裁剪后的图片: 名称:" + imageFile.getName() + " 路径:" + imageFile.getAbsolutePath());
          //保存下载的图片
          FileInputStream inStream = null;
          FileOutputStream outStream = null;
          FileChannel inChannel = null;
          FileChannel outChannel = null;
          try {
            inStream = new FileInputStream(new File(croppedFileUri.getPath()));
            outStream = new FileOutputStream(imageFile);
            inChannel = inStream.getChannel();
            outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);

            //上传图片
            uploadImage();

          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            try {
              outChannel.close();
              outStream.close();
              inChannel.close();
              inStream.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          break;

        case UCrop.RESULT_ERROR:
          HouLog.d(TAG + "裁剪失败！");
          break;
        default:
          break;
      }
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
//    hideBottomUIMenu();
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
      HouToast.showLongToast(TaskDetailsActivity.this, "分享成功");
      uploadImgBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//      hideBottomUIMenu();
      HouToast.showLongToast(TaskDetailsActivity.this, "分享失败");
      HouLog.d("=======>>>>>", "分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
      HouLog.d("=======>>>>>", "分享取消");
//      hideBottomUIMenu();
    }
  };
}

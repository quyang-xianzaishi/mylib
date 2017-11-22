package com.example.administrator.lubanone.activity.training.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.model.TrainTestDetailsModel;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.administrator.lubanone.utils.InitWebView;
import com.example.qlibrary.utils.SPUtils;
import java.util.HashMap;
import java.util.Map;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017\6\22 0022.
 */

public class ExaminationActivity extends BaseActivity {

  private LinearLayout back;
  private WebView mWebView;
  private String testId;
  private String testUrl;
  private ProgressBar mProgressBar;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_examnation_layout;
  }

  @Override
  public void initView() {
    mWebView = (WebView) this.findViewById(R.id.examnation_webview);
    mProgressBar = (ProgressBar) this.findViewById(R.id.train_exam_progressBar);

    back = (LinearLayout) findViewById(R.id.train_exam_details_back_icon);
    back.setOnClickListener(this);
    testId = getIntent().getStringExtra("testId");

    InitWebView.initWebView(mWebView);
    mWebView.setWebChromeClient(new MyWebChromeClient());
    getData();
  }

  class MyWebChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
      super.onProgressChanged(view, newProgress);
      HouLog.d("onProgressChanged----"+newProgress);
      if (newProgress == 100) {
        mProgressBar.setVisibility(View.GONE);
      } else {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(newProgress);
      }
    }
  }

  @Override
  public void loadData() {

  }

  private void getData() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<TrainTestDetailsModel>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        HouToast.showLongToast(ExaminationActivity.this, e.getMessage());
      }

      @Override
      public void onNext(TrainTestDetailsModel trainTestDetailsModel) {
        dismissLoadingDialog();
        if (trainTestDetailsModel != null) {
          testUrl = trainTestDetailsModel.getUrl();
//          syncCookie(mContext,testUrl,trainTestDetailsModel.getSessionid());
          String url = testUrl + "/sessionid/" + trainTestDetailsModel.getSessionid();
          HouLog.d(TAG, " h5地址:" + url);
          mWebView.loadUrl(url);
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("testid", testId);
    myApp.rxNetUtils.getTrainService().getTestDetails(params)
        .map(new BaseModelFunc<TrainTestDetailsModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.train_exam_details_back_icon:
        ExaminationActivity.this.finish();
        break;
      default:
        break;
    }
  }

//  @Override
//  public boolean onKeyDown(int keyCode, KeyEvent event) {
//    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
//      mWebView.goBack();
//      return true;
//    }
//    return super.onKeyDown(keyCode, event);
//  }

  @Override
  protected void onDestroy() {
    InitWebView.destoryWebView(mWebView);
    super.onDestroy();
  }
}

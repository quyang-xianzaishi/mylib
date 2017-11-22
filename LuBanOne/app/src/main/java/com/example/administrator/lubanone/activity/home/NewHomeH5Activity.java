package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.DreamFoundNewsBean.AboutlistBean;
import com.example.administrator.lubanone.bean.homepage.H5ResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 首页h5
 */
public class NewHomeH5Activity extends BaseActivity {


  @BindView(R.id.tv_title)
  TextView mTvTitle;
  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.webView)
  WebView mWebView;
  private AboutlistBean mItem;


  private RequestListener mListner = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        hideCommitDataDialog();
        Result<H5ResultBean> result = GsonUtil
            .processJson(jsonObject, H5ResultBean.class);
        updatePage(result);
      } catch (Exception e) {
        showMsg(getString(R.string.GET_DATE_FAIL));
        hideCommitDataDialog();
      }

    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      hideCommitDataDialog();
    }
  };

  private void updatePage(Result<H5ResultBean> result) {
    if (ResultUtil.isSuccess(result) && null != result.getResult() && !TextUtils
        .isEmpty(result.getResult().getUrl())) {
      mWebView.loadUrl(result.getResult().getUrl());
    } else {
      showMsg(DebugUtils.convert(ResultUtil
          .getErrorMsg(result), getString(R.string.GET_DATE_FAIL)));
    }

  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_dream_found_news_details;
  }

  @Override
  public void initView() {
    Intent intent = getIntent();
    if (intent != null) {
      Bundle bundle = intent.getBundleExtra("bundle");
      mItem = (AboutlistBean) bundle.getSerializable("item");
      if (null != mItem) {
//        mTvTitle.setText(DebugUtils.convert(mItem.getIf_title(), ""));
      }
    }

    mWebView.setWebChromeClient(new MyWebChromeClient());
    mWebView.setWebViewClient(new MyWebViewClient());
    mWebView.setDownloadListener(new MyDownloadListener());

  }

  @Override
  public void loadData() {
    if (null != mItem) {
      String id = mItem.getIf_id();
      //http://42.51.40.5/api.php/home/news/newsDetail/token/c58f6e951cc6f7794973de8fc50df954/if_id/111
      String url = Urls.DREAM_FOUND_NEWS_DETAILS + "?token=" + SPUtils
          .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, "") + "&"
          + "if_id=" + mItem.getIf_id();
      Log.e(TAG, "loadData: " + url);

      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp,this,null, url, mListner, RequestNet.GET);
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.landing_date);
  }

  private List<RequestParams> getParamList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsId = new RequestParams("if_id", DebugUtils.convert(mItem.getIf_id(), ""));
    list.add(paramsId);
    list.add(paramsToken);
    return list;

  }

  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.iv_back, R.id.tv_back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      default:
        break;
    }
  }

  private class MyWebChromeClient extends WebChromeClient {

    @Override
    public void onReceivedTitle(WebView view, String title) {

      //拿到title
      super.onReceivedTitle(view, title);
    }
  }

  private class MyWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return super.shouldOverrideUrlLoading(view, url);
    }
  }

  //获取url
  private class MyDownloadListener implements DownloadListener {

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition,
        String mimetype, long contentLength) {

    }
  }
}

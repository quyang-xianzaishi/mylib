package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.webView;

import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import butterknife.BindView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;

public class ProtocolActivity extends BaseActivity {


  @BindView(R.id.iv_back)
  ImageView iv_back;
  @BindView(webView)
  WebView mWebView;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_protocol;
  }

  @Override
  public void initView() {
    iv_back.setOnClickListener(this);

    mWebView.setWebChromeClient(new MyWebChromeClient());
    mWebView.setWebViewClient(new MyWebViewClient());
    mWebView.setDownloadListener(new MyDownloadListener());

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

//  用户规则
//  api.php/home/taskdetail/questionuri
//      传入方式
//  post
//      qid
//  使用规则  4   用户协议 5   项目制度 6
//  培训制度  1   信用制度 2   等级政策 3

  //获取url
  private class MyDownloadListener implements DownloadListener {

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition,
        String mimetype, long contentLength) {
    }
  }

  @Override
  public void loadData() {
    String url = Urls.ROOT_URL + "api.php/Home/Taskdetail/questiondetail/qid/5";
    mWebView.loadUrl(url);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.iv_back) {
      finish();
    }
  }


}
package com.example.administrator.lubanone.activity.home;

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

/**
 * 会员级别政策
 */
public class UserLevelSystemActivity extends BaseActivity {

  @BindView(R.id.webView)
  WebView mWebView;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_user_level_system;
  }

  @Override
  public void initView() {

    ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
    ivBack.setOnClickListener(this);

    mWebView.setWebChromeClient(new MyWebChromeClient());
    mWebView.setWebViewClient(new MyWebViewClient());
    mWebView.setDownloadListener(new MyDownloadListener());

  }

  @Override
  public void loadData() {
    String url = Urls.ROOT_URL + "api.php/Home/Taskdetail/questiondetail/qid/3";
    mWebView.loadUrl(url);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_back:
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

package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;

/**
 * 信用评分制度
 */
public class CreditGradeSystemActivity extends BaseActivity {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.activity_credit_grade_system)
  LinearLayout mActivityCreditGradeSystem;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.empty_layout)
  View emptyLayout;
  @BindView(R.id.webView)
  WebView webView;


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_credit_grade_system;
  }

  @Override
  public void initView() {
    mTvBack.setOnClickListener(this);

    webView.setWebChromeClient(new MyWebChromeClient());
    webView.setWebViewClient(new MyWebViewClient());
    webView.setDownloadListener(new MyDownloadListener());

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

  @Override
  public void loadData() {
    String url = Urls.ROOT_URL + "api.php/Home/Taskdetail/questiondetail/qid/2";
    webView.loadUrl(url);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.tv_back) {
      finish();
    }
  }

  @OnClick(R.id.iv_back)
  public void onViewClicked() {
    finish();
  }

  public void showEmptyLayout(boolean show) {
    if (null != emptyLayout) {
      if (show) {
        emptyLayout.setVisibility(View.VISIBLE);
      } else {
        emptyLayout.setVisibility(View.GONE);
      }
    }
  }


}

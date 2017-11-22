package com.example.administrator.lubanone.activity.training.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;

/**
 * Created by hou on 2017/8/1.
 */

public class TrainContentActivity extends BaseActivity {

  private WebView mWebView;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.layout_common_webview;
  }

  @Override
  public void initView() {
    mWebView = (WebView) findViewById(R.id.common_webview);
    initWebView();
    mWebView.loadUrl("http://42.51.40.5/api.php/Home/taskdetail/index/token/202cb962ac59075b964b07152d234b70/if_id/4");

  }

  private void initWebView(){
    WebSettings settings = mWebView.getSettings();
    settings.setPluginState(PluginState.ON);
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }
}

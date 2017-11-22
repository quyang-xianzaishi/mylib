package com.example.administrator.lubanone.utils;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by hou on 2017/7/8.
 */

public class InitWebView {

  public static void initWebView(WebView mWebView) {
//    mWebView.canGoBack();
    mWebView.setHorizontalScrollBarEnabled(false);
    mWebView.setVerticalScrollBarEnabled(false);

    /**
     * 本地文件存放在assets中。assets文件夹中可以放置html、jss、image、image等资源。
     * Android Studio需要放置assets到project/app/src/main下面，需要自己创建。
     * 对于assets文件夹下资源的引用，可以通过例如："file:///android_asset/xxx.html"的形式来引用。
     */

    /**
     * 1.WebView加载本地资源
     *
     * webView.loadUrl("file:///android_asset/example.html");
     *
     * 2.加载手机本地的html页面
     *
     * mWebView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");
     *
     * 3.WebView加载web资源
     *
     * mWebView.loadUrl(url);
     */

    mWebView.canGoBack();

    /**
     * WebViewClient类
     *
     * 作用：处理各种通知 & 请求事件
     */
    mWebView.setWebViewClient(new WebViewClient() {

      /**
       * 作用：打开网页时不调用系统浏览器， 而是在本WebView中显示；在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
       * @param view
       * @param request
       * @return
       */
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        //返回值是true的时候是控制网页在WebView中打开，如果false调用系统浏览器或第三方浏览器打开
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
          view.loadUrl(request.getUrl().toString());
          HouLog.d("webView 5.0以上 shouldOverrideUrlLoading: " + "request: " + request
              + "  request.getUrl(): " + request.getUrl() + "  request.getUrl().toString(): "
              + request
              .getUrl().toString());
        } else {
          view.loadUrl(request.toString());
          HouLog.d("webView 5.0以下 shouldOverrideUrlLoading: " + "request: " + request
              + "  request.toString(): " + request.toString());
        }
        return true;
      }

//      @Override
//      public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        view.loadUrl(url);
//        HouLog.d("过时 shouldOverrideUrlLoading: " + url);
//        return true;
//      }

      /**
       * 作用：开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
       * @param view
       * @param url
       * @param favicon
       */
      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        //设定加载开始的操作
        HouLog.d("webView start: " + url);
      }

      /**
       * 作用：在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
       * @param view
       * @param url
       */
      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        HouLog.d("webView finished: " + url);
        //设定加载结束的操作
      }

      /**
       * 作用：在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
       * @param view
       * @param url
       */
      @Override
      public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        //设定加载资源的操作
      }

      /**
       * 作用：加载页面的服务器出现错误时（如404）调用。
       * @param view
       * @param request
       * @param error
       *
       * 6.0之后的onReceivedError
       */
      @Override
      public void onReceivedError(WebView view, WebResourceRequest request,
          WebResourceError error) {
        super.onReceivedError(view, request, error);
        //步骤1：写一个html文件（error_handle.html），用于出错时展示给用户看的提示页面
        //步骤2：将该html文件放置到代码根目录的assets文件夹下
        //步骤3：复写WebViewClient的onRecievedError方法
        //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理

      }

      /**
       * 作用：处理https请求
       * WebView默认不处理https请求，页面显示空白，需要进行如下设置
       * @param view
       * @param handler
       * @param error
       */
      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();    //表示等待证书响应
        // handler.cancel();      //表示挂起连接，为默认方式
        // handler.handleMessage(null);    //可做其他处理
        HouLog.d("sslError: " + error.toString() + "  " + error.getPrimaryError());
      }
    });

    /**
     * WebChromeClient类
     *
     * 作用：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
     *
     */
    mWebView.setWebChromeClient(new WebChromeClient() {


      /**
       * 作用：获得网页的加载进度并显示
       * @param view
       * @param newProgress
       */
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        //进度
      }

      /**
       * 作用：获取Web页中的标题
       *
       * 每个网页的页面都有一个标题，比如www.baidu.com这个页面的标题即“百度一下，你就知道”，
       * 那么如何知道当前webview正在加载的页面的title并进行设置呢？
       *
       * @param view
       * @param title
       */
      @Override
      public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        //title web页标题
      }
    });

    //声明WebSettings子类
    WebSettings settings = mWebView.getSettings();

    //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
    settings.setJavaScriptEnabled(true);

    //支持插件
    settings.setPluginState(WebSettings.PluginState.ON);

    //设置自适应屏幕，两者合用
    settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
    settings.setLoadWithOverviewMode(true);//缩放至屏幕的大小

    //缩放操作
    settings.setSupportZoom(true); // 支持缩放
    settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
    settings.setDisplayZoomControls(false); //隐藏原生的缩放控件

    settings.setAllowFileAccess(true); // 允许访问文件
    settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

    //视频点击的时候 会转圈下后面就加载失败 5.0 以上的手机要加这个
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    //WebView加载页面优先使用缓存加载
    //缓存模式如下：
    //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
    //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
    //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
    //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
//    settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//    settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
  }

  public static void destoryWebView(WebView mWebView) {
    if (mWebView != null) {
      mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
      mWebView.clearHistory();

      ((ViewGroup) mWebView.getParent()).removeView(mWebView);
      mWebView.destroy();
      mWebView = null;
    }
  }


}

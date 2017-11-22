package com.example.administrator.lubanone.fragment.us;

import android.view.View;
import android.webkit.WebView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.UsIntroduceBean;
import com.example.administrator.lubanone.fragment.BaseFragment;
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
 * Created by hou on 2017/7/3.
 */

public class DetailsFundDonationFragment extends BaseFragment {

  private WebView mWebView;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_us_fund_donation, null);
    mWebView = (WebView) view.findViewById(R.id.us_fund_donation_webview);
    return view;
  }

  @Override
  public void initData() {
    InitWebView.initWebView(mWebView);
    getDataRetrofit();
  }

  private void getDataRetrofit() {
    Subscriber getDataByPost = new MySubscriber<UsIntroduceBean>(getActivity()) {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        HouLog.d("基金捐赠error", e.toString());
        HouToast.showLongToast(getActivity(), e.getMessage());
      }

      @Override
      public void onNext(UsIntroduceBean usIntroduceBean) {
        if (null == usIntroduceBean) {
          HouLog.d("基金捐赠result", "usIntroduceBean == null");
        } else if (null != usIntroduceBean) {
          HouLog.d("基金捐赠result", usIntroduceBean.getUrl());
          mWebView.loadUrl(usIntroduceBean.getUrl() + "/textalign/1");
        }
      }
    };
    Map<String, String> map = new HashMap<>();
    map.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
    map.put("if_id", "5");
    HouLog.d("基金捐赠请求参数", map.toString());
    MyApplication.rxNetUtils.getUsIntroduceService().getIntroduceContent(map)
        .map(new BaseModelFunc())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(getDataByPost);
  }

  @Override
  public void onDestroy() {
    InitWebView.destoryWebView(mWebView);
    super.onDestroy();
  }
}

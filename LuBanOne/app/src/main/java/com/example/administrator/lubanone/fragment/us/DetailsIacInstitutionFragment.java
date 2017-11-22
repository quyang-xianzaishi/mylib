package com.example.administrator.lubanone.fragment.us;

import android.view.View;
import android.webkit.WebView;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.UsIntroduceBean;
import com.example.administrator.lubanone.fragment.BaseFragment;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.administrator.lubanone.utils.InitWebView;
import java.util.HashMap;
import java.util.Map;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/9/7.
 */

public class DetailsIacInstitutionFragment extends BaseFragment {

  private WebView mWebView;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_iac_institution, null);
    mWebView = (WebView) view.findViewById(R.id.us_iac_institution_webview);

    return view;
  }

  @Override
  public void initData() {
    InitWebView.initWebView(mWebView);
//    getDataRetrofit();
    mWebView.loadUrl("http://103.210.239.20/api.php/Home/Taskdetail/questiondetail/qid/6");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    InitWebView.destoryWebView(mWebView);
  }

  private void getDataRetrofit() {
    Subscriber getDataByPost = new MySubscriber<UsIntroduceBean>(getActivity()) {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        HouLog.d("iac项目制度error", e.toString());
        HouToast.showLongToast(getActivity(), e.getMessage());
      }

      @Override
      public void onNext(UsIntroduceBean usIntroduceBean) {
        if (usIntroduceBean == null) {
          HouLog.d("iac项目制度result", "usIntroduceBean == null");
        } else if (usIntroduceBean != null) {
          HouLog.d("iac项目制度result", usIntroduceBean.getUrl());
          mWebView.loadUrl(usIntroduceBean.getUrl() + "/textalign/1");
        }
      }
    };
    Map<String, String> map = new HashMap<>();
//    map.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
    map.put("qid", "6");
    HouLog.d("iac项目制度请求参数", map.toString());
    //subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
    //observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
    MyApplication.rxNetUtils.getUsIntroduceService().getInstitution(map)
        .map(new BaseModelFunc<UsIntroduceBean>())
        .subscribeOn(Schedulers.io())//指定subscribe()发生在io线程。
        .observeOn(AndroidSchedulers.mainThread())//目的是让subscribe回调部分的代码在主线程被调用。
        .subscribe(getDataByPost);
  }

}

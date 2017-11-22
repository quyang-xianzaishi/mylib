package com.example.administrator.lubanone.rxjava;

import android.app.Activity;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.utils.HouLog;
import rx.Subscriber;

/**
 * Created by hou on 2017/8/11.
 */

public abstract class MySubscriber<T> extends Subscriber<T> {

  private Activity activity;

  public MySubscriber(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void onError(Throwable e) {
    HouLog.d("MySubscriber: " + MyApplication.isMainThread());
    HouLog.d("MySubscriber+e.toString " + e.toString());
    if (null != e.getMessage()) {
      if ("3".equals(e.getMessage())) {
        MyApplication.getInstance().safeOut(MyApplication.getInstance(), activity);
        HouLog.d("token无效");
        return;
      }
    }
    HouLog.d("this is MySubscriber");
  }


}

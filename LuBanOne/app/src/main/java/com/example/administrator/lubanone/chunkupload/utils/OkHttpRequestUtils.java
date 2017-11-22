package com.example.administrator.lubanone.chunkupload.utils;

import android.content.Context;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.qlibrary.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.Map;
import okhttp3.Call;

public class OkHttpRequestUtils {

  public static OkHttpRequestUtils myOkHttpRequestUtil;
  public static Context m_context;

  public OkHttpRequestUtils (Context context){
    this.m_context = context;
  }

  public static synchronized OkHttpRequestUtils getInstance(Context context){
    if (myOkHttpRequestUtil == null){
      myOkHttpRequestUtil = new OkHttpRequestUtils(context);
    }
    return myOkHttpRequestUtil;
  }

  public synchronized void getUploadInfo(String url, final Map<String, String> params,
      final Callback callback) {

    OkHttpUtils
        .post()
        .url(url)
        .params(params)
        .build()
        .connTimeOut(20000)
        .readTimeOut(20000)
        .writeTimeOut(20000)
        .execute(new StringCallback() {
          @Override
          public void onError(Call call, Exception e, int id) {
            ToastUtil.showShort(e.getMessage(), m_context);
            HouLog.d(m_context.getClass().getSimpleName(),"onError:" + e.getMessage());
            callback.onError(call, e, id);
          }

          @Override
          public void onResponse(String response, int id) {
            HouLog.d(m_context.getClass().getSimpleName(),"success:" + response);
            callback.onResponse(response, id);
          }
        });


  }

}







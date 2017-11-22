package com.example.administrator.lubanone.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.land.LandActivity;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.manager.JyActivityManager;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.qlibrary.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import java.io.File;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求网络
 * Created by quyang on 2017/6/20.
 */

public class RequestNet {


  private MyApplication application;
  private Activity activity;


  public static final int GET = 0;

  public static final int POST = 1;

  private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

  private static Handler sHandler;

  static {
    sHandler = new Handler();
  }

  public RequestNet() {

  }

  /**
   * 上传单个文件
   */
  public RequestNet(MyApplication application, Activity activity, File file, String token,
      String url, String name,
      String fileName,
      RequestListener listener) {
    this.application = application;
    this.activity = activity;
    OkHttpUtils.post()
        .addFile(name, fileName, file)
        .url(url)
        .addParams(Config.TOKEN, token)
        .build()
        .execute(new MyStringCallback(listener));
  }

  /**
   * 上传单个文件 orderid
   */
  public RequestNet(MyApplication application, Activity activity, File file, String token,
      String orderId, String url,
      String name,
      String fileName,
      RequestListener listener) {
    this.application = application;
    this.activity = activity;
    OkHttpUtils.post()
        .addFile(name, fileName, file)
        .url(url)
        .addParams(Config.TOKEN, token)
        .addParams("orderid", orderId)
        .build()
        .execute(new MyStringCallback(listener));
  }

  public RequestNet(MyApplication application, Activity activity, List<RequestParams> list,
      String url,
      final RequestListener listener,
      int method) {
    this.application = application;
    this.activity = activity;

    if (method == GET) {
      GetBuilder builder = OkHttpUtils
          .get()
          .url(url);

      if (null != list) {
        for (RequestParams params : list) {
          if (null == params || null == params.getKey()) {
            continue;
          }
          builder.addParams(params.getKey(), params.getValue());
        }
      }

      builder.build()
          .execute(new MyStringCallback(listener));
    } else {
      PostFormBuilder builder = OkHttpUtils
          .post()
          .url(url);
      if (null != list) {
        for (RequestParams params : list) {
          if (null == params || null == params.getKey() || null == params.getValue()) {
            continue;
          }
          builder.addParams(params.getKey(), params.getValue());
        }
      }

      builder.build().execute(new MyStringCallback(listener));
    }


  }

  private class MyStringCallback extends StringCallback {

    private RequestListener mRequestListener;

    public MyStringCallback(RequestListener listener) {
      this.mRequestListener = listener;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
      mRequestListener.onFail(e.getMessage());
    }

    @Override
    public void onResponse(String response, int id) {
      try {
        if (Config.isOnline) {
          Log.d("返回的json:", response);
          JSONObject jsonObject = new JSONObject(response);
          String type = (String) jsonObject.get("type");
          if (!TextUtils.isEmpty(type) && "3".equals(type)) {
            if (null != application && null != activity) {
              ToastUtil.showShort(activity.getString(R.string.token_no_work), activity);
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  if(JyActivityManager.getInstance().getRunningActivityName().equals
                      ("com.example.administrator.lubanone.activity.land.LandActivity")){
                  }else {
                    application.removeAllActivity();
                    activity.startActivity(new Intent(activity, LandActivity.class));
                    activity.finish();
                  }
                }
              }, 3500);
            }
          } else {
            mRequestListener.testSuccess(response);
          }

        } else {
          mRequestListener.onSuccess(new JSONObject(response));
        }
      } catch (JSONException e) {
        mRequestListener.onFail(e.getMessage());
      }

    }
  }

  public static class getDataCallback implements Callback {

    private RequestListener mRequestListener;

    public getDataCallback(RequestListener listener) {
      this.mRequestListener = listener;
    }

    @Override
    public void onFailure(Call call, IOException e) {
      HouLog.d("请求失败原因：", e.toString());
      mRequestListener.onFail(e.toString());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
      String mResponse = response.body().string();
      try {
        if (Config.isOnline) {
          HouLog.d("返回的json:", mResponse);
          mRequestListener.testSuccess(mResponse);
        } else {
          mRequestListener.onSuccess(new JSONObject(mResponse));
        }
      } catch (JSONException e) {
        mRequestListener.onFail(e.getMessage());
      }
    }
  }

  public void uploadImageByPostFromPath(List<String> imagePath, List<RequestParams> list,
      String url,
      final RequestListener listener) {

    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

    for (RequestParams params : list) {
      if (null == params || null == params.getKey()) {
        continue;
      }
      builder.addFormDataPart(params.getKey(), params.getValue());
    }

    for (int i = 0; i < imagePath.size(); i++) {
      File f = new File(imagePath.get(i));
      if (f != null) {
        HouLog.d("图片" + i + "名", f.getName());
        builder.addFormDataPart("img[]", f.getName(), RequestBody.create(MEDIA_TYPE_JPEG, f));
      }
    }

    MultipartBody requestBody = builder.build();

    //构建请求
    Request request = new Request.Builder()
        .url(url)//地址
        .post(requestBody)//添加请求体
        .build();
    OkHttpClient client = new OkHttpClient();
    HouLog.d("request:", request.toString());
    client.newCall(request).enqueue(new getDataCallback(listener));
  }

  public void uploadImageByPostFromFiles(List<File> imageFiles, List<RequestParams> list,
      String url,
      final RequestListener listener) {

    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

    for (RequestParams params : list) {
      if (null == params || null == params.getKey()) {
        continue;
      }
      builder.addFormDataPart(params.getKey(), params.getValue());
    }

    for (int i = 0; i < imageFiles.size(); i++) {
      File f = imageFiles.get(i);
      if (f != null) {
        HouLog.d("图片" + i + "名", f.getName());
        builder.addFormDataPart("img[]", f.getName(), RequestBody.create(MEDIA_TYPE_JPEG, f));
      }
    }

    MultipartBody requestBody = builder.build();

    //构建请求
    Request request = new Request.Builder()
        .url(url)//地址
        .post(requestBody)//添加请求体
        .build();
    OkHttpClient client = new OkHttpClient();
    HouLog.d("request:", request.toString());
    client.newCall(request).enqueue(new getDataCallback(listener));
  }

  /**
   * post 请求
   */
  public void getDataByPost(List<RequestParams> list, String url, final RequestListener listener) {
    PostFormBuilder builder = OkHttpUtils
        .post()
        .url(url);
    for (RequestParams params : list) {
      if (null == params || null == params.getKey()) {
        continue;
      }
      builder.addParams(params.getKey(), params.getValue());
    }
    builder.build().execute(new MyStringCallback(listener));
  }

  /**
   * get 请求
   */
  public void getDataByGet(List<RequestParams> list, String url, final RequestListener listener) {
    GetBuilder builder = OkHttpUtils
        .get()
        .url(url);

    if (null != list) {
      for (RequestParams params : list) {
        if (null == params || null == params.getKey()) {
          continue;
        }
        builder.addParams(params.getKey(), params.getValue());
      }
    }
  }

}

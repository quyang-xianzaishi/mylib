package com.example.administrator.lubanone;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.antfortune.freeline.FreelineCore;
import com.example.administrator.lubanone.Listener.MyConversationBehaviorListener;
import com.example.administrator.lubanone.Listener.MyConversationListBehaviorListener;
import com.example.administrator.lubanone.Listener.MyIUnReadMessageObserver;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.land.LandActivity;
import com.example.administrator.lubanone.interfaces.NetStateListener;
import com.example.administrator.lubanone.manager.CrashHandler;
import com.example.administrator.lubanone.manager.JyActivityManager;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.receiver.NetWorkStateReceiver;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.administrator.lubanone.utils.RxNetUtils;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation.ConversationType;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/6/20.
 */

public class MyApplication extends MultiDexApplication implements NetStateListener {

  private static RefWatcher mWatcher;

  {
    PlatformConfig.setWeixin("wx1c1cb3739a4471cd", "1bb63c50231b608e721b4be2fd033a34");
  }

  public static MyApplication sMyApplication;
  public static NetStateListener mlistener;
  public static String activateCode = "0";
  public static RequestNet requestNet;
  //存放activity引用
  private List<BaseActivity> appActivityList = appActivityList = new ArrayList<>();
  private static Context mContext;

  public static RxNetUtils rxNetUtils;
  public OkHttpClient client;
  public int unReadCount;//融云消息未读数量
  public static int customerUnReadCount;//客服消息未读数量
  public static int listUnReadCount = 0;//列表消息未读数量
  private static String host = "http://103.210.239.20/";
  //private static String host = "http://192.168.3.215/api.php/Home/";

  private static boolean isExecute = true;

  public static String getHost() {
    return host;
  }

  private static MyApplication instance = null;

  public MyApplication() {
  }

  public static MyApplication getInstance() {
    if (null == instance) {
      synchronized (MyApplication.class) {
        if (instance == null) {
          instance = new MyApplication();
        }
      }
    }
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (com.example.administrator.lubanone.Config.test_memery) {
      mWatcher = LeakCanary.install(this);
    }
    ZXingLibrary.initDisplayOpinion(this);
    Fresco.initialize(this);
    sMyApplication = this;
    mContext = this;
    requestNet = new RequestNet();
    initOk3();
    FreelineCore.init(this);

    mlistener = this;
    registerBroadCast();

    addActivityManager();

    CrashHandler catchHandler = CrashHandler.getInstance();
    catchHandler.init(getApplicationContext());
    //友盟分享
    Config.DEBUG = true;
    QueuedWork.isUseThreadPool = false;
    UMShareAPI.get(this);//初始化友盟分享sdk
    Config.isJumptoAppStore = true;//对应平台没有安装的时候跳转转到应用商店下载
//    mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN);
//    if(Build.VERSION.SDK_INT>=23){
//      String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//          Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
//          Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE,
//          Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP,
//          Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS,
//          Manifest.permission.WRITE_APN_SETTINGS};
//      ActivityCompat.requestPermissions(this, mPermissionList, 123);
//    }

    initRong();//初始化融云
    JPushInterface.setDebugMode(true);//设置极光打印日志模式
    JPushInterface.init(this);//初始化极光

    client = getOkHttpClient();
    rxNetUtils = new RxNetUtils(client);

  }

  public static RefWatcher getWatcher() {
    return mWatcher;
  }


  private void registerBroadCast() {
    //实例化过滤器；
    IntentFilter intentFilter = new IntentFilter();
    //添加过滤的Action值；
    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

    //实例化广播监听器；
    NetWorkStateReceiver netChangReceiver = new NetWorkStateReceiver();

    //将广播监听器和过滤器注册在一起；
    registerReceiver(netChangReceiver, intentFilter);

  }

  public static Context getContext() {
    return mContext;
  }


  public void initOk3() {
    HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        //其他配置
        .build();

    OkHttpUtils.initClient(okHttpClient);
  }

  private InputStream[] getCerInputStream() {
    return null;
  }

  //获取系统当前时间
  public String getSystemCurrentTime() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());
    return format.format(curDate);
  }


  //安全退出
  public void safeOut(final MyApplication application, final Activity activity) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().logout();
    } 
    if (null != application && null != activity) {
      HouToast.showLongToast(activity, activity.getString(R.string.token_no_work));
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
//          if(JyActivityManager.getInstance().getRunningActivityName().equals
//              ("com.example.administrator.lubanone.activity.land.LandActivity")){
          if ("com.example.administrator.lubanone.activity.land.LandActivity"
              .equals(JyActivityManager.getInstance().getRunningActivityName())) {
          } else {
            HouLog.d("safeOut");
            JyActivityManager.getInstance().finishAllActivity();
            activity.startActivity(new Intent(activity, LandActivity.class));
            activity.finish();
          }
        }
      }, 3500);
    }
  }

  //添加activity引用到集合
  public void addActivity(BaseActivity activity) {
    appActivityList.add(activity);
    HouLog
        .d("activity个数" + appActivityList.size() + "  activity引用集合 " + appActivityList.toString());
  }

  //从集合中删除activity引用
  public void removeActivity(BaseActivity activity) {
    appActivityList.remove(activity);
  }

  public void removeAllActivity() {
    HouLog.d("被删除activity个数 " + appActivityList.size());
    for (Activity activity : appActivityList) {
      HouLog.d("被删除activity ", activity.getLocalClassName());
      activity.finish();
    }
  }

  /**
   * 判断某一个activity是否存在
   */
  public boolean hasActivity(Class clazz) {
    for (BaseActivity activity : appActivityList) {
      if (activity.getClass() == clazz) {
        return true;
      }
    }
    return false;
  }

  //取出所有Activity引用，调用finish()，销毁activity
  public void removeOtherActivity(BaseActivity target) {
    for (BaseActivity activity : appActivityList) {
      if (null == activity) {
        continue;
      }
      Log.d("activity:=", activity.getLocalClassName());
      if (!activity.isFinishing()) {
        if (activity != target) {
          activity.finish();
        }
      }
    }
  }

  private OkHttpClient getOkHttpClient() {

    //定制OkHttp
    OkHttpClient.Builder httpClientBuilder = new OkHttpClient
        .Builder();
    httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
    httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
    httpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);

    if (BuildConfig.DEBUG) {
      //日志显示级别
      //新建log拦截器
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
          new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
              Log.d("hou", "OkHttp====Message:" + message);
            }
          });

      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
      httpClientBuilder
          .addInterceptor(loggingInterceptor);
    }

    return httpClientBuilder.build();
  }

  private void initRong() {

    if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
        "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
      /**
       * IMKit SDK调用第一步 初始化
       */
      RongIM.init(this);
    }

    if (SPUtils.getStringValue(getApplicationContext(), "user_info", "rongtoken", "") != null &&
        SPUtils.getStringValue(getApplicationContext(), "user_info", "rongtoken", "").length()
            > 0) {
      //如果用户已经登录，本地保存用户id,token不为空，连接融云服务器
      String token = SPUtils.getStringValue(getApplicationContext(), "user_info", "rongtoken", "");
      RongIM.connect(token, new RongIMClient.ConnectCallback() {
        @Override
        public void onTokenIncorrect() {
        }

        @Override
        public void onSuccess(String userid) {
          Log.e("RongLog", "--onSuccess" + userid);
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {
          ToastUtil.showShort(errorCode.toString(), getApplicationContext());
        }
      });
    }

    RongIM.getInstance().addUnReadMessageCountChangedObserver(
        new MyIUnReadMessageObserver(),
        new ConversationType[]{ConversationType.GROUP, ConversationType.PRIVATE,
            ConversationType.CUSTOMER_SERVICE});
    RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
    RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
  }

  private void addActivityManager() {
    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        JyActivityManager.getInstance().addActivity(activity);
      }

      @Override
      public void onActivityStarted(Activity activity) {
      }

      @Override
      public void onActivityResumed(Activity activity) {
        JyActivityManager.getInstance().setCurrentActivity(activity);
      }

      @Override
      public void onActivityPaused(Activity activity) {
      }

      @Override
      public void onActivityStopped(Activity activity) {
      }

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
      }

      @Override
      public void onActivityDestroyed(Activity activity) {
        if (activity.isFinishing()) {
          JyActivityManager.getInstance().removeActivity(activity);
        }
      }
    });
  }

  @Override
  public void netChange(boolean hasnet) {
    if (hasnet) {
      if (CollectionUtils.isNotEmpty(appActivityList)) {
        BaseActivity activity = appActivityList.get(appActivityList.size() - 1);
        if (null != activity) {
          activity.loadData();
        }
      }
    }
  }

  @NonNull
  public static String isMainThread() {
    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
      return "Main Thread!!!!!!!!!!!!!!!";
    }
    return "Work Thread!!!!!!!!!!!!!!!!!";
  }

}

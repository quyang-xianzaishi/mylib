package com.example.administrator.lubanone.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import com.example.administrator.lubanone.R;
import com.example.qlibrary.utils.DemicalUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Description:更新下载后台进程
 * User: chenzheng
 * Date: 2016/12/14 0014
 * Time: 16:24
 */
public class UpdateService extends Service {

  private String apkUrl;
  private String filePath;
  private NotificationManager notificationManager;
  private Notification notification;
  private int mContentLength;

  protected static final int SUCCESS_MESSAGE = 0;
  protected static final int FAILURE_MESSAGE = 1;
  protected static final int START_MESSAGE = 2;
  protected static final int FINISH_MESSAGE = 3;
  protected static final int NETWORK_OFF = 4;
  private static final int PROGRESS_CHANGED = 5;


  Handler handler = new Handler(Looper.getMainLooper()) {

    @Override
    public void handleMessage(Message msg) {
      handleSelfMessage(msg);
    }
  };


  @Override
  public void onCreate() {
    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    filePath = Environment.getExternalStorageDirectory() + "clown.apk";
//    filePath = Environment.getExternalStorageDirectory() + "/AppUpdate/czhappy.apk";
    System.out.println("UpdateService.onCreate=" + filePath);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent == null) {
      notifyUser(getString(R.string.download_fail),
          getString(R.string.download_fail), 0);

      stopSelf();

    } else {
      apkUrl = intent.getStringExtra("apkUrl");
      notifyUser(getString(R.string.start_download),
          getString(R.string.start_download),
          0);
      startDownload();
    }
    return super.onStartCommand(intent, flags, startId);
  }

  private void startDownload() {

    new Thread(new Runnable() {
      @Override
      public void run() {
        getJsonByInternet(apkUrl);
      }
    }).start();

//    UpdateManager.getInstance().startDownloads(apkUrl, filePath, new UpdateDownloadListener() {
//      @Override
//      public void onStarted() {
//        Log.e("tag", "onStarted()");
//
//        Log.e("UpdateService", "onStarted=");
//
//      }
//
//      @Override
//      public void onProgressChanged(int progress, String downloadUrl) {
//        Log.e("UpdateService", "onProgressChanged=" + progress);
//
//        notifyUser(getString(R.string.update_download_progressing),
//            getString(R.string.update_download_progressing), progress);
//      }
//
//      @Override
//      public void onFinished(float completeSize, String downloadUrl) {
//        Log.e("tag", "onFinished()");
//        notifyUser(getString(R.string.update_download_finish),
//            getString(R.string.update_download_finish), 100);
//        stopSelf();
//      }
//
//      @Override
//      public void onFailure() {
//        Log.e("tag", "onFailure()");
//        notifyUser(getString(R.string.update_download_failed),
//            getString(R.string.update_download_failed), 0);
//        stopSelf();
//      }
//    });
  }

  private boolean isDownloading;

  /**
   * 从网络获取json数据,(String byte[})
   */
  public String getJsonByInternet(String path) {
    try {
      isDownloading = true;
      Log.e("UpdateService", "getJsonByInternet=" + "开始下载");
      URL url = new URL(path.trim());
      //打开连接
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("POST");
      urlConnection.setConnectTimeout(5000);
      urlConnection.setRequestProperty("Connection", "Keep-Alive");
      urlConnection.setRequestProperty("Accept-Encoding", "identity");
      urlConnection.connect();

      mContentLength = urlConnection.getContentLength();

      if (200 == urlConnection.getResponseCode()) {

        Log.e("UpdateService", "getJsonByInternet=" + "响应成功");

        //获取响应流
        sendResponseMessage(urlConnection.getInputStream());

      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private int completeSize;
  private int progress;

  void sendResponseMessage(InputStream is) {

    System.out.println("UpdateService.sendResponseMessage");

    RandomAccessFile randomAccessFile = null;
    completeSize = 0;
    try {
      byte[] buffer = new byte[1024 * 8];
      int length = -1;//读写长度
      randomAccessFile = new RandomAccessFile(
//          new File(filePath), "rwd");
          new File(Environment.getExternalStorageDirectory(), "clown.apk"), "rwd");

      while ((length = is.read(buffer)) != -1) {

        if (isDownloading) {

          randomAccessFile.write(buffer, 0, length);
          completeSize += length;

          if (completeSize <= mContentLength) {

            String divide = DemicalUtil
                .divide(completeSize + "", "" + mContentLength, 4, BigDecimal.ROUND_DOWN);

            String v = String.valueOf(Float.parseFloat(divide) * 100);
            String substring = v.substring(0, v.indexOf("."));
//
            Log.e("UpdateServicedddd",
                "sendResponseMessage=" + substring + ", " + Integer.parseInt(substring) + ", "
                    + divide);
            if (Integer.parseInt(substring) <= 100) {//隔30次更新一次notification
              try {
                Thread.sleep(100);
                sendProgressChangedMessage(Integer.parseInt(substring));
              } catch (InterruptedException e) {
                e.printStackTrace();
                sendFailureMessage(FailureCode.IO);
              }
            }
          } else {
            isDownloading = false;
            sendFinishMessage();
          }
        }
      }
    } catch (IOException e) {
      sendFailureMessage(FailureCode.IO);

    } finally {
      try {
        if (is != null) {
          is.close();
        }
        if (randomAccessFile != null) {
          randomAccessFile.close();
        }
      } catch (IOException e) {
        sendFailureMessage(FailureCode.IO);
      }
    }
  }


  protected Message obtainMessage(int responseMessge, Object response) {
    Message msg = null;
    if (handler != null) {
      msg = handler.obtainMessage(responseMessge, response);
    } else {
      msg = Message.obtain();
      msg.what = responseMessge;
      msg.obj = response;
    }
    return msg;

  }

  protected void sendFinishMessage() {
    sendMessage(obtainMessage(FINISH_MESSAGE, null));
  }

  private void sendProgressChangedMessage(int progress) {
    sendMessage(obtainMessage(PROGRESS_CHANGED, new Object[]{progress}));

  }

  protected void sendFailureMessage(FailureCode failureCode) {
    sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{failureCode}));

  }

  protected void sendMessage(Message msg) {
    if (handler != null) {
      handler.sendMessage(msg);
    } else {
      handleSelfMessage(msg);
    }

  }

  protected void onFinish() {
    notifyUser("下载完成", "下载完成", completeSize);
  }


  protected void handleSelfMessage(Message msg) {

    Object[] response;
    switch (msg.what) {
      case FAILURE_MESSAGE:
        response = (Object[]) msg.obj;
        sendFailureMessage((FailureCode) response[0]);
        break;
      case PROGRESS_CHANGED://正在下载
        response = (Object[]) msg.obj;
        Log.e("正在下载", "handleSelfMessage=" + response[0]);
//        handleProgressChangedMessage(((Integer)response[0]).intValue());
        notifyUser("正在下载", "正在下载", (Integer) response[0]);
        break;
      case FINISH_MESSAGE://下载完成
        onFinish();
        break;
    }
  }

  private String getTwoPointFloatStr(float value) {
    DecimalFormat df = new DecimalFormat("0.00000000000");
    return df.format(value);

  }


  /**
   * 更新notification
   */
  private void notifyUser(String result, String msg, int progress) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    builder.setSmallIcon(R.mipmap.logo)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
        .setContentTitle(getString(R.string.app_name));
    if (progress > 0 && progress <= 100) {

      builder.setProgress(100, progress, false);

    } else {
      builder.setProgress(0, 0, false);
    }
    builder.setAutoCancel(true);
    builder.setWhen(System.currentTimeMillis());
    builder.setTicker(result);
    builder.setContentIntent(progress >= 100 ? getContentIntent() :
        PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
    notification = builder.build();
    notificationManager.notify(0, notification);
  }

  /**
   * 进入apk安装程序
   */
  private PendingIntent getContentIntent() {
//    File apkFile = new File(filePath);
    if (new File(Environment.getExternalStorageDirectory(), "clown.apk").exists()) {
      System.out.println("UpdateService.getContentIntent=exist");
    }
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setDataAndType(
        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "clown.apk")),
//    intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()),
        "application/vnd.android.package-archive");
    PendingIntent pendingIntent = PendingIntent
        .getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    startActivity(intent);
    return pendingIntent;

  }


  @Nullable
  @Override
  public IBinder onBind(Intent intent) {

    return null;
  }

  /**
   * 下载过程中的异常
   */
  public enum FailureCode {
    UnknownHost, Socket, SocketTimeout, connectionTimeout, IO, HttpResponse,
    Json, Interrupted

  }

}

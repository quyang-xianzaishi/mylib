package com.example.qlibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.telephony.TelephonyManager;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/5/29.
 */

public class NetUtil {

  /**
   * Unknown network class
   */
  public static final int NETWORK_CLASS_UNKNOWN = 0;

  /**
   * wifi net work
   */
  public static final int NETWORK_WIFI = 1;

  /**
   * "2G" networks
   */
  public static final int NETWORK_CLASS_2_G = 2;

  /**
   * "3G" networks
   */
  public static final int NETWORK_CLASS_3_G = 3;

  /**
   * "4G" networks
   */
  public static final int NETWORK_CLASS_4_G = 4;


  /**
   * 获取网络类型
   */
  public static int getNetWorkStatus(Context context) {
    int netWorkType = NETWORK_CLASS_UNKNOWN;

    ConnectivityManager connectivityManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

    if (networkInfo != null && networkInfo.isConnected()) {
      int type = networkInfo.getType();

      if (type == ConnectivityManager.TYPE_WIFI) {
        netWorkType = NETWORK_WIFI;
      } else if (type == ConnectivityManager.TYPE_MOBILE) {
        netWorkType = getNetWorkClass(context);
      }
    }

    return netWorkType;
  }


  public static String getNetWorkStatusString(Context context) {
    int netWorkStatus = getNetWorkStatus(context);
    if (NETWORK_WIFI == netWorkStatus) {
      return "wifi";
    }
    if (NETWORK_CLASS_2_G == netWorkStatus) {
      return "2G";
    }
    if (NETWORK_CLASS_3_G == netWorkStatus) {
      return "3G";
    }
    if (NETWORK_CLASS_4_G == netWorkStatus) {
      return "4G";
    }
    return "";
  }


  private static int getNetWorkClass(Context context) {
    TelephonyManager telephonyManager = (TelephonyManager) context
        .getSystemService(Context.TELEPHONY_SERVICE);

    switch (telephonyManager.getNetworkType()) {
      case TelephonyManager.NETWORK_TYPE_GPRS:
      case TelephonyManager.NETWORK_TYPE_EDGE:
      case TelephonyManager.NETWORK_TYPE_CDMA:
      case TelephonyManager.NETWORK_TYPE_1xRTT:
      case TelephonyManager.NETWORK_TYPE_IDEN:
        return NETWORK_CLASS_2_G;
      case TelephonyManager.NETWORK_TYPE_UMTS:
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
      case TelephonyManager.NETWORK_TYPE_HSDPA:
      case TelephonyManager.NETWORK_TYPE_HSUPA:
      case TelephonyManager.NETWORK_TYPE_HSPA:
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
      case TelephonyManager.NETWORK_TYPE_EHRPD:
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        return NETWORK_CLASS_3_G;

      case TelephonyManager.NETWORK_TYPE_LTE:
        return NETWORK_CLASS_4_G;

      default:
        return NETWORK_CLASS_UNKNOWN;
    }
  }


  /**
   * 判断是否网络可用
   */
  public static boolean isConnected(Context context) {
    if (null == context) {
      return false;
    }
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

    if (null != networkInfo && networkInfo.isAvailable()) {
      return true;
    }
    return false;
  }


  private long preRxBytes;

  /**
   * 获取当前网速
   */
  public double getNetSpeed() {
    long curRxBytes = getNetworkRxBytes();
    if (preRxBytes == 0) {
      preRxBytes = curRxBytes;
    }
    long bytes = curRxBytes - preRxBytes;
    preRxBytes = curRxBytes;
    //int kb = (int) Math.floor(bytes / 1024 + 0.5);
    double kb = (double) bytes / (double) 1024;
    BigDecimal bd = new BigDecimal(kb);

    return bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
  }


  /**
   * 获取当前上传流量总和
   */
  public static long getNetworkTxBytes() {
    return TrafficStats.getTotalTxBytes();
  }


  /**
   * 获取当前下载流量总和
   */
  public static long getNetworkRxBytes() {
    return TrafficStats.getTotalRxBytes();
  }
}

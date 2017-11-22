package com.example.administrator.lubanone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.interfaces.NetStateListener;

/**
 * Created by quyang on 2017/7/22.
 */

public class NetWorkStateReceiver extends BroadcastReceiver {

  private NetStateListener mListener = MyApplication.mlistener;

  @Override
  public void onReceive(Context context, Intent intent) {

    //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
    if (android.os.Build.VERSION.SDK_INT < VERSION_CODES.M) {

      //获得ConnectivityManager对象
      ConnectivityManager connMgr = (ConnectivityManager) context
          .getSystemService(Context.CONNECTIVITY_SERVICE);

      //获取ConnectivityManager对象对应的NetworkInfo对象
      //获取WIFI连接的信息
      NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      //获取移动数据连接的信息
      NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
        mListener.netChange(true);
//        Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
      } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
        mListener.netChange(true);
//        Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
      } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//        Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
        mListener.netChange(true);
      } else {
//        Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
      }
    } else {

      if (VERSION.SDK_INT >= VERSION_CODES.N) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
            Context.CONNECTIVITY_SERVICE);
        manager.requestNetwork(new NetworkRequest.Builder().build(), new NetworkCallback() {
          @Override
          public void onAvailable(Network network) {
            super.onAvailable(network);
            mListener.netChange(true);
          }
        });
      }

      //API23时使用下面的方式进行网络监听
      if (VERSION.SDK_INT == 23) {
        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取所有网络连接的信息
        Network[] networks = connMgr.getAllNetworks();
        //用于存放网络连接信息
        StringBuilder sb = new StringBuilder();
        //通过循环将网络信息逐个取出来
        for (int i = 0; i < networks.length; i++) {
          //获取ConnectivityManager对象对应的NetworkInfo对象
          NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
          sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
          if (networkInfo.isConnected()) {
            mListener.netChange(true);
          }
        }
      }
    }
  }

}

package com.example.administrator.lubanone.Listener;

import android.content.Context;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by Administrator on 2017\9\13 0013.
 */

public class DemoNotificationReceiver extends PushMessageReceiver {
  @Override
  public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
    return false;
  }
  @Override
  public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
    return false;
  }
}

package com.example.qlibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * Created by admistrator on 2017/7/25.
 */

public class ActivityUtil {

  /**
   * activity在显示完吐司后的timeSecond秒后销毁
   */
  public static void finishActivityAfterBeforToast(long timeSecond,
      @NonNull final Activity activity,
      @NonNull String msgOfToast) {
    ToastUtil.showShort(msgOfToast, activity);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (!activity.isFinishing()) {
          activity.finish();
        }
      }
    }, timeSecond * 1000);
  }


  public static void startActivity(@NonNull Activity activity, @NonNull Intent intent) {
    if (intent.resolveActivity(activity.getPackageManager()) != null) {
      activity.startActivity(intent);
    }
  }


  public static void startAndFinishActivity(@NonNull Activity activity, @NonNull Intent intent) {
    if (intent.resolveActivity(activity.getPackageManager()) != null) {
      activity.startActivity(intent);
      activity.finish();
    }
  }

}

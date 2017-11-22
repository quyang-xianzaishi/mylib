package com.example.qlibrary.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/6/29.
 */

public class WindoswUtil {

  public static int getWindowWidth(Activity context) {
    WindowManager manager = context.getWindowManager();
    DisplayMetrics outMetrics = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(outMetrics);
    int width2 = outMetrics.widthPixels;
    return width2;
  }

  public static int getWindowHeight(Activity context) {
    WindowManager manager = context.getWindowManager();
    DisplayMetrics outMetrics = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(outMetrics);
    int height2 = outMetrics.heightPixels;
    return height2;
  }


  public static void setFullScreen(Activity activity) {
    activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
  }


  public static void hideActionBar(Activity activity) {
    ActionBar actionBar = activity.getActionBar();
    if (null != actionBar && actionBar.isShowing()) {
      actionBar.hide();
    }
  }

  public static void showActionBar(Activity activity) {
    ActionBar actionBar = activity.getActionBar();
    if (null != actionBar && !actionBar.isShowing()) {
      actionBar.show();
    }
  }
}

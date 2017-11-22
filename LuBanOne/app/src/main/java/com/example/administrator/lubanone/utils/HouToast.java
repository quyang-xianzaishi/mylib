package com.example.administrator.lubanone.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hou on 2017/9/4.
 */

public class HouToast {

  private static Toast mToast;


  public static void showLongToast(Context context, String msg) {

    if (mToast == null) {
      mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
    } else {
      mToast.setText(msg);
    }
    mToast.show();
  }

}

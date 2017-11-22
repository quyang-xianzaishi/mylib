package com.example.qlibrary.utils;

/**
 * Created by admistrator on 2017/7/21.
 */

public class SDUtil {

  public static boolean existSDCard() {
    if (android.os.Environment.getExternalStorageState().equals(
        android.os.Environment.MEDIA_MOUNTED)) {
      return true;
    } else {
      return false;
    }
  }

}

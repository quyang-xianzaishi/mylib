package com.example.administrator.lubanone.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.v4.content.FileProvider;
import com.example.administrator.lubanone.BuildConfig;
import java.io.File;

/**
 * Created by Administrator on 2017/7/30.
 */

public class UriUtils {

  public static Uri getUri(File file, Context context) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
      return FileProvider
          .getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider",
              file);
    } else {
      return Uri.fromFile(file);
    }
  }

}

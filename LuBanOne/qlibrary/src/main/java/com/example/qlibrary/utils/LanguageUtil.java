package com.example.qlibrary.utils;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.os.LocaleList;
import java.util.Locale;

/**
 * Created by admistrator on 2017/7/31.
 */

public class LanguageUtil {

  public static int getCountryType() {
    String country = Locale.getDefault().getCountry();
    if ("CN".equals(country)) {
      return 0;
    }
    if ("US".equals(country)) {
      return 1;
    }
    if ("ID".equals(country)) {
      return 2;
    }
    return -1;
  }


  @TargetApi(VERSION_CODES.N)
  public static void get() {
    LocaleList aDefault = LocaleList.getDefault();
    int size = aDefault.size();
    System.out.println("LanguageUtil.get" + size);
  }

}

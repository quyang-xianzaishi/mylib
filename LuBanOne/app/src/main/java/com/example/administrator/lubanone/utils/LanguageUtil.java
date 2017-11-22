package com.example.administrator.lubanone.utils;

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

}

package com.example.qlibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/31.
 */

public class DateUtil {

  private static final String MODEL = "yyyy-MM-dd HH:mm:ss";

  private static final String YEAR_MONTH = "yyyy年MM月";

  private static final String YEAR_MONTH_ONE = "yyyy-MM";

  private static final String YEAR_MONTH_DAY = "yyyy年MM月dd日";

  public static String getStringData(long timeSnap) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_MONTH_DAY);
    return simpleDateFormat.format(new Date(timeSnap));
  }

  public static String getStringDataMonth(long timeSnap) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_MONTH);
    return simpleDateFormat.format(new Date(timeSnap));
  }

  public static String getDateString(long timeSnap) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MODEL);
    return simpleDateFormat.format(new Date(timeSnap));
  }

  public static Long parseDateString(String timeSnap) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MODEL);
    try {
      Date parse = simpleDateFormat.parse(timeSnap);
      return parse.getTime();
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String getDateYM(long timeSnap) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_MONTH_ONE);
    return simpleDateFormat.format(new Date(timeSnap));
  }


}

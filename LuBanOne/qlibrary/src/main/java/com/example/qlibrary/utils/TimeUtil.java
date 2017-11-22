package com.example.qlibrary.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admistrator on 2017/8/24.
 */

public class TimeUtil {

  /**
   * 返回格式： 142小时12分钟12秒
   */
  public static List<Integer> getFormatDiffTimeString(Long diffTime) {
    if (null == diffTime || diffTime < 0) {
      return null;
    }
    int hours = (int) ((diffTime / (1000 * 60 * 60)));
    int minutes = (int) ((diffTime / (1000 * 60)) % 60);
    int seconds = (int) (diffTime / 1000) % 60;
    ArrayList<Integer> list = new ArrayList<>();
    list.add(hours);
    list.add(minutes);
    list.add(seconds);

    return list;
  }


  /**
   * 返回格式： 12天42小时12分钟12秒
   */
  public static List<Integer> getFormatDiffTimeStringOne(Long diffTime) {
    if (null == diffTime || diffTime < 0) {
      return null;
    }
    int seconds = (int) (diffTime / 1000) % 60;
    int minutes = (int) ((diffTime / (1000 * 60)) % 60);
    int hours = (int) ((diffTime / (1000 * 60 * 60)) % 24);
    int days = (int) ((diffTime / (1000 * 60 * 60 * 24)));

    ArrayList<Integer> list = new ArrayList<>();
    list.add(seconds);
    list.add(minutes);
    list.add(hours);
    list.add(days);

    return list;
  }
}

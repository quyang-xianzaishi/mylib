package com.example.qlibrary.utils;


import android.widget.Button;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by admistrator on 2017/7/27.
 */

public class DemicalUtil {

  //  System.err.println("ROUND_DOWN="+bigDecimal.setScale(1,BigDecimal.ROUND_DOWN));//只取小数点后一位，不管下一位
//    System.err.println("ROUND_UP="+bigDecimal.setScale(1,BigDecimal.ROUND_UP));//只要后面有数字就向上进一位，0除过
//    System.err.println("ROUND_HALF_UP="+bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP));//下一位大于等于0.5就会向上进一位，否则只取小数点后一位
//    System.err.println("ROUND_HALF_DOWN="+bigDecimal.setScale(1,BigDecimal.ROUND_HALF_DOWN));//去小数点后1为，除非下一位大于0.5才会向上进一位


  public static final int ROUND_DOWN = BigDecimal.ROUND_DOWN;
  public static final int ROUND_UP = BigDecimal.ROUND_UP;
  public static final int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;
  public static final int ROUND_HALF_DOWN = BigDecimal.ROUND_HALF_DOWN;


  /**
   * 两个数相乘，去除小数点后面的；
   */
  public static String multile(String a, String b) {
    BigDecimal bigDecimal = new BigDecimal(a);
    BigDecimal bigDecimalb = new BigDecimal(b);
    String s = bigDecimal.multiply(bigDecimalb).toPlainString();
    if (s.contains(".")) {
      int i = s.indexOf(".");
      return s.substring(0, i);
    } else {
      return s;
    }
  }

  /**
   * 两个数相乘
   */
  public static String multileNormal(String a, String b) {
    BigDecimal bigDecimal = new BigDecimal(a);
    BigDecimal bigDecimalb = new BigDecimal(b);
    return bigDecimal.multiply(bigDecimalb).toPlainString();
  }


  public static String add(String a, String b) {
    BigDecimal bigDecimal = new BigDecimal(a);
    BigDecimal bigDecimalb = new BigDecimal(b);
    return bigDecimal.add(bigDecimalb).toPlainString();
  }


  /**
   * @param a 除数
   * @param b 被除数
   * @param scale 精度
   * @param roundMode 精度模式 为null的时候表示精度后一位四舍五入
   */
  public static String divide(String a, String b, int scale, Integer roundMode) {
    BigDecimal bigDecimal = new BigDecimal(a);
    BigDecimal bigDecimalb = new BigDecimal(b);
    //默认是精度后一位四舍五入
    return bigDecimal.divide(bigDecimalb, scale, roundMode == null ? ROUND_HALF_UP : roundMode)
        .toPlainString();
  }


  public static String subtract(String a, String b) {
    BigDecimal bigDecimal = new BigDecimal(a);
    BigDecimal bigDecimalb = new BigDecimal(b);
    return bigDecimal.subtract(bigDecimalb).toPlainString();
  }

  /**
   * 比较a是否大于b
   */
  public static boolean great(String a, String b) {
    int i = new BigDecimal(a).compareTo(new BigDecimal(b));
    if (1 == i) {
      return true;
    }
    return false;
  }

  /**
   * 比较a是否大于等于b
   */
  public static boolean greatEqauls(String a, String b) {
    int i = new BigDecimal(a).compareTo(new BigDecimal(b));
    if (0 <= i) {
      return true;
    }
    return false;
  }


  /**
   * 比较a是否小于b
   */
  public static boolean less(String a, String b) {
    int i = new BigDecimal(a).compareTo(new BigDecimal(b));
    if (i == -1) {
      return true;
    }
    return false;
  }

  /**
   * 比较a是否小于等于b
   */
  public static boolean lessEqual(String a, String b) {
    int i = new BigDecimal(a).compareTo(new BigDecimal(b));
    if (i <= 0) {
      return true;
    }
    return false;
  }

  /**
   * 比较a是否等于b
   */
  public static boolean equal(String a, String b) {
    int i = new BigDecimal(a).compareTo(new BigDecimal(b));
    if (i == 0) {
      return true;
    }
    return false;
  }


  /**
   * 保留2位小数，第三位四舍五入
   */
  public static String hasTwo(Object target) {
    DecimalFormat format = new DecimalFormat("#.00");
    return format.format(target);
  }
}

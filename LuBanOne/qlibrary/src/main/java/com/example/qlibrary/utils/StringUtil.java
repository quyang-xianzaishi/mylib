package com.example.qlibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.example.qlibrary.interfaces.OnClickItemListener;
import com.example.qlibrary.interfaces.OnClickListener;
import com.google.gson.Gson;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/24.
 */
public class StringUtil {

  public static boolean isEmty(String target) {
    if (null == target || "".equals(target) || "null".equals(target)) {
      return true;
    }
    return false;
  }

  public static String getBufferString(String... srcStriing) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String a : srcStriing) {
      stringBuilder.append(a);
    }
    return stringBuilder.toString();
  }

  public static boolean isNotEmpty(String target) {
    return !isEmty(target);
  }

  public static boolean containSub(String src, String sub) {
    if (isEmty(src)) {
      return false;
    }
    if (src.contains(sub)) {
      return true;
    }
    return false;
  }

  public static boolean hasLength(String str) {
    return hasLength((CharSequence) str);
  }

  public static boolean hasLength(CharSequence str) {
    return (str != null && str.length() > 0);
  }

  // 判断一个字符串是否含有数字
  public static boolean hasNumber(String content) {
    boolean flag = false;
    Pattern p = Pattern.compile(".*\\d+.*");
    Matcher m = p.matcher(content);
    if (m.matches()) {
      flag = true;
    }
    return flag;
  }

  /**
   * 判断字符串是否含有中文
   */
  public static boolean isContainChinese(String str) {

    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    Matcher m = p.matcher(str);
    if (m.find()) {
      return true;
    }
    return false;
  }


  /**
   * 正则表达式验证昵称
   */
  public static boolean rexCheckNickName(String nickName) {
    // 昵称格式：，支持中英文、数字
    String regStr = "^([A-Z]|[a-z]|[0-9]){6,20}$";
    return nickName.matches(regStr);
  }

  /**
   * 正则表达式验证密码
   */
  public static boolean rexCheckPassword(String input, int startIndex, int endIndex) {
    // 6-20 位，字母、数字、字符
    String regStr = "^([A-Z]|[a-z]|[0-9]){" + startIndex + "," + endIndex + "}$";
    return input.matches(regStr);
  }

  /**
   * 只能是数字，长度startIndex-endIndex
   */
  public static boolean justNumber(String input, int startIndex, int endIndex) {
    // 数字
//    String regStr = "^([0-9]){1,20}$";
    String regStr = "^([0-9]){" + startIndex + "," + endIndex + "}$";
    return input.matches(regStr);
  }

  /**
   * 只能是数字，长度startIndex-endIndex
   */
  public static boolean justNumber(String input) {
    // 数字
//    String regStr = "^([0-9]){1,20}$";
    String regStr = "^([0-9]){" + 0 + "," + 100 + "}$";
    return input.matches(regStr);
  }


  /**
   * 只能是字母(包括大小写)，长度startIndex-endIndex
   */
  public static boolean justChar(String input, int startIndex, int endIndex) {
    String regStr = "^([A-Z]|[a-z]){" + startIndex + "," + endIndex + "}$";
//    String regStr = "^([A-Z]|[a-z]){1,20}$";
    return input.matches(regStr);
  }


  public static boolean hasSpace(String string) {
    return string.contains(" ");
  }


  public static boolean hasSpecailSymbol(String input) {
    String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    return input.matches(regEx);
  }

  public static String right(String str) {
    String sss = "";
    String string = str.replace(",", "");
    int b = string.length() / 3;
    if (string.length() >= 3) {
      int yushu = string.length() % 3;
      if (yushu == 0) {
        b = string.length() / 3 - 1;
        yushu = 3;
      }
      for (int i = 0; i < b; i++) {
        sss = sss + string.substring(0, yushu) + "," + string.substring(yushu, 3);
        string = string.substring(3, string.length());
      }
      sss = sss + string;
      return sss;
    }

    return sss + string;
  }


  /**
   * 设置textview文字的部分颜色
   */
  public static void setTextPartColor(String target, int colorId, int startIndex, int endIndex,
      TextView targetTextview) {
    SpannableStringBuilder builder = new SpannableStringBuilder(target);
    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
    ForegroundColorSpan redSpan = new ForegroundColorSpan(colorId);
    builder.setSpan(redSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    targetTextview.setText(builder);
  }


  /**
   * 设置textview文字的部分颜色,从startCharIndex字符串开始到结尾
   *
   * @param colorId 字体的颜色
   */
  public static void setTextPartColor(String target, int colorId, String startCharIndex,
      TextView targetTextview) {
    if (TextUtils.isEmpty(target)) {
      return;
    }
    SpannableStringBuilder builder = new SpannableStringBuilder(target);
    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
    ForegroundColorSpan redSpan = new ForegroundColorSpan(colorId);
    builder.setSpan(redSpan, target.indexOf(startCharIndex), target.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    targetTextview.setText(builder);
  }


  /**
   * 设置textview文字的部分颜色,从字符串开始到endCharIndex (不包括endCharIndex处的字符串)
   *
   * @param colorId 字体的颜色
   */
  public static void setTextPrePartColor(String target, int colorId, String endCharIndex,
      TextView targetTextview) {
    if (TextUtils.isEmpty(target)) {
      return;
    }
    SpannableStringBuilder builder = new SpannableStringBuilder(target);
    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
    ForegroundColorSpan redSpan = new ForegroundColorSpan(colorId);
    builder.setSpan(redSpan, 0, target.indexOf(endCharIndex),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    targetTextview.setText(builder);
  }


  /**
   * 将一个字符串修改为123，453，455
   */
  public static String getThreeString(String allPrice) {
    if (TextUtils.isEmpty(allPrice)) {
      return "";
    }
    String sss = "";
    String string = allPrice.replace(",", "");
    int b = string.length() / 3;
    if (string.length() >= 3) {
      int yushu = string.length() % 3;
      if (yushu == 0) {
        b = string.length() / 3 - 1;
        yushu = 3;
      }
      for (int i = 0; i < b; i++) {
        sss = sss + string.substring(0, yushu) + "," + string.substring(yushu, 3);
        string = string.substring(3, string.length());
      }
      sss = sss + string;
      return sss;
    }
    return allPrice;
  }


  /**
   * 设置textview部分文字颜色并可点击
   */
  public static void setPartColorAndClickable(String content, TextView textview, int startIndex,
      int endIndex, int colorId, OnClickListener listener) {
    SpannableStringBuilder spannable = new SpannableStringBuilder(content);
    textview.setMovementMethod(LinkMovementMethod.getInstance());
    spannable
        .setSpan(new TextClick(listener, colorId), startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    textview.setText(spannable);
  }


  private static class TextClick extends ClickableSpan {


    private OnClickListener mListener;
    private int colorId;

    TextClick(OnClickListener listener, int colorId) {
      this.mListener = listener;
      this.colorId = colorId;
    }

    @Override
    public void onClick(View widget) {
      TextView textview = (TextView) widget;
      //如果textview点击后有了蓝色背景，设置透明度，也可在布局文件里面设置    android:shadowColor="@android:color/transparent"
      textview.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
      mListener.onClick();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
//      super.updateDrawState(ds);
      ds.setColor(colorId);
//      ds.setColor(ds.linkColor);
    }
  }


  /**
   * 设置textview部分文字颜色并可点击 listview
   */
  public static void setPartColorAndClickableInList(String content, TextView textview,
      int startIndex,
      int endIndex, OnClickItemListener listener, int position, int colorId) {
    SpannableStringBuilder spannable = new SpannableStringBuilder(content);
    textview.setMovementMethod(LinkMovementMethod.getInstance());
    spannable
        .setSpan(new TextClickList(listener, position, colorId), startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    textview.setText(spannable);
  }


  private static class TextClickList extends ClickableSpan {


    private OnClickItemListener mListener;
    private int mPosition;
    private int colorId;

    TextClickList(OnClickItemListener listener, int position, int colorId) {
      this.mListener = listener;
      this.mPosition = position;
      this.colorId = colorId;
    }

    @Override
    public void onClick(View widget) {
      TextView textview = (TextView) widget;
      //如果textview点击后有了蓝色背景，设置透明度，也可在布局文件里面设置    android:shadowColor="@android:color/transparent"
//      textview.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
      mListener.onClick(mPosition);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
//      super.updateDrawState(ds);
//      ds.setColor(ds.linkColor);
      ds.setColor(colorId);
    }
  }


  public static String toJson(Object bean) {
    Gson gson = new Gson();
    return gson.toJson(bean);
  }

  public static String[] getStringArray(int id, Context context) {
    String[] stringArray = context.getResources().getStringArray(id);
    return stringArray;
  }


  public static void setTextSize(String target, TextView textView, int leftSize, int danwei) {
    Spannable WordtoSpan = new SpannableString(target);
    WordtoSpan.setSpan(new AbsoluteSizeSpan(leftSize, true), 0, target.indexOf("P"),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    WordtoSpan.setSpan(new AbsoluteSizeSpan(danwei, true), target.indexOf("P"), target.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    textView.setText(WordtoSpan);
  }

  public static void setTextSmallSize(String target, TextView textView, int leftSize, int danwei) {
    Spannable WordtoSpan = new SpannableString(target);
    WordtoSpan.setSpan(new AbsoluteSizeSpan(leftSize, true), 0, target.indexOf("p"),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    WordtoSpan.setSpan(new AbsoluteSizeSpan(danwei, true), target.indexOf("p"), target.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    textView.setText(WordtoSpan);
  }

  public static void setTextSizeNew(String target, TextView textView, int leftSize,
      char textIndexStart, int danwei, int colorId) {
    Spannable WordtoSpan = new SpannableString(target);
    WordtoSpan.setSpan(new AbsoluteSizeSpan(leftSize, true), 0, target.indexOf("P"),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    WordtoSpan.setSpan(new AbsoluteSizeSpan(danwei, true), target.indexOf("P"), target.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
    ForegroundColorSpan redSpan = new ForegroundColorSpan(colorId);
    WordtoSpan.setSpan(redSpan, target.indexOf(textIndexStart), target.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    textView.setText(WordtoSpan);
  }

  public static void setTextSizeNewOne(String target, TextView textView, int big, int small,char startCharSize,
      char textIndexStartColor, int colorId) {
    if (null == textView) {
      return;
    }
    //改变字体大小
    Spannable WordtoSpan = new SpannableString(target);
    WordtoSpan.setSpan(new AbsoluteSizeSpan(big, true), 0, target.indexOf(startCharSize),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    WordtoSpan.setSpan(new AbsoluteSizeSpan(small, true), target.indexOf(startCharSize), target.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
    ForegroundColorSpan redSpan = new ForegroundColorSpan(colorId);
    WordtoSpan.setSpan(redSpan, target.indexOf(textIndexStartColor), target.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      textView.setText(WordtoSpan);
  }



  public static String formatBankAccount(String target) {
    if (TextUitl.isEmpty(target)) {
      return "";
    }

    StringBuilder stringBuilder = new StringBuilder();

    String substring = target.substring(0, target.length() - 4);
    String end = target.substring(target.length() - 4, target.length());
    for (int i = 0; i < substring.length(); i++) {
      if (i != 0 && i % 5 == 4) {
        stringBuilder.append("   ");

        char c = substring.charAt(i);
        String s = String.valueOf(c);
        String replace = s.replace(c, '*');
        stringBuilder.append(replace);

      } else {
        char c = substring.charAt(i);
        String s = String.valueOf(c);
        String replace = s.replace(c, '*');
        stringBuilder.append(replace);
      }
    }

    return stringBuilder.toString() + "   " + end;


  }

}

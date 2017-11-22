package com.example.qlibrary.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 输入法
 * Created by admistrator on 2017/8/10.
 */

public class InputMethodUtil {


  public static void showInputMethod(Context context,EditText editText) {
    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//    inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
  }


  public static void hideInputMethod(Context context,EditText editText) {
    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.showSoftInput(editText, InputMethodManager.HIDE_NOT_ALWAYS);
  }



}

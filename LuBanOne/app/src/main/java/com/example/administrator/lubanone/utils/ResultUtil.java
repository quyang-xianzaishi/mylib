package com.example.administrator.lubanone.utils;

import com.example.qlibrary.entity.Result;

/**
 * Created by quyang on 2017/7/5.
 */

public class ResultUtil {

  public static boolean isSuccess(Result result) {
    if ("1".equals(result.getType())) {
      return true;
    }
    return false;
  }

  public static String getErrorMsg(Result result) {
    if (null == result) {
      return null;
    }
    return result.getMsg();
  }


}

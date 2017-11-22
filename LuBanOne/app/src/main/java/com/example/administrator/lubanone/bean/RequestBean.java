package com.example.administrator.lubanone.bean;

import com.alibaba.fastjson.JSON;
import com.example.administrator.lubanone.utils.HouLog;

/**
 * Created by hou on 2017/7/4.
 */

public class RequestBean {

  private String type;
  private String result;
  private String msg;

  public RequestBean(String json) {
    try {
      String type = JSON.parseObject(json).getString("type");
      String result = JSON.parseObject(json).getString("result");
      String msg = JSON.parseObject(json).getString("msg");
      this.type = type;
      this.result = result;
      this.msg = msg;
    } catch (Exception e) {
      HouLog.d("json基本类型错误 "+json +"  e:"+ e.getMessage());
    }
  }

  public String getType() {
    return type;
  }

  public String getResult() {
    return result;
  }

  public String getMsg() {
    return msg;
  }

  @Override
  public String toString() {
    return "RequestBean{" +
        "type='" + type + '\'' +
        ", msg='" + msg + '\'' +
        ", result='" + result + '\'' +
        '}';
  }
}

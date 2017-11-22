package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/7/6.
 * 現在假設服务器回传的json格式如下，定义基础格式
 */

public class BaseModel<T> {

  //判断请求结果
  private int type;

  //提示信息
  private String msg;

  //请求下来的数据（用户需要关心的数据）
  private T result;

  public BaseModel(int type, String msg, T result) {
    this.type = type;
    this.msg = msg;
    this.result = result;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }


  @Override
  public String toString() {
    return "BaseModel{" +
        "type=" + type +
        ", msg='" + msg + '\'' +
        ", result=" + result +
        '}';
  }
}

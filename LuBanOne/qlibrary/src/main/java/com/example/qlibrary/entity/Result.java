package com.example.qlibrary.entity;

/**
 * Created by Administrator on 2017/5/27.
 */
public class Result<T> {

  private String msg;
  private String type;
  private T result;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }
}

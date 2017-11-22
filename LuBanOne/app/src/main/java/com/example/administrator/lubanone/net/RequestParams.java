package com.example.administrator.lubanone.net;

/**
 * Created by quyang on 2017/6/20.
 */

public class RequestParams {

  private String key;
  private String value;

  public RequestParams(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public RequestParams() {
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

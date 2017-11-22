package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/8/5.
 */

public class TrainTestDetailsModel {

  private String url;
  private String sessionid;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
  }

  @Override
  public String toString() {
    return "TrainTestDetailsModel{" +
        "url='" + url + '\'' +
        ", sessionid='" + sessionid + '\'' +
        '}';
  }
}

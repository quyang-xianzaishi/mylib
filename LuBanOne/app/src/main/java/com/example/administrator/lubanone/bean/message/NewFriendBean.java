package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\7\31 0031.
 */

public class NewFriendBean {

  private String userid;
  private String userimg;
  private String username;
  private String applymess;
  private String zt;//状态

  public NewFriendBean(String userid, String userimg, String username, String applymess,
      String zt) {
    this.userid = userid;
    this.userimg = userimg;
    this.username = username;
    this.applymess = applymess;
    this.zt = zt;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getUserimg() {
    return userimg;
  }

  public void setUserimg(String userimg) {
    this.userimg = userimg;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getApplymess() {
    return applymess;
  }

  public void setApplymess(String applymess) {
    this.applymess = applymess;
  }

  public String getZt() {
    return zt;
  }

  public void setZt(String zt) {
    this.zt = zt;
  }
}

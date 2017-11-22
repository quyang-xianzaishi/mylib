package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\7\29 0029.
 */

public class MemberInfoBean {

  private String userid;
  private String userimg;
  private String username;
  private String accounts;
  private String levelname;
  private String autograph;
  private String zt;

  public MemberInfoBean(String userid, String userimg, String username, String accounts,
      String levelname, String autograph,String zt) {
    this.userid = userid;
    this.userimg = userimg;
    this.username = username;
    this.accounts = accounts;
    this.levelname = levelname;
    this.autograph = autograph;
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

  public String getAccounts() {
    return accounts;
  }

  public void setAccounts(String accounts) {
    this.accounts = accounts;
  }

  public String getLevelname() {
    return levelname;
  }

  public void setLevelname(String levelname) {
    this.levelname = levelname;
  }

  public String getAutograph() {
    return autograph;
  }

  public void setAutograph(String autograph) {
    this.autograph = autograph;
  }

  public String getZt() {
    return zt;
  }

  public void setZt(String zt) {
    this.zt = zt;
  }
}

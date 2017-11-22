package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\7\31 0031.
 */

public class SearchFriendBean {

  private String userid;
  private String username;
  private String userimg;

  public SearchFriendBean(String userid, String username, String userimg) {
    this.userid = userid;
    this.username = username;
    this.userimg = userimg;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUserimg() {
    return userimg;
  }

  public void setUserimg(String userimg) {
    this.userimg = userimg;
  }
}

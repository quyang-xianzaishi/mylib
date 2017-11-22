package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\7\27 0027.
 */

public class AddFriendInfoBean {

  private String userId;
  private String img;
  private String userName;
  private String state;

  public AddFriendInfoBean(String userId, String img, String userName, String state) {
    this.userId = userId;
    this.img = img;
    this.userName = userName;
    this.state = state;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}

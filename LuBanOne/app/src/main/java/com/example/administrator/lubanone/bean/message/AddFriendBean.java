package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\7\29 0029.
 */

public class AddFriendBean {

  private String userId;
  private String userImg;
  private String userName;
  private String userState;

  public AddFriendBean(String userId, String userImg, String userName, String userState) {
    this.userId = userId;
    this.userImg = userImg;
    this.userName = userName;
    this.userState = userState;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserImg() {
    return userImg;
  }

  public void setUserImg(String userImg) {
    this.userImg = userImg;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserState() {
    return userState;
  }

  public void setUserState(String userState) {
    this.userState = userState;
  }
}

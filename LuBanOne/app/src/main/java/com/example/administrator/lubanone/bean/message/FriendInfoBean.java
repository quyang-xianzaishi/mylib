package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\7\28 0028.
 */

public class FriendInfoBean {

  private String userImg;
  private String nickName;
  private String userName;
  private String userGrade;
  private String signature;
  private List imgList;

  public FriendInfoBean(String userImg,String nickName, String userName, String userGrade,
      String signature, List imgList) {
    this.userImg = userImg;
    this.nickName = nickName;
    this.userName = userName;
    this.userGrade = userGrade;
    this.signature = signature;
    this.imgList = imgList;
  }

  public String getUserImg() {
    return userImg;
  }

  public void setUserImg(String userImg) {
    this.userImg = userImg;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserGrade() {
    return userGrade;
  }

  public void setUserGrade(String userGrade) {
    this.userGrade = userGrade;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public List getImgList() {
    return imgList;
  }

  public void setImgList(List imgList) {
    this.imgList = imgList;
  }
}

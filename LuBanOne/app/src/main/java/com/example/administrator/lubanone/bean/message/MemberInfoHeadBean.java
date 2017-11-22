package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\7\28 0028.
 */

public class MemberInfoHeadBean {

  private String albumBg;
  private String userImg;
  private String userName;

  public MemberInfoHeadBean(String albumBg, String userImg, String userName) {
    this.albumBg = albumBg;
    this.userImg = userImg;
    this.userName = userName;
  }

  public String getAlbumBg() {
    return albumBg;
  }

  public void setAlbumBg(String albumBg) {
    this.albumBg = albumBg;
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
}

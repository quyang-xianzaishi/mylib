package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\7\29 0029.
 */

public class MemberInfoBeanNewBuy {


  /**
   * headerimage :
   * userid : 1
   * nick : 猪八戒
   * username : admin@qq.com
   * level : M0
   * autograph : This man is lazy, leaving nothing.
   */

  private String headerimage;
  private String userid;
  private String nick;
  private String username;
  private String level;
  private String autograph;
  private String zt;


  public String getZt() {
    return zt;
  }

  public void setZt(String zt) {
    this.zt = zt;
  }

  public String getHeaderimage() {
    return headerimage;
  }

  public void setHeaderimage(String headerimage) {
    this.headerimage = headerimage;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getAutograph() {
    return autograph;
  }

  public void setAutograph(String autograph) {
    this.autograph = autograph;
  }

  public static class FriendType{

    //是好友关系
    public static final String is_friend = "0";

    //不是好友关系
    public static final String no_friend = "1";
  }
}

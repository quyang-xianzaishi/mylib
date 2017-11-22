package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\8\9 0009.
 */

public class SystemMessageListBean {

  private String userid;
  private String username;
  private String title;
  private String content;
  private String time;


  public SystemMessageListBean() {
  }

  public SystemMessageListBean(String userid, String username, String title, String content,
      String time) {
    this.userid = userid;
    this.username = username;
    this.title = title;
    this.content = content;
    this.time = time;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}

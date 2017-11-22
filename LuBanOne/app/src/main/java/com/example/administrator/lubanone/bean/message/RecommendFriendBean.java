package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\8\8 0008.
 */

public class RecommendFriendBean {

  private String title;
  private String time;
  private String content;
  private String berecommendname;
  private String berecommendid;

  public RecommendFriendBean() {
  }

  public RecommendFriendBean(String title, String time, String content,
      String berecommendname, String berecommendid) {
    this.title = title;
    this.time = time;
    this.content = content;
    this.berecommendname = berecommendname;
    this.berecommendid = berecommendid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getBerecommendname() {
    return berecommendname;
  }

  public void setBerecommendname(String berecommendname) {
    this.berecommendname = berecommendname;
  }

  public String getBerecommendid() {
    return berecommendid;
  }

  public void setBerecommendid(String berecommendid) {
    this.berecommendid = berecommendid;
  }
}

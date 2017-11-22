package com.example.administrator.lubanone.bean.message;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\6 0006.
 */

public class SystemMessageBean implements Serializable {

  private String title;
  private String time;
  private String content;
  private String type;

  public SystemMessageBean(String title, String time, String content,String type) {
    this.title = title;
    this.time = time;
    this.content = content;
    this.type = type;
  }

  @Override
  public String toString() {
    return "SystemMessageBean{" +
        "title='" + title + '\'' +
        ", time='" + time + '\'' +
        ", content='" + content + '\'' +
        '}';
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}

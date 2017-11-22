package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/7/10.
 */

public class UsUploadBody {

  private String token;
  private String type;
  private String title;
  private String introduction;
  private String content;

  public UsUploadBody(String token, String type, String title, String introduction,
      String content) {
    this.token = token;
    this.type = type;
    this.title = title;
    this.introduction = introduction;
    this.content = content;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "UsUploadBody{" +
        "token='" + token + '\'' +
        ", type='" + type + '\'' +
        ", title='" + title + '\'' +
        ", introduction='" + introduction + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}

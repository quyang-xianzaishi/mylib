package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/8/5.
 */

public class TrainTestCenterModel {

  private String testid;
  private String theme;
  private String image;
  private String code;
  private String status;

  public String getTestid() {
    return testid;
  }

  public void setTestid(String testid) {
    this.testid = testid;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "TrainTestCenterModel{" +
        "testid='" + testid + '\'' +
        ", theme='" + theme + '\'' +
        ", image='" + image + '\'' +
        ", code='" + code + '\'' +
        ", status='" + status + '\'' +
        '}';
  }
}

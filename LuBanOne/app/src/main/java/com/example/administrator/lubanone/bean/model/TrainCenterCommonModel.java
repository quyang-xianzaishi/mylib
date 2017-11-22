package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/8/3.
 */

public class TrainCenterCommonModel {

  private String trainid;
  private String theme;
  private String status;
  private String image;
  private String code;
  private String notice;
  private String fire;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getNotice() {
    return notice;
  }

  public void setNotice(String notice) {
    this.notice = notice;
  }

  public String getFire() {
    return fire;
  }

  public void setFire(String fire) {
    this.fire = fire;
  }

  public String getTrainid() {
    return trainid;
  }

  public void setTrainid(String trainid) {
    this.trainid = trainid;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "TrainCenterCommonModel{" +
        "trainid='" + trainid + '\'' +
        ", theme='" + theme + '\'' +
        ", status='" + status + '\'' +
        ", image='" + image + '\'' +
        ", code='" + code + '\'' +
        ", notice='" + notice + '\'' +
        ", fire='" + fire + '\'' +
        '}';
  }
}

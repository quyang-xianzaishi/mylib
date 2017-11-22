package com.example.administrator.lubanone.bean.training;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainPreparationBean implements Serializable {

  private String imgUrl;
  private String title;
  private String time;
  private String remindState;

  public TrainPreparationBean(String imgUrl, String title, String time, String remindState) {
    this.imgUrl = imgUrl;
    this.title = title;
    this.time = time;
    this.remindState = remindState;
  }

  @Override
  public String toString() {
    return "TrainPreparationBean{" +
        "imgUrl='" + imgUrl + '\'' +
        ", title='" + title + '\'' +
        ", time='" + time + '\'' +
        ", remindState='" + remindState + '\'' +
        '}';
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
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

  public String getRemindState() {
    return remindState;
  }

  public void setRemindState(String remindState) {
    this.remindState = remindState;
  }
}

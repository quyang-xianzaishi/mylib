package com.example.administrator.lubanone.bean.training;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainPlayBackBean implements Serializable {

  private String title;
  private String audience;
  private String point;
  private String imgUrl;

  public TrainPlayBackBean(String title, String audience, String point, String imgUrl) {
    this.title = title;
    this.audience = audience;
    this.point = point;
    this.imgUrl = imgUrl;
  }

  @Override
  public String toString() {
    return "TrainPlayBackBean{" +
        "title='" + title + '\'' +
        ", audience='" + audience + '\'' +
        ", point='" + point + '\'' +
        ", imgUrl='" + imgUrl + '\'' +
        '}';
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAudience() {
    return audience;
  }

  public void setAudience(String audience) {
    this.audience = audience;
  }

  public String getPoint() {
    return point;
  }

  public void setPoint(String point) {
    this.point = point;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }
}

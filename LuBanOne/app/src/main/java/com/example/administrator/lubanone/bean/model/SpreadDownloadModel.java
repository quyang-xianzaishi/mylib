package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/8/29.
 */

public class SpreadDownloadModel {

  private String image;
  private String title;

  public SpreadDownloadModel(String image, String title) {
    this.image = image;
    this.title = title;
  }

  @Override
  public String toString() {
    return "SpreadDownloadModel{" +
        "image='" + image + '\'' +
        ", title='" + title + '\'' +
        '}';
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}

package com.example.administrator.lubanone.bean.training;

import android.os.Parcelable;
import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainingBean implements Serializable {

  private String imgUrl;
  private String title;
  private String content;

  public TrainingBean(String imgUrl, String title, String content) {
    this.imgUrl = imgUrl;
    this.title = title;
    this.content = content;
  }

  @Override
  public String toString() {
    return "TrainingBean{" +
        "imgUrl='" + imgUrl + '\'' +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

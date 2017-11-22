package com.example.administrator.lubanone.bean.training;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\8 0008.
 */

public class TrainingInfoBean implements Serializable {

  private String trainingInfoImgUrl;
  private String trainingInfoTitle;
  private String trainingInfoSummary;

  public TrainingInfoBean(String trainingInfoImgUrl, String trainingInfoTime,
      String trainingInfoSummary) {
    this.trainingInfoImgUrl = trainingInfoImgUrl;
    this.trainingInfoTitle = trainingInfoTime;
    this.trainingInfoSummary = trainingInfoSummary;
  }

  @Override
  public String toString() {
    return "TrainingInfoBean{" +
        "trainingInfoImgUrl='" + trainingInfoImgUrl + '\'' +
        ", trainingInfoTime='" + trainingInfoTitle + '\'' +
        ", trainingInfoSummary='" + trainingInfoSummary + '\'' +
        '}';
  }

  public String getTrainingInfoImgUrl() {
    return trainingInfoImgUrl;
  }

  public void setTrainingInfoImgUrl(String trainingInfoImgUrl) {
    this.trainingInfoImgUrl = trainingInfoImgUrl;
  }

  public String getTrainingInfoTitle() {
    return trainingInfoTitle;
  }

  public void setTrainingInfoTitle(String trainingInfoTitle) {
    this.trainingInfoTitle = trainingInfoTitle;
  }

  public String getTrainingInfoSummary() {
    return trainingInfoSummary;
  }

  public void setTrainingInfoSummary(String trainingInfoSummary) {
    this.trainingInfoSummary = trainingInfoSummary;
  }
}

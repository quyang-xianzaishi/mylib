package com.example.administrator.lubanone.bean.training;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\8 0008.
 */

public class MyJoinTrainingBean implements Serializable {

  private String trainingAuthor;
  private String trainingTime;
  private String trainingScore;
  private String trainingTheme;
  private String trainingDiscuss;

  public MyJoinTrainingBean(String trainingAuthor, String trainingTime,String trainingScore,
      String trainingTheme, String trainingDiscuss) {
    this.trainingAuthor = trainingAuthor;
    this.trainingTime = trainingTime;
    this.trainingScore = trainingScore;
    this.trainingTheme = trainingTheme;
    this.trainingDiscuss = trainingDiscuss;
  }

  @Override
  public String toString() {
    return "MyJoinTrainingBean{" +
        "trainingAuthor='" + trainingAuthor + '\'' +
        ", trainingScore='" + trainingScore + '\'' +
        ", trainingTheme='" + trainingTheme + '\'' +
        ", trainingDiscuss='" + trainingDiscuss + '\'' +
        '}';
  }

  public String getTrainingAuthor() {
    return trainingAuthor;
  }

  public void setTrainingAuthor(String trainingAuthor) {
    this.trainingAuthor = trainingAuthor;
  }

  public String getTrainingScore() {
    return trainingScore;
  }

  public void setTrainingScore(String trainingScore) {
    this.trainingScore = trainingScore;
  }

  public String getTrainingTheme() {
    return trainingTheme;
  }

  public void setTrainingTheme(String trainingTheme) {
    this.trainingTheme = trainingTheme;
  }

  public String getTrainingDiscuss() {
    return trainingDiscuss;
  }

  public void setTrainingDiscuss(String trainingDiscuss) {
    this.trainingDiscuss = trainingDiscuss;
  }

  public String getTrainingTime() {
    return trainingTime;
  }

  public void setTrainingTime(String trainingTime) {
    this.trainingTime = trainingTime;
  }
}

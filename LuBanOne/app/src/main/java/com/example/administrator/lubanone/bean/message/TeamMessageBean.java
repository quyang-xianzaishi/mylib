package com.example.administrator.lubanone.bean.message;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\6 0006.
 */

public class TeamMessageBean implements Serializable {

  private String title;
  private String time;
  private String content;
  private String trainingTheme;
  private String trainingSummary;
  private String trainingTime;

  public TeamMessageBean(String title, String time, String content, String trainingTheme,
      String trainingSummary, String trainingTime) {
    this.title = title;
    this.time = time;
    this.content = content;
    this.trainingTheme = trainingTheme;
    this.trainingSummary = trainingSummary;
    this.trainingTime = trainingTime;
  }

  @Override
  public String toString() {
    return "TeamMessageBean{" +
        "title='" + title + '\'' +
        ", time='" + time + '\'' +
        ", content='" + content + '\'' +
        ", trainingTheme='" + trainingTheme + '\'' +
        ", trainingSummary='" + trainingSummary + '\'' +
        ", trainingTime='" + trainingTime + '\'' +
        '}';
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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTrainingTheme() {
    return trainingTheme;
  }

  public void setTrainingTheme(String trainingTheme) {
    this.trainingTheme = trainingTheme;
  }

  public String getTrainingSummary() {
    return trainingSummary;
  }

  public void setTrainingSummary(String trainingSummary) {
    this.trainingSummary = trainingSummary;
  }

  public String getTrainingTime() {
    return trainingTime;
  }

  public void setTrainingTime(String trainingTime) {
    this.trainingTime = trainingTime;
  }
}

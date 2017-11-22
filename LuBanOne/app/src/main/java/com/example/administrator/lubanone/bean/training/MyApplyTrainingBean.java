package com.example.administrator.lubanone.bean.training;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\8 0008.
 */

public class MyApplyTrainingBean implements Serializable {

  private String trainignTheme;
  private String trainingSummary;
  private String applyTime;
  private String trainingTime;
  private String applyState;
  private String notifyState;
  private String integration;

  public MyApplyTrainingBean(String trainignTheme, String trainingSummary, String applyTime,
      String trainingTime, String applyState,String notifyState,String integration) {
    this.trainignTheme = trainignTheme;
    this.trainingSummary = trainingSummary;
    this.applyTime = applyTime;
    this.trainingTime = trainingTime;
    this.applyState = applyState;
    this.notifyState = notifyState;

    this.integration = integration;
  }

  @Override
  public String toString() {
    return "MyApplyTrainingBean{" +
        "trainignTheme='" + trainignTheme + '\'' +
        ", trainingSummary='" + trainingSummary + '\'' +
        ", applyTime='" + applyTime + '\'' +
        ", trainingTime='" + trainingTime + '\'' +
        ", applyState='" + applyState + '\'' +
        '}';
  }

  public String getTrainignTheme() {
    return trainignTheme;
  }

  public void setTrainignTheme(String trainignTheme) {
    this.trainignTheme = trainignTheme;
  }

  public String getTrainingSummary() {
    return trainingSummary;
  }

  public void setTrainingSummary(String trainingSummary) {
    this.trainingSummary = trainingSummary;
  }

  public String getApplyTime() {
    return applyTime;
  }

  public void setApplyTime(String applyTime) {
    this.applyTime = applyTime;
  }

  public String getTrainingTime() {
    return trainingTime;
  }

  public void setTrainingTime(String trainingTime) {
    this.trainingTime = trainingTime;
  }

  public String getApplyState() {
    return applyState;
  }

  public void setApplyState(String applyState) {
    this.applyState = applyState;
  }

  public String getNotifyState() {
    return notifyState;
  }

  public void setNotifyState(String notifyState) {
    this.notifyState = notifyState;
  }

  public String getIntegration() {
    return integration;
  }

  public void setIntegration(String integration) {
    this.integration = integration;
  }
}

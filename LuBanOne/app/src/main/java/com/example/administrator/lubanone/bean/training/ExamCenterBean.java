package com.example.administrator.lubanone.bean.training;

import java.io.Serializable;

/**
 * Created by Administrator on 2017\7\8 0008.
 */

public class ExamCenterBean implements Serializable {

  private String trainingTitle;
  private String trainingIntegration;
  private String trainingState;

  public ExamCenterBean(String trainingTitle, String trainingIntegration,
      String trainingState) {
    this.trainingTitle = trainingTitle;
    this.trainingIntegration = trainingIntegration;
    this.trainingState = trainingState;
  }

  @Override
  public String toString() {
    return "ExamCenterBean{" +
        "trainingTitle='" + trainingTitle + '\'' +
        ", trainingIntegration='" + trainingIntegration + '\'' +
        ", trainingState='" + trainingState + '\'' +
        '}';
  }

  public String getTrainingTitle() {
    return trainingTitle;
  }

  public void setTrainingTitle(String trainingTitle) {
    this.trainingTitle = trainingTitle;
  }

  public String getTrainingIntegration() {
    return trainingIntegration;
  }

  public void setTrainingIntegration(String trainingIntegration) {
    this.trainingIntegration = trainingIntegration;
  }

  public String getTrainingState() {
    return trainingState;
  }

  public void setTrainingState(String trainingState) {
    this.trainingState = trainingState;
  }
}

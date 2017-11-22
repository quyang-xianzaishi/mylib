package com.example.administrator.lubanone.bean.model;

import java.util.List;

/**
 * Created by hou on 2017/8/4.
 */

public class TrainTestModel {

  private List<TrainTestQuestions> test;



  public List<TrainTestQuestions> getTest() {
    return test;
  }

  public void setTest(
      List<TrainTestQuestions> test) {
    this.test = test;
  }

  @Override
  public String toString() {
    return "TrainTestModel{" +
        "test=" + test +
        '}';
  }
}

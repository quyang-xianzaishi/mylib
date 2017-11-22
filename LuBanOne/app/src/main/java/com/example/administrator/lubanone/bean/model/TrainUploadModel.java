package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/8/5.
 */

public class TrainUploadModel {

  private String trainid;

  public String getTrainid() {
    return trainid;
  }

  public void setTrainid(String trainid) {
    this.trainid = trainid;
  }

  @Override
  public String toString() {
    return "TrainUploadModel{" +
        "trainid='" + trainid + '\'' +
        '}';
  }
}

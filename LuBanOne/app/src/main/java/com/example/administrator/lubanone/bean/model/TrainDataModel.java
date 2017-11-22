package com.example.administrator.lubanone.bean.model;

import java.util.List;

/**
 * Created by hou on 2017/8/4.
 */

public class TrainDataModel {

  private List<TrainCenterCommonModel> list;

  public List<TrainCenterCommonModel> getList() {
    return list;
  }

  public void setList(
      List<TrainCenterCommonModel> list) {
    this.list = list;
  }

  @Override
  public String toString() {
    return "TrainDataModel{" +
        "list=" + list +
        '}';
  }
}

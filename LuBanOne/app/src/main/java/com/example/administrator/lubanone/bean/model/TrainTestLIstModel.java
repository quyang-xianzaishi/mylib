package com.example.administrator.lubanone.bean.model;

import java.util.List;

/**
 * Created by hou on 2017/8/5.
 */

public class TrainTestLIstModel {
  private List<TrainTestCenterModel> list;

  public List<TrainTestCenterModel> getList() {
    return list;
  }

  public void setList(
      List<TrainTestCenterModel> list) {
    this.list = list;
  }

  @Override
  public String toString() {
    return "TrainTestLIstModel{" +
        "list=" + list +
        '}';
  }
}

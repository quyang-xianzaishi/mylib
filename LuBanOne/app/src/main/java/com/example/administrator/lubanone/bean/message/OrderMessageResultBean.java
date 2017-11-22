package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\8\9 0009.
 */

public class OrderMessageResultBean {

  private List<OrderMessageBean> data;

  public List<OrderMessageBean> getData() {
    return data;
  }

  public void setData(
      List<OrderMessageBean> data) {
    this.data = data;
  }
}

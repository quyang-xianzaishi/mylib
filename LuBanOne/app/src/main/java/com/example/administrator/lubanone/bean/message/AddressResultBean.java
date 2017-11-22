package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\7\27 0027.
 */

public class AddressResultBean {

  private List<AddressBean> stardata;
  private List<AddressBean> data;

  public List<AddressBean> getList() {
    return data;
  }

  public void setList(List<AddressBean> list) {
    data = list;
  }

  public List<AddressBean> getStardata() {
    return stardata;
  }

  public void setStardata(
      List<AddressBean> stardata) {
    this.stardata = stardata;
  }

  public List<AddressBean> getData() {
    return data;
  }

  public void setData(List<AddressBean> data) {
    this.data = data;
  }
}

package com.example.administrator.lubanone.bean;

import java.util.List;

/**
 * Created by admistrator on 2017/8/24.
 */

public class GrowDaysBean {

  private String type;
  private String msg;
  private ResultBean result;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public ResultBean getResult() {
    return result;
  }

  public void setResult(ResultBean result) {
    this.result = result;
  }

  public static class ResultBean {

    private List<GrowDetails> list;

    public List<GrowDetails> getList() {
      return list;
    }

    public void setList(
        List<GrowDetails> list) {
      this.list = list;
    }

    public static class GrowDetails {

      private String seedscount;
      private String days;
      private String orderid;

      public String getOrderid() {
        return orderid;
      }

      public void setOrderid(String orderid) {
        this.orderid = orderid;
      }

      public String getSeedscount() {
        return seedscount;
      }

      public void setSeedscount(String seedscount) {
        this.seedscount = seedscount;
      }

      public String getDays() {
        return days;
      }

      public void setDays(String days) {
        this.days = days;
      }
    }
  }

}

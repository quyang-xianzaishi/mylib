package com.example.administrator.lubanone.bean.finance;

import java.util.List;

/**
 * Created by quyang on 2017/7/8.
 */

public class CuiHuaJiResultBean {


  /**
   * catacount : 23 deallist : [{"date":"2017-07-01 17:01:02","task":"梦想催化剂消耗","count":"-1.000"},{"date":"2017-07-01
   * 16:16:35","task":"梦想催化剂消耗","count":"-1.000"},{"date":"2017-07-01
   * 15:13:10","task":"梦想催化剂消耗","count":"-1.000"},{"date":"2017-06-30
   * 10:03:11","task":"梦想催化剂消耗","count":"-1.000"}]
   */

  private String catacount;
  private List<DeallistBean> deallist;

  public String getCatacount() {
    return catacount;
  }

  public void setCatacount(String catacount) {
    this.catacount = catacount;
  }

  public List<DeallistBean> getDeallist() {
    return deallist;
  }

  public void setDeallist(List<DeallistBean> deallist) {
    this.deallist = deallist;
  }

  public static class DeallistBean {

    /**
     * date : 2017-07-01 17:01:02
     * task : 梦想催化剂消耗
     * count : -1.000
     */

    private String date;
    private String task;
    private String count;
    private String color;

    public String getColor() {
      return color;
    }

    public void setColor(String color) {
      this.color = color;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public String getTask() {
      return task;
    }

    public void setTask(String task) {
      this.task = task;
    }

    public String getCount() {
      return count;
    }

    public void setCount(String count) {
      this.count = count;
    }
  }
}

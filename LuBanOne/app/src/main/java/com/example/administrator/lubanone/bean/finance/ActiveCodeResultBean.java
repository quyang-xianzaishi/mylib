package com.example.administrator.lubanone.bean.finance;

import java.util.List;

/**
 * Created by quyang on 2017/7/8.
 */

public class ActiveCodeResultBean {


  /**
   * activecodecount : 200 deallist : [{"date":"2017-07-07 15:11:27","tradeobject":"Dogrtyyyy","count":"-1.000"},{"date":"2017-07-07
   * 15:06:42","tradeobject":"给用户Weeeeesf激活","count":"-1.000"}]
   */

  private String activecodecount;
  private Boolean more ;
  private List<DeallistBean> deallist;


  public Boolean getMore() {
    return more;
  }

  public void setMore(Boolean more) {
    this.more = more;
  }

  public String getActivecodecount() {
    return activecodecount;
  }

  public void setActivecodecount(String activecodecount) {
    this.activecodecount = activecodecount;
  }

  public List<DeallistBean> getDeallist() {
    return deallist;
  }

  public void setDeallist(List<DeallistBean> deallist) {
    this.deallist = deallist;
  }

  public static class DeallistBean {

    /**
     * date : 2017-07-07 15:11:27
     * tradeobject : Dogrtyyyy
     * count : -1.000
     */

    private String date;
    private String tradeobject;
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

    public String getTradeobject() {
      return tradeobject;
    }

    public void setTradeobject(String tradeobject) {
      this.tradeobject = tradeobject;
    }

    public String getCount() {
      return count;
    }

    public void setCount(String count) {
      this.count = count;
    }
  }
}

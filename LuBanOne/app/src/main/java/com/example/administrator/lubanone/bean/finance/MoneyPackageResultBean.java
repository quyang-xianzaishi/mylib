package com.example.administrator.lubanone.bean.finance;

import java.util.List;

/**
 * Created by quyang on 2017/7/8.
 */

public class MoneyPackageResultBean {


  /**
   * rewardbagcount : 7.000 deallist : [{"date":"2017-07-07 16:55:09","orderid":"","trademember":"lihelin16","rewardtype":"三代"},{"date":"2017-07-07
   * 16:49:29","orderid":"","trademember":"lihelin12","rewardtype":"二代"}]
   */

  private String rewardbagcount;
  private String price;
  private List<DeallistBean> deallist;

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getRewardbagcount() {
    return rewardbagcount;
  }

  public void setRewardbagcount(String rewardbagcount) {
    this.rewardbagcount = rewardbagcount;
  }

  public List<DeallistBean> getDeallist() {
    return deallist;
  }

  public void setDeallist(List<DeallistBean> deallist) {
    this.deallist = deallist;
  }

  public static class DeallistBean {

    /**
     * date : 2017-07-07 16:55:09
     * orderid :
     * trademember : lihelin16
     * rewardtype : 三代
     */

    private String date;
    private String orderid;
    private String trademember;
    private String rewardtype;
    private String color;
    private String tradeseeds;

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

    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getTrademember() {
      return trademember;
    }

    public void setTrademember(String trademember) {
      this.trademember = trademember;
    }

    public String getRewardtype() {
      return rewardtype;
    }

    public void setRewardtype(String rewardtype) {
      this.rewardtype = rewardtype;
    }

    public String getTradeseeds() {
      return tradeseeds;
    }

    public void setTradeseeds(String tradeseeds) {
      this.tradeseeds = tradeseeds;
    }
  }
}

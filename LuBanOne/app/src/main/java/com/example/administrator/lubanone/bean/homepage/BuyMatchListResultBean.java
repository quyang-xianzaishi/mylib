package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/10.
 */

public class BuyMatchListResultBean {


  /**
   * tgbzlist : [{"orderid":"12","ordertime":"2017-07-05 00:00:00","seedcount":"0"},{"orderid":"11","ordertime":"2017-07-12
   * 00:00:00","seedcount":"0"}] p_paylist : 2 p_confirmlist : 3 p_dpingjialist : 7
   */

  private String p_paylist;
  private String listcount;
  private String p_confirmlist;
  private String p_dpingjialist;
  private String price;
  private List<TgbzlistBean> tgbzlist;


  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getListcount() {
    return listcount;
  }

  public void setListcount(String listcount) {
    this.listcount = listcount;
  }

  public String getP_paylist() {
    return p_paylist;
  }

  public void setP_paylist(String p_paylist) {
    this.p_paylist = p_paylist;
  }

  public String getP_confirmlist() {
    return p_confirmlist;
  }

  public void setP_confirmlist(String p_confirmlist) {
    this.p_confirmlist = p_confirmlist;
  }

  public String getP_dpingjialist() {
    return p_dpingjialist;
  }

  public void setP_dpingjialist(String p_dpingjialist) {
    this.p_dpingjialist = p_dpingjialist;
  }

  public List<TgbzlistBean> getTgbzlist() {
    return tgbzlist;
  }

  public void setTgbzlist(List<TgbzlistBean> tgbzlist) {
    this.tgbzlist = tgbzlist;
  }

  public static class TgbzlistBean implements Serializable {

    /**
     * orderid : 12
     * ordertime : 2017-07-05 00:00:00
     * seedcount : 0
     */

    private String orderid;
    private String ordertime;
    private String seedcount;


    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getOrdertime() {
      return ordertime;
    }

    public void setOrdertime(String ordertime) {
      this.ordertime = ordertime;
    }

    public String getSeedcount() {
      return seedcount;
    }

    public void setSeedcount(String seedcount) {
      this.seedcount = seedcount;
    }
  }
}

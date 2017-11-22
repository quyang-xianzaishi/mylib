package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by quyang on 2017/7/10.
 */

public class BuyPingJiaResultBean {


  /**
   * tgbzlist : 2 p_paylist : 5 p_confirmlist : 5 p_dpingjialist : [{"orderid":"42","seedcount":"5","sellmember":"imjocker"},{"orderid":"41","seedcount":"5","sellmember":"imcjocker"},{"orderid":"18","seedcount":"11","sellmember":"imclown"},{"orderid":"17","seedcount":"11","sellmember":"imclown"},{"orderid":"16","seedcount":"11","sellmember":"imclown"}]
   */

  private String tgbzlist;
  private String listcount;
  private String p_paylist;
  private String p_confirmlist;
  private String price;
  private List<PDpingjialistBean> p_dpingjialist;


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

  public String getTgbzlist() {
    return tgbzlist;
  }

  public void setTgbzlist(String tgbzlist) {
    this.tgbzlist = tgbzlist;
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

  public List<PDpingjialistBean> getP_dpingjialist() {
    return p_dpingjialist;
  }

  public void setP_dpingjialist(List<PDpingjialistBean> p_dpingjialist) {
    this.p_dpingjialist = p_dpingjialist;
  }

  public static class PDpingjialistBean {

    /**
     * orderid : 42
     * seedcount : 5
     * sellmember : imjocker
     */

    private String orderid;
    private String seedcount;
    private String sellmember;
    private String price;


    public String getPrice() {
      return price;
    }

    public void setPrice(String price) {
      this.price = price;
    }

    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getSeedcount() {
      return seedcount;
    }

    public void setSeedcount(String seedcount) {
      this.seedcount = seedcount;
    }

    public String getSellmember() {
      return sellmember;
    }

    public void setSellmember(String sellmember) {
      this.sellmember = sellmember;
    }
  }
}

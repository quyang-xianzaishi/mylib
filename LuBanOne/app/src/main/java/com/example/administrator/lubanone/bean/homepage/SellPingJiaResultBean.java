package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by quyang on 2017/7/11.
 */

public class SellPingJiaResultBean {


  /**
   * jsbzlist : 7 g_paylist : 3 g_confirmlist : 3 g_dpingjialist : [{"buymember":"imjocker","orderid":"40","seedcount":"5"},{"buymember":"imjocker","orderid":"39","seedcount":"5"},{"buymember":"imclown","orderid":"18","seedcount":"11"},{"buymember":"imclown","orderid":"17","seedcount":"11"},{"buymember":"imclown","orderid":"16","seedcount":"11"}]
   */

  private String listcount;
  private String jsbzlist;
  private String g_paylist;
  private String g_confirmlist;
  private String price;
  private List<GDpingjialistBean> g_dpingjialist;

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

  public String getJsbzlist() {
    return jsbzlist;
  }

  public void setJsbzlist(String jsbzlist) {
    this.jsbzlist = jsbzlist;
  }

  public String getG_paylist() {
    return g_paylist;
  }

  public void setG_paylist(String g_paylist) {
    this.g_paylist = g_paylist;
  }

  public String getG_confirmlist() {
    return g_confirmlist;
  }

  public void setG_confirmlist(String g_confirmlist) {
    this.g_confirmlist = g_confirmlist;
  }

  public List<GDpingjialistBean> getG_dpingjialist() {
    return g_dpingjialist;
  }

  public void setG_dpingjialist(List<GDpingjialistBean> g_dpingjialist) {
    this.g_dpingjialist = g_dpingjialist;
  }

  public static class GDpingjialistBean {

    /**
     * buymember : imjocker
     * orderid : 40
     * seedcount : 5
     */

    private String buymember;
    private String orderid;
    private String seedcount;

    public String getBuymember() {
      return buymember;
    }

    public void setBuymember(String buymember) {
      this.buymember = buymember;
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
  }
}

package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class SellMatchListResultBean {

  /**
   * jsbzlist : [{"orderid":"19","seedcount":"1","applytime":"2017-07-09
   * 18:04:13"},{"orderid":"18","seedcount":"1","applytime":"2017-07-09
   * 17:28:37"},{"orderid":"17","seedcount":"1","applytime":"2017-07-09
   * 17:28:34"},{"orderid":"16","seedcount":"1","applytime":"2017-07-09
   * 17:27:35"},{"orderid":"15","seedcount":"1","applytime":"2017-07-09 17:26:54"}] g_paylist : 14
   * g_confirmlist : 3 g_dpingjialist : 0
   */

  private String g_paylist;
  private String listcount;
  private String g_confirmlist;
  private String g_dpingjialist;
  private String price;


  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  private List<JsbzlistBean> jsbzlist;

  public String getListcount() {
    return listcount;
  }

  public void setListcount(String listcount) {
    this.listcount = listcount;
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

  public String getG_dpingjialist() {
    return g_dpingjialist;
  }

  public void setG_dpingjialist(String g_dpingjialist) {
    this.g_dpingjialist = g_dpingjialist;
  }

  public List<JsbzlistBean> getJsbzlist() {
    return jsbzlist;
  }

  public void setJsbzlist(List<JsbzlistBean> jsbzlist) {
    this.jsbzlist = jsbzlist;
  }

  public static class JsbzlistBean implements Serializable {

    /**
     * orderid : 19
     * seedcount : 1
     * applytime : 2017-07-09 18:04:13
     */

    private String orderid;
    private String seedcount;
    private String applytime;


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

    public String getApplytime() {
      return applytime;
    }

    public void setApplytime(String applytime) {
      this.applytime = applytime;
    }
  }
}

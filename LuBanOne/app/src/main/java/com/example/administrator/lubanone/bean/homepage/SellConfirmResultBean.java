package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/10.
 */

public class SellConfirmResultBean {


  /**
   * jsbzlist : 7 g_paylist : 3 g_confirmlist : [{"orderid":"11","seedcount":"11","buymember":"imclown","applytime":null,"paytime":"2017-07-11
   * 00:00:00","timeout":47214},{"orderid":"10","seedcount":"11","buymember":"imclown","applytime":null,"paytime":"2017-07-11
   * 00:00:00","timeout":47214},{"orderid":"9","seedcount":"11","buymember":"imclown","applytime":null,"paytime":"2017-07-11
   * 00:00:00","timeout":47214}] g_dpingjialist : 5
   */

  private String listcount;
  private String jsbzlist;
  private String g_paylist;
  private String g_dpingjialist;
  private String price;
  private List<GConfirmlistBean> g_confirmlist;


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

  public String getG_dpingjialist() {
    return g_dpingjialist;
  }

  public void setG_dpingjialist(String g_dpingjialist) {
    this.g_dpingjialist = g_dpingjialist;
  }

  public List<GConfirmlistBean> getG_confirmlist() {
    return g_confirmlist;
  }

  public void setG_confirmlist(List<GConfirmlistBean> g_confirmlist) {
    this.g_confirmlist = g_confirmlist;
  }

  public static class GConfirmlistBean implements Serializable {

    /**
     * orderid : 11
     * seedcount : 11
     * buymember : imclown
     * applytime : null
     * paytime : 2017-07-11 00:00:00
     * timeout : 47214
     */

    private String orderid;
    private String seedcount;
    private String buymember;
    private String applytime;
    private String paytime;
    private Integer timeout;
    private String complaintbutton;
    private String endtime;
    private String dealendtime ;
    private String userid;


    public String getDealendtime() {
      return dealendtime;
    }

    public void setDealendtime(String dealendtime) {
      this.dealendtime = dealendtime;
    }

    public String getUserid() {
      return userid;
    }

    public void setUserid(String userid) {
      this.userid = userid;
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

    public String getBuymember() {
      return buymember;
    }

    public void setBuymember(String buymember) {
      this.buymember = buymember;
    }

    public String getApplytime() {
      return applytime;
    }

    public void setApplytime(String applytime) {
      this.applytime = applytime;
    }

    public String getPaytime() {
      return paytime;
    }

    public void setPaytime(String paytime) {
      this.paytime = paytime;
    }

    public Integer getTimeout() {
      return timeout;
    }

    public void setTimeout(Integer timeout) {
      this.timeout = timeout;
    }

    public String getComplaintbutton() {
      return complaintbutton;
    }

    public void setComplaintbutton(String complaintbutton) {
      this.complaintbutton = complaintbutton;
    }

    public String getEndtime() {
      return endtime;
    }

    public void setEndtime(String endtime) {
      this.endtime = endtime;
    }
  }


  public static class ComplaintType {

    public static final String GRAY = "0";

    public static final String NORMAL = "1";

    //投诉过了
    public static final String DEALED = "2";

  }
}

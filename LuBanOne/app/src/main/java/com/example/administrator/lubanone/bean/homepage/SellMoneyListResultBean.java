package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/11.
 */

public class SellMoneyListResultBean {

  /**
   * jsbzlist : 7 g_paylist : [{"orderid":"18","seedcount":"11","applytime":null,"buymember":"imclown","matchtime":"2017-07-10
   * 10:54:00","matchtimestamp":1499655240,"timeout":"超时","status":0,"complaintbutton":1,"sellpressbutton":0,"prolongbutton":0},{"orderid":"16","seedcount":"11","applytime":"2017-07-19
   * 00:00:00","buymember":"imclown","matchtime":"2017-07-10 22:00:00","matchtimestamp":1499695200,"timeout":16812,"status":1,"complaintbutton":0,"sellpressbutton":1,"prolongbutton":1},{"orderid":"15","seedcount":"11","applytime":"2017-07-19
   * 00:00:00","buymember":"imclown","matchtime":"2017-07-14 00:00:00","matchtimestamp":1499961600,"timeout":283212,"status":1,"complaintbutton":0,"sellpressbutton":2,"prolongbutton":1},{"orderid":"14","seedcount":"11","applytime":null,"buymember":"imclown","matchtime":"2017-07-14
   * 00:00:00","matchtimestamp":1499961600,"timeout":283212,"status":2,"complaintbutton":0,"sellpressbutton":2,"prolongbutton":0},{"orderid":"12","seedcount":"11","applytime":"2017-07-19
   * 00:00:00","buymember":"test02","matchtime":"2017-07-10 18:00:00","matchtimestamp":1499680800,"timeout":2412,"status":2,"complaintbutton":0,"sellpressbutton":2,"prolongbutton":0}]
   * g_confirmlist : 2 g_dpingjialist : 2
   */

  private String jsbzlist;
  private String listcount;
  private String g_confirmlist;
  private String g_dpingjialist;
  private String prolongtime;
  private String price;
  private List<GPaylistBean> g_paylist;


  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getProlongtime() {
    return prolongtime;
  }

  public void setProlongtime(String prolongtime) {
    this.prolongtime = prolongtime;
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

  public List<GPaylistBean> getG_paylist() {
    return g_paylist;
  }

  public void setG_paylist(List<GPaylistBean> g_paylist) {
    this.g_paylist = g_paylist;
  }

  public static class GPaylistBean implements Serializable {

    /**
     * orderid : 18
     * seedcount : 11
     * applytime : null
     * buymember : imclown
     * matchtime : 2017-07-10 10:54:00
     * matchtimestamp : 1499655240
     * timeout : 超时
     * status : 0
     * complaintbutton : 1   投诉按钮  "0" 隐藏 "1"显示 "2"灰色
     * sellpressbutton : 0   催一催  0 隐藏 1 显示可用 2 显示不可点
     * prolongbutton : 0    延长打款  0 隐藏 1 显示可用
     */

    private String orderid;
    private String seedcount;
    private Object applytime;
    private String buymember;
    private String matchtime;
    private int matchtimestamp;
    private String timeout;
    private String status;
    private String complaintbutton;
    private String sellpressbutton;
    private String prolongbutton;
    private String endtime;
    private String userid;
    private Boolean mHasComplianted;//是否已被投诉


    public String getUserid() {
      return userid;
    }

    public void setUserid(String userid) {
      this.userid = userid;
    }

    public String getEndtime() {
      return endtime;
    }

    public void setEndtime(String endtime) {
      this.endtime = endtime;
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

    public Object getApplytime() {
      return applytime;
    }

    public void setApplytime(Object applytime) {
      this.applytime = applytime;
    }

    public String getBuymember() {
      return buymember;
    }

    public void setBuymember(String buymember) {
      this.buymember = buymember;
    }

    public String getMatchtime() {
      return matchtime;
    }

    public void setMatchtime(String matchtime) {
      this.matchtime = matchtime;
    }

    public int getMatchtimestamp() {
      return matchtimestamp;
    }

    public void setMatchtimestamp(int matchtimestamp) {
      this.matchtimestamp = matchtimestamp;
    }

    public String getTimeout() {
      return timeout;
    }

    public void setTimeout(String timeout) {
      this.timeout = timeout;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getComplaintbutton() {
      return complaintbutton;
    }

    public void setComplaintbutton(String complaintbutton) {
      this.complaintbutton = complaintbutton;
    }

    public String getSellpressbutton() {
      return sellpressbutton;
    }

    public void setSellpressbutton(String sellpressbutton) {
      this.sellpressbutton = sellpressbutton;
    }

    public String getProlongbutton() {
      return prolongbutton;
    }

    public void setProlongbutton(String prolongbutton) {
      this.prolongbutton = prolongbutton;
    }

    public Boolean getHasComplianted() {
      return mHasComplianted;
    }

    public void setHasComplianted(Boolean hasComplianted) {
      mHasComplianted = hasComplianted;
    }
  }

  public static class StatusType {

    //被投诉 == 超时
    public static final String COMPLAINTED = "0";

    //延长打款
    public static final String LONG_PAY = "1";

    //催一催
    public static final String CUI = "2";
  }

  public static class ComplainType {

    //诉按钮  "0" 隐藏
    public static final String GONE = "0";

    //"1"显示
    public static final String DISPLAY = "1";

    //"2"灰色
//    public static final String GRAY = "2";
  }

  public static class CuiType {

    // 催一催  0 隐藏
    public static final String GONE = "0";

    //1 显示可用
    public static final String DISPLAY_CAN_CLICK = "1";

    //2 显示不可点
    public static final String DISPLAY_NO_CLICK = "2";
  }

  public static class LongPayType {

    // 催一催  0 隐藏
    public static final String GONE = "0";

    //1 显示可用
    public static final String DISPLAY_CAN_CLICK = "1";
  }


}

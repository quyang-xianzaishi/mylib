package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SellSeedsRecordsresultBean {


  /**
   * result : [{"orderid":"59","applytime":"2017-07-02 00:00:00","seedcount":"1","buymember":"hengmu1021","dealtime":"2017-07-21
   * 10:01:52","getscore":"2","givescore":"3"},{"orderid":"61","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"test10","dealtime":"2017-07-22
   * 18:15:01","getscore":"3","givescore":"2"},{"orderid":"62","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"test10","dealtime":"2017-07-22
   * 18:14:23","getscore":"3","givescore":"5"},{"orderid":"63","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"test10","dealtime":"2017-07-22
   * 18:14:44","getscore":"2","givescore":"5"},{"orderid":"64","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"test10","dealtime":"2017-07-22
   * 18:14:30","getscore":"2","givescore":"5"},{"orderid":"65","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"test10","dealtime":"2017-07-22
   * 12:48:14","getscore":"3","givescore":"5"},{"orderid":"68","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"test10","dealtime":"2017-07-22
   * 12:48:11","getscore":"2","givescore":"5"},{"orderid":"74","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"test10","dealtime":"2017-07-22
   * 18:35:03","getscore":"3","givescore":"5"},{"orderid":"78","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"quyang1234567","dealtime":"2017-07-26
   * 18:44:52","getscore":"5","givescore":"0"},{"orderid":"85","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"quyang1234567","dealtime":"2017-07-26
   * 18:44:48","getscore":"4","givescore":"0"},{"orderid":"86","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"quyang1234567","dealtime":"2017-07-26
   * 17:48:11","getscore":"4","givescore":"5"},{"orderid":"90","applytime":"2017-07-02
   * 00:00:00","seedcount":"1","buymember":"quyang1234567","dealtime":"2017-07-26
   * 18:44:37","getscore":"4","givescore":"0"},{"orderid":"109","applytime":"2017-07-31
   * 16:51:54","seedcount":"10","buymember":"syx888","dealtime":"2017-07-05
   * 16:53:38","getscore":"5","givescore":"5"},{"orderid":"111","applytime":"2017-08-01
   * 12:53:04","seedcount":"20","buymember":"shiyue123","dealtime":"2017-08-01
   * 13:00:49","getscore":"4","givescore":"3"},{"orderid":"112","applytime":"2017-08-01
   * 13:04:29","seedcount":"10","buymember":"shiyue123","dealtime":"2017-08-01
   * 13:54:25","getscore":"0","givescore":"0"},{"orderid":"113","applytime":"2017-08-01
   * 13:04:35","seedcount":"10","buymember":"syx888","dealtime":"2017-07-25
   * 13:20:06","getscore":"5","givescore":"4"},{"orderid":"114","applytime":"2017-08-01
   * 13:14:05","seedcount":"10","buymember":"syx888","dealtime":"2017-07-24
   * 13:19:57","getscore":"5","givescore":"5"}] msg : type : 1
   */

  private String msg;
  private String type;
  private List<ResultBean> result;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<ResultBean> getResult() {
    return result;
  }

  public void setResult(List<ResultBean> result) {
    this.result = result;
  }

  public static class ResultBean {

    /**
     * orderid : 59
     * applytime : 2017-07-02 00:00:00
     * seedcount : 1
     * buymember : hengmu1021
     * dealtime : 2017-07-21 10:01:52
     * getscore : 2
     * givescore : 3
     */

    private String orderid;
    private String applytime;
    private String seedcount;
    private String buymember;
    private String dealtime;
    private String getscore;
    private String givescore;

    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getApplytime() {
      return applytime;
    }

    public void setApplytime(String applytime) {
      this.applytime = applytime;
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

    public String getDealtime() {
      return dealtime;
    }

    public void setDealtime(String dealtime) {
      this.dealtime = dealtime;
    }

    public String getGetscore() {
      return getscore;
    }

    public void setGetscore(String getscore) {
      this.getscore = getscore;
    }

    public String getGivescore() {
      return givescore;
    }

    public void setGivescore(String givescore) {
      this.givescore = givescore;
    }
  }
}

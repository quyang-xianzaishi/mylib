package com.example.administrator.lubanone.bean.finance;

import java.util.List;

/**
 * Created by quyang on 2017/7/8.
 */

public class DreamPackageResultBean {

  /**
   * seedcount : 25.000 deallist : [{"date":"2017-07-07 15:04:19","orderid":"4","trademember":"Wrreee","tradetype":"给用户Wrreee激活","sowseed":0,"growget":0,"tradeseed":null},{"date":"2017-07-06
   * 14:06:46","orderid":"3","trademember":"Wsssabbbbffff","tradetype":"给用户Wsssabbbbffff激活","sowseed":0,"growget":0,"tradeseed":null},{"date":"2017-07-06
   * 14:02:27","orderid":"2","trademember":"Wwwwwwwww","tradetype":"给用户Wwwwwwwww激活","sowseed":0,"growget":0,"tradeseed":null},{"date":"2017-07-06
   * 13:58:43","orderid":"1","trademember":"test03","tradetype":"给用户test03激活","sowseed":0,"growget":0,"tradeseed":null},{"date":"2017-07-01
   * 15:14:43","orderid":"0","trademember":null,"tradetype":"卖出种子扣款","sowseed":0,"growget":0,"tradeseed":null},{"date":"2017-06-26
   * 22:23:15","orderid":"0","trademember":null,"tradetype":"卖出种子扣款","sowseed":0,"growget":0,"tradeseed":null}]
   */

  private String seedcount;
  private String price;
  private List<DeallistBean> deallist;

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getSeedcount() {
    return seedcount;
  }

  public void setSeedcount(String seedcount) {
    this.seedcount = seedcount;
  }

  public List<DeallistBean> getDeallist() {
    return deallist;
  }

  public void setDeallist(List<DeallistBean> deallist) {
    this.deallist = deallist;
  }

  public static class DeallistBean {

    /**
     * date : 2017-07-07 15:04:19
     * orderid : 4
     * trademember : Wrreee
     * tradetype : 给用户Wrreee激活
     * sowseed : 0
     * growget : 0
     * tradeseed : null
     */

    private String date;
    private String orderid;
    private String trademember;
    private String tradetype;
    private String sowseed;
    private String growget;
    private String tradeseed;
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

    public String getTradetype() {
      return tradetype;
    }

    public void setTradetype(String tradetype) {
      this.tradetype = tradetype;
    }

    public String getSowseed() {
      return sowseed;
    }

    public void setSowseed(String sowseed) {
      this.sowseed = sowseed;
    }

    public String getGrowget() {
      return growget;
    }

    public void setGrowget(String growget) {
      this.growget = growget;
    }

    public String getTradeseed() {
      return tradeseed;
    }

    public void setTradeseed(String tradeseed) {
      this.tradeseed = tradeseed;
    }
  }

  public static class ColorType {

    public static final String NORMAL = "0";

    public static final String RED = "1";

  }
}

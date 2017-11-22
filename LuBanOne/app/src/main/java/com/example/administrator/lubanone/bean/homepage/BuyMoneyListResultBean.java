package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/10.
 */

public class BuyMoneyListResultBean {


  /**
   * tgbzlist : 2 p_paylist : [{"orderid":"6","seedcount":"11","sellmember":"imclown","applytime":null,"matchtime":"2017-07-14
   * 00:00:00","matchtimestamp":1499961600,"endtimestamp":1500048000,"timedifferent":376606,"endtime":"2017-07-15
   * 00:00:00","showButton":0},{"orderid":"4","seedcount":"11","sellmember":"imclown","applytime":null,"matchtime":"2017-07-14
   * 00:00:00","matchtimestamp":1499961600,"endtimestamp":1500048000,"timedifferent":376606,"endtime":"2017-07-15
   * 00:00:00","showButton":0}] p_confirmlist : 3 p_dpingjialist : 7
   */

  private String tgbzlist;
  private String listcount;
  private String p_confirmlist;
  private String p_dpingjialist;
  private String prolongtime;
  private String price;
  private List<PPaylistBean> p_paylist;


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

  public String getTgbzlist() {
    return tgbzlist;
  }

  public void setTgbzlist(String tgbzlist) {
    this.tgbzlist = tgbzlist;
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

  public List<PPaylistBean> getP_paylist() {
    return p_paylist;
  }

  public void setP_paylist(List<PPaylistBean> p_paylist) {
    this.p_paylist = p_paylist;
  }

  public static class PPaylistBean implements Serializable {

    /**
     * orderid : 6
     * seedcount : 11
     * sellmember : imclown
     * applytime : null
     * matchtime : 2017-07-14 00:00:00
     * matchtimestamp : 1499961600
     * endtimestamp : 1500048000
     * timedifferent : 376606
     * endtime : 2017-07-15 00:00:00
     * showButton : 0
     */

    private String orderid;
    private String seedcount;
    private String sellmember;
    private Object applytime;
    private String matchtime;
    private String userid;
    private Long matchtimestamp;
    private Long endtimestamp;
    private Long timedifferent;
    private String endtime;
    private Integer showButton;
    private Boolean hasShengQing;


    public String getUserid() {
      return userid;
    }

    public void setUserid(String userid) {
      this.userid = userid;
    }

    public Boolean getHasShengQing() {
      return hasShengQing;
    }

    public void setHasShengQing(Boolean hasShengQing) {
      this.hasShengQing = hasShengQing;
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

    public Object getApplytime() {
      return applytime;
    }

    public void setApplytime(Object applytime) {
      this.applytime = applytime;
    }

    public String getMatchtime() {
      return matchtime;
    }

    public void setMatchtime(String matchtime) {
      this.matchtime = matchtime;
    }

    public Long getMatchtimestamp() {
      return matchtimestamp;
    }

    public void setMatchtimestamp(Long matchtimestamp) {
      this.matchtimestamp = matchtimestamp;
    }

    public Long getEndtimestamp() {
      return endtimestamp;
    }

    public void setEndtimestamp(Long endtimestamp) {
      this.endtimestamp = endtimestamp;
    }

    public Long getTimedifferent() {
      return timedifferent;
    }

    public void setTimedifferent(Long timedifferent) {
      this.timedifferent = timedifferent;
    }

    public void setShowButton(Integer showButton) {
      this.showButton = showButton;
    }

    public void setTimedifferent(int timedifferent) {
      this.timedifferent = timedifferent + System.currentTimeMillis();
    }

    public String getEndtime() {
      return endtime;
    }

    public void setEndtime(String endtime) {
      this.endtime = endtime;
    }

    public int getShowButton() {
      return showButton;
    }

    public void setShowButton(int showButton) {
      this.showButton = showButton;
    }
  }

  //延长打款
  public static class ShowButtonType {

    //隐藏
    public static Integer HIDE_LONG_TIME = 0;

    //正常显示
    public static Integer SHOW_LONG_TIME = 1;

    //置灰 卖家没响应
    public static Integer GRAY_LONG_TIME = 2;

    //灰色 卖家已同意
    public static Integer GRAY_AGREE_LONG_TIME = 3;

  }

}

package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/10.
 */

public class BuyConfirmResultBean {


  /**
   * tgbzlist : 2 p_paylist : 2 p_confirmlist : [{"orderid":"24","seedcount":"11","paytime":"2017-07-19
   * 00:00:00","sellmember":"test02","applytime":"2017-07-05 00:00:00","status":"0","ts_time":"","showpic2button":0,"buypressbutton":1,"timedifferent":802497},{"orderid":"23","seedcount":"11","paytime":"2017-07-19
   * 00:00:00","sellmember":"test02","applytime":null,"status":"0","ts_time":"","showpic2button":0,"buypressbutton":1,"timedifferent":802497},{"orderid":"22","seedcount":"11","paytime":"2017-07-19
   * 00:00:00","sellmember":"test02","applytime":null,"status":"3","ts_time":"2017-07-28
   * 00:00:00","showpic2button":1,"buypressbutton":0,"timedifferent":802497},{"orderid":"7","seedcount":"11","paytime":"2017-07-19
   * 00:00:00","sellmember":"imclown","applytime":null,"status":"0","ts_time":"","showpic2button":0,"buypressbutton":1,"timedifferent":802497},{"orderid":"5","seedcount":"11","paytime":"2017-07-19
   * 00:00:00","sellmember":"imclown","applytime":null,"status":"0","ts_time":"","showpic2button":0,"buypressbutton":1,"timedifferent":802497}]
   * p_dpingjialist : 7
   */

  private String tgbzlist;
  private String listcount;
  private String p_paylist;
  private String p_dpingjialist;
  private String price;

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  private List<PConfirmlistBean> p_confirmlist;

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

  public String getP_dpingjialist() {
    return p_dpingjialist;
  }

  public void setP_dpingjialist(String p_dpingjialist) {
    this.p_dpingjialist = p_dpingjialist;
  }

  public List<PConfirmlistBean> getP_confirmlist() {
    return p_confirmlist;
  }

  public void setP_confirmlist(List<PConfirmlistBean> p_confirmlist) {
    this.p_confirmlist = p_confirmlist;
  }

  public static class PConfirmlistBean implements Serializable {

    /**
     * orderid : 24
     * seedcount : 11
     * paytime : 2017-07-19 00:00:00
     * sellmember : test02
     * applytime : 2017-07-05 00:00:00
     * status : 0
     * ts_time :
     * showpic2button : 0  0 隐藏上传视频按钮 1 正常显示 2 灰色
     * buypressbutton : 1
     * timedifferent : 802497
     */

    private String orderid;
    private String seedcount;
    private String paytime;
    private String sellmember;
    private String applytime;
    private String status;
    private String ts_time;
    private String showpic2button;//视频凭证
    private String buypressbutton;//崔确认
    private String timedifferent;
    private String endtime;
    private String dealendtime;
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

    public String getPaytime() {
      return paytime;
    }

    public void setPaytime(String paytime) {
      this.paytime = paytime;
    }

    public String getSellmember() {
      return sellmember;
    }

    public void setSellmember(String sellmember) {
      this.sellmember = sellmember;
    }

    public String getApplytime() {
      return applytime;
    }

    public void setApplytime(String applytime) {
      this.applytime = applytime;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getTs_time() {
      return ts_time;
    }

    public void setTs_time(String ts_time) {
      this.ts_time = ts_time;
    }

    public String getShowpic2button() {
      return showpic2button;
    }

    public void setShowpic2button(String showpic2button) {
      this.showpic2button = showpic2button;
    }

    public String getBuypressbutton() {
      return buypressbutton;
    }

    public void setBuypressbutton(String buypressbutton) {
      this.buypressbutton = buypressbutton;
    }

    public String getTimedifferent() {
      return timedifferent;
    }

    public void setTimedifferent(String timedifferent) {
      this.timedifferent = timedifferent;
    }
  }


  public static class CuiType {

    //灰色
    public static final String GRAY = "0";

    //正常显示
    public static final String NORMAL = "1";

  }


  public static class ProofType {

    //隐藏
    public static final String GRAY = "0";

    //正常显示
    public static final String NORMAL = "1";

    //6小时倒计时完毕 隐藏按钮  已超时，未处理
    public static final String DOWN_TIME = "2";

  }

  //只要被投诉就显示剩余处理时间
  public static class StatusType {

    // 没有被投诉
    public static final String NO_COMPLAIN = "0";


    //被投诉
    public static final String COMPLAINTED = "3";


  }


}

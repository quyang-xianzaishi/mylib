package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by admistrator on 2017/8/17.
 */

public class BuySellResultBean {


  private List<ListBean> list;

  public List<ListBean> getList() {
    return list;
  }

  public void setList(List<ListBean> list) {
    this.list = list;
  }

  public static class ListBean {

    /**
     * orderid : 1
     * seedcount : 1
     * seedprice : 2,000,000
     * status : 0
     */

    private String orderid;
    private String seedcount;
    private String seedprice;
    private int status;

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

    public String getSeedprice() {
      return seedprice;
    }

    public void setSeedprice(String seedprice) {
      this.seedprice = seedprice;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }
  }

  public static class StatusType {

    public static final int MATCH = 0;

    public static final int PAY = 1;

    public static final int CONFRIM = 2;

    public static final int PINGJIA = 3;


  }
}

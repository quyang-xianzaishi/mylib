package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by admistrator on 2017/8/10.
 */

public class SpotDot {

  private List<BuylistBean> buylist;
  private List<SelllistBean> selllist;

  public List<BuylistBean> getBuylist() {
    return buylist;
  }

  public void setBuylist(List<BuylistBean> buylist) {
    this.buylist = buylist;
  }

  public List<SelllistBean> getSelllist() {
    return selllist;
  }

  public void setSelllist(List<SelllistBean> selllist) {
    this.selllist = selllist;
  }

  public static class BuylistBean {

    /**
     * x : 1
     * y : 2012-12-12 09:09:87
     */

    private String x;
    private String y;

    public String getX() {
      return x;
    }

    public void setX(String x) {
      this.x = x;
    }

    public String getY() {
      return y;
    }

    public void setY(String y) {
      this.y = y;
    }
  }

  public static class SelllistBean {

    /**
     * x : 1
     * y : 2012-12-12 09:09:87
     */

    private String x;
    private String y;

    public String getX() {
      return x;
    }

    public void setX(String x) {
      this.x = x;
    }

    public String getY() {
      return y;
    }

    public void setY(String y) {
      this.y = y;
    }
  }
}

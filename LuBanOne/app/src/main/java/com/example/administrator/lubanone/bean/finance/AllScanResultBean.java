package com.example.administrator.lubanone.bean.finance;

import java.util.List;

/**
 * Created by quyang on 2017/7/8.
 */

public class AllScanResultBean {


  /**
   * dreambag : 258.000 rewardbag : 0.000 activecode : 218 catalyst : 231 totalassets : 258.000
   * sellseedcount : 10 buyseedcount : 36 waitsell : 258.000 totalincome : 232.000 price : 2000
   * chart : {"seedin":[{"x":"2017-07-03","y":"2"},{"x":"2017-07-12","y":"2"},{"x":"2017-07-13","y":"2"},{"x":"2017-07-25","y":"1"},{"x":"2017-07-28","y":"1"}],"seedout":[]}
   */

  private String dreambag;
  private String rewardbag;
  private String activecode;
  private String catalyst;
  private String totalassets;
  private String sellseedcount;
  private String buyseedcount;
  private String waitsell;
  private String totalincome;
  private String price;
  private ChartBean chart;
  private String timechoice;

  public String getTimechoice() {
    return timechoice;
  }

  public void setTimechoice(String timechoice) {
    this.timechoice = timechoice;
  }

  public String getDreambag() {
    return dreambag;
  }

  public void setDreambag(String dreambag) {
    this.dreambag = dreambag;
  }

  public String getRewardbag() {
    return rewardbag;
  }

  public void setRewardbag(String rewardbag) {
    this.rewardbag = rewardbag;
  }

  public String getActivecode() {
    return activecode;
  }

  public void setActivecode(String activecode) {
    this.activecode = activecode;
  }

  public String getCatalyst() {
    return catalyst;
  }

  public void setCatalyst(String catalyst) {
    this.catalyst = catalyst;
  }

  public String getTotalassets() {
    return totalassets;
  }

  public void setTotalassets(String totalassets) {
    this.totalassets = totalassets;
  }

  public String getSellseedcount() {
    return sellseedcount;
  }

  public void setSellseedcount(String sellseedcount) {
    this.sellseedcount = sellseedcount;
  }

  public String getBuyseedcount() {
    return buyseedcount;
  }

  public void setBuyseedcount(String buyseedcount) {
    this.buyseedcount = buyseedcount;
  }

  public String getWaitsell() {
    return waitsell;
  }

  public void setWaitsell(String waitsell) {
    this.waitsell = waitsell;
  }

  public String getTotalincome() {
    return totalincome;
  }

  public void setTotalincome(String totalincome) {
    this.totalincome = totalincome;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public ChartBean getChart() {
    return chart;
  }

  public void setChart(ChartBean chart) {
    this.chart = chart;
  }

  public static class ChartBean {

    private List<SeedinBean> seedin;
    private List<SeedinBeanOut> seedout;

    public List<SeedinBeanOut> getSeedout() {
      return seedout;
    }

    public void setSeedout(
        List<SeedinBeanOut> seedout) {
      this.seedout = seedout;
    }

    public List<SeedinBean> getSeedin() {
      return seedin;
    }

    public void setSeedin(List<SeedinBean> seedin) {
      this.seedin = seedin;
    }

    public static class SeedinBean {

      /**
       * x : 2017-07-03
       * y : 2
       */

      private String seedx;
      private String seedy;

      public String getSeedx() {
        return seedx;
      }

      public void setSeedx(String seedx) {
        this.seedx = seedx;
      }

      public String getSeedy() {
        return seedy;
      }

      public void setSeedy(String seedy) {
        this.seedy = seedy;
      }
    }


    public static class SeedinBeanOut {

      /**
       * x : 2017-07-03
       * y : 2
       */

      private String seedx;
      private String seedy;

      public String getSeedx() {
        return seedx;
      }

      public void setSeedx(String seedx) {
        this.seedx = seedx;
      }

      public String getSeedy() {
        return seedy;
      }

      public void setSeedy(String seedy) {
        this.seedy = seedy;
      }
    }
  }
}

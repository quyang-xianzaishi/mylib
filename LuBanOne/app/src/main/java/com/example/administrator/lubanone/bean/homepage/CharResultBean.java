package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by admistrator on 2017/8/31.
 */

public class CharResultBean {

  /**
   * chart : {"seedin":[{"x":"2017-07-03","y":"2"},{"x":"2017-07-12","y":"2"},{"x":"2017-07-13","y":"2"},{"x":"2017-07-25","y":"1"},{"x":"2017-07-28","y":"1"}],"seedout":[]}
   */

  private ChartBean chart;

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

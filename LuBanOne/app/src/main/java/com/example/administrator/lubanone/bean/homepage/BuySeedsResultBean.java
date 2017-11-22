package com.example.administrator.lubanone.bean.homepage;

/**
 * Created by quyang on 2017/7/10.
 */

public class BuySeedsResultBean {


  /**
   * sowtimes : 35
   * catalyst : 23
   */

  private String sowtimes;
  private String catalyst;
  private String price;
  private String endtime;
  private String leastseeds;


  public String getLeastseeds() {
    return leastseeds;
  }

  public void setLeastseeds(String leastseeds) {
    this.leastseeds = leastseeds;
  }

  public String getEndtime() {
    return endtime;
  }

  public void setEndtime(String endtime) {
    this.endtime = endtime;
  }

  public String getSowtimes() {
    return sowtimes;
  }

  public void setSowtimes(String sowtimes) {
    this.sowtimes = sowtimes;
  }

  public String getCatalyst() {
    return catalyst;
  }

  public void setCatalyst(String catalyst) {
    this.catalyst = catalyst;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }
}

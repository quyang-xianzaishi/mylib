package com.example.administrator.lubanone.bean.homepage;

/**
 * Created by quyang on 2017/7/10.
 */

public class SellSeedsResultBean {


  /**
   * dreambag : 0.000
   * rewardbag : 420
   */

  private String dreambag;
  private String rewardbag;
  private String price;
  private String leastseeds;


  public String getLeastseeds() {
    return leastseeds;
  }

  public void setLeastseeds(String leastseeds) {
    this.leastseeds = leastseeds;
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

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }
}

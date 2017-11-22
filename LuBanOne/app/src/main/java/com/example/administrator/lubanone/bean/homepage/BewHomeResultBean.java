package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by admistrator on 2017/8/16.
 */

public class BewHomeResultBean {

  /**
   * banner : [{"link":"https://www.baidu.com/img/bd_logo1.png","srclink":"heheda"},{"link":"https://www.baidu.com/img/bd_logo1.png","srclink":"heheda"}]
   * footbanner : [{"link":"https://www.baidu.com/img/bd_logo1.png","srclink":"heheda"},{"link":"https://www.baidu.com/img/bd_logo1.png","srclink":"heheda"}]
   * image : http://42.51.40.5/Public/headerimage/2017-08-11/598d10316eb37.jpg level : M0
   * totalassets : 256.000 dreambag : 256.000 rewardbag : 0.000 fund : 1888888 buylist :
   * [{"orderid":"42","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"43","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"44","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"45","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"46","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"47","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"48","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"49","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"50","seedcount":"10","seedprice":"20,000,000","status":0}]
   * selllist : [{"orderid":"37","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"38","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"39","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"40","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"41","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"42","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"43","seedcount":"10","seedprice":"20,000,000","status":0},{"orderid":"64","seedcount":"1","seedprice":"2,000,000","status":0},{"orderid":"65","seedcount":"1","seedprice":"2,000,000","status":0}]
   * growseeds : [{"orderid":"85","growdays":"17394天","getseedcount":"174.940","reapseedbutton":1},{"orderid":"78","growdays":"17394天","getseedcount":"174.940","reapseedbutton":1},{"orderid":"89","growdays":"17394天","getseedcount":"174.940","reapseedbutton":1}]
   */
  private String ue_account;
  private String truename;
  private String image;
  private String level;
  private Boolean tips;
  private String totalassets;
  private String dreambag;
  private String rewardbag;
  private String fund;
  private String endtime ;
  private List<BannerBean> banner;
  private List<FootbannerBean> footbanner;
  private List<BuylistBean> buylist;
  private List<SelllistBean> selllist;
  private List<GrowseedsBean> growseeds;


  public String getEndtime() {
    return endtime;
  }

  public void setEndtime(String endtime) {
    this.endtime = endtime;
  }

  public String getTruename() {
    return truename;
  }

  public void setTruename(String truename) {
    this.truename = truename;
  }

  public String getUe_account() {
    return ue_account;
  }

  public void setUe_account(String ue_account) {
    this.ue_account = ue_account;
  }


  public Boolean getTips() {
    return tips;
  }

  public void setTips(Boolean tips) {
    this.tips = tips;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getTotalassets() {
    return totalassets;
  }

  public void setTotalassets(String totalassets) {
    this.totalassets = totalassets;
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

  public String getFund() {
    return fund;
  }

  public void setFund(String fund) {
    this.fund = fund;
  }

  public List<BannerBean> getBanner() {
    return banner;
  }

  public void setBanner(List<BannerBean> banner) {
    this.banner = banner;
  }

  public List<FootbannerBean> getFootbanner() {
    return footbanner;
  }

  public void setFootbanner(List<FootbannerBean> footbanner) {
    this.footbanner = footbanner;
  }

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

  public List<GrowseedsBean> getGrowseeds() {
    return growseeds;
  }

  public void setGrowseeds(List<GrowseedsBean> growseeds) {
    this.growseeds = growseeds;
  }

  public static class BannerBean {

    /**
     * link : https://www.baidu.com/img/bd_logo1.png
     * srclink : heheda
     */

    private String link;
    private String srclink;

    public String getLink() {
      return link;
    }

    public void setLink(String link) {
      this.link = link;
    }

    public String getSrclink() {
      return srclink;
    }

    public void setSrclink(String srclink) {
      this.srclink = srclink;
    }
  }

  public static class FootbannerBean {

    /**
     * link : https://www.baidu.com/img/bd_logo1.png
     * srclink : heheda
     */

    private String link;
    private String srclink;

    public String getLink() {
      return link;
    }

    public void setLink(String link) {
      this.link = link;
    }

    public String getSrclink() {
      return srclink;
    }

    public void setSrclink(String srclink) {
      this.srclink = srclink;
    }
  }

  public static class BuylistBean {

    /**
     * orderid : 42
     * seedcount : 10
     * seedprice : 20,000,000
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

  public static class SelllistBean {

    /**
     * orderid : 37
     * seedcount : 10
     * seedprice : 20,000,000
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

  public static class GrowseedsBean {

    /**
     * orderid : 85
     * growdays : 17394天
     * getseedcount : 174.940
     * reapseedbutton : 1
     */

    private String orderid;
    private String growdays;
    private String getseedcount;
    private String reapseedbutton;

    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getGrowdays() {
      return growdays;
    }

    public void setGrowdays(String growdays) {
      this.growdays = growdays;
    }

    public String getGetseedcount() {
      return getseedcount;
    }

    public void setGetseedcount(String getseedcount) {
      this.getseedcount = getseedcount;
    }

    public String getReapseedbutton() {
      return reapseedbutton;
    }

    public void setReapseedbutton(String reapseedbutton) {
      this.reapseedbutton = reapseedbutton;
    }
  }

  //是否显示收割按钮
  public static class ReapseedbuttonType {

    public static final String HIDE = "0";
    public static final String SHOW = "1";
  }
}


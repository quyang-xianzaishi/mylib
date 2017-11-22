package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/8.
 */

public class NewHomeFragmentResultBean {

  private List<GrowseedslistBean> growseedslist;

  public List<GrowseedslistBean> getGrowseedslist() {
    return growseedslist;
  }

  public void setGrowseedslist(List<GrowseedslistBean> growseedslist) {
    this.growseedslist = growseedslist;
  }

  public static class GrowseedslistBean implements Serializable {

    /**
     * orderid : 89
     * jb : 1颗(2,000)
     * date : 2017-07-25 11:36:23
     * date_hk : 2017-08-11 13:02:15
     * date_su : 2017-08-15 14:52:26
     * g_user : admin@qq.com
     * gpingjia : 0
     * ppingjia : 0
     * zt : 成长中(计息中)
     * user_jj_ts : 6天
     * user_jj_lx : 0.06颗(120000)
     * isshouge : 1
     */

    private String orderid;
    private String jb;
    private String date;
    private String date_hk;
    private String date_su;
    private String g_user;
    private String gpingjia;
    private String ppingjia;
    private String zt;
    private String user_jj_ts;
    private String user_jj_lx;
    private String isshouge;

    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getJb() {
      return jb;
    }

    public void setJb(String jb) {
      this.jb = jb;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public String getDate_hk() {
      return date_hk;
    }

    public void setDate_hk(String date_hk) {
      this.date_hk = date_hk;
    }

    public String getDate_su() {
      return date_su;
    }

    public void setDate_su(String date_su) {
      this.date_su = date_su;
    }

    public String getG_user() {
      return g_user;
    }

    public void setG_user(String g_user) {
      this.g_user = g_user;
    }

    public String getGpingjia() {
      return gpingjia;
    }

    public void setGpingjia(String gpingjia) {
      this.gpingjia = gpingjia;
    }

    public String getPpingjia() {
      return ppingjia;
    }

    public void setPpingjia(String ppingjia) {
      this.ppingjia = ppingjia;
    }

    public String getZt() {
      return zt;
    }

    public void setZt(String zt) {
      this.zt = zt;
    }

    public String getUser_jj_ts() {
      return user_jj_ts;
    }

    public void setUser_jj_ts(String user_jj_ts) {
      this.user_jj_ts = user_jj_ts;
    }

    public String getUser_jj_lx() {
      return user_jj_lx;
    }

    public void setUser_jj_lx(String user_jj_lx) {
      this.user_jj_lx = user_jj_lx;
    }

    public String getIsshouge() {
      return isshouge;
    }

    public void setIsshouge(String isshouge) {
      this.isshouge = isshouge;
    }

    public static class ShouGeType{

      public static final String HIDE = "0";

      public static final String SHOW = "1";
    }
  }
}

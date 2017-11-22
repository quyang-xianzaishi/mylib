package com.example.administrator.lubanone.bean.homepage;

/**
 * Created by Administrator on 2017/7/8.
 */

public class HomeFragmentLvBean {

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
  private Integer isshouge;
  private String reaptime;


  public String getReaptime() {
    return reaptime;
  }

  public void setReaptime(String reaptime) {
    this.reaptime = reaptime;
  }

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

  public Integer getIsshouge() {
    return isshouge;
  }

  public void setIsshouge(Integer isshouge) {
    this.isshouge = isshouge;
  }


  public static class ReapType {

    public static Integer HIDE = 0;

    public static Integer SHOW = 1;


    public static boolean hasReapType(Integer reapType) {
      return HIDE.equals(reapType) || SHOW.equals(reapType);
    }

  }

}

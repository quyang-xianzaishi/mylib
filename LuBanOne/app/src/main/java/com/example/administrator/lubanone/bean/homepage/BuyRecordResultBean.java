package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class BuyRecordResultBean {

  /**
   * result : [{"orderid":"54","jb":"10颗(20000000)","date":"2017-07-21
   * 10:23:40","date_hk":"2017-07-22 10:58:46","date_su":"2017-07-22 18:19:53","g_user":"hellopai","gpingjia":"0","ppingjia":"2","zt":"成长中(计息中)","user_jj_ts":"1天","user_jj_lx":"0.1颗(200000)","reaptime":"","isshouge":0},{"orderid":"28","jb":"10颗(20000000)","date":"2017-07-21
   * 10:23:40","date_hk":"2017-07-20 00:00:00","date_su":"2017-07-21 19:07:46","g_user":"hellopai","gpingjia":"0","ppingjia":"4","zt":"成长中(计息中)","user_jj_ts":"2天","user_jj_lx":"0.2颗(400000)","reaptime":"","isshouge":0}]
   * msg : type : 1
   */

  private String msg;
  private String type;
  private List<ResultBean> result;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<ResultBean> getResult() {
    return result;
  }

  public void setResult(List<ResultBean> result) {
    this.result = result;
  }

  public static class ResultBean implements Serializable {

    /**
     * orderid : 54
     * jb : 10颗(20000000)  种子数量
     * date : 2017-07-21 10:23:40 匹配时间
     * date_hk : 2017-07-22 10:58:46 打款时间
     * date_su : 2017-07-22 18:19:53 完成时间
     * g_user : hellopai  收款者
     * gpingjia : 0   获得评分
     * ppingjia : 2
     * zt : 成长中(计息中)  成长状态
     * user_jj_ts : 1天   成长天数
     * user_jj_lx : 0.1颗(200000)
     * reaptime :
     * isshouge : 0
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
    private String reaptime;
    private String isshouge;
    private String status;//0 对应成长中  1对应已收割

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
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

    public String getReaptime() {
      return reaptime;
    }

    public void setReaptime(String reaptime) {
      this.reaptime = reaptime;
    }

    public String getIsshouge() {
      return isshouge;
    }

    public void setIsshouge(String isshouge) {
      this.isshouge = isshouge;
    }
  }

  public static class ReapType {

    public static String HIDE = "0";

    public static String SHOW = "1";


    public static boolean hasReapType(String reapType) {
      return HIDE.equals(reapType) || SHOW.equals(reapType);
    }

  }

}

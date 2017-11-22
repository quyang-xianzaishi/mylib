package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\7\29 0029.
 */

public class MemberInfoBeanNew {

  /**
   * headerimage : userid : 1 nick : 猪八戒 username : admin@qq.com level : M0 autograph : This man is
   * lazy, leaving nothing. payinformation : [{"truename":"heheda","bankname":"下沙分行","bankaccount":"12345700000000","phone":"17742039272"},{"truename":"heheda","bankname":"66678954","bankaccount":"5544578","phone":"17742039272"},{"truename":"heheda","bankname":"1234","bankaccount":"1111","phone":"17742039272"}]
   */

  private String headerimage;
  private String userid;
  private String nick;
  private String username;
  private String level;
  private String autograph;
  private String zt;
  private String qrcode;


  public String getQrcode() {
    return qrcode;
  }

  public void setQrcode(String qrcode) {
    this.qrcode = qrcode;
  }

  private List<PayinformationBean> payinformation;


  public String getZt() {
    return zt;
  }

  public void setZt(String zt) {
    this.zt = zt;
  }

  public String getHeaderimage() {
    return headerimage;
  }

  public void setHeaderimage(String headerimage) {
    this.headerimage = headerimage;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getAutograph() {
    return autograph;
  }

  public void setAutograph(String autograph) {
    this.autograph = autograph;
  }

  public List<PayinformationBean> getPayinformation() {
    return payinformation;
  }

  public void setPayinformation(List<PayinformationBean> payinformation) {
    this.payinformation = payinformation;
  }

  public static class PayinformationBean {

    /**
     * truename : heheda
     * bankname : 下沙分行
     * bankaccount : 12345700000000
     * phone : 17742039272
     */

    private String truename;
    private String bankname;
    private String bankaccount;
    private String phone;
    private String bankcode;

    public String getBankcode() {
      return bankcode;
    }

    public void setBankcode(String bankcode) {
      this.bankcode = bankcode;
    }

    public String getTruename() {
      return truename;
    }

    public void setTruename(String truename) {
      this.truename = truename;
    }

    public String getBankname() {
      return bankname;
    }

    public void setBankname(String bankname) {
      this.bankname = bankname;
    }

    public String getBankaccount() {
      return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
      this.bankaccount = bankaccount;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }


  }


  public static class FriendType {

    public static final String friend = "0";

    public static final String send = "1";
  }
}

package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/12.
 */

public class BindCardResultBean implements Serializable {


  private List<BankaccountlistBean> bankaccountlist;

  public List<BankaccountlistBean> getBankaccountlist() {
    return bankaccountlist;
  }

  public void setBankaccountlist(List<BankaccountlistBean> bankaccountlist) {
    this.bankaccountlist = bankaccountlist;
  }

  public static class BankaccountlistBean implements Serializable {

    /**
     * bankaccount : 11
     * bankname : 中国银行
     */

    private String bankaccount;
    private String bankname;
    private String type;
    private String logo;
    private String bankcolor;
    private String bankcode;
    private String bankid;
    private String phone;

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getBankid() {
      return bankid;
    }

    public void setBankid(String bankid) {
      this.bankid = bankid;
    }

    public String getBankcode() {
      return bankcode;
    }

    public void setBankcode(String bankcode) {
      this.bankcode = bankcode;
    }

    public String getBankcolor() {
      return bankcolor;
    }

    public void setBankcolor(String bankcolor) {
      this.bankcolor = bankcolor;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getLogo() {
      return logo;
    }

    public void setLogo(String logo) {
      this.logo = logo;
    }

    public String getBankaccount() {
      return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
      this.bankaccount = bankaccount;
    }

    public String getBankname() {
      return bankname;
    }

    public void setBankname(String bankname) {
      this.bankname = bankname;
    }
  }
}

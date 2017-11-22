package com.example.administrator.lubanone.bean.homepage;

/**
 * Created by quyang on 2017/7/7.
 */

public class AccountCenterResultBean {


  /**
   * account : adsfhdskfh
   * phone : 12345678901
   * nick :
   * truename : null
   * levelname : 0
   * country :
   * sex : 0
   * autograph :
   */

  private String image;
  private String account;
  private String phone;
  private String nick;
  private String truename;
  private String levelname;
  private String country;
  private String countrycode;
  private String sex;
  private String autograph;
  private String qrcode ;

  public String getQrcode() {
    return qrcode;
  }

  public void setQrcode(String qrcode) {
    this.qrcode = qrcode;
  }

  public String getCountrycode() {
    return countrycode;
  }

  public void setCountrycode(String countrycode) {
    this.countrycode = countrycode;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public String getTruename() {
    return truename;
  }

  public void setTruename(String truename) {
    this.truename = truename;
  }

  public String getLevelname() {
    return levelname;
  }

  public void setLevelname(String levelname) {
    this.levelname = levelname;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getAutograph() {
    return autograph;
  }

  public void setAutograph(String autograph) {
    this.autograph = autograph;
  }
}

package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;

/**
 * Created by quyang on 2017/7/8.
 */

public class BuySellBean implements Serializable {

  public String buyMatch;
  public String buyNoMoney;
  public String buyConfirm;
  public String buyPingJia;


  public String sellMatch;
  public String sellNoMoney;
  public String sellConfirm;
  public String sellPingJia;

  public String getBuyMatch() {
    return buyMatch;
  }

  public void setBuyMatch(String buyMatch) {
    this.buyMatch = buyMatch;
  }

  public String getBuyNoMoney() {
    return buyNoMoney == null ? "" : buyNoMoney;
  }

  public void setBuyNoMoney(String buyNoMoney) {
    this.buyNoMoney = buyNoMoney;
  }

  public String getBuyConfirm() {
    return buyConfirm == null ? "" : buyConfirm;
  }

  public void setBuyConfirm(String buyConfirm) {
    this.buyConfirm = buyConfirm;
  }

  public String getBuyPingJia() {
    return buyPingJia == null ? "" : buyPingJia;
  }

  public void setBuyPingJia(String buyPingJia) {
    this.buyPingJia = buyPingJia;
  }

  public String getSellMatch() {
    return sellMatch == null ? "" : sellMatch;
  }

  public void setSellMatch(String sellMatch) {
    this.sellMatch = sellMatch;
  }

  public String getSellNoMoney() {
    return sellNoMoney == null ? "" : sellNoMoney;
  }

  public void setSellNoMoney(String sellNoMoney) {
    this.sellNoMoney = sellNoMoney;
  }

  public String getSellConfirm() {
    return sellConfirm == null ? "" : sellConfirm;
  }

  public void setSellConfirm(String sellConfirm) {
    this.sellConfirm = sellConfirm;
  }

  public String getSellPingJia() {
    return sellPingJia == null ? "" : sellPingJia;
  }

  public void setSellPingJia(String sellPingJia) {
    this.sellPingJia = sellPingJia;
  }
}

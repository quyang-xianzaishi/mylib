package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by admistrator on 2017/8/10.
 */

public class CreditListResultBean {


  private List<CreditDetailsBean> list;


  public static class CreditDetailsBean {

    private String getdate;
    private String note;
    private String code;

    public String getGetdate() {
      return getdate;
    }

    public void setGetdate(String getdate) {
      this.getdate = getdate;
    }

    public String getNote() {
      return note;
    }

    public void setNote(String note) {
      this.note = note;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }
  }


  public List<CreditDetailsBean> getList() {
    return list;
  }

  public void setList(List<CreditDetailsBean> list) {
    this.list = list;
  }
}

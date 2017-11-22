package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by admistrator on 2017/8/24.
 */

public class BankListBean {


  /**
   * result : [{"bankname":"中国工商银行","banktype":"1"},{"bankname":"建设银行","banktype":"2"},{"bankname":"兴业银行","banktype":"3"},{"bankname":"邮政银行","banktype":"4"}]
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

  public static class ResultBean {

    /**
     * bankname : 中国工商银行
     * banktype : 1
     */

    private String bankname;
    private String banktype;

    public String getBankname() {
      return bankname;
    }

    public void setBankname(String bankname) {
      this.bankname = bankname;
    }

    public String getBanktype() {
      return banktype;
    }

    public void setBanktype(String banktype) {
      this.banktype = banktype;
    }
  }
}

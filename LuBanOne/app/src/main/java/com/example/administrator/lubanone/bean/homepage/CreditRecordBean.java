package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by admistrator on 2017/7/19.
 */

public class CreditRecordBean {


  /**
   * result : [{"orderid":"2","date":"2017.07.19","trademember":"test01","evaluate":"5"},{"orderid":"2","date":"2017.07.19","trademember":"test01","evaluate":"5"},{"orderid":"2","date":"2017.07.19","trademember":"test01","evaluate":"5"}]
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
     * orderid : 2
     * date : 2017.07.19
     * trademember : test01
     * evaluate : 5
     */

    private String orderid;
    private String date;
    private String trademember;
    private String evaluate;
    private String evaluatetext;
    private String isShow;


    public String getEvaluatetext() {
      return evaluatetext;
    }

    public void setEvaluatetext(String evaluatetext) {
      this.evaluatetext = evaluatetext;
    }

    public String getIsShow() {
      return isShow;
    }

    public void setIsShow(String isShow) {
      this.isShow = isShow;
    }

    public String getOrderid() {
      return orderid;
    }

    public void setOrderid(String orderid) {
      this.orderid = orderid;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public String getTrademember() {
      return trademember;
    }

    public void setTrademember(String trademember) {
      this.trademember = trademember;
    }

    public String getEvaluate() {
      return evaluate;
    }

    public void setEvaluate(String evaluate) {
      this.evaluate = evaluate;
    }
  }

  public static class ShowRedType {

    /**
     * 正常颜色
     */
    public static final String NORMAL = "0";

    /**
     * 红色
     */
    public static final String RED = "1";
  }
}

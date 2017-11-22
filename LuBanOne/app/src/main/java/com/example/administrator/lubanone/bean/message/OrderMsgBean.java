package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by admistrator on 2017/8/1.
 */

public class OrderMsgBean {


  /**
   * msg : result : {"list":[{"member":"er4534","orderid":"2432423","ordertype":"1","content":"sdfosdfasodifasodf","pushtime":"2018-09-09
   * 12:12:12","time":"fdfsasd","title":"hello"},{"member":"er4534","orderid":"2432423","ordertype":"1","content":"sdfosdfasodifasodf","pushtime":"2018-09-09
   * 12:12:12","time":"fdfsasd","title":"hello"}]} type : 1
   */

  private String msg;
  private ResultBean result;
  private String type;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public ResultBean getResult() {
    return result;
  }

  public void setResult(ResultBean result) {
    this.result = result;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public static class ResultBean {

    private List<ListBean> list;

    public List<ListBean> getList() {
      return list;
    }

    public void setList(List<ListBean> list) {
      this.list = list;
    }

    public static class ListBean {

      /**
       * member : er4534
       * orderid : 2432423
       * ordertype : 1
       * content : sdfosdfasodifasodf
       * pushtime : 2018-09-09 12:12:12
       * time : fdfsasd
       * title : hello
       */

      private String member;
      private String orderid;
      private String ordertype;
      private String content;
      private String pushtime;
      private String time;
      private String title;

      public String getMember() {
        return member;
      }

      public void setMember(String member) {
        this.member = member;
      }

      public String getOrderid() {
        return orderid;
      }

      public void setOrderid(String orderid) {
        this.orderid = orderid;
      }

      public String getOrdertype() {
        return ordertype;
      }

      public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
      }

      public String getContent() {
        return content;
      }

      public void setContent(String content) {
        this.content = content;
      }

      public String getPushtime() {
        return pushtime;
      }

      public void setPushtime(String pushtime) {
        this.pushtime = pushtime;
      }

      public String getTime() {
        return time;
      }

      public void setTime(String time) {
        this.time = time;
      }

      public String getTitle() {
        return title;
      }

      public void setTitle(String title) {
        this.title = title;
      }
    }
  }
}

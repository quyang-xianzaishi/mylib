package com.example.administrator.lubanone.bean.message;

import java.io.Serializable;
import javax.xml.transform.sax.TemplatesHandler;

/**
 * {"result":[{"userid":"2","username":"test01","title":"已接收您的买入种子","orderid":"118",
 * "content":"尊敬的用户:我们已收到您买入种子的申请,系统将在48小时内给您进行匹配,耐心等待哦~！",
 * "ordertype":"1","member":"test01","time":"2017-08-09 02:18:04","pushtime":"2017-08-08"}],
 * "type":"1","msg":"Success!"}
 */

public class OrderMessageBean implements Serializable {

  private String title;
  private String pushtime;
  private String content;
  //private String memberType;
  private String member;
  private String memberid;
  private String orderid;
  private String ordertype;
  private String time;
  private String otherdescription;
  //private String click;


  public OrderMessageBean(String title, String pushtime, String content, String member,
      String memberid, String orderid, String ordertype, String time,String otherdescription) {
    this.title = title;
    this.pushtime = pushtime;
    this.content = content;
    this.member = member;
    this.memberid = memberid;
    this.orderid = orderid;
    this.ordertype = ordertype;
    this.time = time;
    this.otherdescription = otherdescription;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPushtime() {
    return pushtime;
  }

  public void setPushtime(String pushtime) {
    this.pushtime = pushtime;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMember() {
    return member;
  }

  public void setMember(String member) {
    this.member = member;
  }

  public String getMemberid() {
    return memberid;
  }

  public void setMemberid(String memberid) {
    this.memberid = memberid;
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

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getOtherdescription() {
    return otherdescription;
  }

  public void setOtherdescription(String otherdescription) {
    this.otherdescription = otherdescription;
  }
}

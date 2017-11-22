package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/19.
 */

public class AlwaysProblesResulBean {

  /**
   * result : [{"qid":"7","title":"消息漫游","content":["漫游消息记录是指您开通QQ会员并且设置聊天记录漫游功能，在此之后与好友聊天的记录才能漫游到服务器上，开启该功能之前的聊天记录只能保存在本地电脑上，不支持漫游到服务器；"],"note":["注:","1.日本三大运营商为了易于在物联网上使用，将速度定为移动通信的数百分之一，同时将大幅降低资费。用电量也将变小，2节5号电池即可连续使用10年以上等较长时间。运营商将更新现有网络软件，争取在2018年上半年在全国同时启动服务。","2.在物联网通信方面，京瓷信息系统与法国风险企业SIGFOX合作，从2月起在部分地区启动了服务。价格最低仅为每年100日元（约合人民币6元）。三大运营商的资费仍未确定，但据称并不会与上述资费水平相差太大。"]},{"qid":"6","title":"什么是直推","content":["漫游消息记录是指您开通QQ会员并且设置聊天记录漫游功能，在此之后与好友聊天的记录才能漫游到服务器上，开启该功能之前的聊天记录只能保存在本地电脑上，不支持漫游到服务器；"],"note":["注:","1.日本三大运营商为了易于在物联网上使用，将速度定为移动通信的数百分之一，同时将大幅降低资费。用电量也将变小，2节5号电池即可连续使用10年以上等较长时间。运营商将更新现有网络软件，争取在2018年上半年在全国同时启动服务。","2.在物联网通信方面，京瓷信息系统与法国风险企业SIGFOX合作，从2月起在部分地区启动了服务。价格最低仅为每年100日元（约合人民币6元）。三大运营商的资费仍未确定，但据称并不会与上述资费水平相差太大。"]},{"qid":"2","title":"除了直推还有什么推","content":["推"],"note":["注:","1.日本三大运营商为了易于在物联网上使用，将速度定为移动通信的数百分之一，同时将大幅降低资费。用电量也将变小，2节5号电池即可连续使用10年以上等较长时间。运营商将更新现有网络软件，争取在2018年上半年在全国同时启动服务。","2.在物联网通信方面，京瓷信息系统与法国风险企业SIGFOX合作，从2月起在部分地区启动了服务。价格最低仅为每年100日元（约合人民币6元）。三大运营商的资费仍未确定，但据称并不会与上述资费水平相差太大。"]},{"qid":"1","title":"什么叫直推","content":["就是直着推"],"note":["注:","1.日本三大运营商为了易于在物联网上使用，将速度定为移动通信的数百分之一，同时将大幅降低资费。用电量也将变小，2节5号电池即可连续使用10年以上等较长时间。运营商将更新现有网络软件，争取在2018年上半年在全国同时启动服务。","2.在物联网通信方面，京瓷信息系统与法国风险企业SIGFOX合作，从2月起在部分地区启动了服务。价格最低仅为每年100日元（约合人民币6元）。三大运营商的资费仍未确定，但据称并不会与上述资费水平相差太大。"]}]
   * type : 1
   * msg :
   */

  private String type;
  private String msg;
  private List<ResultBean> result;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public List<ResultBean> getResult() {
    return result;
  }

  public void setResult(List<ResultBean> result) {
    this.result = result;
  }

  public static class ResultBean  implements Serializable{

    /**
     * qid : 7
     * title : 消息漫游
     * content : ["漫游消息记录是指您开通QQ会员并且设置聊天记录漫游功能，在此之后与好友聊天的记录才能漫游到服务器上，开启该功能之前的聊天记录只能保存在本地电脑上，不支持漫游到服务器；"]
     * note : ["注:","1.日本三大运营商为了易于在物联网上使用，将速度定为移动通信的数百分之一，同时将大幅降低资费。用电量也将变小，2节5号电池即可连续使用10年以上等较长时间。运营商将更新现有网络软件，争取在2018年上半年在全国同时启动服务。","2.在物联网通信方面，京瓷信息系统与法国风险企业SIGFOX合作，从2月起在部分地区启动了服务。价格最低仅为每年100日元（约合人民币6元）。三大运营商的资费仍未确定，但据称并不会与上述资费水平相差太大。"]
     */

    private String qid;
    private String title;
    private List<String> content;
    private List<String> note;

    public String getQid() {
      return qid;
    }

    public void setQid(String qid) {
      this.qid = qid;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public List<String> getContent() {
      return content;
    }

    public void setContent(List<String> content) {
      this.content = content;
    }

    public List<String> getNote() {
      return note;
    }

    public void setNote(List<String> note) {
      this.note = note;
    }
  }
}

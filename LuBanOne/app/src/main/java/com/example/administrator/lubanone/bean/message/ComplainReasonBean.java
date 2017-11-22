package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\8\4 0004.
 */

public class ComplainReasonBean {

  private String reasonid;
  private String reason;

  public ComplainReasonBean(String reasonid, String reason) {
    this.reasonid = reasonid;
    this.reason = reason;
  }

  public String getReasonid() {
    return reasonid;
  }

  public void setReasonid(String reasonid) {
    this.reasonid = reasonid;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}

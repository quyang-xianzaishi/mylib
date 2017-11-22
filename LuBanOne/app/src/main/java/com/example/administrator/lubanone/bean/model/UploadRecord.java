package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/7/8.
 */

public class UploadRecord {

  private String if_id;
  private String if_title;
  private String if_jhm;
  private String if_time;
  private String state;
  private String reason;
  private String catalyst;
  private String is_notice;

  @Override
  public String toString() {
    return "UploadRecord{" +
        "if_id='" + if_id + '\'' +
        ", if_title='" + if_title + '\'' +
        ", if_jhm='" + if_jhm + '\'' +
        ", if_time='" + if_time + '\'' +
        ", state='" + state + '\'' +
        ", reason='" + reason + '\'' +
        ", catalyst='" + catalyst + '\'' +
        ", is_notice='" + is_notice + '\'' +
        '}';
  }

  public String getIs_notice() {
    return is_notice;
  }

  public void setIs_notice(String is_notice) {
    this.is_notice = is_notice;
  }

  public String getCatalyst() {
    return catalyst;
  }

  public void setCatalyst(String catalyst) {
    this.catalyst = catalyst;
  }

  public String getIf_id() {
    return if_id;
  }

  public void setIf_id(String if_id) {
    this.if_id = if_id;
  }

  public String getIf_title() {
    return if_title;
  }

  public void setIf_title(String if_title) {
    this.if_title = if_title;
  }

  public String getIf_jhm() {
    return if_jhm;
  }

  public void setIf_jhm(String if_jhm) {
    this.if_jhm = if_jhm;
  }

  public String getIf_time() {
    return if_time;
  }

  public void setIf_time(String if_time) {
    this.if_time = if_time;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}

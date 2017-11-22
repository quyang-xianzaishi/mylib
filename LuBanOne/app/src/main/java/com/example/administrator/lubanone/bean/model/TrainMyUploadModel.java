package com.example.administrator.lubanone.bean.model;

import java.util.List;

/**
 * Created by hou on 2017/8/5.
 */

public class TrainMyUploadModel {

  private List<MyUploadInfo> list;

  public List<MyUploadInfo> getList() {
    return list;
  }

  public void setList(
      List<MyUploadInfo> list) {
    this.list = list;
  }

  @Override
  public String toString() {
    return "TrainMyUploadModel{" +
        "list=" + list +
        '}';
  }

  public class MyUploadInfo {

    private String trainid;//培训id
    private String theme;//培训主题
    private String notice;//培训简介
    private String traintime;//申请时间
    private String testnumber;//相关试题数量
    private String status;//申请状态
    private String code;//可获得的培训积分
    private String reason;//未通过审核的理由
    private String is_notice;//通知状态

    public String getIs_notice() {
      return is_notice;
    }

    public void setIs_notice(String is_notice) {
      this.is_notice = is_notice;
    }

    public String getTrainid() {
      return trainid;
    }

    public void setTrainid(String trainid) {
      this.trainid = trainid;
    }

    public String getTheme() {
      return theme;
    }

    public void setTheme(String theme) {
      this.theme = theme;
    }

    public String getNotice() {
      return notice;
    }

    public void setNotice(String notice) {
      this.notice = notice;
    }

    public String getTraintime() {
      return traintime;
    }

    public void setTraintime(String traintime) {
      this.traintime = traintime;
    }

    public String getTestnumber() {
      return testnumber;
    }

    public void setTestnumber(String testnumber) {
      this.testnumber = testnumber;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getReason() {
      return reason;
    }

    public void setReason(String reason) {
      this.reason = reason;
    }

    @Override
    public String toString() {
      return "MyUploadInfo{" +
          "trainid='" + trainid + '\'' +
          ", theme='" + theme + '\'' +
          ", notice='" + notice + '\'' +
          ", traintime='" + traintime + '\'' +
          ", testnumber='" + testnumber + '\'' +
          ", status='" + status + '\'' +
          ", code='" + code + '\'' +
          ", reason='" + reason + '\'' +
          ", is_notice='" + is_notice + '\'' +
          '}';
    }
  }

}

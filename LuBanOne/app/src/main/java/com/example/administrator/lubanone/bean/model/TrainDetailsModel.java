package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/8/4.
 */

public class TrainDetailsModel {

  private String status;
  private String finishstatus;
  private String code;
  private String uploadtime;
  private String uploader;
  private String videourl;
  private String url;
  private String sessionid;

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFinishstatus() {
    return finishstatus;
  }

  public void setFinishstatus(String finishstatus) {
    this.finishstatus = finishstatus;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getUploadtime() {
    return uploadtime;
  }

  public void setUploadtime(String uploadtime) {
    this.uploadtime = uploadtime;
  }

  public String getUploader() {
    return uploader;
  }

  public void setUploader(String uploader) {
    this.uploader = uploader;
  }

  public String getVideourl() {
    return videourl;
  }

  public void setVideourl(String videourl) {
    this.videourl = videourl;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "TrainDetailsModel{" +
        "status='" + status + '\'' +
        ", finishstatus='" + finishstatus + '\'' +
        ", code='" + code + '\'' +
        ", uploadtime='" + uploadtime + '\'' +
        ", uploader='" + uploader + '\'' +
        ", videourl='" + videourl + '\'' +
        ", url='" + url + '\'' +
        ", sessionid='" + sessionid + '\'' +
        '}';
  }
}

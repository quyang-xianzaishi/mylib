package com.example.administrator.lubanone.bean.message;

/**
 * Created by Administrator on 2017\8\9 0009.
 */

public class TrainMessageBean {

  private String userid;
  private String username;
  private String title;
  private String time;
  private String content;
  private String traintheme;
  private String uploadtime;
  private String obtainintegration;
  private String type;
  private String trainid;
  private String trainimg;

  public TrainMessageBean(String userid,String username,String title, String time, String content, String traintheme,
      String uploadtime, String obtainintegration) {
    this.userid = userid;
    this.username = username;
    this.title = title;
    this.time = time;
    this.content = content;
    this.traintheme = traintheme;
    this.uploadtime = uploadtime;
    this.obtainintegration = obtainintegration;
  }

  public TrainMessageBean(String userid, String username, String title, String time,
      String content, String traintheme, String uploadtime, String obtainintegration,
      String type, String trainid, String trainimg) {
    this.userid = userid;
    this.username = username;
    this.title = title;
    this.time = time;
    this.content = content;
    this.traintheme = traintheme;
    this.uploadtime = uploadtime;
    this.obtainintegration = obtainintegration;
    this.type = type;
    this.trainid = trainid;
    this.trainimg = trainimg;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTraintheme() {
    return traintheme;
  }

  public void setTraintheme(String traintheme) {
    this.traintheme = traintheme;
  }

  public String getUploadtime() {
    return uploadtime;
  }

  public void setUploadtime(String uploadtime) {
    this.uploadtime = uploadtime;
  }

  public String getObtainintegration() {
    return obtainintegration;
  }

  public void setObtainintegration(String obtainintegration) {
    this.obtainintegration = obtainintegration;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTrainid() {
    return trainid;
  }

  public void setTrainid(String trainid) {
    this.trainid = trainid;
  }

  public String getTrainimg() {
    return trainimg;
  }

  public void setTrainimg(String trainimg) {
    this.trainimg = trainimg;
  }
}

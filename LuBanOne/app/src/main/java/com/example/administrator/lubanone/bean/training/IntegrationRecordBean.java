package com.example.administrator.lubanone.bean.training;

/**
 * Created by Administrator on 2017\7\12 0012.
 */

public class IntegrationRecordBean {

  private String time;
  private String way;
  private String score;

  public IntegrationRecordBean(String time, String way, String score) {
    this.time = time;
    this.way = way;
    this.score = score;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getWay() {
    return way;
  }

  public void setWay(String way) {
    this.way = way;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }
}

package com.example.administrator.lubanone.bean.model;

import java.util.List;

/**
 * Created by hou on 2017/7/4.
 */

public class UsChildLVCommonBean {

  private List<UsChildLvList> aboutlist;

  public List<UsChildLvList> getAboutlist() {
    return aboutlist;
  }

  public void setAboutlist(
      List<UsChildLvList> aboutlist) {
    this.aboutlist = aboutlist;
  }

  @Override
  public String toString() {
    return "UsChildLVCommonBean{" +
        "aboutlist=" + aboutlist +
        '}';
  }

  public class UsChildLvList {

    private String if_id;
    private String title;
    private String datetime;
    private String thumimg;

    @Override
    public String toString() {
      return "UsChildLVCommonBean{" +
          "if_id='" + if_id + '\'' +
          ", title='" + title + '\'' +
          ", datetime='" + datetime + '\'' +
          ", thumimg='" + thumimg + '\'' +
          '}';
    }

    public String getIf_id() {
      return if_id;
    }

    public void setIf_id(String if_id) {
      this.if_id = if_id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDatetime() {
      return datetime;
    }

    public void setDatetime(String datetime) {
      this.datetime = datetime;
    }

    public String getThumimg() {
      return thumimg;
    }

    public void setThumimg(String thumimg) {
      this.thumimg = thumimg;
    }

  }
}

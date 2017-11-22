package com.example.administrator.lubanone.bean.message;

import java.util.List;

/**
 * Created by Administrator on 2017\7\28 0028.
 */

public class MemberAlbumBean {

  private String time;
  private String type;
  private List imgList;
  private String content;

  public MemberAlbumBean(String time, String type, List imgList, String content) {
    this.time = time;
    this.type = type;
    this.imgList = imgList;
    this.content = content;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List getImgList() {
    return imgList;
  }

  public void setImgList(List imgList) {
    this.imgList = imgList;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

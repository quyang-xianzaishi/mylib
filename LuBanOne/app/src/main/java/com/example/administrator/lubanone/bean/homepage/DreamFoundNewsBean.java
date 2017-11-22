package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quyang on 2017/7/19.
 */

public class DreamFoundNewsBean implements Serializable {


  private List<AboutlistBean> aboutlist;

  public List<AboutlistBean> getAboutlist() {
    return aboutlist;
  }

  public void setAboutlist(List<AboutlistBean> aboutlist) {
    this.aboutlist = aboutlist;
  }

  public static class AboutlistBean implements Serializable {

    /**
     * if_id : 111
     * thumimg : http://103.210.239.20/Public/infothumimg/2.png
     * title : 中国企业家XXX资助平台10000RMB
     * datetime : 2017-07-15 11:24
     */

    private String if_id;
    private String thumimg;
    private String title;
    private String datetime;

    public String getIf_id() {
      return if_id;
    }

    public void setIf_id(String if_id) {
      this.if_id = if_id;
    }

    public String getThumimg() {
      return thumimg;
    }

    public void setThumimg(String thumimg) {
      this.thumimg = thumimg;
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
  }
}

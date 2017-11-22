package com.example.administrator.lubanone.bean;

import com.example.administrator.lubanone.bean.message.RecommendFriendBean;
import java.util.List;

/**
 * Created by Administrator on 2017\8\9 0009.
 */

public class RecommendFriendListResultBean {

  private List<RecommendFriendBean> data;

  public List<RecommendFriendBean> getData() {
    return data;
  }

  public void setData(
      List<RecommendFriendBean> data) {
    this.data = data;
  }
}

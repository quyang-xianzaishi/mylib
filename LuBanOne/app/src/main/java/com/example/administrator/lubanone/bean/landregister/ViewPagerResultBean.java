package com.example.administrator.lubanone.bean.landregister;

import java.util.List;

/**
 * Created by quyang on 2017/7/6.
 */

public class ViewPagerResultBean {


  private List<FlashlistBean> flashlist;

  public List<FlashlistBean> getFlashlist() {
    return flashlist;
  }

  public void setFlashlist(List<FlashlistBean> flashlist) {
    this.flashlist = flashlist;
  }

  public static class FlashlistBean {

    /**
     * flash : http://42.51.40.5/Public/flash/016d0f57d7b3950000018c1be58000.jpg
     */

    private String flash;

    public String getFlash() {
      return flash;
    }

    public void setFlash(String flash) {
      this.flash = flash;
    }
  }
}

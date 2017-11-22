package com.example.administrator.lubanone.bean.homepage;

import java.util.List;

/**
 * Created by quyang on 2017/7/7.
 */

public class AreaResultBean {

  private List<CountrylistBean> countrylist;

  public List<CountrylistBean> getCountrylist() {
    return countrylist;
  }

  public void setCountrylist(List<CountrylistBean> countrylist) {
    this.countrylist = countrylist;
  }

  public static class CountrylistBean {

    /**
     * countryname : 安圭拉
     * countrycode : AIA
     */

    private String countryname;
    private String countrycode;

    public String getCountryname() {
      return countryname;
    }

    public void setCountryname(String countryname) {
      this.countryname = countryname;
    }

    public String getCountrycode() {
      return countrycode;
    }

    public void setCountrycode(String countrycode) {
      this.countrycode = countrycode;
    }
  }
}

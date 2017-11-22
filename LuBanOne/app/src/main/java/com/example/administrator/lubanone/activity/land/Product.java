package com.example.administrator.lubanone.activity.land;

/**
 * Created by admistrator on 2017/7/25.
 */

public class Product {

  public String name;
  public Long time;


  public Product(String name, Long time) {
    this.name = name;
    this.time = time;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }
}

package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/8/17.
 */

public class TrainContinueUpload {

  private String chunk;

  public String getChunk() {
    return chunk;
  }

  public void setChunk(String chunk) {
    this.chunk = chunk;
  }

  @Override
  public String toString() {
    return "TrainContinueUpload{" +
        "chunk='" + chunk + '\'' +
        '}';
  }
}

package com.example.administrator.lubanone.bean.message;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017\7\4 0004.
 */

public class AlbumBean implements Serializable {

    private String time;
    private List<String> imgList;
    private String content;

    public AlbumBean(String time,List<String> imgList,String content){
        this.time = time;
        this.imgList = imgList;
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

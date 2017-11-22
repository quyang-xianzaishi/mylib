package com.example.administrator.lubanone.bean;

/**
 * Created by hou on 2017/6/30.
 */

public class TasksDetailsBean {

    private String creatdate;
    private String title;
    private String catalyst;
    private String videolink;
    private String content;
    private String collect_status;
    private String url;
    private String share_status;
    private String thumimg;

    public String getCreatdate() {
        return creatdate;
    }

    public void setCreatdate(String creatdate) {
        this.creatdate = creatdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatalyst() {
        return catalyst;
    }

    public void setCatalyst(String catalyst) {
        this.catalyst = catalyst;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(String collect_status) {
        this.collect_status = collect_status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShare_status() {
        return share_status;
    }

    public void setShare_status(String share_status) {
        this.share_status = share_status;
    }

    public String getThumimg() {
        return thumimg;
    }

    public void setThumimg(String thumimg) {
        this.thumimg = thumimg;
    }

    @Override
    public String toString() {
        return "TasksDetailsBean{" +
            "creatdate='" + creatdate + '\'' +
            ", title='" + title + '\'' +
            ", catalyst='" + catalyst + '\'' +
            ", videolink='" + videolink + '\'' +
            ", content='" + content + '\'' +
            ", collect_status='" + collect_status + '\'' +
            ", url='" + url + '\'' +
            ", share_status='" + share_status + '\'' +
            ", thumimg='" + thumimg + '\'' +
            '}';
    }
}

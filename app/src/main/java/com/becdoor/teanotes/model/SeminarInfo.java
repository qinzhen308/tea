package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/4.
 * 专题
 */

public class SeminarInfo implements Serializable {

    /**
     * id : 3
     * title : u65f6u5149u5e26u6765u7684u9057u8d602
     * series : 08-14u91d1u6597u7cfbu5217
     * img :
     */

    private String id;
    private String title;
    private String series;
    private String img;
    private String info;

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

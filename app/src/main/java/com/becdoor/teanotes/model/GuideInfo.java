package com.becdoor.teanotes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jjj on 2016/11/21.
 */

public class GuideInfo implements Serializable {

    /**
     * url : ["/images/lead_pics/2015/10/14461942816687.jpg","/images/lead_pics/2015/10/1446194281521.jpg"]
     * time : 1446799394
     * stime : 1180800
     * code : b9ba7e4c0101a6d963097427b0ba32f8
     */

    private int time;
    private int stime;
    private String code;
    private List<String> url;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStime() {
        return stime;
    }

    public void setStime(int stime) {
        this.stime = stime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}

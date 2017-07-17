package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * Created by jjj on 2016/11/17.
 * 地区
 */

public class RegionInfo implements Serializable {

    /**
     * region_id : 0
     * region_name : 全国
     */

    private String region_id;
    private String region_name;

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
}

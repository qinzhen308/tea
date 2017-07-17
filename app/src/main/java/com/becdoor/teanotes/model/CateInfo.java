package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * Created by jjj on 2016/11/7.
 * 分类-首页分类
 */

public class CateInfo implements Serializable{

    /**
     * cat_id : 0
     * cat_name : 全部分类
     * cat_type : 1
     */

    private String cat_id;
    private String cat_name;
    private String cat_type;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }
}

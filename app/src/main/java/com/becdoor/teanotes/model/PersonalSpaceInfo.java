package com.becdoor.teanotes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jjj on 2016/11/8.
 * 会员空间
 */

public class PersonalSpaceInfo implements Serializable {
    private SpaceInfo space_info;
    private List<NoteReInfoBean> article_list;
    private List<CateInfo> all_nav_list;
    private int max_page;
    private int is_atten;

    public SpaceInfo getSpace_info() {
        return space_info;
    }

    public void setSpace_info(SpaceInfo space_info) {
        this.space_info = space_info;
    }

    public List<NoteReInfoBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<NoteReInfoBean> article_list) {
        this.article_list = article_list;
    }

    public List<CateInfo> getAll_nav_list() {
        return all_nav_list;
    }

    public void setAll_nav_list(List<CateInfo> all_nav_list) {
        this.all_nav_list = all_nav_list;
    }

    public int getMax_page() {
        return max_page;
    }

    public void setMax_page(int max_page) {
        this.max_page = max_page;
    }

    public int getIs_atten() {
        return is_atten;
    }

    public void setIs_atten(int is_atten) {
        this.is_atten = is_atten;
    }
}

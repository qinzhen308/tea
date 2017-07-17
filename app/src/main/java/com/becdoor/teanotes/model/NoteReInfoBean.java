package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * 笔记
 */
public class NoteReInfoBean implements Serializable {
    /**
     * article_id : 2
     * cat_id : 2
     * title : 第一篇茶评
     * add_time : 1418745600
     * img :
     * cat_name : 茶评
     */

    private ArticleBean article;
    /**
     * id : 11
     * username : test
     * pic : images/201412/1417662540101732718.jpg
     * rank_name : 豆粉粉
     * space_id :
     */

    private UserInfoBean user_info;
    private String c_count;
    private String s_count;
    private String p_count;

    public boolean isChecked;

    public ArticleBean getArticle() {
        return article;
    }

    public void setArticle(ArticleBean article) {
        this.article = article;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public String getC_count() {
        return c_count;
    }

    public void setC_count(String c_count) {
        this.c_count = c_count;
    }

    public String getS_count() {
        return s_count;
    }

    public void setS_count(String s_count) {
        this.s_count = s_count;
    }

    public String getP_count() {
        return p_count;
    }

    public void setP_count(String p_count) {
        this.p_count = p_count;
    }

}

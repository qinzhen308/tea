package com.becdoor.teanotes.model;


import java.io.Serializable;
import java.util.List;

/**
 * 文章
 */
public class ArticleBean implements Serializable {
    private String article_id;
    private String cat_id;
    private String title;
    private String add_time;
    private String img;
    private String cat_name;
    private String tag;
    private String content;
    private int is_recommend;//推荐
    private int is_new;//最新

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    private List<NoteCommentBean> comm_info;

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<NoteCommentBean> getComm_info() {
        return comm_info;
    }

    public void setComm_info(List<NoteCommentBean> comm_info) {
        this.comm_info = comm_info;
    }
}

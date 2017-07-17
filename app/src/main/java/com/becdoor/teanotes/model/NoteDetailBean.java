package com.becdoor.teanotes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */

public class NoteDetailBean implements Serializable {

    /**
     * cat_id : 10
     * cat_name : 茶之旅
     * article_id : 2771
     * title : 测试一下
     * content :
     * img : /images/cover/2016/10/14778837435606.png
     * user_id : 38
     * author : 珞禅
     * pic : record/avatar/1450610390724395512.jpg
     * add_time : 2016-10-31
     * is_priv : 0
     * is_praise : 0
     * is_collect : 0
     * c_count : 1
     * s_count : 1
     * p_count : 1
     * f_count : 0
     * art_arr : [{"name":"img","value":{"type":"normal","value":"/images/tn/2016/10/14778838346010.png"}},{"name":"content","value":{"type":"text","value":"好的吧"}},{"name":"img","value":{"type":"normal","value":"/images/tn/2016/10/14778838347543.png"}},{"name":"content","value":{"type":"text","value":"上学期在课上老师刚好讲过相关内容，现学现卖一下。想看结论请直接拉到最后（*´∀`）～♥\n\n首先我个人理解的顿悟有两类，一种是佛教术语，一种是问题解决中发生的顿悟，不知道题主指哪类，这里只讨论第二类（第一种实在不了解\u2026\u2026）。\n\n问题解决中的顿悟，说白了就是对一个问题，在瞬间发生的从不会到会的过程。它包含两个方面 ,一是新的有效的问题解决思路如何实现 ,二是旧的无效的思路如何被抛弃（即打破思维定势"}},{"name":"address","value":{"type":"text","value":"重庆"}},{"name":"img","value":{"type":"normal","value":"/images/tn/2016/10/14778838344457.png"}},{"name":"content","value":{"type":"text","value":"有没有\n\n上学期在课上老师刚好讲过相关内容，现学现卖一下。想看结论请直接拉到最后（*´∀`）～♥\n\n首先我个人理解的顿悟有两类，一种是佛教术语，一种是问题解决中发生的顿悟，不知道题主指哪类，这里只讨论第二类（第一种实在不了解\u2026\u2026）。\n\n问题解决中的顿悟，说白了就是对一个问题，在瞬间发生的从不会到会的过程。它包含两个方面 ,一是新的有效的问题解决思路如何实现 ,二是旧的无效的思路如何被抛弃（即打破思维定势\n\n\n上学期在课上老师刚好讲过相关内容，现学现卖一下。想看结论请直接拉到最后（*´∀`）～♥\n\n首先我个人理解的顿悟有两类，一种是佛教术语，一种是问题解决中发生的顿悟，不知道题主指哪类，这里只讨论第二类（第一种实在不了解\u2026\u2026）。\n\n\n\n\n上学期在课上老师刚好讲过相关内容，现学现卖一下。想看结论请直接拉到最后（*´∀`）～♥\n\n首先我个人理解的顿悟有两类，一种是佛教术语，一种是问题解决中发生的顿悟，不知道题主指哪类，这里只讨论第二类（第一种实在不了解\u2026\u2026）。\n\n问题解决中的顿悟，说白了就是对一个问题，在瞬间发生的从不会到会的过程。它包含两个方面 ,一是新的有效的问题解决思路如何实现 ,二是旧的无效的思路如何被抛弃（即打破思维定势\n\n\n\n\n\n\n问题解决中的顿悟，说白了就是对一个问题，在瞬间发生的从不会到会的过程。它包含两个方面 ,一是新的有效的问题解决思路如何实现 ,二是旧的无效的思路如何被抛弃（即打破思维定势\n\n\n\n"}}]
     * cat_list : []
     * comm_list : [{"id":"1519","uid":"38","username":"珞禅","user_avatar":"record/avatar/1450610390724395512.jpg","content":"拍彼此","pid":"0","nickname":"珞禅"}]
     * more_notes : [{"article_id":"2579","title":"安静下来，品【莫辞醉】","img":"/images/cover/2016/07/14676085641013.jpg"},{"article_id":"2581","title":"一个人的电影夜","img":"/images/cover/2016/07/14678908646916.jpg"}]
     */

    public boolean isChecked;

    private int cat_id;
    private String cat_name;
    private String article_id;
    private String title;
    private String content;
    private String img;
    private String user_id;
    private String author;
    private String pic;
    private String add_time;
    private String is_priv;
    private int is_praise;
    private int is_collect;
    private String c_count;
    private String s_count;
    private int p_count;
    private int f_count;
    private List<NoteCommentBean> comm_list;
    private List<ArticleBean> more_notes;
    private List<NoteElement> art_arr;

    public boolean isNative;//标记图片地址是本地路径，还是网络url，涉及到网络图片和本地图片同步上的问题。

    public void setArt_arr(List<NoteElement> art_arr) {
        this.art_arr = art_arr;
    }

    public void setComm_list(List<NoteCommentBean> comm_list) {
        this.comm_list = comm_list;
    }

    public void setMore_notes(List<ArticleBean> more_notes) {
        this.more_notes = more_notes;
    }

    public List<NoteElement> getArt_arr() {
        return art_arr;
    }

    public List<ArticleBean> getMore_notes() {
        return more_notes;
    }

    public List<NoteCommentBean> getComm_list() {
        return comm_list;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getIs_priv() {
        return is_priv;
    }

    public void setIs_priv(String is_priv) {
        this.is_priv = is_priv;
    }

    public int getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(int is_praise) {
        this.is_praise = is_praise;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
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

    public int getP_count() {
        return p_count;
    }

    public void setP_count(int p_count) {
        this.p_count = p_count;
    }

    public int getF_count() {
        return f_count;
    }

    public void setF_count(int f_count) {
        this.f_count = f_count;
    }
}

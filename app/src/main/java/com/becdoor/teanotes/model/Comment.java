package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * Created by paulz on 2016/10/31.
 * 个人中心---评论
 */

public abstract class Comment {
    public String type;

    public abstract String getAatar();
    public abstract String getDate();
    public abstract String getName();
    public abstract String getTitle();
    public abstract String getType();
    public abstract String getContent();
    public abstract String getCommentContent();
    public abstract String getAddDate();
    public abstract String getRankName();
    public abstract String getCommentName();
    public abstract String getCommentId();
    public abstract String getNoteId();
    public abstract boolean hasNote();


    public static class Note{
        public String add_time;
        public String article_id;
        public String author;
        public String cat_name;
        public String content;
        public String rank_name;
        public String title;
        public String uid;
        public String user_pic;
    }

    public static class NoteCom{
        public String aid;
        public String content;
        public String id;
        public String ptime;
        public String user_avatar;
        public String username;


    }

    public static class Act{
        public String act_end_date;
        public String act_id;
        public String act_pic;
        public String act_start_date;
        public String act_status;
        public String act_title;
        public String add_time;
        public String author;
        public String pic;
    }

    public static class ActComm{
        public String act_id;
        public String content;
        public String id;
        public String ptime;
        public String username;

    }
}

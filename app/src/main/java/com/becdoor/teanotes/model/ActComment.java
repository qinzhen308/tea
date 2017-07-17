package com.becdoor.teanotes.model;

/**
 * Created by paulz on 2016/10/31.
 * 个人中心---评论
 */

public class ActComment extends Comment{

    public ActComm comm_info;
    public Act note_info;

    @Override
    public boolean hasNote() {
        if(note_info!=null)return true;
        return false;
    }

    @Override
    public String getCommentName() {
        if(note_info==null)return "未知";
        return note_info.author;
    }

    @Override
    public String getCommentId() {
        return comm_info.id;
    }

    @Override
    public String getNoteId() {
        return note_info.act_id;
    }

    @Override
    public String getAatar() {
        return note_info.act_pic;
    }

    @Override
    public String getDate() {
        return comm_info.ptime;
    }

    @Override
    public String getAddDate() {
        return note_info.add_time;
    }

    @Override
    public String getRankName() {
        return "";
    }

    @Override
    public String getName() {
        return note_info.author;
    }

    @Override
    public String getTitle() {
        return note_info.act_title;
    }

    @Override
    public String getType() {
        return "[活动]";
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public String getCommentContent() {
        return comm_info.content;
    }
}

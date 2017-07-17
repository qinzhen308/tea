package com.becdoor.teanotes.model;

/**
 * Created by paulz on 2016/10/31.
 * 个人中心---评论
 */

public class NoteComment extends Comment{

    public NoteCom comm_info;
    public Note note_info;

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
        return comm_info.aid;
    }

    @Override
    public boolean hasNote() {
        if(note_info!=null)return true;
        return false;
    }

    @Override
    public String getAatar() {
        return note_info.user_pic;
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
        return note_info.rank_name;
    }

    @Override
    public String getName() {
        return note_info.author;
    }

    @Override
    public String getTitle() {
        return note_info.title;
    }

    @Override
    public String getType() {
        return "["+note_info.cat_name+"]";
    }

    @Override
    public String getContent() {
        return note_info.content;
    }

    @Override
    public String getCommentContent() {
        return comm_info.content;
    }
}

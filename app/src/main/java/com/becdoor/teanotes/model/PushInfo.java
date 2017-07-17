package com.becdoor.teanotes.model;

/**
 * Created by jjj on 2016/11/14.
 * 推送设置
 */

public class PushInfo {

    /**
     * id : 1
     * uid : 14
     * note_push : 1 笔记推荐
     * friend_push : 1 好友关注
     * collect_push : 1 收藏笔记
     * praise_push : 1 喜欢笔记
     * comm_push : 1 评论笔记
     * msg_push : 1 私信
     * area_push : 1 靠近合作商区域接收推送信息
     */

    private String id;
    private String uid;
    private int note_push;
    private int friend_push;
    private int collect_push;
    private int praise_push;
    private int comm_push;
    private int msg_push;
    private int area_push;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getNote_push() {
        return note_push;
    }

    public void setNote_push(int note_push) {
        this.note_push = note_push;
    }

    public int getFriend_push() {
        return friend_push;
    }

    public void setFriend_push(int friend_push) {
        this.friend_push = friend_push;
    }

    public int getCollect_push() {
        return collect_push;
    }

    public void setCollect_push(int collect_push) {
        this.collect_push = collect_push;
    }

    public int getPraise_push() {
        return praise_push;
    }

    public void setPraise_push(int praise_push) {
        this.praise_push = praise_push;
    }

    public int getComm_push() {
        return comm_push;
    }

    public void setComm_push(int comm_push) {
        this.comm_push = comm_push;
    }

    public int getMsg_push() {
        return msg_push;
    }

    public void setMsg_push(int msg_push) {
        this.msg_push = msg_push;
    }

    public int getArea_push() {
        return area_push;
    }

    public void setArea_push(int area_push) {
        this.area_push = area_push;
    }
}

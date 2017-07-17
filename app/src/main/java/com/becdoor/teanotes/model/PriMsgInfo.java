package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * Created by jjj on 2016/11/9.
 * 私信
 */

public class PriMsgInfo implements Serializable {

    /**
     * parent_id : 20
     * send_time : 2014-04-03
     * sender_name : NEWuser
     * avatar :
     * message : u79c1u4fe11
     * receiver_name : NEWuser
     * receiver_avatar :
     * status : 1
     */

    private String parent_id;
    private String send_time;
    private String sender_name;
    private String avatar;
    private String message;
    private String receiver_name;
    private String receiver_avatar;
    private String status;
    private String sender_id;//发送人ID;
    private String type;//right"显示在右边，"left"显示在左边
    private String receiver_id;//接收人ID;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_avatar() {
        return receiver_avatar;
    }

    public void setReceiver_avatar(String receiver_avatar) {
        this.receiver_avatar = receiver_avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

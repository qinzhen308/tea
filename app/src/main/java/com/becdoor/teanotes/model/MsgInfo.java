package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * Created by jjj on 2016/11/9.
 * 通知
 */

public class MsgInfo implements Serializable {

    /**
     * msg_id : 22
     * title : u6d3bu52a8u63a8u9001
     * send_time : 2015-04-23
     * status : 1
     * link_id : 11
     * ptype : 1
     */

    private String msg_id;
    private String title;
    private String send_time;
    private String status;//信息状态【1为已读，0为未读】}
    private String link_id;
    private String ptype;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }
}

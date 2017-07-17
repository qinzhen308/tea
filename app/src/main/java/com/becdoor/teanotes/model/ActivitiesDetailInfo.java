package com.becdoor.teanotes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 * 活动详情
 */

public class ActivitiesDetailInfo implements Serializable {
    private ActivityInfo act_info;
    private List<NoteCommentBean> comm_list;
    private int is_join;
    private int is_praise;
    private int is_collect;

    public ActivityInfo getAct_info() {
        return act_info;
    }

    public void setAct_info(ActivityInfo act_info) {
        this.act_info = act_info;
    }

    public List<NoteCommentBean> getComm_list() {
        return comm_list;
    }

    public void setComm_list(List<NoteCommentBean> comm_list) {
        this.comm_list = comm_list;
    }

    public int getIs_join() {
        return is_join;
    }

    public void setIs_join(int is_join) {
        this.is_join = is_join;
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
}

package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/1.
 * 活动
 */

public class ActivityInfo implements Serializable {

    /**
     * act_id : 4
     * act_title : u6597u8bb0u767eu4e07u8336u4f1au5927u8d5bu6d3bu52a83
     * act_pic : record/activity/1423547428194210019.png
     * act_type : 1
     * act_start_date : 2015-01-15
     * act_end_date : 2015-01-16
     * act_address : u6606u660eu4e07u8c6au9152u5e97u4e09u697cu4f1au8baeu5ba4
     * act_status : 0
     */

    private String act_id;
    private String act_title;
    private String act_pic;
    private String act_type;//"act_type":1为线上,2为线下 "
    private String act_start_date;
    private String act_end_date;
    private String act_address;
    private String act_status;//活动状态,0为已结束1为即将开始2为火热进行中
    /**
     * act_s_pic : images/201510/1446111779060548067.jpg
     * act_desc : 【活动内容】 本届茶会分为“常规活动”和“竞技活动”两个类别。旨在吸引更多新老顾客能够参与到茶会中，能够更多的接触斗记，并了解盲品是什么！【活动组织】 主办单位：云南斗记茶业有限公司；  协办单位：云南斗记茶业有限公司各地门店 【活动对象】 爱茶人士、茶行业人士（除斗记总部工作人员及全国斗记各级合作商外）。
     * act_rule : 斗记-百万茶会斗茶大赛将设100万元现金大奖，经初赛、复赛、决赛、总决赛，最终决出斗记-百万茶会2014年度唯一“斗茶王”，奖励现金100万元。在历时8个月的赛程中，还将以“百万茶会”为平台，在全国倡导正确的普洱茶冲泡方法，培养茶友健康饮茶文化，并将在今年面向海内外征集100万名普洱茶爱好者，送出总价值100万元的品鉴茶礼。
     * act_user_num : 10000
     * act_add_time : 1435460518
     * act_sort : 255
     * praise_uid :
     * share_uid :
     * act_open : 1
     * is_recommend : 1
     * keywords :
     * description :
     * uid : 0
     * author : 斗记官方
     * province : 6
     * telephone : 4008-510-51
     * cost : 0
     * audit_state : 2
     * is_audit : 1
     * show_index : 1
     * c_count : 6
     * s_count : 3
     * f_count : 0
     * j_count : 80
     */

    private String act_s_pic;
    private String act_desc;
    private String act_rule;
    private String act_user_num;
    private String act_add_time;
    private String act_sort;
    private String praise_uid;
    private String share_uid;
    private String act_open;
    private String is_recommend;
    private String keywords;
    private String description;
    private String uid;
    private String author;
    private String province;
    private String telephone;
    private String cost;
    private String audit_state;
    private String is_audit;
    private String show_index;
    private String c_count;
    private String s_count;
    private int f_count;
    private int j_count;

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    public String getAct_title() {
        return act_title;
    }

    public void setAct_title(String act_title) {
        this.act_title = act_title;
    }

    public String getAct_pic() {
        return act_pic;
    }

    public void setAct_pic(String act_pic) {
        this.act_pic = act_pic;
    }

    public String getAct_type() {
        return act_type;
    }

    public void setAct_type(String act_type) {
        this.act_type = act_type;
    }

    public String getAct_start_date() {
        return act_start_date;
    }

    public void setAct_start_date(String act_start_date) {
        this.act_start_date = act_start_date;
    }

    public String getAct_end_date() {
        return act_end_date;
    }

    public void setAct_end_date(String act_end_date) {
        this.act_end_date = act_end_date;
    }

    public String getAct_address() {
        return act_address;
    }

    public void setAct_address(String act_address) {
        this.act_address = act_address;
    }

    public String getAct_status() {
        return act_status;
    }

    public void setAct_status(String act_status) {
        this.act_status = act_status;
    }

    public String getAct_s_pic() {
        return act_s_pic;
    }

    public void setAct_s_pic(String act_s_pic) {
        this.act_s_pic = act_s_pic;
    }

    public String getAct_desc() {
        return act_desc;
    }

    public void setAct_desc(String act_desc) {
        this.act_desc = act_desc;
    }

    public String getAct_rule() {
        return act_rule;
    }

    public void setAct_rule(String act_rule) {
        this.act_rule = act_rule;
    }

    public String getAct_user_num() {
        return act_user_num;
    }

    public void setAct_user_num(String act_user_num) {
        this.act_user_num = act_user_num;
    }

    public String getAct_add_time() {
        return act_add_time;
    }

    public void setAct_add_time(String act_add_time) {
        this.act_add_time = act_add_time;
    }

    public String getAct_sort() {
        return act_sort;
    }

    public void setAct_sort(String act_sort) {
        this.act_sort = act_sort;
    }

    public String getPraise_uid() {
        return praise_uid;
    }

    public void setPraise_uid(String praise_uid) {
        this.praise_uid = praise_uid;
    }

    public String getShare_uid() {
        return share_uid;
    }

    public void setShare_uid(String share_uid) {
        this.share_uid = share_uid;
    }

    public String getAct_open() {
        return act_open;
    }

    public void setAct_open(String act_open) {
        this.act_open = act_open;
    }

    public String getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(String is_recommend) {
        this.is_recommend = is_recommend;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAudit_state() {
        return audit_state;
    }

    public void setAudit_state(String audit_state) {
        this.audit_state = audit_state;
    }

    public String getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(String is_audit) {
        this.is_audit = is_audit;
    }

    public String getShow_index() {
        return show_index;
    }

    public void setShow_index(String show_index) {
        this.show_index = show_index;
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

    public int getF_count() {
        return f_count;
    }

    public void setF_count(int f_count) {
        this.f_count = f_count;
    }

    public int getJ_count() {
        return j_count;
    }

    public void setJ_count(int j_count) {
        this.j_count = j_count;
    }
}

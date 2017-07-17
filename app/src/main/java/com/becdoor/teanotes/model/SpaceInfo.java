package com.becdoor.teanotes.model;

/**
 * Created by jjj on 2016/11/8.
 * 空间信息
 */

public class SpaceInfo {

    /**
     * uid : 14
     * name : user123
     * focus_pic : ./images/focus.jpg
     * avatar :
     * rank_name : u8c46u7c89u7c89
     * nickname : user123
     * slogan : null
     * privacy : 1
     */

    private String uid;
    private String name;
    private String focus_pic;
    private String avatar;
    private String rank_name;
    private String nickname;
    private String slogan;
    private String privacy;
    private int is_atten;//是否关注

    public void setIs_atten(int is_atten) {
        this.is_atten = is_atten;
    }

    public int getIs_atten() {
        return is_atten;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFocus_pic() {
        return focus_pic;
    }

    public void setFocus_pic(String focus_pic) {
        this.focus_pic = focus_pic;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}

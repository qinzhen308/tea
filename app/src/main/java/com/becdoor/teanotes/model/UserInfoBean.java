package com.becdoor.teanotes.model;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfoBean implements Serializable {
    private String id;
    private String username;
    private String pic;//头像
    private String avatar;//头像
    private String rank_name;//等级名
    private String space_id;
    private int point;//积分
    private String signed;//个性签名
    private int sex;//个性签名 1-男 2-女
    private String birthday;//个性签名
    private String hometown;//个性签名
    private String hobby;//个性签名
    private String nickname;//个性签名

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public void setSigned(String signed) {
        this.signed = signed;
    }

    public String getSigned() {
        return signed;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public String getSpace_id() {
        return space_id;
    }

    public void setSpace_id(String space_id) {
        this.space_id = space_id;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", pic='" + pic + '\'' +
                ", rank_name='" + rank_name + '\'' +
                ", space_id='" + space_id + '\'' +
                '}';
    }
}

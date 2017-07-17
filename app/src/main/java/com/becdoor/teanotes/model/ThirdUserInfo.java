package com.becdoor.teanotes.model;

/**
 * Created by jjj on 2016/11/17.
 */

public class ThirdUserInfo {
    private WXUser wxUser;
    private SinaUser sinaUser;

    public WXUser getWxUser() {
        return wxUser;
    }

    public void setWxUser(WXUser wxUser) {
        this.wxUser = wxUser;
    }

    public void setSinaUser(SinaUser sinaUser) {
        this.sinaUser = sinaUser;
    }

    public SinaUser getSinaUser() {
        return sinaUser;
    }

    public static class WXUser {
        private String access_token;//access_token",
        private String openid;
        private String nickname;// 昵称, "
        private String sex;//性别, " +
        private String province;//省 "
        private String city;//市, "
        private String country;//区, "
        private String headimgurl;//头像, "
        private String privilege;//[特权信息 ], "
        private String unionid;//UID'

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getPrivilege() {
            return privilege;
        }

        public void setPrivilege(String privilege) {
            this.privilege = privilege;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
    }

    public static class SinaUser {
        private String uid;//  用户id
        private String access_token;
        private String screen_name;// 用户名：
        private String profile_image_url;//头像：

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }
    }
}

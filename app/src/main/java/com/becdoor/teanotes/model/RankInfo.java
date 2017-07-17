package com.becdoor.teanotes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jjj on 2016/11/11.
 * 等级
 */

public class RankInfo implements Serializable {

    /**
     * avatar : images/201412/1417662540101732718.jpg
     * rank_name : 斗粉
     * username : 大神
     * point : 575
     * rank_list : [{"rank_name":"u6597u7c89u7c89","rank_point":"1-500"},{"rank_name":"u6597u7c89","rank_point":"501-1500"},{"rank_name":"u4e0au6597","rank_point":"1501-3500"},{"rank_name":"u5927u6597","rank_point":"3501-7500"},{"rank_name":"u7389u6597","rank_point":"7501-15000"},{"rank_name":"u91d1u6597","rank_point":"15001-3万"},{"rank_name":"巅斗","rank_point":"3万以上"}]
     * rule_list : [{"name":"u6bcfu65e5u8bbfu95eeu9001u79efu5206","val":"5"},{"name":"u5206u4eabu7b14u8bb0","val":"5"},{"name":"u8bc4u8bba","val":"5"},{"name":"u53d1u5e03u7b14u8bb0","val":"50"},{"name":"u63a8u8350u7cbeu9009u5f97u5206","val":"100"},{"name":"u5f53u5929u6700u9ad8u7d2fu8ba1u79efu5206u4e0au9650","val":"1000000"},{"name":"每年积分自动清空一次","val":""}]
     */

    private String avatar;
    private String rank_name;
    private String username;
    private String point;
    /**
     * rank_name : u6597u7c89u7c89
     * rank_point : 1-500
     */

    private List<RankListBean> rank_list;
    /**
     * name : u6bcfu65e5u8bbfu95eeu9001u79efu5206
     * val : 5
     */

    private List<RuleListBean> rule_list;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public List<RankListBean> getRank_list() {
        return rank_list;
    }

    public void setRank_list(List<RankListBean> rank_list) {
        this.rank_list = rank_list;
    }

    public List<RuleListBean> getRule_list() {
        return rule_list;
    }

    public void setRule_list(List<RuleListBean> rule_list) {
        this.rule_list = rule_list;
    }

    public static class RankListBean {
        private String rank_name;
        private String rank_point;

        public String getRank_name() {
            return rank_name;
        }

        public void setRank_name(String rank_name) {
            this.rank_name = rank_name;
        }

        public String getRank_point() {
            return rank_point;
        }

        public void setRank_point(String rank_point) {
            this.rank_point = rank_point;
        }
    }

    public static class RuleListBean {
        private String name;
        private String val;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}

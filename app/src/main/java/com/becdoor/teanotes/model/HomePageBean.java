package com.becdoor.teanotes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 * 默认首页
 */

public class HomePageBean implements Serializable {
    /**
     * notice_list : [{"act_id":"5","title":"u534eu5357u5730u533au767du4e07u8336u4f1au6700u65b0u6d3bu52a8u4e0au7ebf"}]
     * focus_pics : [{"src":"http://dj.com/record/afficheimg/20150603mybypv.jpg","sel_id":"9","cate_type":"1"},{"src":"http://dj.com/record/afficheimg/20150602msflkh.jpg","sel_id":"5","cate_type":"3"}]
     * uuid_arr : [{"uuid":"12312322aa","uptime":"1429757573"},{"uuid":"dfsdf1232","uptime":"1429757602"}]
     * alert_msg : 每日签到，加5个茶籽！
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String alert_msg;
        /**
         * act_id : 5
         * title : u534eu5357u5730u533au767du4e07u8336u4f1au6700u65b0u6d3bu52a8u4e0au7ebf
         */

        private List<NoticeListBean> notice_list;
        /**
         * src : http://dj.com/record/afficheimg/20150603mybypv.jpg
         * sel_id : 9
         * cate_type : 1
         */

        private List<FocusPicsBean> focus_pics;
        /**
         * uuid : 12312322aa
         * uptime : 1429757573
         */

        private List<UuidArrBean> uuid_arr;

        public String getAlert_msg() {
            return alert_msg;
        }

        public void setAlert_msg(String alert_msg) {
            this.alert_msg = alert_msg;
        }

        public List<NoticeListBean> getNotice_list() {
            return notice_list;
        }

        public void setNotice_list(List<NoticeListBean> notice_list) {
            this.notice_list = notice_list;
        }

        public List<FocusPicsBean> getFocus_pics() {
            return focus_pics;
        }

        public void setFocus_pics(List<FocusPicsBean> focus_pics) {
            this.focus_pics = focus_pics;
        }

        public List<UuidArrBean> getUuid_arr() {
            return uuid_arr;
        }

        public void setUuid_arr(List<UuidArrBean> uuid_arr) {
            this.uuid_arr = uuid_arr;
        }

        public static class NoticeListBean {
            private String act_id;
            private String title;

            public String getAct_id() {
                return act_id;
            }

            public void setAct_id(String act_id) {
                this.act_id = act_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class FocusPicsBean {
            //             sel_id":关联信息的ID（
//            当cate_type为1和2时，表示为笔记的ID
//            当cate_type为3时，表示为专题的ID
//            当cate_type为4时，表示为活动的ID
//            当cate_type为5时，表示为会员的ID）
//                    "cate_type":关联的类别，1为茶评，2为茶志，3为专题，4为活动，5为空间

            private String src;
            private String sel_id;
            private String cate_type;

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

            public String getSel_id() {
                return sel_id;
            }

            public void setSel_id(String sel_id) {
                this.sel_id = sel_id;
            }

            public String getCate_type() {
                return cate_type;
            }

            public void setCate_type(String cate_type) {
                this.cate_type = cate_type;
            }
        }

        public static class UuidArrBean {
            private String uuid;
            private String uptime;

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public String getUptime() {
                return uptime;
            }

            public void setUptime(String uptime) {
                this.uptime = uptime;
            }
        }
    }
}

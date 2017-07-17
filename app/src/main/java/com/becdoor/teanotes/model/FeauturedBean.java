package com.becdoor.teanotes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 * 精选  ----》笔记推荐
 */

public class FeauturedBean implements Serializable {

    /**
     * cate_list : [{"cat_id":"2","cat_name":"u8336u8bc4"},{"cat_id":"5","cat_name":"u85cfu8336"},{"cat_id":"9","cat_name":"u8336u4ebau7b14u8bb0"},{"cat_id":"7","cat_name":"u4fbfu7b7e"}]
     * noteRe_info : [{"article":{"article_id":"2","cat_id":"2","title":"第一篇茶评","add_time":"1418745600","img":"","cat_name":"茶评"},"user_info":{"id":"11","username":"test","pic":"images/201412/1417662540101732718.jpg","rank_name":"豆粉粉","space_id":""},"c_count":"0","s_count":"0","p_count":0}]
     * max_page : 5
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int max_page;
        /**
         * cat_id : 2
         * cat_name : u8336u8bc4
         */

        private List<CateListBean> cate_list;
        /**
         * article : {"article_id":"2","cat_id":"2","title":"第一篇茶评","add_time":"1418745600","img":"","cat_name":"茶评"}
         * user_info : {"id":"11","username":"test","pic":"images/201412/1417662540101732718.jpg","rank_name":"豆粉粉","space_id":""}
         * c_count : 0
         * s_count : 0
         * p_count : 0
         */

        private List<NoteReInfoBean> noteRe_info;

        public int getMax_page() {
            return max_page;
        }

        public void setMax_page(int max_page) {
            this.max_page = max_page;
        }

        public List<CateListBean> getCate_list() {
            return cate_list;
        }

        public void setCate_list(List<CateListBean> cate_list) {
            this.cate_list = cate_list;
        }

        public List<NoteReInfoBean> getNoteRe_info() {
            return noteRe_info;
        }

        public void setNoteRe_info(List<NoteReInfoBean> noteRe_info) {
            this.noteRe_info = noteRe_info;
        }

        public static class CateListBean {
            private String cat_id;
            private String cat_name;

            public String getCat_id() {
                return cat_id;
            }

            public void setCat_id(String cat_id) {
                this.cat_id = cat_id;
            }

            public String getCat_name() {
                return cat_name;
            }

            public void setCat_name(String cat_name) {
                this.cat_name = cat_name;
            }
        }


    }
}

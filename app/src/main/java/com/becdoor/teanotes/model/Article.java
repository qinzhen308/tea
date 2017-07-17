package com.becdoor.teanotes.model;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */

public class Article {
    public String cat_id;
    public String article_id;
    public String title;
    public String content;
    public String img;
    public String user_id;
    public String author;
    public String pic;
    public String add_time;
    public String is_priv;
    public String is_collect;
    public String is_praise;
    public String c_count;
    public String s_count;
    public String p_count;
    public String f_count;
    public List<String> cate_list;
    public List<String> art_arr;
    public List<String> comm_list;
    public List<String> more_notes;


//    public static class NoteElement{
//        public final static String TYPE_TEXT="content";
//        public final static String TYPE_ADDRESS="address";
//        public final static String TYPE_IMG="img";
//        public String name;
//        public NValue value;
//
//        public NoteElement(){
//
//        }
//
//        public NoteElement(String name){
//            this.name=name;
//            value=new NValue();
//            if(TYPE_IMG.equals(name)){
//                value.value="normal";
//            }else {
//                value.value="text";
//            }
//        }
//
//        public static class NValue{
//            public String type;
//            public String value;
//        }
//
//        public String getNamge(){
//            return name;
//        }
//        public void setEnableValue(String v){
//            if(value==null){
//                value=new NValue();
//            }
//            value.value=v;
//        }
//
//        public String getEnableValue(){
//            if(value==null){
//                return "";
//            }
//            return value.value;
//        }
//
//        public boolean isValid(){
//            if(value!=null&& !TextUtils.isEmpty(value.value))return true;
//            return false;
//        }
//
//    }


}

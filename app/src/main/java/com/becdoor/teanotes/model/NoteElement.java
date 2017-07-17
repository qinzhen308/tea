package com.becdoor.teanotes.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/31.
 * 笔记详情的内容
 */

public class NoteElement implements Serializable{
    public final static String TYPE_TEXT="content";
    public final static String TYPE_ADDRESS="address";
    public final static String TYPE_IMG="img";
    public String name;
    public NValue value;

    public NoteElement(){

    }

    public NoteElement(String name){
        this.name=name;
        value=new NValue();
        if(TYPE_IMG.equals(name)){
            value.type="normal";
        }else {
            value.type="text";
        }
    }

    public static class NValue implements Serializable{
        public String type;
        public String value;
        private boolean isNative;
    }

    public String getNamge(){
        return name;
    }

    public void setEnableValue(String v){
        if(value==null){
            value=new NValue();
        }
        value.value=v;
    }

    public void setEnableValue(String v,boolean isNative){
        if(value==null){
            value=new NValue();
        }
        if(TYPE_IMG.equals(name)){
            this.value.isNative=isNative;
        }
        value.value=v;
    }

    public boolean isNative(){
        if(value==null)return false;
        return value.isNative;
    }

    public String getEnableValue(){
        if(value==null){
            return "";
        }
        return value.value;
    }

    public boolean isValid(){
        if(value!=null&& !TextUtils.isEmpty(value.value))return true;
        return false;
    }

}

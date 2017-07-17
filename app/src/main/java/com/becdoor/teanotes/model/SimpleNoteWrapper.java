package com.becdoor.teanotes.model;


import java.io.Serializable;
import java.util.List;

/**
 * 便签
 */
public class SimpleNoteWrapper implements Serializable{
    public int max_page;
    List<SimpleNote> note_list;


    public static class SimpleNote implements Serializable{
        public String article_id;
        public String title;
        public String add_time;
        public String content;
        public boolean isChecked;

    }

}

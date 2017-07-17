package com.becdoor.teanotes.model;


import java.util.List;

/**
 * 藏茶
 */
public class CollectedTeaWrapper {
    public int max_page;
    List<CollectedTea> note_list;


    public static class CollectedTea{
        public String article_id;
        public String title;
        public String cat_id;
        public String img;
        public String num;
        public String money;
        public String brand;
        public boolean isChecked;

    }

}

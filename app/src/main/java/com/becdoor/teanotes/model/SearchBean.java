package com.becdoor.teanotes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */

public class SearchBean implements Serializable {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<String> words;

        public List<String> getWords() {
            return words;
        }

        public void setWords(List<String> words) {
            this.words = words;
        }
    }
}

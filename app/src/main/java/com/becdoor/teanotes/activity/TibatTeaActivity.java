package com.becdoor.teanotes.activity;

import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * 藏茶管理
 * Created by Administrator on 2016/10/14.
 */

public class TibatTeaActivity extends TitleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}

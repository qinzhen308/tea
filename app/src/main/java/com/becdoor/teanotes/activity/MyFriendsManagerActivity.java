package com.becdoor.teanotes.activity;

import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/14.
 * 好友管理
 */

public class MyFriendsManagerActivity extends TitleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}

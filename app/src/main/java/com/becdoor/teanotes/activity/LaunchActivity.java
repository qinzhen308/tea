package com.becdoor.teanotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.FragmentActivity1;
import com.becdoor.teanotes.fragment.MainActivity;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.until.Remember;

/**
 * Created by Administrator on 2016/11/3.
 */

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (Remember.getBoolean("isFirst", true)) {
                    startActivity(new Intent(LaunchActivity.this, GuideActivity.class));
                } else {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                }

                finish();
            }
        }, 2000);

    }
}

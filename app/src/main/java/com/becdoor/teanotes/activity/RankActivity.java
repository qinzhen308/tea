package com.becdoor.teanotes.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.FragmentRank;

/**
 * Created by jjj on 2016/11/14.
 * 积分等级
 */

public class RankActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        FragmentRank firstFragment = new FragmentRank();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBack", true);
        firstFragment.setArguments(bundle);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rank_frameLayout, firstFragment).commit();
    }

}

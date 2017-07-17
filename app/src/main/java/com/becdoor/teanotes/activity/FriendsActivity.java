package com.becdoor.teanotes.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.CollectionActiveFragment;
import com.becdoor.teanotes.fragment.CollectionArticleFragment;
import com.becdoor.teanotes.fragment.MyFriendsFragment;
import com.becdoor.teanotes.model.AtticleCate;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的好友
 * 粉丝和关注
 */
public class FriendsActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private View tvTab1;
    private TextView tvTab2;
    private TextView btnBack;
    private ImageView ivManage;


    private List<String> cateNameList = new ArrayList<>();
    MyPagerAdapter adapter;
    private int curIndex;

    private int mode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        initView();
    }


    private void initView() {
        btnBack = (TextView) findViewById(R.id.title_leftTv);
        ivManage = (ImageView) findViewById(R.id.setting_btn);
        tvTab1 = findViewById(R.id.tab1);
        tvTab2 = (TextView) findViewById(R.id.tab2);
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        ivManage.setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewpager);
        viewpager.setCurrentItem(0);
        changeData(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeData(position);
                curIndex = position;
//                if(position==0){
//                    ((CollectionArticleFragment)adapter.mFragment.get(curIndex)).doRefresh();
//                }else {
//                    ((CollectionActiveFragment)adapter.mFragment.get(curIndex)).doRefresh();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupViewPager(ViewPager viewpager) {
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MyFriendsFragment.newInstance(MyFriendsFragment.TAB_CARE), "关注");
        adapter.addFragment(MyFriendsFragment.newInstance(MyFriendsFragment.TAB_FANS), "粉丝");
        viewpager.setAdapter(adapter);
    }

    public void switchMode(){
        if(mode==1){
            mode=0;
            ivManage.setImageResource(R.drawable.setting_icon_unchecked);
        }else if(mode==0){
            mode=1;
            ivManage.setImageResource(R.drawable.setting_icon_checked);
        }
        if(curIndex==0){
            adapter.mFragment.get(0).switchMode();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == tvTab1) {
            if (curIndex == 0) {
                return;
            }
            viewpager.setCurrentItem(0);
            changeData(0);
        } else if (v == tvTab2) {
            if (curIndex == 1) {
                return;
            }
            changeData(1);
            viewpager.setCurrentItem(1);
        } else if (v == btnBack) {//返回
            finish();
        }else if(v==ivManage){
            switchMode();
        }
    }

    /**
     * 改变数据
     *
     * @param position
     */
    private void changeData(int position) {
        if (position == 0) {
            tvTab1.setSelected(true);
            tvTab2.setSelected(false);
            ivManage.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            tvTab1.setSelected(false);
            tvTab2.setSelected(true);
            ivManage.setVisibility(View.GONE);
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<MyFriendsFragment> mFragment = new ArrayList<MyFriendsFragment>();
        private final List<String> mFragmentTitle = new ArrayList<String>();

        public void addFragment(MyFriendsFragment fragment, String title) {
            mFragment.add(fragment);
            mFragmentTitle.add(title);
        }

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitle.get(position);
        }
    }


    public static void invoke(Context context) {
        Intent intent = new Intent(context, FriendsActivity.class);
        context.startActivity(intent);
    }
}

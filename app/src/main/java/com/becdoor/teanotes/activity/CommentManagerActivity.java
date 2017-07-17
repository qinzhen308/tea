package com.becdoor.teanotes.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.CollectionActiveFragment;
import com.becdoor.teanotes.fragment.CollectionArticleFragment;
import com.becdoor.teanotes.fragment.CommentFragment;
import com.becdoor.teanotes.model.AtticleCate;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论管理
 * 筛选笔记、活动和全部
 * 分我评论别人和别人评论我两个标签
 */
public class CommentManagerActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private View tvTab1;
    private View layoutCate;
    private TextView tvTab2;
    private TextView btnBack;
    private TextView tvCate;

    private PopupWindow pop;

    private List<String> tpyeList = new ArrayList<>();
    MyPagerAdapter adapter;
    private int curIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_manager);
        tpyeList.add("笔记");
        tpyeList.add("活动");
        initView();
    }


    private void initView() {
        layoutCate =  findViewById(R.id.main_catergoryLayout);
        tvCate = (TextView) findViewById(R.id.tv_cate);
        btnBack = (TextView) findViewById(R.id.title_leftTv);
        tvTab1 = findViewById(R.id.tab1);
        tvTab2 = (TextView) findViewById(R.id.tab2);
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        layoutCate.setOnClickListener(this);
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
        adapter.addFragment(CommentFragment.newInstance(CommentFragment.TAB_MINE), "我评别人");
        adapter.addFragment(CommentFragment.newInstance(CommentFragment.TAB_TO_ME), "别人评我");
        viewpager.setAdapter(adapter);
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
        }else if(v==layoutCate){
            showCateDialog();
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
            layoutCate.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            tvTab1.setSelected(false);
            tvTab2.setSelected(true);
            layoutCate.setVisibility(View.GONE);
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<CommentFragment> mFragment = new ArrayList<CommentFragment>();
        private final List<String> mFragmentTitle = new ArrayList<String>();

        public void addFragment(CommentFragment fragment, String title) {
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

    private void showCateDialog() {
        this.pop = initSimpalPopWindow(this, tpyeList, new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                        pop.dismiss();
                        String curType = tpyeList.get(paramAnonymousInt);
                        tvCate.setText(curType);
                        //设置分类
                        adapter.mFragment.get(0).setType(curType.equals("活动")?"act":"note");
                        //刷新当前页
                    }
                }
                , layoutCate);
    }

    public PopupWindow initSimpalPopWindow(Activity paramActivity, List<String> paramList, AdapterView.OnItemClickListener paramOnItemClickListener, View paramView) {
        Object localObject = null;
        if (paramActivity != null) {
            localObject = (ListView) LayoutInflater.from(this).inflate(R.layout.layout_popwindow_listview1, null);
            ((ListView) localObject).setAdapter(new ArrayAdapter<String>(paramActivity, R.layout.layout_popwindow_listview_item2, R.id.popwindow_tv, paramList));
            ((ListView) localObject).setOnItemClickListener(paramOnItemClickListener);
            localObject = new PopupWindow((View) localObject, getResources().getDimensionPixelSize(R.dimen.height_60), WindowManager.LayoutParams.WRAP_CONTENT);
            ((PopupWindow) localObject).setFocusable(true);
            ((PopupWindow) localObject).setTouchable(true);
            ((PopupWindow) localObject).setBackgroundDrawable(new BitmapDrawable());
            ((PopupWindow) localObject).setOutsideTouchable(true);
            ((PopupWindow) localObject).showAsDropDown(paramView, -20, 0);
        }
        return (PopupWindow) localObject;
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, CommentManagerActivity.class);
        context.startActivity(intent);
    }
}

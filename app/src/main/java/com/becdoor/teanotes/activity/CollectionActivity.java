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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.CollectionActiveFragment;
import com.becdoor.teanotes.fragment.CollectionArticleFragment;
import com.becdoor.teanotes.fragment.TeaAtticleFragment;
import com.becdoor.teanotes.model.AtticleCate;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏
 * 笔记和活动
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private View tvTab1;
    private TextView tvTab2;
    private TextView btnBack;
    private TextView tvCate;

    private PopupWindow pop;

    private List<AtticleCate> cateList;
    private List<String> cateNameList = new ArrayList<>();
    private AtticleCate selectedCate;
    MyPagerAdapter adapter;
    private int curIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
    }


    private void initView() {
        findViewById(R.id.title_leftTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCate = (TextView) findViewById(R.id.tv_cate);
        btnBack = (TextView) findViewById(R.id.title_leftTv);
        tvTab1 = findViewById(R.id.tab1);
        tvTab2 = (TextView) findViewById(R.id.tab2);
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
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

    //catelist是在fragment中的列表接口伴随返回的数据，所以fragment获取数据后重新更新。
    //为了防止频繁更新，fragment处应先判断cate数据有变化才调用这个方法。
    public void updateCate(List<AtticleCate> list) {
        cateList = list;
        if (list == null) {
            return;
        }
        cateNameList.clear();
        for (int i = 0; i < list.size(); i++) {
            cateNameList.add(list.get(i).cat_name);
        }
    }

    private void setupViewPager(ViewPager viewpager) {
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CollectionArticleFragment.newInstance(), "笔记");
        adapter.addFragment(CollectionActiveFragment.newInstance(), "活动");
        viewpager.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        if (v == tvTab1) {
            if (curIndex == 0) {
                showCateDialog();
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
        } else if (position == 1) {
            tvTab1.setSelected(false);
            tvTab2.setSelected(true);
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragment = new ArrayList<Fragment>();
        private final List<String> mFragmentTitle = new ArrayList<String>();

        public void addFragment(Fragment fragment, String title) {
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
        this.pop = initSimpalPopWindow(this, cateNameList, new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                        pop.dismiss();
                        selectedCate = cateList.get(paramAnonymousInt);
                        tvCate.setText(selectedCate.cat_name);
                        //设置分类
                        ((CollectionArticleFragment) adapter.mFragment.get(0)).setCate(selectedCate.cat_id);
                        //刷新当前页
                        ((CollectionArticleFragment) adapter.mFragment.get(curIndex)).doRefresh();
                    }
                }
                , tvTab1);
    }

    public PopupWindow initSimpalPopWindow(Activity paramActivity, List<String> paramList, AdapterView.OnItemClickListener paramOnItemClickListener, View paramView) {
        Object localObject = null;
        if (paramActivity != null) {
            localObject = (ListView) LayoutInflater.from(this).inflate(R.layout.layout_popwindow_listview1, null);
            ((ListView) localObject).setAdapter(new ArrayAdapter<String>(paramActivity, R.layout.layout_popwindow_listview_item2, R.id.popwindow_tv, paramList));
            ((ListView) localObject).setOnItemClickListener(paramOnItemClickListener);
            localObject = new PopupWindow((View) localObject, getResources().getDimensionPixelSize(R.dimen.height_120), WindowManager.LayoutParams.WRAP_CONTENT);
            ((PopupWindow) localObject).setFocusable(true);
            ((PopupWindow) localObject).setTouchable(true);
            ((PopupWindow) localObject).setBackgroundDrawable(new BitmapDrawable());
            ((PopupWindow) localObject).setOutsideTouchable(true);
            ((PopupWindow) localObject).showAsDropDown(paramView, -20, 0);
        }
        return (PopupWindow) localObject;
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }
}

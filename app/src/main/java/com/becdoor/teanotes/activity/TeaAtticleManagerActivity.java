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
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.FirstListFragment;
import com.becdoor.teanotes.fragment.MainFeaturedFragment;
import com.becdoor.teanotes.fragment.TeaAtticleFragment;
import com.becdoor.teanotes.fragment.TeaAtticleOfflineFragment;
import com.becdoor.teanotes.model.AtticleCate;

import java.util.ArrayList;
import java.util.List;

/**
 * 茶志管理界面
 * 分 草稿，已发布，隐藏三个类别
 * 有编辑和浏览两种模式
 */
public class TeaAtticleManagerActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private TextView tvTab1;
    private TextView tvTab2;
    private TextView tvTab3;
    private TextView btnBack;
    private TextView tvCate;
    private ImageView btnManage;
    private View mCatergoryView;
    private int mode;

    private PopupWindow pop;

    private List<AtticleCate> cateList;
    private List<String> cateNameList=new ArrayList<>();
    private AtticleCate selectedCate;
    MyPagerAdapter adapter;
    private int curIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_atticle_manager);
        initView();
    }


    private void initView() {
        mCatergoryView = findViewById(R.id.main_catergoryLayout);
        mCatergoryView.setOnClickListener(this);
        tvCate = (TextView) findViewById(R.id.tv_cate);
        btnBack = (TextView) findViewById(R.id.title_leftTv);
        btnManage = (ImageView) findViewById(R.id.iv_manage);
        tvTab1 = (TextView) findViewById(R.id.tab1);
        tvTab2 = (TextView) findViewById(R.id.tab2);
        tvTab3 = (TextView) findViewById(R.id.tab3);
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
        tvTab3.setOnClickListener(this);
        btnManage.setOnClickListener(this);
        btnBack.setOnClickListener(this);
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
                curIndex=position;
                adapter.mFragment.get(curIndex).doRefresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //catelist是在fragment中的列表接口伴随返回的数据，所以fragment获取数据后重新更新。
    //为了防止频繁更新，fragment处应先判断cate数据有变化才调用这个方法。
    public void updateCate(List<AtticleCate> list){
        cateList=list;
        if(list==null){
            return;
        }
        cateNameList.clear();
        for(int i=0;i<list.size();i++){
            cateNameList.add(list.get(i).cat_name);
        }
    }

    private void setupViewPager(ViewPager viewpager) {
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TeaAtticleFragment.newInstance(0), "已发布");
        adapter.addFragment(TeaAtticleFragment.newInstance(1), "隐藏");
        adapter.addFragment(TeaAtticleOfflineFragment.newInstance(2), "草稿箱");
        viewpager.setAdapter(adapter);
    }


    public void switchMode(){
        if(mode==1){
            mode=0;
            btnManage.setImageResource(R.drawable.setting_icon_unchecked);
        }else if(mode==0){
            mode=1;
            btnManage.setImageResource(R.drawable.setting_icon_checked);
        }
        for(int i=0;i<3;i++){
            if(curIndex==i){
                adapter.mFragment.get(i).switchMode(true);
            }else {
                adapter.mFragment.get(i).switchMode(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvTab1) {
            viewpager.setCurrentItem(0);
            changeData(0);
        } else if (v == tvTab2) {
            changeData(1);
            viewpager.setCurrentItem(1);
        }else if(v==tvTab3){
            changeData(2);
            viewpager.setCurrentItem(2);
        } else if (v == mCatergoryView) {//分类
            showCateDialog();
        } else if (v == btnBack) {//返回
            finish();
        }else if(v==btnManage){
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
            tvTab3.setSelected(false);
        } else if(position==1){
            tvTab1.setSelected(false);
            tvTab2.setSelected(true);
            tvTab3.setSelected(false);
        }else if(position==2){
            tvTab1.setSelected(false);
            tvTab2.setSelected(false);
            tvTab3.setSelected(true);
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<TeaAtticleFragment> mFragment = new ArrayList<TeaAtticleFragment>();
        private final List<String> mFragmentTitle = new ArrayList<String>();

        public void addFragment(TeaAtticleFragment fragment, String title) {
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

    private void showCateDialog(){
        this.pop = initSimpalPopWindow(this,cateNameList , new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                        pop.dismiss();
                        selectedCate=cateList.get(paramAnonymousInt);
                        tvCate.setText(selectedCate.cat_name);
                        //设置分类
                        adapter.mFragment.get(0).setCate(selectedCate.cat_id);
                        adapter.mFragment.get(1).setCate(selectedCate.cat_id);
                        adapter.mFragment.get(2).setCate(selectedCate.cat_id);
                        //刷新当前页
                        adapter.mFragment.get(curIndex).doRefresh();
                    }
                }
                , mCatergoryView);
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
        return (PopupWindow)localObject;
    }

    public static void invoke(Context context){
        Intent intent=new Intent(context,TeaAtticleManagerActivity.class);
        context.startActivity(intent);
    }
}

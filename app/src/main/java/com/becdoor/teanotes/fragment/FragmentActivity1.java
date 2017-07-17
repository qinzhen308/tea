package com.becdoor.teanotes.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页-sub
 */
public class FragmentActivity1 extends Fragment implements View.OnClickListener {
    public static String POSITION_NAME = "subitem";
    private ViewPager viewpager;
    private TextView mHomeTv;
    private TextView mFeatureTv;
    private TextView mCatergoryTv;
    private View mSearchView;
    private MainFeaturedFragment mainFeaturedFragment;
    private int position = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, null);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION_NAME, 1);
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        mCatergoryTv = (TextView) view.findViewById(R.id.main_catergoryTv);
        mCatergoryTv.setOnClickListener(this);
        mSearchView = view.findViewById(R.id.main_searchLayout);
        mSearchView.setOnClickListener(this);
        mHomeTv = (TextView) view.findViewById(R.id.first_homeTv);
        mFeatureTv = (TextView) view.findViewById(R.id.first_freatureTv);
        mHomeTv.setOnClickListener(this);
        mFeatureTv.setOnClickListener(this);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewpager);
        viewpager.setCurrentItem(position);
        changeData(position);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void setupViewPager(ViewPager viewpager) {
        mainFeaturedFragment = MainFeaturedFragment.newInstance("");
        mainFeaturedFragment.setMainFeaturwListener(new MainFeaturedFragment.MainFeaturwListener() {
            @Override
            public void setRightView(String content) {
                mCatergoryTv.setText(content);
            }
        });
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(FirstListFragment.newInstance(), "主页");
        adapter.addFragment(mainFeaturedFragment, "笔记");
        viewpager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == mHomeTv) {
            viewpager.setCurrentItem(0);
            changeData(0);
        } else if (v == mFeatureTv) {
            changeData(1);
            viewpager.setCurrentItem(1);
        } else if (v == mCatergoryTv) {//分类
            mainFeaturedFragment.showRightCate(mCatergoryTv);
        } else if (v == mSearchView) {//搜索
            SearchActivity.invoke(getActivity());
        }
    }

    /**
     * 改变数据
     *
     * @param position
     */
    private void changeData(int position) {
        if (position == 0) {
            mCatergoryTv.setVisibility(View.INVISIBLE);
            mFeatureTv.setSelected(false);
            mHomeTv.setSelected(true);
        } else {
            mCatergoryTv.setVisibility(View.VISIBLE);
            mHomeTv.setSelected(false);
            mFeatureTv.setSelected(true);
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


}

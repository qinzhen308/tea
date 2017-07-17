package com.becdoor.teanotes.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Main_Title_Adapter extends FragmentPagerAdapter{
private final List<Fragment >mFragments=new ArrayList<>();
    private final  List<String> mFragmentTitles=new ArrayList<>();
    public Main_Title_Adapter(FragmentManager fm) {
        super(fm);
    }
public void  addFragment(Fragment fragment,String title){

    mFragments.add(fragment);
    mFragmentTitles.add(title);
}
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}

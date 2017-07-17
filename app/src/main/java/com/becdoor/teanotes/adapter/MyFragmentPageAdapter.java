package com.becdoor.teanotes.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.becdoor.teanotes.fragment.PageFragment;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    public  final  int COUNT=5;
    private  String []titles =new String[]{"tab1","tab2"};
    private Context context;
    public MyFragmentPageAdapter(FragmentManager fm ,Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}

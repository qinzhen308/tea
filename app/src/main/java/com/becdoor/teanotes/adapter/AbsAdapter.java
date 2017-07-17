package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by paulz on 2016/10/31.
 */

public abstract class AbsAdapter<T> extends BaseAdapter{
    protected List<T> mList;
    protected Activity mContext;

    public AbsAdapter(Activity activity){
        mContext=activity;
    }

    public void setList(List<T> list){
        mList=list;
    }

    public void setList(T[] list){
        if(list!=null){
            mList=Arrays.asList(list);
        }
    }

    public List<T> getList(){
        return mList;
    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void insert(T object, int index) {
        if(mList==null){
            mList=new ArrayList<>();
        }
        mList.add(index, object);
        notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        mList.remove(object);
        notifyDataSetChanged();
    }

}

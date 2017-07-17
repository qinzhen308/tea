package com.becdoor.teanotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.model.CateInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jjj on 2016/11/7.
 * 分类 首页
 */

public class CateAdapter extends BaseAdapter {
    private Context mContext;
    private List<CateInfo> mList;

    public CateAdapter(Context context) {
        this.mContext = context;
    }

    public void setmList(List<CateInfo> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CateInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindow_listview_item1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) != null) {
            holder.tv.setText(getItem(position).getCat_name());
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv;

        ViewHolder(View view) {
            tv = (TextView) view.findViewById(R.id.popwindow_tv);
        }
    }
}

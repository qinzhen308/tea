package com.becdoor.teanotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.model.CateInfo;
import com.becdoor.teanotes.model.RankInfo;

import java.util.List;

/**
 * Created by jjj on 2016/11/7.
 * 积分等级 首页
 */

public class RankAdapter extends BaseAdapter {
    private Context mContext;
    private List<RankInfo.RankListBean> mList;

    public RankAdapter(Context context, List<RankInfo.RankListBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RankInfo.RankListBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rank, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RankInfo.RankListBean rankBean = getItem(position);
        if (rankBean != null) {
            holder.nameTv.setText(rankBean.getRank_name());
            holder.rankTv.setText(rankBean.getRank_point());
        }
        if (position % 2 == 0) {
            holder.nameTv.setBackgroundResource(R.color.item_rank_one);
            holder.rankTv.setBackgroundResource(R.color.bg);
        } else {
            holder.nameTv.setBackgroundResource(R.color.item_rank_two);
            holder.rankTv.setBackgroundResource(R.color.white);
        }
        return convertView;
    }

    class ViewHolder {
        TextView nameTv;
        TextView rankTv;

        ViewHolder(View view) {
            nameTv = (TextView) view.findViewById(R.id.item_rank_nameTv);
            rankTv = (TextView) view.findViewById(R.id.item_rank_rankTv);
        }
    }
}

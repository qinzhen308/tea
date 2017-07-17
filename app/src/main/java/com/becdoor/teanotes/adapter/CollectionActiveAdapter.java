package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.ActivitiesDetailActivity;
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.ActivityInfo;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏--活动
 */
public class CollectionActiveAdapter extends RecyclerView.Adapter<CollectionActiveAdapter.ItemViewHolder> {
    private Context mContext;
    private List<ActivityInfo> mData;
    int screenW = 0;
    private OnItemOperateListener mOnItemOperateListener;

    private boolean fromSearch;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<ActivityInfo> data) {
        this.mData = data;
    }


    public CollectionActiveAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    public void setFrom(boolean fromSearch){
        this.fromSearch=fromSearch;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection_tea_active, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ActivityInfo info = mData.get(position);
        holder.timeEndTv.setText("结束时间    "+info.getAct_end_date());
        holder.timeStartTv.setText("开始时间    "+info.getAct_start_date());
        Glide.with(mContext).load(Constant.REALM_NAME+info.getAct_pic()).into(holder.ivPic);
        holder.titleTv.setText(info.getAct_title());
        if(fromSearch){
            holder.ivFunction.setVisibility(View.GONE);
        }else {
            holder.ivFunction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemOperateListener!=null){
                        mOnItemOperateListener.onDelete(info);
                    }
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitiesDetailActivity.inVoke(mContext,info.getAct_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView timeStartTv;
        TextView timeEndTv;
        ImageView ivPic;
        TextView ivFunction;

        public ItemViewHolder(View view) {
            super(view);
            titleTv = (TextView) view.findViewById(R.id.tv_title);
            timeStartTv = (TextView) view.findViewById(R.id.tv_time_start);
            timeEndTv = (TextView) view.findViewById(R.id.tv_time_end);
            ivPic = (ImageView) view.findViewById(R.id.iv_pic);
            ivFunction = (TextView) view.findViewById(R.id.iv_function);
        }
    }

    public void setOnItemOperateListener(OnItemOperateListener onItemOperateListener){
        mOnItemOperateListener=onItemOperateListener;
    }

    public static interface OnItemOperateListener{
        public void onDelete(ActivityInfo act);
    }
}

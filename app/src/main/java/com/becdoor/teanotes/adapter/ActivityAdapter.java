package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.List;

/**
 * 活动
 */
public class ActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ActivityInfo> mData;
    int screenW = 0;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<ActivityInfo> data) {
        this.mData = data;
    }


    public ActivityAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ActivityInfo dataBean = mData.get(position);
        if (dataBean != null) {
            ((ItemViewHolder) holder).titleTv.setText(dataBean.getAct_title());
            Glide.with(mContext).load(Constant.REALM_NAME + dataBean.getAct_pic()).into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.520)));
            ((ItemViewHolder) holder).timeTv.setText(dataBean.getAct_start_date() + "\t至\t" + dataBean.getAct_end_date());
            if (TextUtils.isEmpty(dataBean.getAct_address())) {
                ((ItemViewHolder) holder).addressShowTv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).addressTv.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).addressShowTv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).addressTv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).addressTv.setText(dataBean.getAct_address());
            }

            if ("0".equals(dataBean.getAct_status())) {//活动结束
                ((ItemViewHolder) holder).enrollTv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).typeIv.setImageResource(R.drawable.activities_activities_type_00);
            } else {
                ((ItemViewHolder) holder).enrollTv.setVisibility(View.VISIBLE);
                if ("1".equals(dataBean.getAct_type())) {//线上
                    ((ItemViewHolder) holder).typeIv.setImageResource(R.drawable.activities_activities_type_01);

                } else if ("2".equals(dataBean.getAct_type())) {//线下
                    ((ItemViewHolder) holder).typeIv.setImageResource(R.drawable.activities_activities_type_02);
                }
            }
            ((ItemViewHolder) holder).allLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitiesDetailActivity.inVoke(mContext, dataBean.getAct_id());
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        ImageView typeIv;
        TextView enrollTv;
        TextView timeTv;
        TextView addressTv;
        TextView addressShowTv;
        ImageView imageView;
        RelativeLayout layout;
        View allLayout;

        public ItemViewHolder(View view) {
            super(view);
            titleTv = (TextView) view.findViewById(R.id.item_activity_titleTv);
            typeIv = (ImageView) view.findViewById(R.id.item_activity_typeIv);
            enrollTv = (TextView) view.findViewById(R.id.item_activity_enrollTv);
            addressTv = (TextView) view.findViewById(R.id.item_activity_addressTv);
            timeTv = (TextView) view.findViewById(R.id.item_activity_timeTv);
            addressShowTv = (TextView) view.findViewById(R.id.item_activity_addressShowTv);
            layout = (RelativeLayout) view.findViewById(R.id.item_activity_layout);
            imageView = (ImageView) view.findViewById(R.id.item_activity_iv);
            allLayout = view.findViewById(R.id.item_activity_allLayout);
        }
    }
}


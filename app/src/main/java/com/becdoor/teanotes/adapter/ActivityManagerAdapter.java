package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.ActivitiesDetailActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.ActivityInfo;
import com.becdoor.teanotes.until.AppUtil;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 活动
 */
public class ActivityManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ActivityInfo> mData;
    int screenW = 0;
    int m;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<ActivityInfo> data) {
        this.mData = data;
    }


    public ActivityManagerAdapter(Context context) {
        this.mContext = context;
        m = context.getResources().getDimensionPixelSize(R.dimen.activity_15dp);
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activitymanager, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ActivityInfo dataBean = mData.get(position);
        if (dataBean != null) {
            ((ItemViewHolder) holder).titleTv.setText(dataBean.getAct_title());
            Glide.with(mContext).load(Constant.REALM_NAME + dataBean.getAct_pic()).into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).startTimeTv.setText("开始时间\t\t" + dataBean.getAct_start_date());
            ((ItemViewHolder) holder).endTimeTv.setText("结束时间\t\t" + dataBean.getAct_end_date());

            if ("0".equals(dataBean.getAct_status())) {//活动结束
                ((ItemViewHolder) holder).stateTv.setSelected(true);
                ((ItemViewHolder) holder).stateTv.setText("活动结束");
            } else {
                ((ItemViewHolder) holder).stateTv.setSelected(false);
                ((ItemViewHolder) holder).stateTv.setText("活动进行中");
            }
            ((ItemViewHolder) holder).allLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitiesDetailActivity.inVoke(mContext, dataBean.getAct_id());
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.318));
            params.setMargins(m, m, m, 0);
            ((ItemViewHolder) holder).layout.setLayoutParams(params);
            RelativeLayout.LayoutParams ivPa = new RelativeLayout.LayoutParams((int) (screenW * 0.318), RelativeLayout.LayoutParams.MATCH_PARENT);
            ((ItemViewHolder) holder).imageView.setLayoutParams(ivPa);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_activityManager_titleTv)
        TextView titleTv;
        @Bind(R.id.item_activityManager_startTimeTv)
        TextView startTimeTv;
        @Bind(R.id.item_activityManager_endTimeTv)
        TextView endTimeTv;
        @Bind(R.id.item_activityManager_stateTv)
        TextView stateTv;
        @Bind(R.id.item_activityManager_iv)
        ImageView imageView;
        @Bind(R.id.item_activityManager_allLayout)
        View allLayout;
        @Bind(R.id.item_activityManager_layout)
        View layout;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


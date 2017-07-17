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
import com.becdoor.teanotes.activity.SeminarDetailActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.ActivityInfo;
import com.becdoor.teanotes.model.SeminarInfo;
import com.becdoor.teanotes.until.AppUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 专题
 */
public class SeminarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<SeminarInfo> mData;
    int screenW = 0;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<SeminarInfo> data) {
        this.mData = data;
    }


    public SeminarAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seminar, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SeminarInfo dataBean = mData.get(position);
        if (dataBean != null) {
            ((ItemViewHolder) holder).titleTv.setText(dataBean.getTitle());
            ((ItemViewHolder) holder).contentTv.setText(dataBean.getSeries());
            Glide.with(mContext).load(Constant.REALM_NAME + dataBean.getImg()).into(((ItemViewHolder) holder).imageView);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.564));
            params.bottomMargin=mContext.getResources().getDimensionPixelOffset(R.dimen.all_margrin);
            ((ItemViewHolder) holder).layout.setLayoutParams(params);
            ((ItemViewHolder) holder).allLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeminarDetailActivity.inVoke(mContext, dataBean.getId());
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
        TextView contentTv;
        ImageView imageView;
        RelativeLayout layout;
        View allLayout;

        public ItemViewHolder(View view) {
            super(view);
            titleTv = (TextView) view.findViewById(R.id.item_seminar_titleTv);
            contentTv = (TextView) view.findViewById(R.id.item_seminar_contentTv);
            layout = (RelativeLayout) view.findViewById(R.id.item_seminar_layout);
            imageView = (ImageView) view.findViewById(R.id.item_seminar_iv);
            allLayout = view.findViewById(R.id.item_seminar_allLayout);
        }
    }
}


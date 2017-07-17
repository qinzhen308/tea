package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.model.ArticleBean;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.until.AppUtil;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 精选
 */
public class CommonDetailMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ArticleBean> mData;
    int screenW = 0;
    private String aid;
    private int catId;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<ArticleBean> data, String aid,int cat_id) {
        this.mData = data;
        this.aid = aid;
        this.catId=cat_id;
    }

    public CommonDetailMoreAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
        screenW = (int) ((float) (screenW - 3 * context.getResources().getDimensionPixelSize(R.dimen.all_margrin)) / 2);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commondetail_moredata, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mData != null) {
            final ArticleBean dataBean = mData.get(position);
            if (dataBean != null) {
                ((ItemViewHolder) holder).titleTv.setText(dataBean.getTitle());
                Glide.with(mContext).load(Constant.REALM_NAME + dataBean.getImg()).into(((ItemViewHolder) holder).imageView);
            }
            ((ItemViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonDetailActivity.inVoke(mContext, String.valueOf(catId), dataBean.getArticle_id());
                }
            });
        }
        ((ItemViewHolder) holder).layout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, (int) (screenW * 0.603)));

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        ImageView imageView;
        RelativeLayout layout;

        public ItemViewHolder(View view) {
            super(view);
            titleTv = (TextView) view.findViewById(R.id.item_commonDetail_moreDataTv);
            imageView = (ImageView) view.findViewById(R.id.item_commonDetail_moreDataIv);
            layout = (RelativeLayout) view.findViewById(R.id.item_commonDetail_layout);
        }
    }
}


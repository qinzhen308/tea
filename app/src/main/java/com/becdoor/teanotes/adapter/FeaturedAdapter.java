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
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 精选
 */
public class FeaturedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NoteReInfoBean> mData;
    int screenW = 0;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<NoteReInfoBean> data) {
        this.mData = data;
    }


    public FeaturedAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feauture, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NoteReInfoBean dataBean = mData.get(position);
        ((ItemViewHolder) holder).commentTv.setText(dataBean.getC_count());
        ((ItemViewHolder) holder).collectTv.setText(dataBean.getS_count());
        ((ItemViewHolder) holder).prasieTv.setText(dataBean.getP_count());
        if (dataBean.getArticle() != null) {
            ((ItemViewHolder) holder).timeTv.setText(dataBean.getArticle().getAdd_time());
            ((ItemViewHolder) holder).titleTv.setText(dataBean.getArticle().getTitle());
            ((ItemViewHolder) holder).discountTv.setText(dataBean.getArticle().getCat_name());
            Glide.with(mContext).load(Constant.REALM_NAME + dataBean.getArticle().getImg()).into(((ItemViewHolder) holder).imageView);
            if (dataBean.getArticle().getIs_recommend() == 1) {
                ((ItemViewHolder) holder).iconIv.setImageResource(R.drawable.icon_best);
                ((ItemViewHolder) holder).iconIv.setVisibility(View.VISIBLE);
            } else if (dataBean.getArticle().getIs_new() == 1) {
                ((ItemViewHolder) holder).iconIv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).iconIv.setImageResource(R.drawable.icon_new);
            } else {
                ((ItemViewHolder) holder).iconIv.setVisibility(View.GONE);
            }
        }
        if (dataBean.getUser_info() != null) {
            ((ItemViewHolder) holder).gradeTv.setText(dataBean.getUser_info().getRank_name());
            ((ItemViewHolder) holder).userNameTv.setText(dataBean.getUser_info().getUsername());
            Glide.with(mContext).load(Constant.REALM_NAME + dataBean.getUser_info().getPic()).transform(new CircleTransform(mContext)).into(((ItemViewHolder) holder).avatarIv);
        }
        ((ItemViewHolder) holder).layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.520)));
        ((ItemViewHolder) holder).allLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBean.getArticle() != null) {
                    CommonDetailActivity.inVoke(mContext, dataBean.getArticle().getCat_id(), dataBean.getArticle().getArticle_id());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView discountTv;
        TextView userNameTv;
        TextView timeTv;
        TextView prasieTv;
        TextView collectTv;
        TextView commentTv;
        TextView gradeTv;
        ImageView imageView;
        ImageView avatarIv;
        RelativeLayout layout;
        View allLayout;
        ImageView iconIv;

        public ItemViewHolder(View view) {
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.item_feauture_ivLayout);
            titleTv = (TextView) view.findViewById(R.id.item_feauture_titleTv);
            discountTv = (TextView) view.findViewById(R.id.item_feauture_discountTv);
            userNameTv = (TextView) view.findViewById(R.id.item_feauture_userNameTv);
            timeTv = (TextView) view.findViewById(R.id.item_feauture_timeTv);
            prasieTv = (TextView) view.findViewById(R.id.item_feauture_prasieTv);
            collectTv = (TextView) view.findViewById(R.id.item_feauture_collectTv);
            commentTv = (TextView) view.findViewById(R.id.item_feauture_commentTv);
            gradeTv = (TextView) view.findViewById(R.id.item_feauture_gradeTv);
            imageView = (ImageView) view.findViewById(R.id.item_feauture_iv);
            avatarIv = (ImageView) view.findViewById(R.id.item_feauture_avatarIv);
            iconIv = (ImageView) view.findViewById(R.id.item_feauture_iconIv);
            allLayout = view.findViewById(R.id.item_feauture_allLayout);
        }
    }
}


package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.model.SpaceInfo;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.becdoor.teanotes.view.PersonalSpaceHeaderView;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 某某空间
 */
public class PersonalSpaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_ONE = 0;
    private final int TYPE_TWO = 1;
    private Context mContext;
    private List<NoteReInfoBean> mData;
    int screenW = 0;
    private SpaceInfo spaceInfo;
    private String uid;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<NoteReInfoBean> data, SpaceInfo spaceInfo, String uid) {
        this.mData = data;
        this.spaceInfo = spaceInfo;
        this.uid = uid;
    }


    public PersonalSpaceAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ONE;
        }
        return TYPE_TWO;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            return new HeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.item_personalspace_one, null));
        }
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personalspace, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final NoteReInfoBean dataBean = mData.get(position - 1);
            ((ItemViewHolder) holder).commentTv.setText(dataBean.getC_count());
            ((ItemViewHolder) holder).collectTv.setText(dataBean.getS_count());
            ((ItemViewHolder) holder).prasieTv.setText(dataBean.getP_count());
            if (dataBean.getArticle() != null) {
                ((ItemViewHolder) holder).timeTv.setText(dataBean.getArticle().getAdd_time());
                ((ItemViewHolder) holder).titleTv.setText(dataBean.getArticle().getTitle());
                Glide.with(mContext).load(Constant.REALM_NAME + dataBean.getArticle().getImg()).into(((ItemViewHolder) holder).imageView);
                if (dataBean.getArticle().getIs_recommend() == 1) {
                    (( ItemViewHolder) holder).iconIv.setImageResource(R.drawable.icon_best);
                    (( ItemViewHolder) holder).iconIv.setVisibility(View.VISIBLE);
                } else if (dataBean.getArticle().getIs_new() == 1) {
                    (( ItemViewHolder) holder).iconIv.setVisibility(View.VISIBLE);
                    (( ItemViewHolder) holder).iconIv.setImageResource(R.drawable.icon_new);
                } else {
                    (( ItemViewHolder) holder).iconIv.setVisibility(View.GONE);
                }
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
        } else if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).headerView.setSpaceInfo(spaceInfo, screenW,uid);
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 1 : mData.size() + 1;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView timeTv;
        TextView prasieTv;
        TextView collectTv;
        TextView commentTv;
        ImageView imageView;
        ImageView iconIv;
        RelativeLayout layout;
        View allLayout;

        public ItemViewHolder(View view) {
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.item_personalspace_ivLayout);
            titleTv = (TextView) view.findViewById(R.id.item_personalspace_titleTv);
            timeTv = (TextView) view.findViewById(R.id.item_personalspace_timeTv);
            prasieTv = (TextView) view.findViewById(R.id.item_personalspace_prasieTv);
            collectTv = (TextView) view.findViewById(R.id.item_personalspace_collectTv);
            commentTv = (TextView) view.findViewById(R.id.item_personalspace_commentTv);
            imageView = (ImageView) view.findViewById(R.id.item_personalspace_iv);
            iconIv = (ImageView) view.findViewById(R.id.item_personalspace_iconIv);
            allLayout = view.findViewById(R.id.item_personalspace_allLayout);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        PersonalSpaceHeaderView headerView;

        public HeaderHolder(View itemView) {
            super(itemView);
            headerView = (PersonalSpaceHeaderView) itemView.findViewById(R.id.item_personalSpace_headerView);
            itemView.findViewById(R.id.item_personalspace_allLayout).setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        }
    }
}


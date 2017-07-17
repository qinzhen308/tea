package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.activity.CreateAtticleActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.NoteDetailBean;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 精选
 */
public class TeaAtticleOfflineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NoteDetailBean> mData;
    int screenW = 0;
    private int mode;
    private int tab;
    private OnItemOperateListener mOnItemOperateListener;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<NoteDetailBean> data) {
        this.mData = data;
    }


    public TeaAtticleOfflineAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tea_atticle, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NoteDetailBean dataBean = mData.get(position);
        ((ItemViewHolder) holder).commentTv.setText(TextUtils.isEmpty(dataBean.getC_count())?"0":dataBean.getC_count());
        ((ItemViewHolder) holder).collectTv.setText(TextUtils.isEmpty(dataBean.getS_count())?"0":dataBean.getS_count());
        ((ItemViewHolder) holder).prasieTv.setText(""+dataBean.getP_count());
        ((ItemViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataBean.isChecked = isChecked;
            }
        });
        ((ItemViewHolder) holder).checkBox.setChecked(dataBean.isChecked);
        if (mode == 1) {
            ((ItemViewHolder) holder).ivFunction.setVisibility(View.GONE);
            ((ItemViewHolder) holder).checkBox.setVisibility(View.VISIBLE);

        } else {
            ((ItemViewHolder) holder).checkBox.setVisibility(View.GONE);
            ((ItemViewHolder) holder).ivFunction.setVisibility(View.VISIBLE);
            if (tab == 1) {
                ((ItemViewHolder) holder).ivFunction.setImageResource(R.drawable.icon_xianshi3_03);
                ((ItemViewHolder) holder).ivFunction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemOperateListener != null) {
                            mOnItemOperateListener.onShow(dataBean);
                        }
                    }
                });
            } else if (tab == 2) {
                ((ItemViewHolder) holder).ivFunction.setImageResource(R.drawable.icon_fabu2_03);
                ((ItemViewHolder) holder).ivFunction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemOperateListener != null) {
                            mOnItemOperateListener.onPublish(dataBean);
                        }
                    }
                });
            } else {
                ((ItemViewHolder) holder).ivFunction.setVisibility(View.GONE);
            }
        }

        ((ItemViewHolder) holder).timeTv.setText(dataBean.getAdd_time());
        ((ItemViewHolder) holder).titleTv.setText(dataBean.getTitle());
        ((ItemViewHolder) holder).discountTv.setText(dataBean.getCat_name());
        if(dataBean.isNative){
//            Glide.with(mContext).load(new File(dataBean.getImg())).error(R.drawable.no_image).into(((ItemViewHolder) holder).imageView);
            ((ItemViewHolder) holder).imageView.setImageURI(Uri.fromFile(new File(dataBean.getImg())));
        }else {
            Glide.with(mContext).load(Constant.REALM_NAME + dataBean.getImg()).error(R.drawable.no_image).into(((ItemViewHolder) holder).imageView);
        }
        ((ItemViewHolder) holder).layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.520)));
        ((ItemViewHolder) holder).allLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonDetailActivity.inVoke(mContext, dataBean.getArticle().getCat_id(), dataBean.getArticle().getArticle_id());
                CreateAtticleActivity.invoke((Activity) mContext,dataBean,true);
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
        ImageView ivFunction;
        CheckBox checkBox;
        RelativeLayout layout;
        View allLayout;

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
            ivFunction = (ImageView) view.findViewById(R.id.iv_function);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            allLayout = view.findViewById(R.id.item_feauture_allLayout);

            avatarIv.setVisibility(View.GONE);
            gradeTv.setVisibility(View.GONE);
            userNameTv.setVisibility(View.GONE);
        }
    }

    public List<NoteDetailBean> getSelectedAtticles() {
        ArrayList selecteds = new ArrayList();
        if (mData == null) return selecteds;
        for (int i = 0, count = getItemCount(); i < count; i++) {
            NoteDetailBean bean = mData.get(i);
            if (bean.isChecked) {
                selecteds.add(bean);
            }
        }
        return selecteds;
    }

    public void setOnItemOperateListener(OnItemOperateListener onItemOperateListener) {
        mOnItemOperateListener = onItemOperateListener;
    }

    public static interface OnItemOperateListener {
        public void onPublish(NoteDetailBean note);

        public void onShow(NoteDetailBean note);
    }
}

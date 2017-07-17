package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.CollectTeaDetailActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.CollectedTeaWrapper;
import com.becdoor.teanotes.model.Comment;
import com.becdoor.teanotes.model.SimpleNoteWrapper;
import com.becdoor.teanotes.until.AppUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 便签
 */
public class TeaCollectAdapter extends RecyclerView.Adapter<TeaCollectAdapter.ItemViewHolder> {
    private Context mContext;
    private List<CollectedTeaWrapper.CollectedTea> mData;
    int screenW = 0;
    private int mode;
    int picSize;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<CollectedTeaWrapper.CollectedTea> data) {
        this.mData = data;
    }


    public TeaCollectAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
        picSize=mContext.getResources().getDimensionPixelSize(R.dimen.height_120);
    }



    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public TeaCollectAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect_tea, parent, false));
    }

    @Override
    public void onBindViewHolder(TeaCollectAdapter.ItemViewHolder holder, int position) {
        final CollectedTeaWrapper.CollectedTea note = mData.get(position);
        holder.brandTv.setText("品牌："+note.brand);
        holder.titleTv.setText(note.title);
        holder.moneyTv.setText("金额："+note.money);
        holder.countTv.setText("数量："+note.num);
        Glide.with(mContext).load(Constant.REALM_NAME+note.img).override(picSize,picSize).into(holder.ivPic);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                note.isChecked = isChecked;
            }
        });
        holder.checkBox.setChecked(note.isChecked);
        if (mode == 1) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectTeaDetailActivity.invoke((Activity) mContext,note.cat_id,note.article_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView titleTv;
        TextView brandTv;
        TextView moneyTv;
        TextView countTv;
        CheckBox checkBox;

        public ItemViewHolder(View view) {
            super(view);
            ivPic = (ImageView) view.findViewById(R.id.collect_tea_img_iv);
            titleTv = (TextView) view.findViewById(R.id.collect_tea_title_tv);
            brandTv = (TextView) view.findViewById(R.id.collect_tea_brand_tv);
            moneyTv = (TextView) view.findViewById(R.id.collect_tea_money_tv);
            countTv = (TextView) view.findViewById(R.id.collect_tea_num_tv);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }

    public List<CollectedTeaWrapper.CollectedTea> getSelectedAtticles() {
        ArrayList selecteds = new ArrayList();
        if (mData == null) return selecteds;
        for (int i = 0, count = getItemCount(); i < count; i++) {
            CollectedTeaWrapper.CollectedTea bean = mData.get(i);
            if (bean.isChecked) {
                selecteds.add(bean);
            }
        }
        return selecteds;
    }
}

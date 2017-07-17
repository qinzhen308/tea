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
import com.becdoor.teanotes.activity.PersonalSpaceActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.Friend;
import com.becdoor.teanotes.model.SearchUser;
import com.becdoor.teanotes.until.AppUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的好友
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ItemViewHolder> {
    private Context mContext;
    private List<SearchUser> mData;
    int screenW = 0;
    private int mode;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<SearchUser> data) {
        this.mData = data;
    }


    public SearchUserAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }



    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public SearchUserAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_friends, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.ItemViewHolder holder, int position) {
        final SearchUser friend = mData.get(position);
        Glide.with(mContext).load(Constant.REALM_NAME+friend.pic).into(holder.ivAvatar);
        holder.rankNameTv.setText(friend.rank_name);
        holder.nameTv.setText(friend.username);
        holder.tomsignedTv.setText(friend.signed);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalSpaceActivity.inVoke(mContext,friend.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView rankNameTv;
        TextView tomsignedTv;
        ImageView ivAvatar;
        CheckBox checkBox;

        public ItemViewHolder(View view) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.head_iv);
            nameTv = (TextView) view.findViewById(R.id.name_tv);
            rankNameTv = (TextView) view.findViewById(R.id.f_rankname_tv);
            tomsignedTv = (TextView) view.findViewById(R.id.f_tomsigned_tv);
            checkBox = (CheckBox) view.findViewById(R.id.fri_checkbox);
            checkBox.setVisibility(View.GONE);
        }
    }

}

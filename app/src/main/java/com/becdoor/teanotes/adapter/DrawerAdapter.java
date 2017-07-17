package com.becdoor.teanotes.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.becdoor.teanotes.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/11.
 */

public class DrawerAdapter extends BaseAdapter {

    private String[] members;
    private int[] memberIvs;
    private LayoutInflater mInflater;
    private Context context;
    private int mScreenW = 0;

    public DrawerAdapter(Context context, int screenW) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        members = context.getResources().getStringArray(R.array.list_member);
        memberIvs = new int[]{
                R.drawable.member_home,
                R.drawable.member_account,
                R.drawable.member_notes,
                R.drawable.member_tibat_tea,
                R.drawable.member_notes2,
                R.drawable.member_activity,
                R.drawable.member_friends,
                R.drawable.member_collcation,
                R.drawable.member_comment
        };
        this.mScreenW = screenW;
        mScreenW = (int) ((float) (mScreenW - 4 * context.getResources().getDimensionPixelSize(R.dimen.all_margrin)) / 3);
    }

    @Override
    public int getCount() {
        return members == null ? 0 : members.length;
    }

    @Override
    public String getItem(int position) {
        return members[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItemViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_layout_member, null);
            holder = new DrawerItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DrawerItemViewHolder) convertView.getTag();
        }

        holder.nameTV.setText(getItem(position));
        holder.mImageView.setImageResource(memberIvs[position]);
        convertView.setLayoutParams(new AbsListView.LayoutParams(mScreenW, mScreenW));
        return convertView;
    }

    class DrawerItemViewHolder {
        @Bind(R.id.member_image)
        ImageView mImageView;
        @Bind(R.id.member_iamge_text)
        TextView nameTV;

        DrawerItemViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}

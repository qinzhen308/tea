package com.becdoor.teanotes.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.PersonalSpaceActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.becdoor.teanotes.model.PriMsgInfo;
import com.becdoor.teanotes.view.CircleTransform;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 * 我的私信
 */

public class MyPriMsgAdapter extends BaseAdapter {
    private Context mContext;
    private final static int TYPE_ONE = 0;
    private final static int TYPE_TWO = 1;
    private List<PriMsgInfo> mList;


    public MyPriMsgAdapter(Context context) {
        this.mContext = context;
    }

    public void setmList(List<PriMsgInfo> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        if ("left".equals(getItem(position).getType())) {
            return TYPE_ONE;
        } else {
            return TYPE_TWO;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PriMsgInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        final PriMsgInfo info = getItem(position);
        if (info != null) {
            switch (type) {
                case TYPE_ONE:
                    FromViewHolder fromViewHolder;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment_from, null);
                        fromViewHolder = new FromViewHolder(convertView);
                        convertView.setTag(fromViewHolder);
                    } else {
                        fromViewHolder = (FromViewHolder) convertView.getTag();
                    }

                    Glide.with(mContext).load(Constant.REALM_NAME + info.getAvatar()).placeholder(R.drawable.icon_login_user).transform(new CircleTransform(mContext)).into((fromViewHolder).imageView);
                    sett(info.getSender_name(), fromViewHolder.nameTv);
                    sett(info.getMessage(), fromViewHolder.tv);
                    (fromViewHolder).imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PersonalSpaceActivity.inVoke(mContext, info.getSender_id());
                        }
                    });
                    break;
                case TYPE_TWO:
                    ToViewHolder toViewHolder;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment_to, null);
                        toViewHolder = new ToViewHolder(convertView);
                        convertView.setTag(toViewHolder);
                    } else {
                        toViewHolder = (ToViewHolder) convertView.getTag();
                    }
                    sett(info.getSender_name(), toViewHolder.nameTv);
                    sett(info.getMessage(), toViewHolder.tv);
                    Glide.with(mContext).load(Constant.REALM_NAME + info.getMessage()).placeholder(R.drawable.icon_login_user).transform(new CircleTransform(mContext)).into((toViewHolder).imageView);
                    (toViewHolder).imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PersonalSpaceActivity.inVoke(mContext, info.getSender_id());
                        }
                    });
                    break;
            }
        }
        return convertView;
    }

    private void sett(String string, TextView textView) {
        if (TextUtils.isEmpty(string)) {
            string = "";
        }
        textView.setText(string);
    }

    class FromViewHolder {
        ImageView imageView;
        TextView nameTv;
        TextView tv;

        FromViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.item_comment_from_avatarIv);
            nameTv = (TextView) view.findViewById(R.id.item_comment_from_nameTv);
            tv = (TextView) view.findViewById(R.id.item_comment_from_tv);
        }
    }

    class ToViewHolder {
        TextView tv;
        TextView nameTv;
        ImageView imageView;

        ToViewHolder(View view) {
            tv = (TextView) view.findViewById(R.id.item_comment_to_tv);
            nameTv = (TextView) view.findViewById(R.id.item_comment_to_nameTv);
            imageView = (ImageView) view.findViewById(R.id.item_comment_to_avatarIv);
        }
    }
}

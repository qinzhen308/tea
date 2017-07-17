package com.becdoor.teanotes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.becdoor.teanotes.listener.NoteCommentListener;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.becdoor.teanotes.view.CircleTransform;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 * 笔记评价
 */

public class NoteCommentForRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private final static int TYPE_ONE = 0;
    private final static int TYPE_TWO = 1;
    private List<NoteCommentBean> mList;
    private NoteCommentListener mNoteCommentListener;

    public NoteCommentForRecyclerViewAdapter(Context context, NoteCommentListener noteCommentListener) {
        this.mNoteCommentListener = noteCommentListener;
        this.mContext = context;
    }

    public void setmList(List<NoteCommentBean> mList) {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            return new FromViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_comment_from, null));
        }
        return new ToViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_comment_to, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NoteCommentBean noteCommentBean = mList.get(position);
        if (noteCommentBean != null) {
            if (holder instanceof FromViewHolder) {
                Glide.with(mContext).load(Constant.REALM_NAME + noteCommentBean.getUser_avatar()).placeholder(R.drawable.icon_login_user).transform(new CircleTransform(mContext)).into(((FromViewHolder) holder).imageView);
                sett(noteCommentBean.getUsername(), ((FromViewHolder) holder).nameTv);
                sett(noteCommentBean.getContent(), ((FromViewHolder) holder).tv);
                ((FromViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersonalSpaceActivity.inVoke(mContext, noteCommentBean.getUid());
                    }
                });
                ((FromViewHolder) holder).tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNoteCommentListener != null) {
                            mNoteCommentListener.commSingleNote(noteCommentBean);
                        }
                    }
                });

            } else if (holder instanceof ToViewHolder) {
                sett(noteCommentBean.getUsername(), ((ToViewHolder) holder).nameTv);
                sett(noteCommentBean.getContent(), ((ToViewHolder) holder).tv);
                Glide.with(mContext).load(Constant.REALM_NAME + noteCommentBean.getUser_avatar()).placeholder(R.drawable.icon_login_user).transform(new CircleTransform(mContext)).into(((ToViewHolder) holder).imageView);
                ((ToViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersonalSpaceActivity.inVoke(mContext, noteCommentBean.getUid());
                    }
                });
                ((ToViewHolder) holder).tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNoteCommentListener != null) {
                            mNoteCommentListener.commSingleNote(noteCommentBean);
                        }
                    }
                });
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return TYPE_ONE;
        }
        return TYPE_TWO;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private void sett(String string, TextView textView) {
        if (TextUtils.isEmpty(string)) {
            string = "";
        }
        textView.setText(string);
    }

    class FromViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTv;
        TextView tv;

        FromViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.item_comment_from_avatarIv);
            nameTv = (TextView) view.findViewById(R.id.item_comment_from_nameTv);
            tv = (TextView) view.findViewById(R.id.item_comment_from_tv);
        }
    }

    class ToViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView nameTv;
        ImageView imageView;

        ToViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.item_comment_to_tv);
            nameTv = (TextView) view.findViewById(R.id.item_comment_to_nameTv);
            imageView = (ImageView) view.findViewById(R.id.item_comment_to_avatarIv);
        }
    }


}

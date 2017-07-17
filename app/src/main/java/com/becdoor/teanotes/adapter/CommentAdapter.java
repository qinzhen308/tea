package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.ActivitiesDetailActivity;
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.activity.PersonalSpaceActivity;
import com.becdoor.teanotes.fragment.CommentFragment;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.Comment;
import com.becdoor.teanotes.model.Friend;
import com.becdoor.teanotes.model.NoteComment;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DateUtil;
import com.becdoor.teanotes.until.Remember;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的评论
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {
    private Context mContext;
    private List<Comment> mData;
    int screenW = 0;
    private int tab;

    OnDeleteListener mOnDeleteListener;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<Comment> data) {
        this.mData = data;
    }


    public CommentAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    public void setTab(int tab){
        this.tab=tab;
    }


    @Override
    public CommentAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ItemViewHolder holder, int position) {
        final Comment comment = mData.get(position);
        if(tab== CommentFragment.TAB_TO_ME){//别人评论我的情况
            if(comment instanceof NoteComment){
                holder.ptime_tv.setText(DateUtil.getTime("yyyy-MM-dd HH:mm",new Date(1000L*Long.valueOf(comment.getDate())))+" 由@"+((NoteComment)comment).comm_info.username+" 评论");
            }else {
                holder.ptime_tv.setText(DateUtil.getTime("yyyy-MM-dd HH:mm",new Date(1000L*Long.valueOf(comment.getDate())))+" 由@"+comment.getCommentName()+" 评论");
            }
            Glide.with(mContext).load(Constant.REALM_NAME+Remember.getString("key_avatar","")).error(R.drawable.pic_head).into(holder.ivAvatar);
            holder.nameTv.setText(Remember.getString("key_user_name",""));
            String rankName=Remember.getString("key_rank_name","");
            if(TextUtils.isEmpty(rankName)){
                holder.rankNameTv.setVisibility(View.GONE);
            }else {
                holder.rankNameTv.setVisibility(View.VISIBLE);
                holder.rankNameTv.setText(rankName);
            }
            holder.add_time_tv.setText("");
            holder.type_tv.setText(comment.getType());
            holder.title_tv.setText(comment.getTitle());
        }else {
            holder.ptime_tv.setText(comment.getDate()+" 我回复 @"+comment.getCommentName());
            if(comment.hasNote()){
                holder.layoutNote.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(Constant.REALM_NAME+comment.getAatar()).error(R.drawable.pic_head).into(holder.ivAvatar);
                holder.nameTv.setText(comment.getName());
                holder.content_tv.setText(TextUtils.isEmpty(comment.getContent())?"暂无描述":comment.getContent());
                holder.type_tv.setText(comment.getType());
                holder.title_tv.setText(comment.getTitle());
                holder.add_time_tv.setText(comment.getAddDate());
                if(TextUtils.isEmpty(comment.getRankName())){
                    holder.rankNameTv.setVisibility(View.GONE);
                }else {
                    holder.rankNameTv.setVisibility(View.VISIBLE);
                    holder.rankNameTv.setText(comment.getRankName());
                }
            }else {
                holder.layoutNote.setVisibility(View.GONE);
                holder.type_tv.setText("");
                holder.title_tv.setText("该文章已被删除");
            }
        }
        holder.comm_content_tv.setText(comment.getCommentContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comment.type.equals("act")){
//                    ActivitiesDetailActivity.inVoke(mContext,comment.getNoteId());
                }else {
//                    CommonDetailActivity.inVoke(mContext, "2", comment.getNoteId());
                }

            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDeleteListener!=null){
                    mOnDeleteListener.onDelete(comment);
                }
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
        TextView type_tv;
        TextView title_tv;
        TextView add_time_tv;
        TextView content_tv;
        TextView ptime_tv;
        TextView comm_content_tv;
        TextView delete_btn;
        ImageView ivAvatar;
        View layoutNote;

        public ItemViewHolder(View view) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.head_iv);
            nameTv = (TextView) view.findViewById(R.id.author_tv);
            rankNameTv = (TextView) view.findViewById(R.id.comm_rank_name_tv);
            tomsignedTv = (TextView) view.findViewById(R.id.f_tomsigned_tv);
            type_tv = (TextView) view.findViewById(R.id.type_tv);
            title_tv = (TextView) view.findViewById(R.id.title_tv);
            add_time_tv = (TextView) view.findViewById(R.id.add_time_tv);
            content_tv = (TextView) view.findViewById(R.id.content_tv);
            ptime_tv = (TextView) view.findViewById(R.id.ptime_tv);
            comm_content_tv = (TextView) view.findViewById(R.id.comm_content_tv);
            delete_btn = (TextView) view.findViewById(R.id.delete_btn);
            layoutNote = view.findViewById(R.id.layout_note);
        }
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener){
        mOnDeleteListener=onDeleteListener;
    }

    public interface OnDeleteListener{
        public void onDelete(Comment comment);
    }

}

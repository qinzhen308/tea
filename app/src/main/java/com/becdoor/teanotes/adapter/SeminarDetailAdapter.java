package com.becdoor.teanotes.adapter;

import android.content.Context;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.ArticleBean;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/4.
 * 专题详情
 */

public class SeminarDetailAdapter extends BaseAdapter {
    private Context mContext;
    private List<ArticleBean> mList;

    public SeminarDetailAdapter(Context context) {
        this.mContext = context;
    }

    public void setmList(List<ArticleBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ArticleBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_seminardetail, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ArticleBean info = getItem(position);
        if (info != null) {
            holder.titleTv.setText(info.getTitle());
            holder.contentTv.setText(Html.fromHtml(info.getContent()));
            Glide.with(mContext).load(Constant.REALM_NAME + info.getImg()).into(holder.imageView);
            holder.tagTv.setText("#" + info.getTag() + "#");
            createCommentTextView(info.getComm_info(), holder.conmentLayout);
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.item_seminarDetail_titleTv)
        TextView titleTv;
        @Bind(R.id.item_seminarDetail_contentTv)
        TextView contentTv;
        @Bind(R.id.item_seminarDetail_tagTv)
        TextView tagTv;
        @Bind(R.id.item_seminarDetail_iv)
        ImageView imageView;
        @Bind(R.id.item_seminarDetail_layout)
        LinearLayout conmentLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 创建评论
     *
     * @param commentLisr
     * @param layout
     */
    void createCommentTextView(List<NoteCommentBean> commentLisr, LinearLayout layout) {
        if (commentLisr == null) return;
        int m = mContext.getResources().getDimensionPixelSize(R.dimen.all_margrin);
        for (int i = 0; i < commentLisr.size(); i++) {
            NoteCommentBean bean = commentLisr.get(i);
            if (bean != null) {
                TextView tv = new TextView(mContext);
                tv.setTextColor(mContext.getResources().getColor(R.color.common_black));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tv.setText(Html.fromHtml("<font color='#7F654C'>@" + bean.getUsername() + "</font>\t\t" + bean.getContent()));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(m, 0, m, m);
                layout.addView(tv);
            }

        }
    }
}

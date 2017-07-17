package com.becdoor.teanotes.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.NoteCommentActivity;
import com.becdoor.teanotes.adapter.NoteCommentAdapter;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.listener.DetailListener;
import com.becdoor.teanotes.listener.NoteCommentListener;
import com.becdoor.teanotes.model.ActivitiesDetailInfo;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.becdoor.teanotes.model.NoteDetailBean;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.JsonData;
import com.becdoor.teanotes.until.NetUtil;
import com.nispok.snackbar.SnackbarManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/1.
 */

public class ActivitiesDetailFooterView extends LinearLayout implements View.OnClickListener {
    @Bind(R.id.activitiesDetail_commentListView)
    ReListView mCommentLv;
    @Bind(R.id.activitiesDetail_moreCommentTv)
    TextView mMoreCommentTv;
    @Bind(R.id.activitiesDetail_enrollIv1)
    ImageView mEnrollIv;
    @Bind(R.id.activitiesDetail_commentIv)
    ImageView mCommentIv;
    @Bind(R.id.activitiesDetail_collectIv)
    ImageView mCollectIv;
    @Bind(R.id.activitiesDetail_shareIv)
    ImageView mShareIv;
    @Bind(R.id.activitiesDetail_btnLayout)
    LinearLayout mBtnLayout;


    private NoteCommentAdapter noteCommentAdapter;
    private Context mContext;
    private ActivitiesDetailInfo info;

    public ActivitiesDetailFooterView(Context context) {
        super(context);
        init(context);
    }

    public ActivitiesDetailFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActivitiesDetailFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 设置数据
     *
     * @param noteCommentBeanList
     */
    public void setData(List<NoteCommentBean> noteCommentBeanList, int screenW, ActivitiesDetailInfo info) {
        this.info = info;
        noteCommentAdapter.setmList(noteCommentBeanList);
        noteCommentAdapter.notifyDataSetChanged();

        mCollectIv.setSelected(info.getIs_collect() == 1);
        int m = mContext.getResources().getDimensionPixelSize(R.dimen.all_margrin);
        screenW = (int) ((float) (screenW - 5 * m) / 4);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, screenW);
        params.setMargins(m, m, m, m);
        mBtnLayout.setLayoutParams(params);
    }

    private void init(Context context) {
        this.mContext = context;
        View footerView = View.inflate(context, R.layout.view_footer_activitiesdetail, null);
        addView(footerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this, footerView);

        mEnrollIv.setOnClickListener(this);
        mCommentIv.setOnClickListener(this);
        mCollectIv.setOnClickListener(this);
        mShareIv.setOnClickListener(this);
        mMoreCommentTv.setOnClickListener(this);

        noteCommentAdapter = new NoteCommentAdapter(context, new  NoteCommentListener() {
            @Override
            public void commSingleNote(NoteCommentBean noteComment) {
                if (detailListener != null) {
                    detailListener.onComment(noteComment);
                }
            }
        });
        mCommentLv.setAdapter(noteCommentAdapter);
    }

    private DetailListener detailListener;

    public void setDetailListener(DetailListener detailListener) {
        this.detailListener = detailListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activitiesDetail_enrollIv1://报名
                if (detailListener != null) {
                    detailListener.onEnroll();
                }
                break;
            case R.id.activitiesDetail_commentIv://评论
                if (detailListener != null) {
                    detailListener.onComment(null);
                }
                break;
            case R.id.activitiesDetail_collectIv://收藏
                if (detailListener != null) {
                    detailListener.onCollect(mCollectIv);
                }
                break;
            case R.id.activitiesDetail_shareIv://分享
                if (detailListener != null) {
                    detailListener.onShare();
                }
                break;
            case R.id.activitiesDetail_moreCommentTv://更多评论
                NoteCommentActivity.inVokeForActivity(mContext, info.getAct_info().getAct_id(), info.getAct_info().getAct_title());
                break;
        }
    }

    public void doSelectedForCollectIv() {
        mCollectIv.setSelected(true);
    }
}

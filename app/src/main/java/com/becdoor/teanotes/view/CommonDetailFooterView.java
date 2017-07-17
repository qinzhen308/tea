package com.becdoor.teanotes.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.NoteCommentActivity;
import com.becdoor.teanotes.adapter.CommonDetailMoreAdapter;
import com.becdoor.teanotes.adapter.NoteCommentAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.DetailListener;
import com.becdoor.teanotes.listener.NoteCommentListener;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.becdoor.teanotes.model.NoteDetailBean;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.JsonData;
import com.becdoor.teanotes.until.NetUtil;
import com.nispok.snackbar.SnackbarManager;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/1.
 */

public class CommonDetailFooterView extends LinearLayout implements View.OnClickListener {
    private ReListView mCommentLv;
    private TextView mMoreCommentTv;
    private ImageView mPrasieIv;
    private ImageView mCommentIv;
    private ImageView mCollectIv;
    private ImageView mShareIv;
    private TextView mMoreDataTv;
    private RecyclerView mMoreDataRecyclerView;
    private LinearLayout mBtnLayout;

    private CommonDetailMoreAdapter moreDataAdapter;
    private NoteCommentAdapter noteCommentAdapter;
    private String aid;
    private String title;
    private Context mContext;


    private NoteDetailBean mData;

    public CommonDetailFooterView(Context context) {
        super(context);
        init(context);
    }

    public CommonDetailFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonDetailFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 设置数据
     *
     * @param noteDetailBean
     */
    public void setData(NoteDetailBean noteDetailBean, int screenW, String aid) {
        this.aid = aid;
        mData = noteDetailBean;
        this.title = noteDetailBean.getTitle();
        int m = getResources().getDimensionPixelSize(R.dimen.all_margrin);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.283));
        params2.setMargins(m, 0, 0, 0);
        mMoreDataRecyclerView.setLayoutParams(params2);

        LayoutParams paramsBtn = new LayoutParams(LayoutParams.MATCH_PARENT, (int) ((float) (screenW - 5 * m) / 4));
        paramsBtn.setMargins(m, m, m, m);
        mBtnLayout.setLayoutParams(paramsBtn);

        noteCommentAdapter.setmList(noteDetailBean.getComm_list());
        noteCommentAdapter.notifyDataSetChanged();
        moreDataAdapter.setData(noteDetailBean.getMore_notes(), aid, noteDetailBean.getCat_id());
        moreDataAdapter.notifyDataSetChanged();

        mPrasieIv.setSelected(noteDetailBean.getIs_praise() == 1);
        mCollectIv.setSelected(noteDetailBean.getIs_collect() == 1);

    }

    private void init(Context context) {
        this.mContext = context;

        View footerView = View.inflate(context, R.layout.view_footer_commondetail, null);
        mCommentLv = (ReListView) footerView.findViewById(R.id.commonDetail_commentListView);
        mMoreCommentTv = (TextView) footerView.findViewById(R.id.commonDetail_moreCommentTv);
        mMoreDataTv = (TextView) footerView.findViewById(R.id.commonDetail_moreDataTv);
        mPrasieIv = (ImageView) footerView.findViewById(R.id.commonDetail_prasieIv);
        mCommentIv = (ImageView) footerView.findViewById(R.id.commonDetail_commentIv);
        mCollectIv = (ImageView) footerView.findViewById(R.id.commonDetail_collectIv);
        mShareIv = (ImageView) footerView.findViewById(R.id.commonDetail_shareIv);
        mMoreDataRecyclerView = (RecyclerView) footerView.findViewById(R.id.commonDetail_moreDataRecyclerView);
        mBtnLayout = (LinearLayout) footerView.findViewById(R.id.commonDetail_btnLayout);
        addView(footerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mMoreDataRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mMoreDataRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加空隙
        mMoreDataRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
                    state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = getResources().getDimensionPixelSize(R.dimen.all_margrin);
            }
        });

        mPrasieIv.setOnClickListener(this);
        mCommentIv.setOnClickListener(this);
        mCollectIv.setOnClickListener(this);
        mShareIv.setOnClickListener(this);
        mMoreDataTv.setOnClickListener(this);
        mMoreCommentTv.setOnClickListener(this);

        moreDataAdapter = new CommonDetailMoreAdapter(context);
        noteCommentAdapter = new NoteCommentAdapter(context, new NoteCommentListener() {
            @Override
            public void commSingleNote(NoteCommentBean noteComment) {
                if (detailListener != null) {
                    detailListener.onComment(noteComment);
                }
            }
        });
        mMoreDataRecyclerView.setAdapter(moreDataAdapter);
        mCommentLv.setAdapter(noteCommentAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commonDetail_prasieIv://赞
                if (detailListener != null) {
                    detailListener.onPrise(mPrasieIv);
                }

                break;
            case R.id.commonDetail_commentIv://评论
                if (detailListener != null) {
                    detailListener.onComment(null);
                }
                break;
            case R.id.commonDetail_collectIv://收藏
                if (detailListener != null) {
                    detailListener.onCollect(mCollectIv);
                }
                break;
            case R.id.commonDetail_shareIv://分享
                if (detailListener != null) {
                    detailListener.onShare();
                }
                break;
            case R.id.commonDetail_moreCommentTv://更多评论
                NoteCommentActivity.inVokeForNote(mContext, aid, title);
                break;
            case R.id.commonDetail_moreDataTv://更多茶游

                break;
        }
    }

    public void doSelectedForCollect() {
        mCollectIv.setSelected(true);
    }

    public void doSelectedForPrasie() {
        mPrasieIv.setSelected(true);
    }

    private DetailListener detailListener;

    public void setDetailListener(DetailListener detailListener) {
        this.detailListener = detailListener;
    }

}

package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.CommonDetailAdapter;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.listener.DetailListener;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.becdoor.teanotes.model.NoteDetailBean;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.JsonData;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.ScreenUtil;
import com.becdoor.teanotes.until.ShareUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.becdoor.teanotes.view.CommentDialogView;
import com.becdoor.teanotes.view.CommonDetailFooterView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/28.
 * 茶志、茶评、活动详情
 */

public class CommonDetailActivity extends TitleActivity {
    private ImageView mImageView;
    private TextView mTitleTv;
    private ImageView mAvatarIv;
    private TextView mNameTv;
    private ListView mContentLv;
    private TextView mNoTv;
    private CommonDetailFooterView commonDetailFooterView;

    private int screenW = 0;
    private CommonDetailAdapter mAdapter;

    private String cat_id;
    private String aid;
    private NoteDetailBean mNoteDetail;
    private ShareUtil mShareUtil;
    private boolean fromManager;
    private Dialog mDialog;
    private Dialog mComDialog;
    private CommentDialogView commentDialogView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExtra();
        setActivityContextView(R.layout.activity_commondetail);
        if (fromManager) {
            setTitleForRightTextView("", "编辑", true, true);
        } else {
            setTitleForRightIamgeView("", R.drawable.detail_menu_btn_icon, true, false);
        }
        showTietleImageView();
        initView();
        initData();
        init();
    }

    void detailShare() {
        if (mNoteDetail != null && mShareUtil != null) {
            mShareUtil.showShareDialog();
        }
    }

    void detailCollect(View view) {
        collect(view);
    }

    void detailComment() {
        if (mComDialog != null && mNoteDetail != null) {
            mComDialog.show();
        }
    }

    void detailPrasie(final View view) {
        Map<String, String> map = new HashMap<>();
        map.put("aid", aid);
        AppStatic.prasie(this, Constant.BASE_URL + "note_manage.php?", map, "cp", new AppStatic.PrasieListener() {
            @Override
            public void prasieScuccess() {
                if (view != null) {
                    view.setSelected(true);
                } else {
                    commonDetailFooterView.doSelectedForPrasie();
                }
            }

            @Override
            public void prasieFail() {
            }
        });

    }

    void init() {
        mDialog = DialogUtil.getLoadingDialog(this);

        commentDialogView = new CommentDialogView(this);
        mComDialog = DialogUtil.getMenuDialog(this, commentDialogView);
        commentDialogView.setCommentListener(new CommentDialogView.CommentListener() {
            @Override
            public void onComment(String content, NoteCommentBean noteCommentBean) {
                if (mComDialog != null && mComDialog.isShowing()) {
                    mComDialog.dismiss();
                }
                submitComment(content, noteCommentBean);
            }
        });
        commonDetailFooterView.setDetailListener(new DetailListener() {
            @Override
            public void onShare() {
                detailShare();
            }

            @Override
            public void onComment(NoteCommentBean noteCommentBean) {
                commentDialogView.updateEdtHintContent(noteCommentBean);
                detailComment();
            }

            @Override
            public void onPrise(View view) {
                detailPrasie(view);
            }

            @Override
            public void onEnroll() {

            }

            @Override
            public void onCollect(View view) {
                detailCollect(view);
            }
        });
    }

    private void getData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("cat_id", cat_id);
        map.put("aid", aid);

        NetUtil.getData(this, Constant.BASE_URL + "note_manage.php?", map, "showNote", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                mNoTv.setVisibility(View.VISIBLE);
                mContentLv.setVisibility(View.GONE);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && !object.isNull("data")) {
                        JSONObject data = object.getJSONObject("data");
                        if (data.has("art_arr") && !data.get("art_arr").toString().startsWith("[")) {
                            data.remove("art_arr");
                        }
                        mNoteDetail = new Gson().fromJson(data.toString(), NoteDetailBean.class);
                        if (mNoteDetail != null) {
                            handleData();
                        }

                        mNoTv.setVisibility(View.GONE);
                        mContentLv.setVisibility(View.VISIBLE);
                    } else {
                        DialogUtil.dismissDialog(mLoaDailog);
                        mNoTv.setVisibility(View.VISIBLE);
                        mContentLv.setVisibility(View.GONE);
                        mNoTv.setText(object == null ? "获取数据失败" : object.optString("message"));
                    }
                } catch (Exception e) {
                    DialogUtil.dismissDialog(mLoaDailog);
                    mNoTv.setVisibility(View.VISIBLE);
                    mContentLv.setVisibility(View.GONE);
                }
            }
        });
    }

    void initShare() {
        Map<String, String> map = new HashMap<>();
        map.put("cat_id", String.valueOf(mNoteDetail.getCat_id()));
        map.put("aid", aid);
        AppStatic.getShareData(this, Constant.BASE_URL + "note_manage.php?", map, new AppStatic.ShareListener() {
            @Override
            public void shareScuccess(String url) {
                DialogUtil.dismissDialog(mLoaDailog);
                mShareUtil = new ShareUtil(CommonDetailActivity.this, mNoteDetail.getTitle(), mNoteDetail.getContent(), url, Constant.REALM_NAME + mNoteDetail.getImg());
                mShareUtil.setShareListener(new ShareUtil.ShareListener() {
                    @Override
                    public void onScuccess() {
                        AppStatic.shareCallBack(CommonDetailActivity.this, aid, "note");
                    }
                });
            }

            @Override
            public void shareFail() {
                DialogUtil.dismissDialog(mLoaDailog);
            }
        });
    }

    private void handleData() {
        Glide.with(CommonDetailActivity.this).load(Constant.REALM_NAME + mNoteDetail.getImg()).placeholder(R.drawable.ic_stub).centerCrop().into(mImageView);

        mTitleTv.setText(mNoteDetail.getTitle());
        Glide.with(CommonDetailActivity.this).load(Constant.REALM_NAME + mNoteDetail.getPic()).transform(new CircleTransform(CommonDetailActivity.this)).into(mAvatarIv);
        mNameTv.setText(mNoteDetail.getAuthor());

        mAdapter.setmList(mNoteDetail.getArt_arr());
        mAdapter.notifyDataSetChanged();
        commonDetailFooterView.setData(mNoteDetail, screenW, aid);
        initShare();
    }

    @Override
    protected void onRightClick(View rightView) {
        super.onRightClick(rightView);
        if (fromManager) {
            CreateAtticleActivity.invoke(this, mNoteDetail, false);
        } else {
            showCommonRightView(rightView);
        }
    }

    private void setExtra() {
        if (getIntent() != null) {
            cat_id = getIntent().getStringExtra("cat_id");
            aid = getIntent().getStringExtra("aid");
            fromManager = getIntent().getBooleanExtra("from_manager", false);
            Bundle data = getIntent().getBundleExtra("data");
            if (data != null) {
                mNoteDetail = (NoteDetailBean) data.getSerializable("offline_note");
                fromManager = true;
            }
        }
    }

    private void initData() {
        mAdapter = new CommonDetailAdapter(this);
        mContentLv.setAdapter(mAdapter);
        //离线笔记
        if (mNoteDetail != null) {
            handleData();
        } else {
            getData();
        }
    }

    private void initView() {
        View headerView = View.inflate(this, R.layout.view_header_commondetail, null);
        mImageView = (ImageView) headerView.findViewById(R.id.commonDetail_imgIv);
        mAvatarIv = (ImageView) headerView.findViewById(R.id.commonDetail_avatarIv);
        mNameTv = (TextView) headerView.findViewById(R.id.commonDetail_nameTv);
        mTitleTv = (TextView) headerView.findViewById(R.id.commonDetail_titleTv);
        mContentLv = (ListView) findViewById(R.id.commonDetail_contentListView);
        mNoTv = (TextView) findViewById(R.id.commonDetail_tv);
        commonDetailFooterView = new CommonDetailFooterView(this);
        screenW = AppUtil.getScreenWH(this)[0];
        mImageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.533)));

        mContentLv.addHeaderView(headerView);
        mContentLv.addFooterView(commonDetailFooterView);
    }

    public static void inVoke(Context context, String cat_id, String aid) {
        Intent intent = new Intent(context, CommonDetailActivity.class);
        intent.putExtra("cat_id", cat_id);
        intent.putExtra("aid", aid);
        context.startActivity(intent);
    }

    public static void invoke(Context context, NoteDetailBean note) {
        Intent intent = new Intent(context, CommonDetailActivity.class);
        Bundle data = new Bundle();
        data.putSerializable("offline_note", note);
        intent.putExtra("cat_id", data);
        context.startActivity(intent);
    }

    public static void invoke(Context context, String cat_id, String aid, boolean fromManager) {
        Intent intent = new Intent(context, CommonDetailActivity.class);
        intent.putExtra("cat_id", cat_id);
        intent.putExtra("aid", aid);
        intent.putExtra("from_manager", fromManager);
        context.startActivity(intent);
    }

    @Override
    void onRightItemClick(Object object, int position) {
        super.onRightItemClick(object, position);
        switch (position) {
            case 0:
                detailPrasie(null);
                break;
            case 1:
                detailShare();
                break;
            case 2:
                detailCollect(null);
                break;
            case 3:
                detailComment();
                break;
        }
    }

    private void collect(final View view) {
        DialogUtil.showDialog(mDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("cat_id", "" + mNoteDetail.getCat_id());
        params.put("aid", aid);
        NetUtil.getData(CommonDetailActivity.this, Constant.BASE_URL + "note_manage.php?", params, "collect", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!CommonDetailActivity.this.isFinishing()) DialogUtil.dismissDialog(mDialog);
                SnackbarManager.show(com.nispok.snackbar.Snackbar.with(CommonDetailActivity.this).color(Color.GRAY).textColor(Color.BLACK).text(R.string.net_work_error + R.string.please_again));
                Toast.makeText(CommonDetailActivity.this, "收藏失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!CommonDetailActivity.this.isFinishing()) DialogUtil.dismissDialog(mDialog);
                if (response != null) {
                    JsonData jsonData = JsonData.create(response);
                    int status = jsonData.optInt("status");
                    if (status == 200) {
                        String msg = jsonData.optString("message");
                        if (view != null) {
                            view.setSelected(true);
                        } else {
                            commonDetailFooterView.doSelectedForCollect();
                        }
                        Toast.makeText(CommonDetailActivity.this, TextUtils.isEmpty(msg) ? "收藏成功" : msg, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CommonDetailActivity.this, jsonData.optString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    /**
     * 提交评论
     *
     * @param content
     */
    void submitComment(String content, NoteCommentBean noteCommentBean) {
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("title", mNoteDetail.getTitle());
        map.put("aid", aid);
        if (noteCommentBean != null && !TextUtils.isEmpty(noteCommentBean.getUid())) {
            map.put("comm_id", noteCommentBean.getUid());
        }

        AppStatic.submitComment(this, Constant.BASE_URL + "note_manage.php?", map, "addComm", new AppStatic.CommentListener() {
            @Override
            public void commentScuccess() {
                getData();
            }

            @Override
            public void commentFail() {

            }
        });

    }
}

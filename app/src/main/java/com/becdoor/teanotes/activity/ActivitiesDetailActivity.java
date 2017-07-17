package com.becdoor.teanotes.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View

        ;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.listener.DetailListener;
import com.becdoor.teanotes.model.ActivitiesDetailInfo;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.JsonData;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.ShareUtil;
import com.becdoor.teanotes.view.ActivitiesDetailFooterView;
import com.becdoor.teanotes.view.CircleTransform;
import com.becdoor.teanotes.view.CommentDialogView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * Created by Administrator on 2016/11/2.
 * 活动详情
 */

public class ActivitiesDetailActivity extends TitleActivity {
    @Bind(R.id.activitiesDetail_imgLayout)
    RelativeLayout mImgLayout;
    @Bind(R.id.activitiesDetail_imgIv)
    ImageView mImageView;
    @Bind(R.id.activitiesDetail_titleTv)
    TextView mTitleTv;
    @Bind(R.id.activitiesDetail_detailTv)
    TextView mDetailTv;
    @Bind(R.id.activitiesDetail_enrollTv)
    TextView mEnrollTv;
    @Bind(R.id.activitiesDetail_footerView)
    ActivitiesDetailFooterView activitiesDetailFooterView;
    @Bind(R.id.activitiesDetail_descTv)
    TextView mDescTv;
    @Bind(R.id.activitiesDetail_ruleTv)
    TextView mRuleTv;
    @Bind(R.id.item_AD_One_avatarIv)
    ImageView mAvatarTv;
    @Bind(R.id.item_AD_One_nameTv)
    TextView nameTv;
    @Bind(R.id.item_AD_One_startTimeTv)
    TextView startTimeTv;
    @Bind(R.id.item_AD_One_endTimeTv)
    TextView endTimeTv;
    @Bind(R.id.item_AD_One_addressTv)
    TextView addressTv;
    @Bind(R.id.item_AD_One_limitTv)
    TextView limitTv;
    @Bind(R.id.activitiesDetail_tv)
    TextView mNoTv;

    EditText dialogNameEdt;
    EditText dialogPhoneEdt;
    EditText dialogNumEdt;
    Button dialogEnrollBtn;
    ImageView dialogCloseIv;

    private int screenW = 0;
    private String act_id;//活动id
    private ActivitiesDetailInfo info;

    private Dialog mEnrollDialog;
    private ShareUtil mShareUtil;
    Dialog mDialog;
    Dialog mComDialog;
    private CommentDialogView commentDialogView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_activitiesdetail);
        setTitleForRightIamgeView("", R.drawable.detail_menu_btn_icon, true, false);
        showTietleImageView();
        initView();

        act_id = getIntent().getStringExtra("act_id");
        showData();
        init();
    }


    void init() {
        activitiesDetailFooterView.setDetailListener(new DetailListener() {
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

            }

            @Override
            public void onEnroll() {
                detailEnroll();
            }

            @Override
            public void onCollect(View view) {
                detailCollect(view);
            }
        });
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
    }

    void getData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("act_id", act_id);
        NetUtil.getData(this, Constant.BASE_URL + "activity.php?", map, "actInfo", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                mNoTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        info = new Gson().fromJson(object.getJSONObject("data").toString(), ActivitiesDetailInfo.class);
                        showData();
                        mNoTv.setVisibility(View.GONE);
                    } else {
                        mNoTv.setVisibility(View.VISIBLE);
                        mNoTv.setText(object == null ? "获取数据失败" : object.optString("message"));
                        DialogUtil.dismissDialog(mLoaDailog);
                    }
                } catch (JSONException e) {
                    DialogUtil.dismissDialog(mLoaDailog);
                    mNoTv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void initShare() {

        Map<String, String> map = new HashMap<>();
        map.put("act_id", info.getAct_info().getAct_id());
        AppStatic.getShareData(this, Constant.BASE_URL + "activity.php?", map, new AppStatic.ShareListener() {
            @Override
            public void shareScuccess(String url) {
                DialogUtil.dismissDialog(mLoaDailog);
                mShareUtil = new ShareUtil(ActivitiesDetailActivity.this, info.getAct_info().getAct_title(), info.getAct_info().getDescription(), url, Constant.REALM_NAME + info.getAct_info().getAct_pic());
                mShareUtil.setShareListener(new ShareUtil.ShareListener() {
                    @Override
                    public void onScuccess() {
                        AppStatic.shareCallBack(ActivitiesDetailActivity.this, info.getAct_info().getAct_id(), "acts");
                    }
                });
            }

            @Override
            public void shareFail() {
                DialogUtil.dismissDialog(mLoaDailog);
            }
        });
    }

    void showData() {
        if (info != null) {
            if (info.getAct_info() != null) {
                mTitleTv.setText(info.getAct_info().getAct_title());
                Glide.with(ActivitiesDetailActivity.this).load(Constant.REALM_NAME + info.getAct_info().getAct_pic()).placeholder(R.drawable.ic_stub).into(mImageView);
                Glide.with(ActivitiesDetailActivity.this).load(Constant.REALM_NAME + info.getAct_info().getAct_s_pic()).placeholder(R.drawable.login_username_icon).transform(new CircleTransform(ActivitiesDetailActivity.this)).into(mAvatarTv);
                addressTv.setText(info.getAct_info().getAct_address());
                startTimeTv.setText(info.getAct_info().getAct_start_date());
                endTimeTv.setText(info.getAct_info().getAct_end_date());
                limitTv.setText(info.getAct_info().getAct_user_num() + "人");
                nameTv.setText(info.getAct_info().getAuthor());
                mDescTv.setText(Html.fromHtml(info.getAct_info().getAct_desc()));
                mRuleTv.setText(Html.fromHtml(info.getAct_info().getAct_rule()));
                initShare();
            }
            activitiesDetailFooterView.setData(info.getComm_list(), screenW, info);
        } else {
            getData();
        }
    }

    void initView() {
        ButterKnife.bind(this);
        screenW = AppUtil.getScreenWH(this)[0];
        mImgLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.536)));

        mDetailTv.setOnClickListener(this);
        mEnrollTv.setOnClickListener(this);
        mDetailTv.setSelected(true);

        View dialogView = View.inflate(this, R.layout.dialog_enroll_online, null);
        dialogNameEdt = (EditText) dialogView.findViewById(R.id.dialog_enrollOnline_nameEdt);
        dialogPhoneEdt = (EditText) dialogView.findViewById(R.id.dialog_enrollOnline_phoneEdt);
        dialogNumEdt = (EditText) dialogView.findViewById(R.id.dialog_enrollOnline_numEdt);
        dialogEnrollBtn = (Button) dialogView.findViewById(R.id.dialog_enrollOnline_enrrollBtn);
        dialogCloseIv = (ImageView) dialogView.findViewById(R.id.dialog_enrollOnline_closeIv);

        dialogEnrollBtn.setOnClickListener(this);
        dialogCloseIv.setOnClickListener(this);
        mEnrollDialog = DialogUtil.getCenterDialog(this, dialogView);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activitiesDetail_detailTv:
                changeView(true);
                break;
            case R.id.activitiesDetail_enrollTv:
                detailEnroll();
                break;
            case R.id.dialog_enrollOnline_closeIv:
                mEnrollDialog.dismiss();
                changeView(true);
                break;
            case R.id.dialog_enrollOnline_enrrollBtn:
                String name = dialogNameEdt.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    CustomToast.showToast(this, "请填写您的真实姓名！", 1500);
                    return;
                }
                String phone = dialogPhoneEdt.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    CustomToast.showToast(this, "请填写您的真实电话！", 1500);
                    return;
                }
                String num = dialogNumEdt.getText().toString();
                if (TextUtils.isEmpty(num)) {
                    CustomToast.showToast(this, "请填写参加人数！", 1500);
                    return;
                }
                enrollAct(num, name, phone);
                break;
        }
    }

    void detailEnroll() {
        changeView(false);
        mEnrollDialog.show();
    }

    void detailShare() {
        if (mShareUtil != null) {
            mShareUtil.showShareDialog();
        }
    }

    void detailCollect(final View view) {
        DialogUtil.showDialog(mDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("act_id", info.getAct_info().getAct_id());
        params.put("act_title", info.getAct_info().getAct_title());
        NetUtil.getData(this, Constant.BASE_URL + "activity.php?", params, "collect", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!ActivitiesDetailActivity.this.isFinishing()) DialogUtil.dismissDialog(mDialog);
                SnackbarManager.show(com.nispok.snackbar.Snackbar.with(ActivitiesDetailActivity.this).color(Color.GRAY).textColor(Color.BLACK).text(R.string.net_work_error + R.string.please_again));
                Toast.makeText(ActivitiesDetailActivity.this, "收藏失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!ActivitiesDetailActivity.this.isFinishing()) DialogUtil.dismissDialog(mDialog);
                if (response != null) {
                    JsonData jsonData = JsonData.create(response);
                    int status = jsonData.optInt("status");
                    if (status == 200) {
                        String msg = jsonData.optString("message");
                        if (view != null) {
                            view.setSelected(true);
                        } else {
                            activitiesDetailFooterView.doSelectedForCollectIv();
                        }

                        Toast.makeText(ActivitiesDetailActivity.this, TextUtils.isEmpty(msg) ? "收藏成功" : msg, Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(ActivitiesDetailActivity.this, jsonData.optString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    void detailComment() {
        if (info != null && mComDialog != null && !mComDialog.isShowing()) {
            mComDialog.show();
        }
    }

    @Override
    protected void onRightClick(View rightView) {
        super.onRightClick(rightView);
        showCommonRightViewForEnroll(rightView);
    }

    void changeView(boolean isDetail) {
        mDetailTv.setSelected(isDetail);
        mEnrollTv.setSelected(!isDetail);
    }

    /**
     * 参加活动
     *
     * @param join_num
     * @param username
     * @param telephone
     */
    void enrollAct(String join_num, String username, final String telephone) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("act_id", act_id);
        map.put("username", username);
        map.put("telephone", telephone);
        map.put("act_title", info.getAct_info().getAct_title());
        map.put("join_num", join_num);
        NetUtil.postData(this, Constant.BASE_URL + "activity.php?", map, "joinAct", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(ActivitiesDetailActivity.this, "报名成功！", Toast.LENGTH_SHORT).show();
                        mEnrollDialog.dismiss();
                        changeView(true);
                    } else {
                        Toast.makeText(ActivitiesDetailActivity.this, object == null ? "报名失败！" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ActivitiesDetailActivity.this, "报名失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void inVoke(Context context, String act_id) {
        Intent intent = new Intent(context, ActivitiesDetailActivity.class);
        intent.putExtra("act_id", act_id);
        context.startActivity(intent);
    }

    @Override
    void onRightItemClick(Object object, int position) {
        super.onRightItemClick(object, position);
        switch (position) {
            case 0:
                detailEnroll();
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

    /**
     * 提交评论
     *
     * @param content
     */
    void submitComment(String content, NoteCommentBean noteCommentBean) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("act_id", info.getAct_info().getAct_id());
        map.put("act_title", info.getAct_info().getAct_title());
        if (noteCommentBean != null && !TextUtils.isEmpty(noteCommentBean.getUid())) {
            map.put("comm_id", noteCommentBean.getUid());
        }

        AppStatic.submitComment(this, Constant.BASE_URL + "activity.php?", map, "addComm", new AppStatic.CommentListener() {
            @Override
            public void commentScuccess() {
                DialogUtil.dismissDialog(mLoaDailog);
                getData();
            }

            @Override
            public void commentFail() {
                DialogUtil.dismissDialog(mLoaDailog);
            }
        });

    }

}

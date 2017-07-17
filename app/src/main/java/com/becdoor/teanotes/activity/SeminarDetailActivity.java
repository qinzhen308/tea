package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.SeminarDetailAdapter;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.Article;
import com.becdoor.teanotes.model.ArticleBean;
import com.becdoor.teanotes.model.SeminarInfo;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.ShareUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/4.
 * 专题详情
 */

public class SeminarDetailActivity extends TitleActivity {
    private ImageView mImageView;
    private TextView mTitleTv;
    private TextView mContentTv;
    private TextView mDetailTv;
    private RelativeLayout mLayout;
    private ListView mListView;

    private String sid;
    private SeminarDetailAdapter mAdapter;
    private ShareUtil mShareUtil;
    private SeminarInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_seminardetail);
//        setTitleForRightIamgeView("专题详情", R.drawable., true, false);
        setTitleForRightTextView("专题详情", "分享", true, true);
        sid = getIntent().getStringExtra("sid");

        initView();
        mAdapter = new SeminarDetailAdapter(this);
        mListView.setAdapter(mAdapter);
        getData();
    }

    @Override
    protected void onRightClick(View rightView) {
        super.onRightClick(rightView);
        if (mShareUtil != null) {
            mShareUtil.showShareDialog();
        }
    }

    void initView() {
        View headerView = View.inflate(this, R.layout.view_header_seminardetail, null);
        mImageView = (ImageView) headerView.findViewById(R.id.seminarDetail_iv);
        mTitleTv = (TextView) headerView.findViewById(R.id.seminarDetail_titleTv);
        mContentTv = (TextView) headerView.findViewById(R.id.seminarDetail_contentTv);
        mDetailTv = (TextView) headerView.findViewById(R.id.seminarDetail_detailTv);
        mLayout = (RelativeLayout) headerView.findViewById(R.id.seminarDetail_layout);
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (AppUtil.getScreenWH(this)[0] * 0.564)));
        mListView = (ListView) findViewById(R.id.seminarDetail_listView);
        mListView.addHeaderView(headerView);
    }

    void getData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("sid", sid);
        NetUtil.getData(this, Constant.BASE_URL + "index.php?", map, "spec_info", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        JSONObject object1 = object.getJSONObject("data");
                        info = new Gson().fromJson(object1.getJSONObject("spec_info").toString(), SeminarInfo.class);
                        if (info != null) {
                            mTitleTv.setText(info.getTitle());
                            mContentTv.setText(info.getSeries());
                            if (TextUtils.isEmpty(info.getInfo())) {
                                mDetailTv.setVisibility(View.GONE);
                            } else {
                                mDetailTv.setText(info.getInfo());
                            }
                            Glide.with(SeminarDetailActivity.this).load(Constant.REALM_NAME + info.getImg()).into(mImageView);
                            initShare();
                        }

                        List<ArticleBean> list = new Gson().fromJson(object1.getJSONArray("article_list").toString(), new TypeToken<List<ArticleBean>>() {
                        }.getType());
                        mAdapter.setmList(list);
                        mAdapter.notifyDataSetChanged();
                    }else {
                        DialogUtil.dismissDialog(mLoaDailog);
                    }
                } catch (JSONException e) {
                    DialogUtil.dismissDialog(mLoaDailog);
                }
            }
        });
    }

    void initShare() {
        Map<String, String> map = new HashMap<>();
        map.put("sid", sid);
        AppStatic.getShareData(this, Constant.BASE_URL + "index.php?", map, new AppStatic.ShareListener() {
            @Override
            public void shareScuccess(String url) {
                DialogUtil.dismissDialog(mLoaDailog);
                mShareUtil = new ShareUtil(SeminarDetailActivity.this, info.getTitle(), info.getInfo(), url, Constant.REALM_NAME + info.getImg());
                mShareUtil.setShareListener(new ShareUtil.ShareListener() {
                    @Override
                    public void onScuccess() {
                        AppStatic.shareCallBack(SeminarDetailActivity.this, sid, "spec");
                    }
                });
            }

            @Override
            public void shareFail() {
                DialogUtil.dismissDialog(mLoaDailog);
            }
        });
    }


    /**
     * @param context
     * @param sid     专题id
     */
    public static void inVoke(Context context, String sid) {
        Intent intent = new Intent(context, SeminarDetailActivity.class);
        intent.putExtra("sid", sid);
        context.startActivity(intent);
    }

}

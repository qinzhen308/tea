package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.CateAdapter;
import com.becdoor.teanotes.adapter.PersonalSpaceAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.CateInfo;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.model.PersonalSpaceInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jjj on 2016/11/8.
 * 私人空间
 */

public class PersonalSpaceActivity extends BaseRecyclerViewActivity {
    private TextView mBackTv;
    private TextView mTitleTv;
    private TextView mCateTv;
    private RelativeLayout mTitleLayout;
    private TextView mErroTv;

    private PersonalSpaceAdapter mAdapter;
    private PersonalSpaceInfo mInfo;
    private String uid;
    private String cat_id = "";
    private List<NoteReInfoBean> mList;
    private CateAdapter cateAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalspace);

        initView();
        initData();
    }

    void getData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("cat_id", cat_id);
        setPostFormBuilder(Constant.BASE_URL + "space.php?", map, "space", new INetCallBackListener() {
            @Override
            public void onFail() {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response) {
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        mInfo = new Gson().fromJson(object.getJSONObject("data").toString(), PersonalSpaceInfo.class);
                        totalPage = mInfo.getMax_page();
                        if (mInfo != null) {
                            if (mInfo.getArticle_list() != null) {
                                mList.addAll(mInfo.getArticle_list());
                            }
                            if (mInfo.getAll_nav_list() != null) {
                                cateAdapter.setmList(mInfo.getAll_nav_list());
                            }
                            if (mInfo.getSpace_info() != null) {
                                mInfo.getSpace_info().setIs_atten(mInfo.getIs_atten());
                                mTitleTv.setText(mInfo.getSpace_info().getName());
                                mAdapter.setData(mList, mInfo.getSpace_info(), uid);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        mErroTv.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        mErroTv.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        mErroTv.setText(object == null ? "获取失败" : object.optString("message"));
                    }
                } catch (JSONException e) {
                    mErroTv.setVisibility(View.VISIBLE);
                    mErroTv.setText("获取失败了");
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    void initData() {
        uid = getIntent().getStringExtra("uid");

        mList = new ArrayList<>();
        mAdapter = new PersonalSpaceAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        getData();
        cateAdapter = new CateAdapter(this);
        initRightWindow(0, 0, cateAdapter);
    }

    void initView() {
        mTitleLayout = (RelativeLayout) findViewById(R.id.personelSpace_titleLayout);
        mBackTv = (TextView) findViewById(R.id.personelSpace_backTv);
        mTitleTv = (TextView) findViewById(R.id.personelSpace_titleTv);
        mCateTv = (TextView) findViewById(R.id.personelSpace_cateTv);
        mErroTv = (TextView) findViewById(R.id.personelSpace_erroTv);
        mRecyclerView = (RBCallbkRecyclerView) findViewById(R.id.personelSpace_recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.personelSpace_swip_refresh_widget);
        mBackTv.setOnClickListener(this);
        mCateTv.setOnClickListener(this);
        mTitleLayout.getBackground().setAlpha(100);
        initListener();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mBackTv) {
            finish();
        } else if (v == mCateTv) {
            showRightVindow(mCateTv);
        }
    }

    @Override
    void onRightItemClick(Object object, int position) {
        super.onRightItemClick(object, position);
        if (object != null) {
            cat_id = ((CateInfo) object).getCat_id();
            mCateTv.setText(((CateInfo) object).getCat_name());
            mList.clear();
            getData();
        }
    }

    @Override
    void loadNextPage() {
        getData();
    }

    @Override
    void refreshData() {
        mList.clear();
        getData();
    }

    public static void inVoke(Context context, String uid) {
        Intent intent = new Intent(context, PersonalSpaceActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

}

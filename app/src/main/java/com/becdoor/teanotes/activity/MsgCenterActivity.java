package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.MsgCenterAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.MsgInfo;
import com.becdoor.teanotes.model.PriMsgInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jjj on 2016/11/9.
 * 消息中心
 */

public class MsgCenterActivity extends BaseRecyclerViewActivity {
    private TextView mMsgTv;
    private TextView mPriMsgTv;
    private MsgCenterAdapter mAdapter;

    private List<MsgInfo> mMsgList;
    private List<PriMsgInfo> mPriMsgList;
    private int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_msgcenter);
        setTitleForRightIamgeView("消息中心", 0, true, false);

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(true);
    }

    void initData() {
        mMsgList = new ArrayList<>();
        mPriMsgList = new ArrayList<>();
        mAdapter = new MsgCenterAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mMsgTv.setSelected(true);
        mPriMsgTv.setSelected(false);
    }

    void initView() {
        mMsgTv = (TextView) findViewById(R.id.msgCenter_msgTv);
        mPriMsgTv = (TextView) findViewById(R.id.msgCenter_priMsgTv);
        mRecyclerView = (RBCallbkRecyclerView) findViewById(R.id.msgCenter_recycleview_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.msgCenter_swip_refresh_widget);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMsgTv.setOnClickListener(this);
        mPriMsgTv.setOnClickListener(this);
        initListener();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mMsgTv) {
            if (mMsgTv.isSelected()) {
                return;
            }
            type = 0;
            getData(true);
        } else if (v == mPriMsgTv) {
            if (mPriMsgTv.isSelected()) {
                return;
            }
            type = 1;
            getData(true);
        }
    }

    void changeData() {
        mMsgList.clear();
        mPriMsgList.clear();
        mAdapter.setType(type);
    }

    @Override
    void loadNextPage() {
        getData(false);
    }

    @Override
    void refreshData() {

        if (type == 0) {
            mMsgList.clear();
        } else {
            mPriMsgList.clear();
        }
        getData(false);
    }

    void getData(final boolean hasDialog) {
        if (hasDialog) {
            curPage=1;
            DialogUtil.showDialog(mLoaDailog);
            if (type == 0) {
                mMsgTv.setSelected(true);
                mPriMsgTv.setSelected(false);
            } else {
                mMsgTv.setSelected(false);
                mPriMsgTv.setSelected(true);
            }
        }
        Map<String, String> map = new HashMap<>();
        if (type == 0) {
            map.put("op", "notice");
        } else {
            map.put("op", "priv");
        }
        setPostFormBuilder(Constant.BASE_URL + "center.php?", map, "msg_center", new INetCallBackListener() {
            @Override
            public void onFail() {
                if (hasDialog) {
                    changeData();
                }
                mAdapter.notifyDataSetChanged();
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response) {
                try {
                    if (hasDialog) {
                        changeData();
                    }

                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null) {

                        JSONObject dObject = object.getJSONObject("data");
                        totalPage = dObject.optInt("max_page");
                        if (type == 0) {
                            List<MsgInfo> msgInfos = new Gson().fromJson(dObject.getJSONArray("msg_list").toString(), new TypeToken<List<MsgInfo>>() {
                            }.getType());
                            if (msgInfos != null) {
                                mMsgList.addAll(msgInfos);
                            }
                            mAdapter.setMsgList(mMsgList);
                        } else {
                            List<PriMsgInfo> priMsgInfos = new Gson().fromJson(dObject.getJSONArray("msg_list").toString(), new TypeToken<List<PriMsgInfo>>() {
                            }.getType());
                            if (priMsgInfos != null) {
                                mPriMsgList.addAll(priMsgInfos);
                            }
                            mAdapter.setPriMsgList(mPriMsgList);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static void inVoke(Context context) {
        Intent intent = new Intent(context, MsgCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            getData(true);
        }
    }
}

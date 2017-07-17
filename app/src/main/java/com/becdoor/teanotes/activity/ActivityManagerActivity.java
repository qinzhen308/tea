package com.becdoor.teanotes.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.ActivityManagerAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.ActivityInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjj on 2016/11/14.
 * 活动管理
 */

public class ActivityManagerActivity extends BaseRecyclerViewActivity {
    private List<ActivityInfo> mList;
    private ActivityManagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_activitymanager);
        setTitleForRightIamgeView("活动管理", 0, true, false);

        mRecyclerView = (RBCallbkRecyclerView) findViewById(R.id.recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_refresh_widget);

        mList = new ArrayList<>();
        mAdapter = new ActivityManagerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        initListener();
        getData();
    }

    void getData() {
        DialogUtil.showDialog(mLoaDailog);
        setPostFormBuilder(Constant.BASE_URL + "center.php?", null,"act_manage", new INetCallBackListener() {
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
                        totalPage = object.getJSONObject("data").optInt("max_page");
                        List<ActivityInfo> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("act_list").toString(), new TypeToken<List<ActivityInfo>>() {
                        }.getType());
                        if (list != null) {
                            mList.addAll(list);
                        }
                        mAdapter.setData(mList);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
}

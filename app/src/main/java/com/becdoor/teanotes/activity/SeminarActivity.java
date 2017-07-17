package com.becdoor.teanotes.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.SeminarAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.SeminarInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/4.
 * 专题
 */

public class SeminarActivity extends BaseRecyclerViewActivity {
    List<SeminarInfo> mList;
    private SeminarAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_seminar);
        setTitleForRightIamgeView("专题推荐", 0, true, false);

        mRecyclerView = (RBCallbkRecyclerView) findViewById(R.id.seminar_recycleview_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.seminar_swip_refresh_widget);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initListener();
        mList = new ArrayList<>();
        mAdapter = new SeminarAdapter(this);
        mAdapter.setData(mList);
        mRecyclerView.setAdapter(mAdapter);
        DialogUtil.showDialog(mLoaDailog);
        getData();
    }

    void getData() {
        setPostFormBuilder(Constant.BASE_URL + "index.php?", null, "special", new INetCallBackListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onScuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        totalPage = object.getJSONObject("data").optInt("max_page");
                        List<SeminarInfo> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("special_list").toString(), new TypeToken<List<SeminarInfo>>() {
                        }.getType());
                        if (list != null) {
                            mList.addAll(list);
                        }
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

package com.becdoor.teanotes.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.ActivityAdapter;
import com.becdoor.teanotes.adapter.FeaturedAdapter;
import com.becdoor.teanotes.adapter.RegionAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.ActivityInfo;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.model.RegionInfo;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/14.
 */
public class FragmentActivities extends BaseRecyclerViewFragment {
    private View view;
    private ActivityAdapter mAdapter;
    private ArrayList<ActivityInfo> mData;
    private String prov = "";//省份ID
    private TextView mAddressTv;

    private List<RegionInfo> mRegionList;
    private RegionAdapter mRegionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_activities, null);
            initView(view);
        } else {
            ViewGroup group = (ViewGroup) view.getParent();
            if (group != null) {
                group.removeView(view);
            }
        }
        return view;
    }

    private void initView(View view) {
        mAddressTv = (TextView) view.findViewById(R.id.f_activities_addressTv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_widget);
        mRecyclerView = (RBCallbkRecyclerView) view.findViewById(R.id.recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ActivityAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        initListener();
        mAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRegionAdapter != null && mRegionList != null) {
                    showRightWindow(mAddressTv, 0, 0, mRegionAdapter);
                    mRegionAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mData = new ArrayList<>();
        mAdapter.setData(mData);
        mRegionAdapter = new RegionAdapter(getActivity());
        getData();
    }


    @Override
    void onRightItemClick(Object object, int position) {
        super.onRightItemClick(object, position);
        if (object != null) {
            RegionInfo regionInfo = (RegionInfo) object;
            mAddressTv.setText(regionInfo.getRegion_name());
            prov = regionInfo.getRegion_id();
            mData.clear();
            getData();
        }


    }

    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("prov", prov);

        setPostFormBuilder(Constant.BASE_URL + "activity.php?", map, "activity", new INetCallBackListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onScuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        JSONObject dObject = object.getJSONObject("data");

                        if (mRegionList == null && dObject.has("prov_list") && !dObject.isNull("prov_list")) {
                            mRegionList = new Gson().fromJson(dObject.getJSONArray("prov_list").toString(), new TypeToken<List<RegionInfo>>() {
                            }.getType());
                            mRegionAdapter.setmList(mRegionList);
                        }

                        if (dObject.has("act_list") && !dObject.isNull("act_list")) {
                            List<ActivityInfo> list = new Gson().fromJson(dObject.getJSONArray("act_list").toString(), new TypeToken<List<ActivityInfo>>() {
                            }.getType());
                            totalPage = dObject.getInt("max_page");
                            if (list != null) {
                                mData.addAll(list);
                            }
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
        //加载下一页
        getData();
    }

    @Override
    void refreshData() {
        //下拉刷新
        mData.clear();
        getData();
    }


}

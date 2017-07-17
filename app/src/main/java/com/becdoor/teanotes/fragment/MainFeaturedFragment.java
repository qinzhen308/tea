package com.becdoor.teanotes.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.CateAdapter;
import com.becdoor.teanotes.adapter.FeaturedAdapter;
import com.becdoor.teanotes.model.CateInfo;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.until.NetUtil;
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
 * Created by Administrator on 2016/10/19.
 * 首页精选 选项卡
 */

public class MainFeaturedFragment extends BaseRecyclerViewFragment {
    private ArrayList<NoteReInfoBean> mData;
    private FeaturedAdapter mAdapter;

    private String cat_id = "";
    private View view;
    private MainFeaturwListener mainFeaturwListener;
    private CateAdapter cateAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_featured, null);
            initView(view);
        } else {
            ViewGroup group = (ViewGroup) view.getParent();
            if (group != null) {
                group.removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!TextUtils.isEmpty(getArguments().getString("cat_id"))) {
            cat_id = getArguments().getString("cat_id", "");
        }
        mData = new ArrayList<>();
        mAdapter.setData(mData);
        cateAdapter = new CateAdapter(getActivity());
        getData();
    }

    @Override
    void onRightItemClick(Object object, int position) {
        super.onRightItemClick(object, position);
        if (object != null) {
            cat_id = ((CateInfo) object).getCat_id();
            if (mainFeaturwListener != null) {
                mainFeaturwListener.setRightView(((CateInfo) object).getCat_name());
            }
            mData.clear();
            getData();
        }
    }

    /**
     * create
     *
     * @param cat_id 笔记分类
     * @return
     */
    public static MainFeaturedFragment newInstance(String cat_id) {

        MainFeaturedFragment mainFeaturedFragment = new MainFeaturedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cat_id", cat_id);
        mainFeaturedFragment.setArguments(bundle);
        return mainFeaturedFragment;
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_widget);
        mRecyclerView = (RBCallbkRecyclerView) view.findViewById(R.id.recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FeaturedAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }


    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("cat_id", cat_id);

        setPostFormBuilder(Constant.BASE_URL + "index.php?", map, Constant.HOME_NOTE, new INetCallBackListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onScuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        List<CateInfo> mCateList = new Gson().fromJson(object.getJSONObject("data").getJSONArray("cate_list").toString(), new TypeToken<List<CateInfo>>() {
                        }.getType());
                        cateAdapter.setmList(mCateList);

                        List<NoteReInfoBean> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("noteRe_info").toString(), new TypeToken<List<NoteReInfoBean>>() {
                        }.getType());

                        totalPage = object.getJSONObject("data").getInt("max_page");
                        if (list != null) {
                            mData.addAll(list);
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

    /**
     * 显示右边的分类
     *
     * @param view
     */
    public void showRightCate(View view) {
        showRightWindow(view, 0, 0, cateAdapter);
    }

    public void setMainFeaturwListener(MainFeaturwListener mainFeaturwListener) {
        this.mainFeaturwListener = mainFeaturwListener;
    }

    public interface MainFeaturwListener {
        void setRightView(String content);
    }
}



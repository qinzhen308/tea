package com.becdoor.teanotes.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.CollectionActivity;
import com.becdoor.teanotes.adapter.CollectionActiveAdapter;
import com.becdoor.teanotes.adapter.CollectionAtticleAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.ActivityInfo;
import com.becdoor.teanotes.model.AtticleCate;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.until.DialogUtil;
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

import okhttp3.Call;

/**
 * Created by paulz on 2016/10/19.
 * 收藏--活动
 */

public class CollectionActiveFragment extends BaseRecyclerViewFragment implements View.OnClickListener{
    private ArrayList<ActivityInfo> mData;
    private CollectionActiveAdapter mAdapter;

    private View view;

    private Dialog loadDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDialog=DialogUtil.getLoadingDialog(getActivity());
        setExtra();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_collection_article, null);
            initView(view);
            setListener();
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
        mData = new ArrayList<>();
        mAdapter.setData(mData);
        getData();
    }

    private void setExtra(){
        Bundle bundle=getArguments();
        if(bundle!=null){
        }
    }


    @Override
    public void onClick(View v) {

    }

    /**
     *
     * @return
     */
    public static CollectionActiveFragment newInstance() {
        CollectionActiveFragment mainFeaturedFragment = new CollectionActiveFragment();
        Bundle bundle = new Bundle();
        mainFeaturedFragment.setArguments(bundle);
        return mainFeaturedFragment;
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_widget);
        mRecyclerView = (RBCallbkRecyclerView) view.findViewById(R.id.recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CollectionActiveAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void setListener(){
        mAdapter.setOnItemOperateListener(new CollectionActiveAdapter.OnItemOperateListener(){
            @Override
            public void onDelete(ActivityInfo act) {
                delete(act.getAct_id());
            }
        });
    }


    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("op", "acts");
        setPostFormBuilder(Constant.BASE_URL + "center.php?", map, "col_manage",new INetCallBackListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onScuccess(String response) {
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        List<ActivityInfo> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("acts_list").toString(), new TypeToken<List<ActivityInfo>>() {
                        }.getType());
                        totalPage = object.getJSONObject("data").getInt("max_page");
                        if (list != null) {
                            mData.addAll(list);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }



    private void delete(String id){
        DialogUtil.showDialog(loadDialog);
        HashMap<String ,String> params=new HashMap<>();
        params.put("aid",id);
        params.put("op","acts");
        NetUtil.postData(getActivity(), Constant.BASE_URL + "center.php?", params, "abolish_col", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
                try {
                    JSONObject obj=new JSONObject(response);
                    if(obj.optInt("status")==200){
                        Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_LONG).show();
                        mSwipeRefreshLayout.setRefreshing(true);
                        refreshData();
                    }else {
                        Toast.makeText(getActivity(),obj.optString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    public void doRefresh(){
        if(view==null)return;
        if(!mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(true);
            refreshData();
        }
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
        curPage=1;
        getData();
    }
}



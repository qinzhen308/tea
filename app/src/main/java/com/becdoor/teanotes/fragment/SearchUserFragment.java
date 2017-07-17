package com.becdoor.teanotes.fragment;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.MyFriendsAdapter;
import com.becdoor.teanotes.adapter.SearchUserAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.Friend;
import com.becdoor.teanotes.model.SearchUser;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by paulz on 2016/10/19.
 * 我的好友
 * 关注与粉丝
 */

public class SearchUserFragment extends BaseRecyclerViewFragment implements View.OnClickListener{
    private ArrayList<SearchUser> mData;
    private SearchUserAdapter mAdapter;

    private View view;

    private Dialog loadDialog;

    private String cate_id;

    public static final int TAB_CARE=0;
    public static final int TAB_FANS=1;
    private int tab;

    public static final int MODE_EDIT=1;
    public static final int MODE_NORMAL=0;

    private int mode;

    private View manageLayout;
    private Button btnCancelCare;
    private String keywords;

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
            view = inflater.inflate(R.layout.fragment_my_friends, null);
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
            keywords=bundle.getString("keywords");
        }
    }


    @Override
    public void onClick(View v) {


    }


    public void switchMode(){
        if(mode==MODE_EDIT){
            mode=MODE_NORMAL;
            manageLayout.setVisibility(View.GONE);
        }else if(mode==MODE_NORMAL){
            mode=MODE_EDIT;
            manageLayout.setVisibility(View.VISIBLE);
        }
        mAdapter.setMode(mode);
        mAdapter.notifyDataSetChanged();
    }



    /**
     *
     * @return
     */
    public static SearchUserFragment newInstance(String keywords) {
        SearchUserFragment mainFeaturedFragment = new SearchUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keywords",keywords);
        mainFeaturedFragment.setArguments(bundle);
        return mainFeaturedFragment;
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_widget);
        btnCancelCare = (Button) view.findViewById(R.id.delete_btn);
        manageLayout = view.findViewById(R.id.delete_layout);
        mRecyclerView = (RBCallbkRecyclerView) view.findViewById(R.id.recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int space=getResources().getDimensionPixelSize(R.dimen.margin_5);
                outRect.set(0,0,0,space);
            }
        });
        mAdapter = new SearchUserAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void setListener(){
        btnCancelCare.setOnClickListener(this);

    }


    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("op", tab==TAB_FANS?"fans":"attent");
        map.put("type", "user");
        map.put("keywords", keywords);
        setPostFormBuilder(Constant.BASE_URL + "search.php?", map, "doSearch",new INetCallBackListener() {
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
                        List<SearchUser> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("search_res_list").toString(), new TypeToken<List<SearchUser>>() {
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



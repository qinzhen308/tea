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
import com.becdoor.teanotes.activity.SearchResultActivity;
import com.becdoor.teanotes.adapter.CollectionAtticleAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
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
 * 收藏--茶志
 */

public class SearchArticleFragment extends BaseRecyclerViewFragment implements View.OnClickListener{
    private ArrayList<NoteReInfoBean> mData;
    private CollectionAtticleAdapter mAdapter;

    private View view;

    private Dialog loadDialog;

    private String cate_id;

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
            keywords=bundle.getString("keywords");
        }
    }


    @Override
    public void onClick(View v) {

    }





    /**
     *
     * @return
     */
    public static SearchArticleFragment newInstance(String keywords) {
        SearchArticleFragment mainFeaturedFragment = new SearchArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keywords",keywords);
        mainFeaturedFragment.setArguments(bundle);
        return mainFeaturedFragment;
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_widget);
        mRecyclerView = (RBCallbkRecyclerView) view.findViewById(R.id.recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CollectionAtticleAdapter(getActivity());
        mAdapter.setFrom(true);
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void setListener(){

    }


    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("cat_id", TextUtils.isEmpty(cate_id)?"0":cate_id);
        map.put("type", "note");
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
                        String cateStr=object.getJSONObject("data").getString("cate_list");
                        tryUpdateCate(cateStr);
                        List<NoteReInfoBean> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("search_res_list").toString(), new TypeToken<List<NoteReInfoBean>>() {
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

    String mCateListStr="";
    private void tryUpdateCate(String cateListStr){
        if(!mCateListStr.equals(cateListStr)){
            if(cateListStr!=null){
                List<AtticleCate> list=new Gson().fromJson(cateListStr,new TypeToken<List<AtticleCate>>(){}.getType());
                ((SearchResultActivity)getActivity()).updateCate(list);
            }else {
                ((SearchResultActivity)getActivity()).updateCate(null);
            }
            mCateListStr=cateListStr;
        }
    }


    public void setCate(String cate_id){
        this.cate_id=cate_id;
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



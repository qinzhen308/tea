package com.becdoor.teanotes.fragment;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.CollectionActivity;
import com.becdoor.teanotes.adapter.CollectionAtticleAdapter;
import com.becdoor.teanotes.adapter.MyFriendsAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.AtticleCate;
import com.becdoor.teanotes.model.Friend;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.until.AppUtil;
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

public class MyFriendsFragment extends BaseRecyclerViewFragment implements View.OnClickListener{
    private ArrayList<Friend> mData;
    private MyFriendsAdapter mAdapter;

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
            tab=bundle.getInt("tab",TAB_CARE);
        }
    }


    @Override
    public void onClick(View v) {
        if(v==btnCancelCare){
            cancelCare();
        }

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
    public static MyFriendsFragment newInstance(int tab) {
        MyFriendsFragment mainFeaturedFragment = new MyFriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab",tab);
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
        mAdapter = new MyFriendsAdapter(getActivity());
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
        setPostFormBuilder(Constant.BASE_URL + "center.php?", map, "fri_manage",new INetCallBackListener() {
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
                        List<Friend> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("f_list").toString(), new TypeToken<List<Friend>>() {
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


    private void cancelCare(){
        List<Friend> friends=mAdapter.getSelecteds();
        if(friends==null||friends.size()==0){
            Toast.makeText(getActivity(),"请选择",Toast.LENGTH_LONG).show();
            return;
        }
        DialogUtil.showDialog(loadDialog);
        JSONArray ids=new JSONArray();
            try {
                for(int i=0;i<friends.size();i++){
                    ids.put(i,friends.get(i).f_tomid);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        HashMap<String ,String> params=new HashMap<>();
        for(int i=0;i<friends.size();i++){
            params.put("user_ids["+i+"]",friends.get(i).f_tomid);
        }
//        params.put("user_ids",ids.toString());
        NetUtil.postData(getActivity(), Constant.BASE_URL + "center.php?", params, "abolish_att", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getActivity(),"取消失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
                try {
                    JSONObject obj=new JSONObject(response);
                    if(obj.optInt("status")==200){
                        Toast.makeText(getActivity(),"取消成功",Toast.LENGTH_LONG).show();
                        mSwipeRefreshLayout.setRefreshing(true);
                        refreshData();
                    }else {
                        Toast.makeText(getActivity(),obj.optString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"取消失败",Toast.LENGTH_LONG).show();
                }

            }
        });

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



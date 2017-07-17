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
import com.becdoor.teanotes.adapter.CommentAdapter;
import com.becdoor.teanotes.adapter.MyFriendsAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.ActComment;
import com.becdoor.teanotes.model.Comment;
import com.becdoor.teanotes.model.Friend;
import com.becdoor.teanotes.model.NoteComment;
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
 * 评论管理
 */

public class CommentFragment extends BaseRecyclerViewFragment implements View.OnClickListener{
    private ArrayList<Comment> mData;
    private CommentAdapter mAdapter;

    private View view;

    private Dialog loadDialog;

    private String mType="note";

    public static final int TAB_MINE=0;
    public static final int TAB_TO_ME=1;
    private int tab;


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
            view = inflater.inflate(R.layout.fragment_comment, null);
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
        mAdapter.setTab(tab);
        mAdapter.setData(mData);
        getData();
    }

    private void setExtra(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            tab=bundle.getInt("tab",TAB_MINE);
        }
    }


    @Override
    public void onClick(View v) {


    }


    /**
     *
     * @return
     */
    public static CommentFragment newInstance(int tab) {
        CommentFragment mainFeaturedFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab",tab);
        mainFeaturedFragment.setArguments(bundle);
        return mainFeaturedFragment;
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_widget);
        mRecyclerView = (RBCallbkRecyclerView) view.findViewById(R.id.recycleview_view);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int space=getResources().getDimensionPixelSize(R.dimen.margin_5);
                outRect.set(0,space,0,space);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CommentAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void setListener(){
        mAdapter.setOnDeleteListener(new CommentAdapter.OnDeleteListener() {
            @Override
            public void onDelete(Comment comment) {
                delete(comment);
            }
        });

    }


    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("op", tab==TAB_TO_ME?"otherComm":"myComm");
        map.put("type", mType);
        setPostFormBuilder(Constant.BASE_URL + "center.php?", map, "com_manage",new INetCallBackListener() {
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
                        List<Comment> list=null;
                        if(mType.equals("act")){
                            list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("data").toString(), new TypeToken<List<ActComment>>() {}.getType());
                        }else {
                            list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("data").toString(), new TypeToken<List<NoteComment>>() {}.getType());
                        }
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


    private void delete(Comment comment){
        DialogUtil.showDialog(loadDialog);
        HashMap<String ,String> params=new HashMap<>();
        params.put("comm_id",comment.getCommentId());
        params.put("type",comment.type);
        NetUtil.postData(getActivity(), Constant.BASE_URL + "center.php?", params, "delComm", new NetUtil.NetUtilCallBack() {
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

    public void setType(String type){
        if(type.equals(mType))return;
        this.mType=type;
        doRefresh();
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



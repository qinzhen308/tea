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
import android.widget.Button;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.TeaAtticleManagerActivity;
import com.becdoor.teanotes.adapter.FeaturedAdapter;
import com.becdoor.teanotes.adapter.TeaAtticleAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.Article;
import com.becdoor.teanotes.model.ArticleBean;
import com.becdoor.teanotes.model.AtticleCate;
import com.becdoor.teanotes.model.CollectedTeaWrapper;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.model.SimpleNoteWrapper;
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
 * Created by Administrator on 2016/10/19.
 * 茶志管理---茶志列表
 * 分 草稿，已发布，隐藏三个类别
 * 有编辑和浏览两种模式
 */

public class TeaAtticleFragment extends BaseRecyclerViewFragment implements View.OnClickListener{
    private ArrayList<NoteReInfoBean> mData;
    private TeaAtticleAdapter mAdapter;

    protected View view;

    protected View layoutManage;
    protected Button btnPublish;
    protected Button btnDelete;

    public static final int MODE_EDIT=1;
    public static final int MODE_NORMAL=0;

    public static final int TAB_NORMAL=0;
    public static final int TAB_HIDE=1;
    public static final int TAB_DRAFT=2;

    protected int tab;
    protected int mode;
    protected Dialog loadDialog;

    protected String cate_id;

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
            view = inflater.inflate(R.layout.fragment_tea_atticle_list, null);
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
        if(tab!=TAB_DRAFT){
            mData = new ArrayList<>();
            mAdapter.setData(mData);
            getData();
        }
    }

    protected void setExtra(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            tab=bundle.getInt("tab");
        }
    }


    public void switchMode(boolean immediate){
        if(mode==MODE_EDIT){
            mode=MODE_NORMAL;
            layoutManage.setVisibility(View.GONE);
        }else if(mode==MODE_NORMAL){
            mode=MODE_EDIT;
            layoutManage.setVisibility(View.VISIBLE);
        }
        mAdapter.setMode(mode);
        if(immediate){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btnPublish){
            batchOperate();
        }else if(v==btnDelete){
            delete();
        }
    }

    public void batchOperate(){
        if(tab==TAB_NORMAL){
            showOrHide(null,"hide");
        }else if(tab==TAB_HIDE){
            showOrHide(null,"show");
        }else if(tab==TAB_DRAFT){
        }
    }



    /**
     *
     * @return
     */
    public static TeaAtticleFragment newInstance(int tab) {
        TeaAtticleFragment mainFeaturedFragment = new TeaAtticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        mainFeaturedFragment.setArguments(bundle);
        return mainFeaturedFragment;
    }

    protected void initView(View view) {
        layoutManage = view.findViewById(R.id.delete_layout);
        btnDelete = (Button) view.findViewById(R.id.delete_btn);
        btnPublish = (Button) view.findViewById(R.id.publish_btn);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_widget);
        mRecyclerView = (RBCallbkRecyclerView) view.findViewById(R.id.recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TeaAtticleAdapter(getActivity());
        mAdapter.setTab(tab);
        mAdapter.setMode(mode);
        mRecyclerView.setAdapter(mAdapter);
        initListener();
        if(tab==TAB_NORMAL){
            btnPublish.setText("隐藏");
        }else if(tab==TAB_HIDE){
            btnPublish.setText("显示");
        }else if(tab==TAB_DRAFT){
            btnPublish.setText("发布");
        }
    }

    protected void setListener(){
        btnPublish.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        mAdapter.setOnItemOperateListener(new TeaAtticleAdapter.OnItemOperateListener(){
            @Override
            public void onShow(NoteReInfoBean note) {
                showOrHide(note,"show");
            }

            @Override
            public void onPublish(NoteReInfoBean note) {
            }
        });
    }


    String[] ops={"published","hideNote","drafts"};
    /**
     * 获取数据
     */
    protected void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("cat_id", TextUtils.isEmpty(cate_id)?"0":cate_id);
        map.put("op", ops[tab]);

        setPostFormBuilder(Constant.BASE_URL + "center.php?", map, "noteManage",new INetCallBackListener() {
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
                        List<NoteReInfoBean> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("noteRe_info").toString(), new TypeToken<List<NoteReInfoBean>>() {
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
    protected void tryUpdateCate(String cateListStr){
        if(!mCateListStr.equals(cateListStr)){
            if(cateListStr!=null){
                List<AtticleCate> list=new Gson().fromJson(cateListStr,new TypeToken<List<AtticleCate>>(){}.getType());
                ((TeaAtticleManagerActivity)getActivity()).updateCate(list);
            }else {
                ((TeaAtticleManagerActivity)getActivity()).updateCate(null);
            }
            mCateListStr=cateListStr;
        }
    }

   /* protected void publish(NoteReInfoBean singleOne){
        List<NoteReInfoBean> seleteds=null;
        if(singleOne==null){
            seleteds=mAdapter.getSelectedAtticles();
            if(seleteds==null||seleteds.isEmpty()){
                Toast.makeText(getActivity(),"请选择",Toast.LENGTH_LONG).show();
                return;
            }
        }else {
            seleteds=new ArrayList<>();
            seleteds.add(singleOne);
        }
        DialogUtil.showDialog(loadDialog);
        HashMap<String ,String> params=new HashMap<>();
        JSONArray data=new JSONArray();
        for(int i=0;i<seleteds.size();i++){
            ArticleBean att=seleteds.get(i).getArticle();
//            att.getContent()
        }

        params.put("data",data.toString());
        NetUtil.postData(getActivity(), Constant.BASE_URL + "note_manage.php?", params, "delNote", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getActivity(),"发布失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
                try {
                    JSONObject obj=new JSONObject(response);
                    if(obj.optInt("status")==200){
                        Toast.makeText(getActivity(),"发布成功",Toast.LENGTH_LONG).show();
                        mSwipeRefreshLayout.setRefreshing(true);
                        refreshData();
                    }else {
                        Toast.makeText(getActivity(),obj.optString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"发布失败",Toast.LENGTH_LONG).show();
                }

            }
        });

    }*/

    protected void showOrHide(NoteReInfoBean singleOne,String op){
        List<NoteReInfoBean> seleteds=null;
        if(singleOne==null){
            seleteds=mAdapter.getSelectedAtticles();
            if(seleteds==null||seleteds.isEmpty()){
                Toast.makeText(getActivity(),"请选择",Toast.LENGTH_LONG).show();
                return;
            }
        }else{
            seleteds=new ArrayList<>();
            seleteds.add(singleOne);
        }

        DialogUtil.showDialog(loadDialog);
        HashMap<String ,String> params=new HashMap<>();
        String ids="(";
        for(int i=0,size=seleteds.size();i<size;i++){
            ids+=seleteds.get(i).getArticle().getArticle_id()+",";
        }
        ids=ids.substring(0,ids.length()-1)+")";
        params.put("aid",ids);
        params.put("op",op);
        NetUtil.postData(getActivity(), Constant.BASE_URL + "note_manage.php?", params, "toggle_show", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getActivity(),"设置失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
                try {
                    JSONObject obj=new JSONObject(response);
                    if(obj.optInt("status")==200){
                        Toast.makeText(getActivity(),"设置成功",Toast.LENGTH_LONG).show();
                        mSwipeRefreshLayout.setRefreshing(true);
                        refreshData();
                    }else {
                        Toast.makeText(getActivity(),obj.optString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"设置失败",Toast.LENGTH_LONG).show();
                }

            }
        });
    }



    protected void delete(){
        List<NoteReInfoBean> seleteds=mAdapter.getSelectedAtticles();
        if(seleteds==null||seleteds.isEmpty()){
            Toast.makeText(getActivity(),"请选择",Toast.LENGTH_LONG).show();
            return;
        }
        DialogUtil.showDialog(loadDialog);
        HashMap<String ,String> params=new HashMap<>();
        String ids="(";
        for(int i=0,size=seleteds.size();i<size;i++){
            ids+=seleteds.get(i).getArticle().getArticle_id()+",";
        }
        ids=ids.substring(0,ids.length()-1)+")";
        params.put("aid",ids);
        NetUtil.postData(getActivity(), Constant.BASE_URL + "note_manage.php?", params, "delNote", new NetUtil.NetUtilCallBack() {
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



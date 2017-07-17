package com.becdoor.teanotes.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.TeaAtticleManagerActivity;
import com.becdoor.teanotes.adapter.TeaAtticleAdapter;
import com.becdoor.teanotes.adapter.TeaAtticleOfflineAdapter;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.Article;
import com.becdoor.teanotes.model.ArticleBean;
import com.becdoor.teanotes.model.AtticleCate;
import com.becdoor.teanotes.model.NoteDetailBean;
import com.becdoor.teanotes.model.NoteElement;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.tables.ArticleTable;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.ImageUploader;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class TeaAtticleOfflineFragment extends TeaAtticleFragment{
    private TeaAtticleOfflineAdapter mAdapter;

    private List<NoteDetailBean> mData=new ArrayList<>();

    @Override
    public void batchOperate(){
        checkAndPublish(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mData = new ArrayList<>();
        mAdapter.setData(mData);
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    /**
     *
     * @return
     */
    public static TeaAtticleOfflineFragment newInstance(int tab) {
        TeaAtticleOfflineFragment mainFeaturedFragment = new TeaAtticleOfflineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        mainFeaturedFragment.setArguments(bundle);
        return mainFeaturedFragment;
    }

    @Override
    protected void initView(View view) {
        layoutManage = view.findViewById(R.id.delete_layout);
        btnDelete = (Button) view.findViewById(R.id.delete_btn);
        btnPublish = (Button) view.findViewById(R.id.publish_btn);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh_widget);
        mRecyclerView = (RBCallbkRecyclerView) view.findViewById(R.id.recycleview_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TeaAtticleOfflineAdapter(getActivity());
        mAdapter.setTab(tab);
        mAdapter.setMode(mode);
        mRecyclerView.setAdapter(mAdapter);
        btnPublish.setText("发布");
        if(mode==MODE_NORMAL){
            layoutManage.setVisibility(View.GONE);
        }else {
            layoutManage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void switchMode(boolean immediate) {
        if(mode==MODE_EDIT){
            mode=MODE_NORMAL;
            if(layoutManage!=null) layoutManage.setVisibility(View.GONE);
        }else if(mode==MODE_NORMAL){
            mode=MODE_EDIT;
            if(layoutManage!=null) layoutManage.setVisibility(View.VISIBLE);
        }
        if(mAdapter!=null){
            mAdapter.setMode(mode);
            if(immediate){
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void setListener(){
        btnPublish.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        mAdapter.setOnItemOperateListener(new TeaAtticleOfflineAdapter.OnItemOperateListener(){
            @Override
            public void onShow(NoteDetailBean note) {

            }

            @Override
            public void onPublish(NoteDetailBean note) {
                checkAndPublish(note);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    @Override
    protected void delete(){
        final List<NoteDetailBean> selecteds= mAdapter.getSelectedAtticles();
        if(selecteds.size()==0){
            Toast.makeText(getActivity(),"请选择",Toast.LENGTH_LONG).show();
            return;
        }
        DialogUtil.showDialog(loadDialog);
        new Thread() {
            @Override
            public void run() {
                final boolean isSuc=ArticleTable.getInstance().delete(selecteds);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuc) {
                            mData.removeAll(selecteds);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_LONG).show();
                        }
                        if (!getActivity().isFinishing()) DialogUtil.dismissDialog(loadDialog);
                    }
                });

            }
        }.start();
    }

    protected void publish(final List<NoteDetailBean> seleteds){

        HashMap<String ,String> params=new HashMap<>();
        JSONArray data=new JSONArray();
        try {
            for(int i=0;i<seleteds.size();i++){
                NoteDetailBean item=seleteds.get(i);
                JSONObject obj=new JSONObject();
                obj.put("title",item.getTitle());
                obj.put("pid","9");
                obj.put("cover",item.getImg());
                obj.put("cat_id",item.getCat_id());
                obj.put("privacy",item.getIs_priv());
                if(item.getArt_arr()==null){
                    obj.put("art_arr",new JSONArray());
                }else {
                    obj.put("art_arr",new JSONArray(new Gson().toJson(item.getArt_arr())));
                }
                data.put(i,obj);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(),"发布失败",Toast.LENGTH_LONG).show();
            if(!getActivity().isFinishing())DialogUtil.dismissDialog(loadDialog);
            return;
        }

        params.put("data",data.toString());
        NetUtil.postData(getActivity(), Constant.BASE_URL + "note_manage.php?", params, "batch_pub", new NetUtil.NetUtilCallBack() {
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
                        ArticleTable.getInstance().delete(seleteds);
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
    }

    protected void checkAndPublish(NoteDetailBean singleOne){
        final List<NoteDetailBean> seleteds=new ArrayList<>();
        if(singleOne==null){
            seleteds.addAll(mAdapter.getSelectedAtticles());
            if(seleteds==null||seleteds.isEmpty()){
                Toast.makeText(getActivity(),"请选择",Toast.LENGTH_LONG).show();
                return;
            }
        }else {
            seleteds.add(singleOne);
        }
        DialogUtil.showDialog(loadDialog);
        if(upLoadNativeCoverImages(seleteds)){
            return;
        }
        publish(seleteds);
    }


    public List<NoteElement> getNativeImgItems(List<NoteDetailBean> source){
        List<NoteElement> list=new ArrayList<>();
        for(int i=0,size=source.size();i<size;i++){
            NoteDetailBean n=source.get(i);
            List<NoteElement> eList=n.getArt_arr();
            if(eList==null)continue;
            for(int j=0,size2=eList.size();j<size2;j++) {
                NoteElement e=eList.get(j);
                if(e.isNative()){
                    list.add(e);
                }
            }
        }
        return list;
    }


    /**
     *
     * @return 是否需要上传
     */
    private boolean upLoadNativeImages(final List<NoteDetailBean> notes) {
        final List<NoteElement> list=getNativeImgItems(notes);
        if(list==null||list.isEmpty()){
            return false;
        }
        final List<File> files=new ArrayList<>();
        for(final NoteElement e:list){
            files.add(new File(e.getEnableValue()));
            Log.d("uploads","up--start--files----"+e.getEnableValue());
        }
        ImageUploader.uploadBatch(getActivity(), files, new ImageUploader.Callback() {
            @Override
            public void onSuccess(String url) {
                Map<String ,String > urls=new Gson().fromJson(url,new TypeToken<Map<String, String>>() {}.getType());
                for(int i=0 ;i<list.size();i++){
                    Log.d("uploads","up---maps------"+urls.toString());
                    String u=urls.get(files.get(i).getName().split("\\.")[0]);
                    Log.d("uploads","up---url---"+i+"----"+u);
                    if(u.startsWith("/")){
                        u=u.substring(1,u.length());
                    }
                    list.get(i).setEnableValue(u,false);
                }
                publish(notes);
            }

            @Override
            public void onFailed(String msg) {
                if (!getActivity().isFinishing()) DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
        return true;
    }

    /**
     *
     * @return 是否需要上传
     */
    private boolean upLoadNativeCoverImages(final List<NoteDetailBean> notes) {
        final List<NoteDetailBean> needsUploadList=new ArrayList<>();
        for(int i=0,size=notes.size();i<size;i++){
            NoteDetailBean n=notes.get(i);
            if(n.isNative){
                needsUploadList.add(n);
            }
        }
        if(needsUploadList==null||needsUploadList.isEmpty()){
            return upLoadNativeImages(notes);
        }
        final List<File> files=new ArrayList<>();
        for(final NoteDetailBean n:needsUploadList){
            files.add(new File(n.getImg()));
        }
        ImageUploader.uploadBatch(getActivity(), files, new ImageUploader.Callback() {
            @Override
            public void onSuccess(String url) {
                Map<String ,String > urls=new Gson().fromJson(url,new TypeToken<Map<String, String>>() {}.getType());
                for(int i=0 ;i<needsUploadList.size();i++){
                    String u=urls.get(files.get(i).getName().split("\\.")[0]);
                    Log.d("uploads","up---cover--url---"+i+"----"+u);
                    if(u.startsWith("/")){
                        u=u.substring(1,u.length());
                    }
                    needsUploadList.get(i).setImg(u);
                }
                if(upLoadNativeImages(notes)){
                    return;
                }
                publish(notes);
            }

            @Override
            public void onFailed(String msg) {
                if (!getActivity().isFinishing()) DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
        return true;
    }


    @Override
    protected void getData() {
        new Thread() {
            @Override
            public void run() {
                if(!TextUtils.isEmpty(cate_id)&&Integer.valueOf(cate_id)>0){
                    mData=ArticleTable.getInstance().getListByCateId(Integer.valueOf(cate_id));
                }else {
                    mData=ArticleTable.getInstance().getList();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.setData(mData);
                        mAdapter.notifyDataSetChanged();
                        if(mData.size()==0){
                            Toast.makeText(getActivity(),"暂无数据",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }.start();

    }

    @Override
    void refreshData() {
        getData();
    }

    @Override
    void loadNextPage() {

    }
}



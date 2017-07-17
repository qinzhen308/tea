package com.becdoor.teanotes.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.NoteAdapter;
import com.becdoor.teanotes.adapter.TeaCollectAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.model.CollectedTeaWrapper;
import com.becdoor.teanotes.model.SimpleNoteWrapper;
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
 * Created by Administrator on 2016/10/19.
 * 便签管理---茶志列表
 * 分 草稿，已发布，隐藏三个类别
 * 有编辑和浏览两种模式
 */

public class TeaCollectActivity extends BaseRecyclerViewActivity {
    private ArrayList<CollectedTeaWrapper.CollectedTea> mData;
    private TeaCollectAdapter mAdapter;

    private String cat_id = "";

    public static final int MODE_EDIT=1;
    public static final int MODE_NORMAL=0;

    private int mode;

    private ImageView btnManage;
    private ImageView btnEdit;
    private View btnBack;
    private Button btnDelete;
    private View layoutDelete;
    private Dialog loadDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExtra();
        setContentView(R.layout.activity_tea_collect);
        initView();
        mData = new ArrayList<>();
        mAdapter.setData(mData);
        setListener();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setListener() {
        btnManage.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }


    private void setExtra(){

    }

    public void switchMode(boolean immediate){
        if(mode==1){
            mode=0;
            layoutDelete.setVisibility(View.GONE);
            btnManage.setImageResource(R.drawable.setting_icon_unchecked);
        }else if(mode==0){
            mode=1;
            layoutDelete.setVisibility(View.VISIBLE);
            btnManage.setImageResource(R.drawable.setting_icon_checked);
        }
        mAdapter.setMode(mode);
        if(immediate){
            mAdapter.notifyDataSetChanged();
        }
    }



    private void initView() {
        loadDialog=DialogUtil.getLoadingDialog(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_refresh_widget);
        mRecyclerView = (RBCallbkRecyclerView) findViewById(R.id.recycleview_view);
        btnManage = (ImageView) findViewById(R.id.setting_btn);
        btnEdit = (ImageView) findViewById(R.id.edit_my_notes_btn);
        btnBack =  findViewById(R.id.top_back_btn);
        layoutDelete =  findViewById(R.id.delete_layout);
        btnDelete = (Button) findViewById(R.id.delete_btn);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(10,10,10,10);
            }

        });
        mAdapter = new TeaCollectAdapter(this);
        mAdapter.setMode(mode);
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    @Override
    public void onClick(View v) {
        if(v==btnBack){
            finish();
        }else if(v==btnDelete){
            delete();
        }else if(v==btnEdit){
            CreateCollectTeaActivity.invoke(this);
        }else if(v==btnManage){
            switchMode(true);
        }else {
            super.onClick(v);
        }
    }

    private void delete(){
        List<CollectedTeaWrapper.CollectedTea> seleteds=mAdapter.getSelectedAtticles();
        if(seleteds==null||seleteds.isEmpty()){
            Toast.makeText(getApplicationContext(),"请选择",Toast.LENGTH_LONG).show();
            return;
        }
        DialogUtil.showDialog(loadDialog);
        HashMap<String ,String> params=new HashMap<>();
        String ids="(";
        for(int i=0,size=seleteds.size();i<size;i++){
            ids+=seleteds.get(i).article_id+",";
        }
        ids=ids.substring(0,ids.length()-1)+")";
        params.put("aid",ids);
        NetUtil.postData(this, Constant.BASE_URL + "note_manage.php?", params, "delNote", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if(!isFinishing())DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getApplicationContext(),"删除失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if(!isFinishing())DialogUtil.dismissDialog(loadDialog);
                try {
                    JSONObject obj=new JSONObject(response);
                    if(obj.optInt("status")==200){
                        Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                        refreshData();
                    }else {
                        Toast.makeText(getApplicationContext(),obj.optString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"删除失败",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    /**
     * 获取数据
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        setPostFormBuilder(Constant.BASE_URL + "index.php?", map, "storage",new INetCallBackListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onScuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {

                        List<CollectedTeaWrapper.CollectedTea> list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("note_list").toString(), new TypeToken<List<CollectedTeaWrapper.CollectedTea>>() {
                        }.getType());
                        totalPage = object.getJSONObject("data").getInt("max_page");
                        if (list != null) {
                            mData.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK==resultCode){
            mSwipeRefreshLayout.setRefreshing(true);
            refreshData();
        }
    }

    public static void invoke(Context context){
        Intent intent=new Intent(context,TeaCollectActivity.class);
        context.startActivity(intent);
    }
}



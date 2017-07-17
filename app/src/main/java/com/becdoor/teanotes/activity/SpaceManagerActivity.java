package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.CateInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by jjj on 2016/11/10.
 * 空间管理
 */

public class SpaceManagerActivity extends TitleActivity {
    private String op;//控制参数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_spacemanager);
        setTitleForRightIamgeView("空间管理", 0, true, false);

        findViewById(R.id.spaceManager_dataTv).setOnClickListener(this);
        findViewById(R.id.spaceManager_privacyTv).setOnClickListener(this);
        findViewById(R.id.spaceManager_columnTv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.spaceManager_dataTv://资料
                op = "base";
                getDataForOP();
                break;
            case R.id.spaceManager_privacyTv://隐私
                op = "priv";
                getDataForOP();
                break;
            case R.id.spaceManager_columnTv://栏目
                op = "nav";
                getDataForOP();
                break;
        }
    }

    void getDataForOP() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("op", op);
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "spa_manage", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {

                        if (op.equals("base")) {
                            MyDataManagerActivity.inVoke(SpaceManagerActivity.this, object.getJSONObject("data").getString("name"), object.getJSONObject("data").optString("slogan"));
                        } else {
                            if (op.equals("priv")) {
                                PrivacySettingActivity.inVoke(SpaceManagerActivity.this, Integer.valueOf(object.getString("data")));
                            } else {
                                //栏目
                                List<CateInfo> cateInfos = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CateInfo>>() {
                                }.getType());
                                NavigationActivity.inVoke(SpaceManagerActivity.this, cateInfos);
                            }
                        }

                    } else {
                        Toast.makeText(SpaceManagerActivity.this, object == null ? "获取失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(SpaceManagerActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

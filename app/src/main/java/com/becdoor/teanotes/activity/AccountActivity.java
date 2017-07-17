package com.becdoor.teanotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.RankAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.RankInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/14.
 * 账号管理
 */

public class AccountActivity extends TitleActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_account);
        setTitleForRightIamgeView("账号管理", 0, true, false);
    }

    public void onAccountClick(View v) {
        switch (v.getId()) {
            case R.id.account_nomalSettingTv://基本设置
                startActivity(new Intent(this, MyInfoActivity.class));
                break;
            case R.id.account_safeSettingTv://安全设置
                startActivity(new Intent(this, SafeSettingActivity.class));
                break;
            case R.id.account_pointGadeTv://积分等级
                startActivity(new Intent(this, RankActivity.class));
                break;
            case R.id.account_jpushSettingTv://消息推送设置
                startActivity(new Intent(this, PushSettingActivity.class));
                break;
            case R.id.account_clearCacheTv://清除缓存
                clearCache();
                break;
            case R.id.account_logoutTv://退出
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    void clearCache() {
        DialogUtil.showDialog(mLoaDailog);
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", null, "clearCache", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(AccountActivity.this, "清除缓存成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AccountActivity.this, object == null ? "清除缓存失败！" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(AccountActivity.this, "清除缓存失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

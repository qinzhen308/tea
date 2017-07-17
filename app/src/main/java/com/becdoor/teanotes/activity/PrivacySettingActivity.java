package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.SpaceInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/10.
 * 隐私设置
 */

public class PrivacySettingActivity extends TitleActivity {
    @Bind(R.id.privacySetting_allTv)
    TextView mAllTv;
    @Bind(R.id.privacySetting_attentionTv)
    TextView mAttentionTv;
    @Bind(R.id.privacySetting_onlyMyselfTv)
    TextView mMyselfTv;
    private int priv = 1;//1为所有人可见，2为关注自己的好友可见，3为仅自己可见

    private Drawable drawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_privacysetting);
        setTitleForRightIamgeView("隐私设置", 0, true, false);
        ButterKnife.bind(this);

        mAllTv.setOnClickListener(this);
        mAttentionTv.setOnClickListener(this);
        mMyselfTv.setOnClickListener(this);

        drawable = getResources().getDrawable(R.drawable.icon_is_choose);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        priv = getIntent().getIntExtra("priv", 1);
        changeData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.privacySetting_allTv:
                if (priv == 1) {
                    return;
                }
                priv = 1;
                submitData();
                break;
            case R.id.privacySetting_attentionTv:
                if (priv == 2) {
                    return;
                }
                priv = 2;
                submitData();
                break;
            case R.id.privacySetting_onlyMyselfTv:
                if (priv == 3) {
                    return;
                }
                priv = 3;
                submitData();
                break;
        }
    }

    void submitData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("priv", String.valueOf(priv));
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "doPrivSet", new NetUtil.NetUtilCallBack() {
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
                        Toast.makeText(PrivacySettingActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                        changeData();
                    } else {
                        Toast.makeText(PrivacySettingActivity.this, object == null ? "修改失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(PrivacySettingActivity.this, "修改失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeData() {
        if (priv == 1) {
            mAllTv.setCompoundDrawables(null, null, drawable, null);
            mAttentionTv.setCompoundDrawables(null, null, null, null);
            mMyselfTv.setCompoundDrawables(null, null, null, null);
        } else if (priv == 2) {
            mAllTv.setCompoundDrawables(null, null, null, null);
            mAttentionTv.setCompoundDrawables(null, null, drawable, null);
            mMyselfTv.setCompoundDrawables(null, null, null, null);
        } else {
            mAllTv.setCompoundDrawables(null, null, null, null);
            mAttentionTv.setCompoundDrawables(null, null, null, null);
            mMyselfTv.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public static void inVoke(Context context, int priv) {
        Intent intent = new Intent(context, PrivacySettingActivity.class);
        intent.putExtra("priv", priv);
        context.startActivity(intent);
    }
}

package com.becdoor.teanotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
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
 * Created by jjj on 2016/11/11.
 * 安全设置
 */

public class SafeSettingActivity extends TitleActivity {
    @Bind(R.id.safeSetting_updatePWTv)
    TextView mPWTv;
    @Bind(R.id.safeSetting_phoneTv)
    TextView mPhoneTv;
    @Bind(R.id.safeSetting_updatePhoneTv)
    TextView mUpdatePhoneTv;
    @Bind(R.id.safeSetting_phoneStateTv)
    TextView mPhoneStateTv;
    @Bind(R.id.safeSetting_emailTv)
    TextView mEmailTv;
    @Bind(R.id.safeSetting_AuthenticationEmailTv)
    TextView mUpdateEmailTv;
    @Bind(R.id.safeSetting_emailStateTv)
    TextView mEmailStateTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_safesetting);
        setTitleForRightIamgeView("安全设置", 0, true, false);
        ButterKnife.bind(this);

        mPWTv.setOnClickListener(this);
        mUpdatePhoneTv.setOnClickListener(this);
        mUpdateEmailTv.setOnClickListener(this);
        getData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.safeSetting_updatePWTv:
                startActivity(new Intent(this, UpdatePWActivity.class));
                break;
            case R.id.safeSetting_updatePhoneTv:
                UpdatePhoneOrEmailActivity.inVoke(this, true);
                break;
            case R.id.safeSetting_AuthenticationEmailTv:
                UpdatePhoneOrEmailActivity.inVoke(this, false);
                break;
        }
    }

    void getData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("op", "save");
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "acc_manage", new NetUtil.NetUtilCallBack() {
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
                        JSONObject object1 = object.getJSONObject("data");
                        mPhoneTv.setText(object1.optString("telephone"));
                        mPhoneStateTv.setSelected(object1.optInt("phoneIsident") == 1);
                        mEmailTv.setText(object1.optString("email"));
                        mEmailStateTv.setSelected(object1.optInt("emailIsident") == 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

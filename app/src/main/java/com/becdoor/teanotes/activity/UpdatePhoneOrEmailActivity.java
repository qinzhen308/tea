package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
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
 * 手机 邮箱 修改
 */

public class UpdatePhoneOrEmailActivity extends TitleActivity {
    @Bind(R.id.updatePE_tipTv)
    TextView mTipTv;
    @Bind(R.id.updatePE_codeEdt)
    EditText mCodeEdt;
    @Bind(R.id.updatePE_verifyEdt)
    EditText mVerifyEdt;
    @Bind(R.id.updatePE_verifyGetTv)
    TextView mVerifyGetTv;
    @Bind(R.id.updatePE_okTv)
    TextView mOkBtn;
    public boolean isPhone = true;//是否是手机
    private String temp_access_token = "";
    private String code;

    private int time = 60;
    private TimeCount timeCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_updatephoneoremail);
        ButterKnife.bind(this);

        isPhone = getIntent().getBooleanExtra("isPhone", true);
        if (isPhone) {
            setTitleForRightIamgeView("手机号修改", 0, true, false);
        } else {
            setTitleForRightIamgeView("邮箱修改", 0, true, false);
            mTipTv.setVisibility(View.GONE);
            mCodeEdt.setHint("请输入邮箱");
        }
        mOkBtn.setOnClickListener(this);
        mVerifyGetTv.setOnClickListener(this);
        timeCount = new TimeCount(60000, 1000);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mVerifyGetTv) {
            code = mCodeEdt.getText().toString();
            if (TextUtils.isEmpty(code)) {
                if (isPhone) {
                    CustomToast.showToast(this, "请输入手机号码", 1000);
                } else {
                    CustomToast.showToast(this, "请输入邮箱号", 1000);
                }
                return;
            }
            timeCount.start();
            getVerify();
        } else if (v == mOkBtn) {
            String vCode = mVerifyEdt.getText().toString();
            if (TextUtils.isEmpty(vCode)) {
                CustomToast.showToast(this, "请输入验证码", 1000);
                return;
            }
            verify(vCode);
        }
    }

    /**
     * 获取验证码
     */
    void getVerify() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        if (isPhone) {
            map.put("op", "tel");
            map.put("telephone", code);
        } else {
            map.put("mail", code);
            map.put("op", "mail");
        }
        NetUtil.postData(this, Constant.BASE_URL + "acc_manage.php?", map, "sendCode", new NetUtil.NetUtilCallBack() {
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
                        temp_access_token = object.getJSONObject("data").getString("temp_access_token");
                        Toast.makeText(UpdatePhoneOrEmailActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdatePhoneOrEmailActivity.this, object == null ? "获取失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(UpdatePhoneOrEmailActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 验证
     */
    void verify(String vCode) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        if (isPhone) {
            map.put("op", "tel");
            map.put("telephone", code);
        } else {
            map.put("mail", code);
            map.put("op", "mail");
        }
        map.put("code", vCode);
        map.put("temp_access_token", temp_access_token);
        NetUtil.postData(this, Constant.BASE_URL + "acc_manage.php?", map, "verify", new NetUtil.NetUtilCallBack() {
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
                        Toast.makeText(UpdatePhoneOrEmailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdatePhoneOrEmailActivity.this, object == null ? "修改失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(UpdatePhoneOrEmailActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mVerifyGetTv.setEnabled(true);
            mVerifyGetTv.setText("获取验证码");
            time = 60;
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mVerifyGetTv.setEnabled(false);
            mVerifyGetTv.setText(time + "秒");
            time--;
        }
    }

    public static void inVoke(Context context, boolean isPhone) {
        Intent intent = new Intent(context, UpdatePhoneOrEmailActivity.class);
        intent.putExtra("isPhone", isPhone);
        context.startActivity(intent);
    }

}

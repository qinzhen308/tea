package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/14.
 * 忘记密码
 */

public class FindPWActivity extends TitleActivity {
    @Bind(R.id.findPw_tipTv)
    TextView mTipTv;
    @Bind(R.id.findPw_codeTv)
    TextView mCodeTv;
    @Bind(R.id.findPw_Btn)
    Button mBtn;
    @Bind(R.id.findPw_topEdt)
    EditText mTopEdt;
    @Bind(R.id.findPw_bottomEdt)
    EditText mBottomEdt;

    private String account;
    private boolean isTwo = true;//找回密码第二步
    private int time = 60;
    private TimeCount timeCount;
    private String temp_access_token = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_findpw);
        ButterKnife.bind(this);

        isTwo = getIntent().getBooleanExtra("isTwo", false);
        account = getIntent().getStringExtra("account");
        if (isTwo) {
            setTitleForRightIamgeView("填写新的密码", 0, true, false);
            mTipTv.setText("请填写新的密码");
            mTopEdt.setHint("输入新密码");
            mBottomEdt.setHint("确认新密码");
            mBtn.setText("完成");
            Drawable drawable = getResources().getDrawable(R.drawable.icon_key);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTopEdt.setCompoundDrawables(drawable, null, null, null);
            mBottomEdt.setCompoundDrawables(drawable, null, null, null);
            mCodeTv.setVisibility(View.GONE);
            mTopEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mBottomEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mBtn.setEnabled(false);
            setTitleForRightIamgeView("找回密码", 0, true, false);
        }
        mCodeTv.setOnClickListener(this);
        mBtn.setOnClickListener(this);

        timeCount = new TimeCount(60000, 1000);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.findPw_codeTv:
                account = mTopEdt.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    CustomToast.showToast(this, "请输入手机号码或者邮箱账号！", 1500);
                    return;
                }
                getCode(account);
                timeCount.start();
                break;
            case R.id.findPw_Btn:
                if (isTwo) {
                    findPW(mTopEdt.getText().toString(), mBottomEdt.getText().toString());
                } else {

                    confirmCode(mBottomEdt.getText().toString());
                }
                break;
        }
    }

    /**
     * 验证验证码
     */
    void confirmCode(String vCode) {
        if (TextUtils.isEmpty(vCode)) {
            CustomToast.showToast(this, "请输入验证码", 1500);
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("ycode", vCode);
        map.put("temp_access_token", temp_access_token);
        map.put("act", "affirmCode");
        map.put("app_key", "dj_app_key");

        OkHttpUtils.post().tag(this).url(Constant.BASE_URL + "api.php?").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {

                        FindPWActivity.inVoke(FindPWActivity.this, true, account);
                    } else {
                        Toast.makeText(FindPWActivity.this, object == null ? "验证码错误" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(FindPWActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getCode(String account) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("type", "1");
        map.put("app_key", "dj_app_key");
        map.put("act", "sendCode");
        OkHttpUtils.post().tag(this).url(Constant.BASE_URL + "api.php?").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onResponse(String response, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        mBtn.setEnabled(true);
                        Toast.makeText(FindPWActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                        temp_access_token = object.getJSONObject("data").getString("temp_access_token");
                    } else {
                        timeCount.onFinish();
                        timeCount.cancel();
                        mBtn.setEnabled(false);
                        Toast.makeText(FindPWActivity.this, object == null ? "验证码发送失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    timeCount.onFinish();
                    timeCount.cancel();
                    mBtn.setEnabled(false);
                    Toast.makeText(FindPWActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void findPW(String pwd, String confirm) {
        if (TextUtils.isEmpty(pwd)) {
            CustomToast.showToast(this, "请输入新密码", 1500);
            return;
        }
        if (TextUtils.isEmpty(confirm)) {
            CustomToast.showToast(this, "请再次输入新密码", 1500);
            return;
        }
        if (!pwd.equals(confirm)) {
            CustomToast.showToast(this, "两次密码输入不一致，请重新输入", 1500);
            return;
        }
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("pwd", pwd);
        map.put("confirm", confirm);

        NetUtil.postData(this, Constant.BASE_URL + "api.php?", map, "newPas", new NetUtil.NetUtilCallBack() {
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
                        Toast.makeText(FindPWActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(FindPWActivity.this, object == null ? "密码修改失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(FindPWActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * @param context
     * @param isTwo   是否是第二次
     */
    public static void inVoke(Context context, boolean isTwo, String account) {
        Intent intent = new Intent(context, FindPWActivity.class);
        intent.putExtra("isTwo", isTwo);
        intent.putExtra("account", account);
        ((Activity) context).startActivityForResult(intent, 2);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mCodeTv.setEnabled(true);
            mCodeTv.setText("获取验证码");
            time = 60;
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mCodeTv.setEnabled(false);
            mCodeTv.setText("重新发送（" + time + "）");
            time--;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            finish();
        }
    }
}

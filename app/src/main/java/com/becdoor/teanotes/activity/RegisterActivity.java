package com.becdoor.teanotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.FragmentActivity1;
import com.becdoor.teanotes.fragment.MainActivity;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.Remember;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import wang.raye.preioc.PreIOC;

/**
 * Created by bobozai09 如果代码写的够快  我就能忘却错误的提示 on 2016/10/24.
 */

public class RegisterActivity extends TitleActivity {
    @Bind(R.id.register_usernameEdt)
    EditText mNameEdt;
    @Bind(R.id.register_PWEdt)
    EditText mPwEdt;
    @Bind(R.id.register_confirmPWEdt)
    EditText mConfirmPwEdt;
    @Bind(R.id.register_codeEdt)
    EditText mCodeEdt;
    @Bind(R.id.register_codeBtn)
    Button mCodeBtn;
    @Bind(R.id.delete1)
    ImageView mDeleteIv1;
    @Bind(R.id.delete2)
    ImageView mDeleteIv2;
    @Bind(R.id.delete3)
    ImageView mDeleteIv3;
    @Bind(R.id.register_checkTv)
    TextView mCheckTv;
    @Bind(R.id.register_registerBtn)
    Button mRegisterBtn;

    private String name;
    private String temp_access_token;
    private int time = 60;
    private TimeCount timeCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_register);
        setTitleForRightTextView("注册", "", true, false);
        ButterKnife.bind(this);

        initView();
        timeCount = new TimeCount(60000, 1000);
    }

    /**
     * 获取验证码
     *
     * @param account
     */
    public void getCode(String account) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("type", "0");
        map.put("app_key", "dj_app_key");
        map.put("act", "sendCode");
        OkHttpUtils.post().tag(this).url(Constant.BASE_URL + "api.php?").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.e("-getCode--", response);
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                        temp_access_token = object.getJSONObject("data").getString("temp_access_token");
                        mRegisterBtn.setEnabled(true);
                    } else {
                        timeCount.onFinish();
                        timeCount.cancel();
                        mRegisterBtn.setEnabled(false);
                        Toast.makeText(RegisterActivity.this, object == null ? "验证码发送失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    timeCount.onFinish();
                    mRegisterBtn.setEnabled(false);
                    timeCount.cancel();
                    Toast.makeText(RegisterActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 验证验证码
     *
     * @param pwd
     * @param confirm
     * @param vcode
     */
    public void comfirmCode(final String pwd, final String confirm, final String vcode) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("account", name);
        map.put("ycode", vcode);
        map.put("temp_access_token", temp_access_token);
        map.put("act", "affirmCode");
        map.put("app_key", "dj_app_key");
//        Log.e("comfirmCode--", map.toString());

        OkHttpUtils.post().tag(this).url(Constant.BASE_URL + "api.php?").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        register(pwd, confirm, vcode);
                    } else {
                        Toast.makeText(RegisterActivity.this, object == null ? "验证码错误" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 注册
     *
     * @param pwd
     * @param confirm
     * @param vcode
     */
    public void register(String pwd, String confirm, String vcode) {
        if (TextUtils.isEmpty(AppStatic.jpush_id)) {
            AppStatic.jpush_id = JPushInterface.getRegistrationID(this);
            Remember.putString("jpush_id", AppStatic.jpush_id);
        }
        Map<String, String> map = new HashMap<>();
        map.put("username", name);
        map.put("pwd", pwd);
        map.put("confirm", confirm);
        map.put("vcode", vcode);
        map.put("temp_access_token", temp_access_token);
        map.put("reg_id", AppStatic.jpush_id);
        map.put("act", "doReg");
        map.put("app_key", "dj_app_key");

        OkHttpUtils.post().tag(this).url(Constant.BASE_URL + "api.php?").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onResponse(String response, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                try {
//                    Log.e("zhuce", response);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 100 == object.optInt("status")) {
                        Constant.DJ_APP_KEY = object.getString("app_key");
                        Constant.VALUE_ACCESS_TOKEN = object.getString("access_token");
                        Remember.putString(Constant.ACCESS_TOKEN, Constant.VALUE_ACCESS_TOKEN);
                        AppStatic.isLogin = true;
                        Remember.putBoolean(AppStatic.isLoginName, true);
                        Toast.makeText(RegisterActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class).putExtra("tag", MainActivity.TAB1).putExtra(FragmentActivity1.POSITION_NAME, 1));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, object == null ? "注册失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void initView() {
        mCheckTv.setSelected(true);
        mNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mDeleteIv1.setVisibility(View.INVISIBLE);
                } else {
                    mDeleteIv1.setVisibility(View.VISIBLE);
                }
            }
        });
        mPwEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mDeleteIv2.setVisibility(View.INVISIBLE);
                } else {
                    mDeleteIv2.setVisibility(View.VISIBLE);
                }
            }
        });
        mConfirmPwEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mDeleteIv3.setVisibility(View.INVISIBLE);
                } else {
                    mDeleteIv3.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void registerDelete(View v) {
        switch (v.getId()) {
            case R.id.delete1:
                mNameEdt.setText("");
                break;
            case R.id.delete2:
                mPwEdt.setText("");
                break;
            case R.id.delete3:
                mConfirmPwEdt.setText("");
                break;
        }
    }

    public void registerCode(View v) {
        if (v.getId() == R.id.register_codeBtn) {
            name = mNameEdt.getText().toString();
            if (TextUtils.isEmpty(name)) {
                CustomToast.showToast(this, "请输入用户名！", 1500);
                return;
            }
            timeCount.start();
            getCode(name);
        }
    }

    public void registerRegister(View v) {
        if (!mCheckTv.isSelected()) {
            CustomToast.showToast(this, "您还未同意该服务条款和隐私策略！", 1500);
            return;
        }
        if (v.getId() == R.id.register_registerBtn) {
            String pw = mPwEdt.getText().toString();
            if (TextUtils.isEmpty(pw)) {
                CustomToast.showToast(this, "请输入密码！", 1500);
                return;
            }
            String pw1 = mConfirmPwEdt.getText().toString();
            if (TextUtils.isEmpty(pw1)) {
                CustomToast.showToast(this, "请再次输入密码！", 1500);
                return;
            }
            String code = mCodeEdt.getText().toString();
            if (TextUtils.isEmpty(code)) {
                CustomToast.showToast(this, "请输入验证码！", 1500);
                return;
            }
            comfirmCode(pw, pw1, code);
        }
    }

    public void registerCheck(View v) {
        if (v.getId() == R.id.register_checkTv) {
            mCheckTv.setSelected(!mCheckTv.isSelected());
        }
    }

    public void registerProtocol(View v) {
        if (v.getId() == R.id.register_protocolTv) {
            startActivity(new Intent(this, UserProtocolActivity.class));
        }
    }

    public void registerLogin(View v) {
        if (v.getId() == R.id.register_loginTv) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mCodeBtn.setEnabled(true);
            mCodeBtn.setText("获取验证码");
            time = 60;
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mCodeBtn.setEnabled(false);
            mCodeBtn.setText("重新发送（" + time + "）");
            time--;
        }
    }

}

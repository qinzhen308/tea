package com.becdoor.teanotes.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.FragmentActivity1;
import com.becdoor.teanotes.fragment.MainActivity;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.ThirdUserInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.MD5Utils;
import com.becdoor.teanotes.until.Remember;
import com.becdoor.teanotes.until.ShareUtil;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import wang.raye.preioc.PreIOC;
import wang.raye.preioc.annotation.BindById;

/**
 * Created by bobozai09 如果代码写的够快  我就能忘却错误的提示 on 2016/10/21.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindById(R.id.login_nameEdt)
    EditText mEdiUsername;
    @BindById(R.id.login_pwEdt)
    EditText mEdiPassword;
    @BindById(R.id.login_loginBtn)
    Button mBtnOk;
    @BindById(R.id.login_findPWTv)
    TextView mTxtForgetpassword;
    @BindById(R.id.login_registerTv)
    TextView mTxtRegister;
    @BindById(R.id.login_useOtherTv)
    TextView mTxtUseCompany;
    @BindById(R.id.login_wxLayout)
    RelativeLayout mWXLayout;
    @BindById(R.id.login_sinaLayout)
    RelativeLayout mSinaLayout;

    private ShareUtil mShareUtil;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PreIOC.binder(this);

        initListener();

        AppStatic.isLogin = false;
        Remember.putBoolean(AppStatic.isLoginName, false);
        Remember.putString(Constant.ACCESS_TOKEN, "");
        initThirdLogin();

    }

    void initThirdLogin() {
        mShareUtil = new ShareUtil(this);
        mShareUtil.setThirdLoginCallBack(new ShareUtil.ThirdLoginCallBack() {
            @Override
            public void onScuccess(SHARE_MEDIA platform, ThirdUserInfo thirdUser) {
                startLogin(thirdUser);
            }

            @Override
            public void onFial(SHARE_MEDIA platform, String fialMsg) {
                Toast.makeText(LoginActivity.this, "登录失败:" + fialMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(LoginActivity.this, "已取消登录", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListener() {
        mTxtForgetpassword.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
        mTxtRegister.setClickable(true);
        mTxtRegister.setFocusable(true);
        mTxtRegister.setOnClickListener(this);
        mWXLayout.setOnClickListener(this);
        mSinaLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_loginBtn:

                startLogin(null);
                break;
            case R.id.login_registerTv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.login_findPWTv:
                FindPWActivity.inVoke(this, false, "");
                break;
            case R.id.login_useOtherTv://合作账号登录
                break;
            case R.id.login_wxLayout://微信
                mShareUtil.doThirdLogin(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.login_sinaLayout://新浪
                mShareUtil.doThirdLogin(SHARE_MEDIA.SINA);
                break;
        }
    }

    private void isCanLogin() {
        if (TextUtils.isEmpty(mEdiUsername.getText().toString().trim())) {
            CustomToast.showToast(this, "请输入你的用户名", 1500);
            return;
        }
        if (TextUtils.isEmpty(mEdiPassword.getText().toString().trim())) {
            CustomToast.showToast(this, "请输入你的密码", 1500);
            return;
        }
    }


    private void startLogin(ThirdUserInfo thirdUserInfo) {
        isCanLogin();
        DialogUtil.showDialog(mLoaDailog);
        if (TextUtils.isEmpty(AppStatic.jpush_id)) {
            AppStatic.jpush_id = JPushInterface.getRegistrationID(this);
            Remember.putString("jpush_id", AppStatic.jpush_id);
        }

        Map<String, String> map = new HashMap<>();
        String type = "tel";

        if (thirdUserInfo == null) {
            map.put("account", mEdiUsername.getText().toString().trim());
            map.put("pwd", mEdiPassword.getText().toString().trim());
        } else {
            if (thirdUserInfo.getSinaUser() != null) {
                type = "wb";
                map.put("users_info", new Gson().toJson(thirdUserInfo.getSinaUser()));
                map.put("access_yz_token", MD5Utils.getStringMD5(thirdUserInfo.getSinaUser().getUid() + thirdUserInfo.getSinaUser().getAccess_token()));
            } else if (thirdUserInfo.getWxUser() != null) {

                type = "wx";
                map.put("users_info", new Gson().toJson(thirdUserInfo.getWxUser()));
                map.put("access_yz_token", MD5Utils.getStringMD5(thirdUserInfo.getWxUser().getOpenid() + thirdUserInfo.getWxUser().getAccess_token()));
            }
        }
        map.put("type", type);
        map.put("reg_id", AppStatic.jpush_id);
        map.put("act", "login");
        map.put("app_key", "dj_app_key");
//        Log.e("login--", Constant.BASE_URL + "api.php?" + map.toString().replace(",", "&").replace("}", "").replace("{", ""));

        OkHttpUtils.post().tag(this).url(Constant.BASE_URL + "api.php?").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.e("====", response);

                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Constant.DJ_APP_KEY = object.getString("app_key");
                        Constant.VALUE_ACCESS_TOKEN = object.getString("access_token");
                        Remember.putString(Constant.ACCESS_TOKEN, Constant.VALUE_ACCESS_TOKEN);
                        AppStatic.isLogin = true;
                        Remember.putBoolean(AppStatic.isLoginName, true);
                        isFirstLogin(object.optString("user_id"));
                    } else {
                        DialogUtil.dismissDialog(mLoaDailog);
                        Toast.makeText(LoginActivity.this, object == null ? "登录失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    DialogUtil.dismissDialog(mLoaDailog);
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void isFirstLogin(String user_id) {
        Map<String, String> map = new HashMap<>();
        map.put("act", "is_firstLogin");
        map.put("app_key", "dj_app_key");
        map.put("user_id", user_id);
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

                        if (object.has("msg") && !TextUtils.isEmpty(object.optString("msg"))) {
                            Toast.makeText(LoginActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    doLogin();
                } catch (JSONException e) {
                    doLogin();
                }
            }
        });
    }

    void doLogin() {
        DialogUtil.dismissDialog(mLoaDailog);
        startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("tag", MainActivity.TAB1).putExtra(FragmentActivity1.POSITION_NAME, 1));
        finish();
    }

}

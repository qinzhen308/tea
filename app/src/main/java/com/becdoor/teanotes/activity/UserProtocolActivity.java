package com.becdoor.teanotes.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/24.
 * 用户协议
 */

public class UserProtocolActivity extends TitleActivity {
    @Bind(R.id.userProtocol_webview)
    WebView mWebView;
    @Bind(R.id.userProtocol_textView)
    TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_userprotocol);
        setTitleForRightIamgeView("用户协议", 0, true, false);
        ButterKnife.bind(this);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        getData1();
    }

    void getData1() {
        OkHttpUtils.get().tag(this).url(Constant.BASE_URL + "api.php?")
                .addParams("act", "getUserProtocol")
                .addParams("app_key", "dj_app_key")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                error();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        String content = object.optString("content");
                        mWebView.loadData(content, "text/html;charset=utf-8", "");
                    } else {
                        error();
                    }
                } catch (JSONException e) {
                    error();
                }
            }
        });
    }

    void error() {
        mTextView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
    }
}


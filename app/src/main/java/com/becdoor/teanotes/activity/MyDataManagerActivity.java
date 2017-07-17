package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
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
 */

public class MyDataManagerActivity extends TitleActivity {
    @Bind(R.id.myDataManager_nameEdt)
    EditText mNameEdt;
    @Bind(R.id.myDataManager_contentEdt)
    EditText mContentEdt;
    @Bind(R.id.myDataManager_numTv)
    TextView mNumTv;
    @Bind(R.id.myDataManager_saveBtn)
    Button mSaveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_mydatamanager);
        setTitleForRightIamgeView("我的资料管理", 0, true, false);

        ButterKnife.bind(this);

        mNameEdt.setText(getIntent().getStringExtra("name"));
        mNameEdt.setSelection(mNameEdt.getText().toString().length());
        mContentEdt.setText(getIntent().getStringExtra("content"));
        mContentEdt.setSelection(mContentEdt.getText().toString().length());

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEdt.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    CustomToast.showToast(MyDataManagerActivity.this, "请输入空间名称", 1000);
                    return;
                }
                submitData(name, mContentEdt.getText().toString());
            }
        });

        mContentEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int num = 0;
                if (TextUtils.isEmpty(s)) {
                    num = s.toString().length();
                }
                mNumTv.setText(num + "/18");
            }
        });
    }

    void submitData(String name, String slogan) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("slogan", slogan);
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "doBaseSet", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(MyDataManagerActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(MyDataManagerActivity.this, object == null ? "修改失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MyDataManagerActivity.this, "修改失败!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static void inVoke(Context context, String name, String content) {
        Intent intent = new Intent(context, MyDataManagerActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("content", content);
        context.startActivity(intent);

    }
}

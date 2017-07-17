package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.MyPriMsgAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.model.PriMsgInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/17.
 * 我的私信
 */

public class MyPriMsgActivity extends TitleActivity {
    @Bind(R.id.myPriMsg_litsView)
    ListView mListView;
    @Bind(R.id.myPriMsg_layout)
    View mInputLayout;
    @Bind(R.id.myPriMsg_commentEdt)
    EditText mCommEdt;
    @Bind(R.id.myPriMsg_numTv)
    TextView mNumTv;

    private String parent_id;
    private String reply_id;
    private MyPriMsgAdapter msgAdapter;
    private List<PriMsgInfo> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_myprimsg);
        setTitleForRightIamgeView("我的私信", 0, true, false);
        ButterKnife.bind(this);

        parent_id = getIntent().getStringExtra("parent_id");
        mList = new ArrayList<>();
        msgAdapter = new MyPriMsgAdapter(this);
        mListView.setAdapter(msgAdapter);
        msgAdapter.setmList(mList);

        mCommEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int num = 0;
                if (!TextUtils.isEmpty(s)) {
                    num = s.toString().length();
                }
                mNumTv.setText(num + "/150");
            }
        });
        mNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mCommEdt.getText().toString())) {
                    CustomToast.showToast(MyPriMsgActivity.this, "请输入私信内容!", 1500);
                    return;
                }
                replyMsg(mCommEdt.getText().toString());
            }
        });
        getData();
    }

    void getData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("parent_id", parent_id);
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "msg_info", new NetUtil.NetUtilCallBack() {
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
                        List<PriMsgInfo> list = new Gson().fromJson(object.optJSONArray("all_msg").toString(), new TypeToken<List<PriMsgInfo>>() {
                        }.getType());
                        if (list != null && list.size() > 0) {
                            reply_id = list.get(0).getSender_id();
                            mList.addAll(list);
                        }
                        msgAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void replyMsg(String message) {
        if (TextUtils.isEmpty(reply_id)) {
            return;
        }
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("parent_id", parent_id);
        map.put("reply_id", reply_id);
        map.put("message", message);
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "msg_reply", new NetUtil.NetUtilCallBack() {
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
                        mList.clear();
                        getData();
                    } else {
                        Toast.makeText(MyPriMsgActivity.this, object == null ? "回复失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MyPriMsgActivity.this, "回复失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @param context
     * @param parent_id 私信id
     */
    public static void inVoke(Context context, String parent_id) {
        Intent intent = new Intent(context, MyPriMsgActivity.class);
        intent.putExtra("parent_id", parent_id);
        context.startActivity(intent);
    }
}

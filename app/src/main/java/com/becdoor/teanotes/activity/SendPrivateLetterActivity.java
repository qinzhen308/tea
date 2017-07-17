package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by jjj on 2016/11/9.
 * 发私信
 */

public class SendPrivateLetterActivity extends TitleActivity {
    @Bind(R.id.sendPrivateLetter_edt)
    EditText mEdittext;
    @Bind(R.id.sendPrivateLetter_okBtn)
    Button mOkBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_sendprivateletter);
        setTitleForRightIamgeView("发布私信", 0, true, false);
        ButterKnife.bind(this);

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLetter(getIntent().getStringExtra("uid"), mEdittext.getText().toString());
            }
        });
    }

    /**
     * 发布私信
     *
     * @param uid
     * @param msg
     */
    void submitLetter(String uid, String msg) {
        if (TextUtils.isEmpty(msg)) {
            CustomToast.showToast(this, "请输入私信内容!", 1000);
            return;
        }
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("msg", msg);
        NetUtil.postData(this, Constant.BASE_URL + "space.php?", map, "privMsg", new NetUtil.NetUtilCallBack() {
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
                        Toast.makeText(SendPrivateLetterActivity.this, "发布成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SendPrivateLetterActivity.this, object == null ? "发布失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(SendPrivateLetterActivity.this, "发布失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void inVoke(Context context,String uid){
        Intent intent=new Intent(context,SendPrivateLetterActivity.class);
        intent.putExtra("uid",uid);
        context.startActivity(intent);
    }
}

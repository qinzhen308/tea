package com.becdoor.teanotes.activity;

import android.os.Bundle;
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
 * 修改密码
 */

public class UpdatePWActivity extends TitleActivity {
    @Bind(R.id.updatePW_oldPWEdt)
    EditText mOldPWEdt;
    @Bind(R.id.updatePW_newPWEdt)
    EditText mNewPWEdt;
    @Bind(R.id.updatePW_confirmPWEdt)
    EditText mConfirmPWEdt;
    @Bind(R.id.updatePW_okBtn)
    TextView mOkBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_updatepw);
        setTitleForRightIamgeView("密码修改", 0, true, false);
        ButterKnife.bind(this);

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old = mOldPWEdt.getText().toString();
                if (TextUtils.isEmpty(old)) {
                    CustomToast.showToast(UpdatePWActivity.this, "请输入原密码", 1000);
                    return;
                }
                String newPW = mNewPWEdt.getText().toString();
                if (TextUtils.isEmpty(newPW)) {
                    CustomToast.showToast(UpdatePWActivity.this, "请输入新密码", 1000);
                    return;
                }
                String confirm = mConfirmPWEdt.getText().toString();
                if (TextUtils.isEmpty(confirm)) {
                    CustomToast.showToast(UpdatePWActivity.this, "请再次输入新密码", 1000);
                    return;
                }
                if (!newPW.equals(confirm)) {
                    CustomToast.showToast(UpdatePWActivity.this, "两次密码输入不同，请重新输入", 1000);
                    return;
                }
                updatePW(old, newPW, confirm);
            }
        });
    }

    void updatePW(String oPass, String nPass, String rPass) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("oPass", oPass);
        map.put("nPass", nPass);
        map.put("rPass", rPass);
        NetUtil.postData(this, Constant.BASE_URL + "acc_manage.php?", map, "editPas", new NetUtil.NetUtilCallBack() {
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
                        Toast.makeText(UpdatePWActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdatePWActivity.this, object == null ? "修改失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(UpdatePWActivity.this, "修改失败!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

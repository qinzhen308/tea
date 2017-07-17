package com.becdoor.teanotes.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.PushInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/14.
 * 推送设置
 */

public class PushSettingActivity extends TitleActivity {
    @Bind(R.id.pushSetting_noteRecommTv)
    TextView mNoteRecommTv;
    @Bind(R.id.pushSetting_collectTv)
    TextView mCollectTv;
    @Bind(R.id.pushSetting_firendCareTv)
    TextView mFirendCareTv;
    @Bind(R.id.pushSetting_priMsgTv)
    TextView mPriMsgTv;
    @Bind(R.id.pushSetting_pushTv)
    TextView mPushTv;
    @Bind(R.id.pushSetting_likeTv)
    TextView mLikeTv;
    @Bind(R.id.pushSetting_dicountTv)
    TextView mDiscountTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_pushsetting);
        setTitleForRightIamgeView("推送消息设置", 0, true, false);
        ButterKnife.bind(this);

        mNoteRecommTv.setOnClickListener(this);
        mCollectTv.setOnClickListener(this);
        mFirendCareTv.setOnClickListener(this);
        mPriMsgTv.setOnClickListener(this);
        mPushTv.setOnClickListener(this);
        mLikeTv.setOnClickListener(this);
        mDiscountTv.setOnClickListener(this);

        getData1();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pushSetting_noteRecommTv:
                sumitSetting("note_push", mNoteRecommTv);
                break;
            case R.id.pushSetting_firendCareTv:
                sumitSetting("friend_push", mFirendCareTv);
                break;
            case R.id.pushSetting_collectTv:
                sumitSetting("collect_push", mCollectTv);
                break;
            case R.id.pushSetting_likeTv:
                sumitSetting("praise_push", mLikeTv);
                break;
            case R.id.pushSetting_priMsgTv:
                sumitSetting("msg_push", mPriMsgTv);
                break;
            case R.id.pushSetting_dicountTv:
                sumitSetting("comm_push", mDiscountTv);
                break;
            case R.id.pushSetting_pushTv:
                sumitSetting("area_push", mPushTv);
                break;
        }
    }

    /**
     * @param op "note_push":笔记推荐,"friend_push":好友关注,"collect_push":收藏笔记,"praise_push":喜欢笔记, "comm_push":评论笔记,"msg_push":私信,"area_push":靠近合作商区域接收推送信息
     *           val 1为开始，0为关闭
     */
    void sumitSetting(String op, final TextView textView) {
        DialogUtil.showDialog(mLoaDailog);
        final Map<String, String> map = new HashMap<>();
        map.put("op", op);
        if (textView.isSelected()) {
            map.put("val", "0");
        } else {
            map.put("val", "1");
        }

        NetUtil.postData(this, Constant.BASE_URL + "acc_manage.php?", map, "push", new NetUtil.NetUtilCallBack() {
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
                        Toast.makeText(PushSettingActivity.this, "设置成功!", Toast.LENGTH_SHORT).show();
                        textView.setSelected(!textView.isSelected());
                    } else {
                        Toast.makeText(PushSettingActivity.this, object == null ? "设置失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(PushSettingActivity.this, "设置失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void getData1() {
        DialogUtil.showDialog(mLoaDailog);
        final Map<String, String> map = new HashMap<>();
        map.put("op", "push");
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
                        PushInfo pushInfo = new Gson().fromJson(object.getJSONObject("data").toString(), PushInfo.class);
                        if (pushInfo != null) {
                            mNoteRecommTv.setSelected(pushInfo.getNote_push() == 1);
                            mFirendCareTv.setSelected(pushInfo.getFriend_push() == 1);
                            mCollectTv.setSelected(pushInfo.getCollect_push() == 1);
                            mLikeTv.setSelected(pushInfo.getPraise_push() == 1);
                            mDiscountTv.setSelected(pushInfo.getComm_push() == 1);
                            mPriMsgTv.setSelected(pushInfo.getMsg_push() == 1);
                            mPushTv.setSelected(pushInfo.getArea_push() == 1);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

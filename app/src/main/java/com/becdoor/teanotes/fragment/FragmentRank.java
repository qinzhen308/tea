package com.becdoor.teanotes.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.RankAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.RankInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.becdoor.teanotes.view.ReListView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/11.
 * 斗记 /积分界面
 */
public class FragmentRank extends BaseFragment {
    private View view;
    @Bind(R.id.title_leftTv)
    TextView mLeftTv;
    @Bind(R.id.title_titleTv)
    TextView mTitleTv;
    @Bind(R.id.f_rank_rankListView)
    ReListView mRankListLv;
    @Bind(R.id.f_rank_avatarIv)
    ImageView mAvatarIv;
    @Bind(R.id.f_rank_rankNameTv)
    TextView mRankTv;
    @Bind(R.id.f_rank_nickNameTv)
    TextView mNickNameTv;
    @Bind(R.id.f_rank_pointTv)
    TextView mPointTv;
    @Bind(R.id.f_rank_ruleLayout)
    LinearLayout mRuleLayout;

    private RankAdapter mRankAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_rank, container, false);
            ButterKnife.bind(this, view);
        } else {
            ViewGroup group = (ViewGroup) view.getParent();
            if (group != null) {
                group.removeView(view);
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        getData();
    }

    void initView() {
        mTitleTv.setText("积分等级");
        if (getArguments() != null && getArguments().getBoolean("isBack", false)) {
            mLeftTv.setVisibility(View.VISIBLE);
        } else {
            mLeftTv.setVisibility(View.INVISIBLE);
        }
        mLeftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    void getData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("op", "rank");
        NetUtil.postData(getActivity(), Constant.BASE_URL + "center.php?", map, "acc_manage", new NetUtil.NetUtilCallBack() {
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
                        RankInfo gradeInfo = new Gson().fromJson(object.getJSONObject("data").toString(), RankInfo.class);
                        if (gradeInfo != null) {

                            Glide.with(getActivity()).load(Constant.REALM_NAME + gradeInfo.getAvatar()).transform(new CircleTransform(getActivity())).placeholder(R.drawable.icon_login_user).into(mAvatarIv);
                            mRankTv.setText(gradeInfo.getRank_name());
                            mNickNameTv.setText(gradeInfo.getUsername());
                            mPointTv.setText(Html.fromHtml("<font color='#E12929'>" + gradeInfo.getPoint() + "</font>积分"));
                            mRankAdapter = new RankAdapter(getActivity(), gradeInfo.getRank_list());
                            mRankListLv.setAdapter(mRankAdapter);
                            if (gradeInfo.getRule_list() != null) {
                                for (int i = 0; i < gradeInfo.getRule_list().size(); i++) {
                                    RankInfo.RuleListBean ruleBean = gradeInfo.getRule_list().get(i);
                                    if (ruleBean != null) {
                                        String content = ruleBean.getName();
                                        if (!TextUtils.isEmpty(ruleBean.getVal())) {
                                            content = content + "：" + ruleBean.getVal() + "点积分";
                                        }
                                        createRuleTv(content);
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void createRuleTv(String content) {
        TextView textView = new TextView(getActivity());
        textView.setTextColor(getResources().getColor(R.color.common_black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.all_margrin), 0, 0);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(content);
        mRuleLayout.addView(textView);
    }
}

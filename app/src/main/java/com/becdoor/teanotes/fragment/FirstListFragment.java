package com.becdoor.teanotes.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.ActivitiesDetailActivity;
import com.becdoor.teanotes.activity.ActivityManagerActivity;
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.activity.CreateNotesActivity;
import com.becdoor.teanotes.activity.MakeTeaActivity;
import com.becdoor.teanotes.activity.NoteActivity;
import com.becdoor.teanotes.activity.PersonalSpaceActivity;
import com.becdoor.teanotes.activity.SeminarActivity;
import com.becdoor.teanotes.activity.SeminarDetailActivity;
import com.becdoor.teanotes.activity.SpaceManagerActivity;
import com.becdoor.teanotes.activity.TeaCollectActivity;
import com.becdoor.teanotes.model.HomePageBean;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.BillboardLayout;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/ m/18.
 */

public class FirstListFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView mRecommend1Iv;
    private ImageView mRecommend2Iv;
    private BillboardLayout mBillboardLayout;
    private View mLayout1;
    private View mLayout2;
    private View mLayout3;
    private View mLayout4;
    private int screenW = 0;
    private HomePageBean homePageBean;

    public static FirstListFragment newInstance() {
        FirstListFragment mFirstListFragment = new FirstListFragment();
        return mFirstListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, null);
            mBillboardLayout = (BillboardLayout) view.findViewById(R.id.fragment_main_msgLayout);
            mRecommend1Iv = (ImageView) view.findViewById(R.id.tuijian1);
            mRecommend2Iv = (ImageView) view.findViewById(R.id.tuijian2);
            view.findViewById(R.id.line_fabubiji).setOnClickListener(this);
            view.findViewById(R.id.line_paochadaojishi).setOnClickListener(this);
            view.findViewById(R.id.line_cangchaguanli).setOnClickListener(this);
            view.findViewById(R.id.line_bianqianguanli).setOnClickListener(this);
            view.findViewById(R.id.line_kongjianguanli).setOnClickListener(this);
            view.findViewById(R.id.line_huodong).setOnClickListener(this);
            view.findViewById(R.id.line_zhuanti).setOnClickListener(this);
            view.findViewById(R.id.tuijian1_layout).setOnClickListener(this);
            view.findViewById(R.id.tuijian2_layout).setOnClickListener(this);
            mLayout1 = view.findViewById(R.id.layout1);
            mLayout2 = view.findViewById(R.id.layout2);
            mLayout3 = view.findViewById(R.id.layout3);
            mLayout4 = view.findViewById(R.id.layout4);

            setParams();
        } else {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
    }

    void setParams() {
        int m = getActivity().getResources().getDimensionPixelSize(R.dimen.all_margrin);
        screenW = AppUtil.getScreenWH(getActivity())[0];
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.324));
        params1.setMargins(m, m, m, m);
        mLayout1.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.305));
        params2.setMargins(m, 0, m, 0);
        mLayout2.setLayoutParams(params2);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.305));
        params3.setMargins(m, m, m, m);
        mLayout3.setLayoutParams(params3);
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.249));
        params4.setMargins(m, 0, m, 0);
        mLayout4.setLayoutParams(params4);
    }

    private void getData() {
        NetUtil.postData(getActivity(), Constant.BASE_URL + "index.php?", null, "index", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        homePageBean = new Gson().fromJson(response, HomePageBean.class);
                        if (homePageBean != null) {

                            //显示焦点图
                            if (homePageBean.getData().getFocus_pics() != null && homePageBean.getData().getFocus_pics().size() > 0) {
                                Glide.with(getActivity()).load(homePageBean.getData().getFocus_pics().get(0).getSrc()).into(mRecommend1Iv);
                                if (homePageBean.getData().getFocus_pics().size() > 1) {
                                    Glide.with(getActivity()).load(homePageBean.getData().getFocus_pics().get(1).getSrc()).into(mRecommend2Iv);
                                }
                            }

                            //公告
                            if (homePageBean.getData().getNotice_list() != null && homePageBean.getData().getNotice_list().size() > 0) {
                                mBillboardLayout.showView(homePageBean.getData().getNotice_list());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tuijian1_layout:
                chargeComm(0);
                break;
            case R.id.tuijian2_layout:
                chargeComm(1);
                break;
            case R.id.line_fabubiji://发布笔记
                CreateNotesActivity.invoke(getActivity());
                break;
            case R.id.line_paochadaojishi://泡茶计时
                MakeTeaActivity.invoke(getActivity());
                break;
            case R.id.line_bianqianguanli://便签管理
                NoteActivity.invoke(getActivity());
                break;
            case R.id.line_cangchaguanli://藏茶管理
                TeaCollectActivity.invoke(getActivity());
                break;
            case R.id.line_kongjianguanli://空间管理
                startActivity(new Intent(getActivity(), SpaceManagerActivity.class));
                break;
            case R.id.line_huodong://活动
                startActivity(new Intent(getActivity(), ActivityManagerActivity.class));
                break;
            case R.id.line_zhuanti://专题
                startActivity(new Intent(getActivity(), SeminarActivity.class));
                break;
        }
    }

    //1为茶评，2为茶志，3为专题，4为活动，5为空间
    void chargeComm(int position) {
        if (homePageBean != null && homePageBean.getData().getFocus_pics().size() >= position) {
            HomePageBean.DataBean.FocusPicsBean focusPicsBean = homePageBean.getData().getFocus_pics().get(position);
            if (focusPicsBean != null && !TextUtils.isEmpty(focusPicsBean.getSel_id())) {

                switch (Integer.valueOf(focusPicsBean.getCate_type())) {
                    case 1:
                        CommonDetailActivity.inVoke(getActivity(), focusPicsBean.getCate_type(), focusPicsBean.getSel_id());
                        break;
                    case 2:
                        CommonDetailActivity.inVoke(getActivity(), focusPicsBean.getCate_type(), focusPicsBean.getSel_id());
                        break;
                    case 3:
                        SeminarDetailActivity.inVoke(getActivity(), focusPicsBean.getSel_id());
                        break;
                    case 4:
                        ActivitiesDetailActivity.inVoke(getActivity(), focusPicsBean.getSel_id());
                        break;
                    case 5:
                        PersonalSpaceActivity.inVoke(getActivity(), focusPicsBean.getSel_id());
                        break;
                }

            }
        }

    }
}

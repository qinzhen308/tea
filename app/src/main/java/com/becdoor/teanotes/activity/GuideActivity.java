package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.fragment.FragmentActivity1;
import com.becdoor.teanotes.fragment.MainActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.GuideInfo;
import com.becdoor.teanotes.until.Remember;
import com.becdoor.teanotes.view.LoopCirclePageIndicator;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class GuideActivity extends BaseActivity {
    ViewPager mPager;
    LoopCirclePageIndicator indicator;
    private List<String> imgs;

    ArrayList<ImageView> pages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        setListener();
        getData();
    }

    void getData() {
        Map<String, String> map = new HashMap<>();
        map.put(Constant.APP_KEY, Constant.DJ_APP_KEY);
        map.put(Constant.ACT, "getLeadPic");
        OkHttpUtils.get().tag(this).url(Constant.BASE_URL + "api.php?").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                jump();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        GuideInfo guideInfo = new Gson().fromJson(object.getJSONObject("data").toString(), GuideInfo.class);
                        if (guideInfo != null && guideInfo.getUrl() != null) {
                            imgs.addAll(guideInfo.getUrl());
                            initData();
                        } else {
                            jump();
                        }

                    } else {
                        jump();
                    }

                } catch (JSONException e) {
                    jump();
                }
            }
        });
    }

    private void initView() {
        imgs = new ArrayList<>();
        mPager = (ViewPager) findViewById(R.id.vp_guide);
        indicator = (LoopCirclePageIndicator) findViewById(R.id.idc_ad_indicator);
    }

    void initData() {
        for (int i = 0; i <= imgs.size(); i++) {
            ImageView view = new ImageView(GuideActivity.this);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            view.setScaleType(ScaleType.CENTER_CROP);
            pages.add(view);
        }
        indicator.setRealCount(imgs.size());
        mPager.setAdapter(new GuideAdapter());
        indicator.setViewPager(mPager);

        Remember.putBoolean("isFirst", false);
    }

    private void setListener() {
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == imgs.size()) {
                    jump();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    private void jump() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.size() + 1;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = pages.get(position);
            container.addView(view);
            if (imgs.size() > position) {
//				view.setBackgroundColor(getResources().getColor(R.color.white));
//                view.setImageResource(imgs.get(position));
                Glide.with(GuideActivity.this).load(Constant.REALM_NAME + imgs.get(position)).into(view);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pages.get(position));
        }
    }

}

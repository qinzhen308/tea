package com.becdoor.teanotes.view;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.model.HomePageBean;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BillboardLayout extends RelativeLayout {
    ScheduledExecutorService executor;
    List<HomePageBean.DataBean.NoticeListBean> list;
    private TextView middleView;
    private TextView bottomView;
    private View allView;
    private int curPositon;

    private boolean isSingleLine = false;//是否显示一行

    LayoutInflater inflater;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what != 1) {
                return;
            }
            AnimationSet animationSet = new AnimationSet(true);
            TranslateAnimation animation1 = new TranslateAnimation(0, 0, 0, -getHeight());
            animation1.setDuration(500);
            animationSet.addAnimation(animation1);
            animationSet.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    showData();
                }
            });
            allView.startAnimation(animationSet);
        }

        ;
    };

    public void setSingleLine(boolean isSingleLine) {
        this.isSingleLine = isSingleLine;
    }

    public BillboardLayout(Context context) {
        super(context);
        init();
    }

    public BillboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BillboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflater = LayoutInflater.from(getContext());
    }

    /**
     * 设置数据并显示
     *
     * @param list
     */
    public void showView(List<HomePageBean.DataBean.NoticeListBean> list) {
        stop();
        this.list = list;
        curPositon = 0;
        initItem();

        if (list == null || list.size() == 0) {
            middleView.setText("暂无消息");
            bottomView.setVisibility(View.GONE);
            return;
        }

        if (list.size() > 1) {
            start();
        }

        showData();
    }

    private void showData() {
        HomePageBean.DataBean.NoticeListBean msg1 = list.get(curPositon);
        if (msg1 != null) {
            middleView.setText(msg1.getTitle());
        }
    }


    private void initItem() {
        removeAllViews();
        inflater.inflate(R.layout.item_billboard, this);
        allView = findViewById(R.id.item_billboard);
        middleView = (TextView) findViewById(R.id.tv_middle);
        bottomView = (TextView) findViewById(R.id.tv_bottom);
    }

    private void start() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                if (!isSingleLine) {
                    curPositon = curPositon + 2;
                    if (curPositon >= list.size()) {
                        curPositon = 0;
                    }
                } else {
                    if (++curPositon >= list.size()) {
                        curPositon = 0;
                    }
                }


                handler.sendEmptyMessage(1);
            }
        }, 5000, 5000, TimeUnit.MILLISECONDS);
    }

    private void stop() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }


}

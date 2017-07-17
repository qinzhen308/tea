package com.becdoor.teanotes.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.util.TypedValue;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/10/28.
 * 通用的列表 上拉加载 下载刷新
 */

public abstract class BaseRecyclerViewActivity extends TitleActivity {
    RBCallbkRecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int curPage = 1;
    int totalPage = 1;

    /**
     * post请求数据
     *
     * @param url
     * @param map
     */
    void setPostFormBuilder(String url, Map<String, String> map,String act, final INetCallBackListener iCallBackListener) {
        if (map == null) {
            map = new HashMap<>();
        }
        map.put("page", String.valueOf(curPage));
        NetUtil.postData(this, url, map, act, new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (iCallBackListener != null) {
                    iCallBackListener.onFail();
                }
            }

            @Override
            public void onScuccess(String response, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (iCallBackListener != null) {
                    iCallBackListener.onScuccess(response);
                }
            }
        });
    }

    /**
     * 监听
     */
    void initListener() {
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setOnReachBottomListener(new RBCallbkRecyclerView.OnReachBottomListener() {
                @Override
                public void onReachBottom() {
                    if (curPage < totalPage) {
                        curPage++;
                        //加载下一页
                        loadNextPage();
                    }
                }
            });
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage=1;
                refreshData();
            }
        });
    }

    /**
     * 加载下一页
     */
    abstract void loadNextPage();

    /**
     * 刷新数据
     */
    abstract void refreshData();

}

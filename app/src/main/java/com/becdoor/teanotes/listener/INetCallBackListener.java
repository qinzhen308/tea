package com.becdoor.teanotes.listener;

/**
 * Created by Administrator on 2016/10/31.
 * 网络请求返回结果监听
 */

public interface INetCallBackListener {
    /**
     * 获取失败
     */
    void onFail();

    /**
     * 成功返回
     *
     * @param response
     */
    void onScuccess(String response);
}

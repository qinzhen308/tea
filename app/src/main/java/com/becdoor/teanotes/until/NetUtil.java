package com.becdoor.teanotes.until;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.becdoor.teanotes.activity.LoginActivity;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/1.
 */

public class NetUtil {
    /**
     * get
     *
     * @param context
     * @param url
     * @param paramMap        包含了app_key ,access_token ,act(需要传值) 可为null
     * @param act             接口需要的act
     * @param netUtilCallBack 回调
     */
    public static void getData(final Context context, String url, Map<String, String> paramMap, String act, final NetUtilCallBack netUtilCallBack) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        paramMap.put(Constant.APP_KEY, Constant.DJ_APP_KEY);
        paramMap.put(Constant.ACCESS_TOKEN, Constant.VALUE_ACCESS_TOKEN);
        paramMap.put(Constant.ACT, act);
        Log.e(act, url + paramMap.toString().replace(",", "&").replace("}", "").replace("{", ""));

        OkHttpUtils.get().tag(context).url(url).params(paramMap).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (netUtilCallBack != null) {
                    netUtilCallBack.onFail(call, e, id);
                }
            }

            @Override

            public void onResponse(String response, int id) {
                Log.e("---get-", response);
                checkLogin(context, response);
                if (netUtilCallBack != null) {
                    netUtilCallBack.onScuccess(response, id);
                }
            }
        });
    }

    /**
     * post
     *
     * @param context
     * @param url
     * @param paramMap        包含了app_key ,access_token ,act(需要传值) 可为null
     * @param act             接口需要的act
     * @param netUtilCallBack 回调
     */
    public static void postData(final Context context, String url, Map<String, String> paramMap, String act, final NetUtilCallBack netUtilCallBack) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        paramMap.put(Constant.APP_KEY, Constant.DJ_APP_KEY);
        paramMap.put(Constant.ACCESS_TOKEN, Constant.VALUE_ACCESS_TOKEN);
        paramMap.put(Constant.ACT, act);
        Log.e(act, url + paramMap.toString().replace(",", "&").replace("}", "").replace("{", ""));
        OkHttpUtils.post().tag(context).url(url).params(paramMap).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (netUtilCallBack != null) {
                    netUtilCallBack.onFail(call, e, id);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                checkLogin(context, response);
                Log.e("----", response);
                if (netUtilCallBack != null) {
                    netUtilCallBack.onScuccess(response, id);
                }
            }
        });
    }

    /**
     * 带有文件的数据
     *
     * @param context
     * @param url
     * @param paramMap
     * @param nameForFile     上传的参数
     * @param filename        文件名
     * @param file            文件
     * @param act
     * @param netUtilCallBack
     */
    public static void postDataWhithFile(final Context context, String url, Map<String, String> paramMap, String nameForFile, String filename, File file, String act, final NetUtilCallBack netUtilCallBack) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        paramMap.put(Constant.APP_KEY, Constant.DJ_APP_KEY);
        paramMap.put(Constant.ACCESS_TOKEN, Constant.VALUE_ACCESS_TOKEN);
        paramMap.put(Constant.ACT, act);
        Log.e(act, url + paramMap.toString().replace(",", "&").replace("}", "").replace("{", ""));
        OkHttpUtils.post().tag(context).url(url).params(paramMap).addFile(nameForFile, filename, file).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (netUtilCallBack != null) {
                    netUtilCallBack.onFail(call, e, id);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                checkLogin(context, response);
//                Log.e("----", response);
                if (netUtilCallBack != null) {
                    netUtilCallBack.onScuccess(response, id);
                }
            }
        });
    }


    public interface NetUtilCallBack {
        void onFail(Call call, Exception e, int id);

        void onScuccess(String response, int id);
    }

    /**
     * 检查是否登录
     *
     * @param context
     * @param response
     */
    static void checkLogin(Context context, String response) {
        try {
            JSONObject object = new JSONObject(response);
            if (object != null && object.optInt("status") == 211) {
                if (AppStatic.isLogin) {
                    Toast.makeText(context, "您的账号已在其他设备登录，请重新登录！", Toast.LENGTH_SHORT).show();
                }
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity) context).finish();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

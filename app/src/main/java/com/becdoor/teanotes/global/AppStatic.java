package com.becdoor.teanotes.global;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.becdoor.teanotes.model.UserInfoBean;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.Remember;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/3.
 */

public class AppStatic {
    public static final String isLoginName = "isLogin";
    public static boolean isLogin = false;//是否登录
    public static String jpush_id = "";//极光的id

    public String down_url = " ";

    private static AppStatic mInstance;

    public static AppStatic getInstance() {
        if (mInstance == null) {
            mInstance = new AppStatic();
        }
        return mInstance;
    }


    /**
     * 点赞
     *
     * @param context
     * @param url
     * @param map
     * @param act
     * @param prasieListener
     */
    public static void prasie(final Context context, String url, Map<String, String> map, String act, final PrasieListener prasieListener) {
        NetUtil.postData(context, url, map, act, new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        if (object.has("alert_msg") && !object.isNull("alert_msg")) {
                            CustomToast.showToast(context, object.getString("alert_msg"), 1500);
                        } else {
                            Toast.makeText(context, object == null ? "点赞成功" : object.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                        if (prasieListener != null) {
                            prasieListener.prasieScuccess();
                        }
                    } else {
                        Toast.makeText(context, object == null ? "点赞失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                        if (prasieListener != null) {
                            prasieListener.prasieFail();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
                    if (prasieListener != null) {
                        prasieListener.prasieFail();
                    }
                }
            }
        });
    }

    /**
     * 提交评论
     *
     * @param context
     * @param url
     * @param map
     * @param act
     */
    public static void submitComment(final Context context, String url, Map<String, String> map, String act, final CommentListener commentListener) {

        NetUtil.postData(context, url, map, act, new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                Toast.makeText(context, "评论失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        if (object.has("alert_msg") && !object.isNull("alert_msg")) {
                            Toast.makeText(context, object.getString("alert_msg"), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                        }
                        if (commentListener != null) {
                            commentListener.commentScuccess();
                        }
                    } else {
                        if (commentListener != null) {
                            commentListener.commentFail();
                        }
                        Toast.makeText(context, object == null ? "评论失败" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    if (commentListener != null) {
                        commentListener.commentFail();
                    }
                    Toast.makeText(context, "评论失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 分享
     *
     * @param context
     * @param url
     * @param map
     * @param shareListener
     */
    public static void getShareData(final Context context, String url, Map<String, String> map, final ShareListener shareListener) {
        NetUtil.postData(context, url, map, "share", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (shareListener != null) {
                    shareListener.shareFail();
                }
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        if (shareListener != null) {
                            shareListener.shareScuccess(object.getString("url"));
                        }
                    } else {
                        if (shareListener != null) {
                            shareListener.shareFail();
                        }
                    }
                } catch (JSONException e) {
                    if (shareListener != null) {
                        shareListener.shareFail();
                    }
                }
            }
        });
    }

    /**
     * 分享成功后~
     *
     * @param context
     * @param aid     活动的ID/专题ID/笔记的ID
     * @param type    分享类型,acts:活动，note:笔记，spec:专题
     */
    public static void shareCallBack(final Context context, String aid, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("aid", aid);
        map.put("type", type);
        NetUtil.postData(context, Constant.BASE_URL + "index.php?", map, "doshare", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                Toast.makeText(context, "网络出错了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(context, object.optString("alert_msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, object == null ? "网络错误" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 评论的接口
     */
    public interface CommentListener {
        void commentScuccess();

        void commentFail();
    }

    /**
     * 点赞的接口
     */
    public interface PrasieListener {
        void prasieScuccess();

        void prasieFail();
    }

    public interface ShareListener {
        void shareScuccess(String url);

        void shareFail();
    }

}

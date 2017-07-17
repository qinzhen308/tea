package com.becdoor.teanotes.receiver;


import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.activity.FriendsActivity;
import com.becdoor.teanotes.activity.MsgCenterActivity;
import com.becdoor.teanotes.fragment.FragmentActivity1;
import com.becdoor.teanotes.fragment.MainActivity;
import com.becdoor.teanotes.global.AppStatic;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送
 *
 * @author jjj
 * @time 2016-7-20
 */
public class JPushReceiver extends BroadcastReceiver {
    private static List<MsgListener> msgObservers = new ArrayList<MsgListener>();

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            AppStatic.jpush_id = JPushInterface.getRegistrationID(context);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//	            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e("---jpush-收到了通知-", bundle.toString());
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // 用户点击打开了通知

            String message = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!TextUtils.isEmpty(message)) {
                jumpTo(context, message);
            } else {
                Intent intent0 = new Intent(context, MainActivity.class);
                intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent0.putExtra("tag", MainActivity.TAB1);
                intent0.putExtra(FragmentActivity1.POSITION_NAME, 1);
                context.startActivity(intent0);
            }
        }
    }


    public static void addMsgListener(MsgListener msgListener) {
        msgObservers.add(msgListener);
    }

    public static void removeMsgListener(MsgListener msgListener) {
        msgObservers.remove(msgListener);
    }

    public static void clearListeners() {
        msgObservers.clear();
    }

    public interface MsgListener {
        public void onUpdateCount(Object... args);
    }

    //    1为笔记推荐:跳转到推荐笔记列表
//    2为好友关注:跳转到我的粉丝列表
//    3为收藏笔记:跳转到该笔记详情
//    4为喜欢笔记:跳转到该笔记详情
//    5为评论笔记:跳转到该笔记详情
//    6为私信:跳转到用户私信列表
    private void jumpTo(Context context, String message) {
        try {
            JSONObject msgJson = new JSONObject(message);
            if (msgJson != null) {
                int pType = msgJson.optInt("ptype");
               
                switch (pType) {
                    case 1://首页笔记
                        Intent intent1 = new Intent(context, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("tag", MainActivity.TAB1);
                        intent1.putExtra(FragmentActivity1.POSITION_NAME, 1);
                        context.startActivity(intent1);
                        break;
                    case 2://我的好友列表
                        Intent intent2 = new Intent(context, FriendsActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent2);
                        break;
                    case 3://
                    case 4:
                    case 5://笔记详情
                        if (!TextUtils.isEmpty(msgJson.optString("link_id"))) {
                            Intent intent5 = new Intent(context, CommonDetailActivity.class);
                            intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent5.putExtra("cat_id", msgJson.optString("cat_id"));
                            intent5.putExtra("aid", msgJson.optString("link_id"));
                            context.startActivity(intent5);
                        }
                        break;
                    case 6://消息中心
                        Intent intent6 = new Intent(context, MsgCenterActivity.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent6);
                        break;
                    default:
                        Intent intent0 = new Intent(context, MainActivity.class);
                        intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent0.putExtra("tag", MainActivity.TAB1);
                        intent0.putExtra(FragmentActivity1.POSITION_NAME, 1);
                        context.startActivity(intent0);
                        break;
                }
            }

        } catch (JSONException e) {

        }
    }

}

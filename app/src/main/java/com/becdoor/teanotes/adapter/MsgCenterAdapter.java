package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.ActivitiesDetailActivity;
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.activity.FriendsActivity;
import com.becdoor.teanotes.activity.MsgCenterActivity;
import com.becdoor.teanotes.activity.MyPriMsgActivity;
import com.becdoor.teanotes.activity.PersonalSpaceActivity;
import com.becdoor.teanotes.fragment.FragmentActivity1;
import com.becdoor.teanotes.fragment.MainActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.MsgInfo;
import com.becdoor.teanotes.model.PriMsgInfo;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.StringUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 消息中心消息
 */
public class MsgCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MsgInfo> msgList;
    private List<PriMsgInfo> priMsgList;
    int screenW = 0;
    private int type = 0;//0-通知 1-私信


    public void setMsgList(List<MsgInfo> msgList) {
        this.msgList = msgList;
    }

    public void setPriMsgList(List<PriMsgInfo> priMsgList) {
        this.priMsgList = priMsgList;
    }

    public void setType(int type) {
        this.type = type;
    }


    public MsgCenterAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }

    @Override
    public int getItemViewType(int position) {
        if (type == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new MsgHolder(LayoutInflater.from(mContext).inflate(R.layout.item_msgcenter_msg, null));
        } else {
            return new PriMsgHolder(LayoutInflater.from(mContext).inflate(R.layout.item_msgcenter_primsg, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MsgHolder) {//通知
            if (msgList == null) {
                return;
            }
            final MsgInfo msgInfo = msgList.get(position);
            if (msgInfo != null) {
                ((MsgHolder) holder).titleTv.setText(msgInfo.getTitle());
                ((MsgHolder) holder).timeTv.setText(msgInfo.getSend_time());
                if ("1".equals(msgInfo.getStatus())) {
                    ((MsgHolder) holder).statusIv.setVisibility(View.INVISIBLE);
                } else {
                    ((MsgHolder) holder).statusIv.setVisibility(View.VISIBLE);
                }
            }
            ((MsgHolder) holder).allLayout.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            ((MsgHolder) holder).allLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (msgInfo != null && !TextUtils.isEmpty(msgInfo.getPtype()) && !TextUtils.isEmpty(msgInfo.getLink_id())) {
                        jumpForNotify(mContext, Integer.valueOf(msgInfo.getPtype()), msgInfo.getLink_id());
                        msgResult(msgInfo.getMsg_id());
                    }
                }
            });
        } else if (holder instanceof PriMsgHolder) {
            //私信
            if (priMsgList == null) {
                return;
            }
            final PriMsgInfo priMsgInfo = priMsgList.get(position);
            if (priMsgInfo != null) {
                ((PriMsgHolder) holder).titleTv.setText(priMsgInfo.getMessage());
                ((PriMsgHolder) holder).timeTv.setText(priMsgInfo.getSend_time());
                ((PriMsgHolder) holder).nameTv.setText(priMsgInfo.getSender_name());
                Glide.with(mContext).load(Constant.REALM_NAME + priMsgInfo.getAvatar()).transform(new CircleTransform(mContext)).placeholder(R.drawable.icon_login_user).into(((PriMsgHolder) holder).imageView);
                if ("1".equals(priMsgInfo.getStatus())) {
                    ((PriMsgHolder) holder).statusIv.setVisibility(View.INVISIBLE);
                } else {
                    ((PriMsgHolder) holder).statusIv.setVisibility(View.VISIBLE);
                }
            }
            ((PriMsgHolder) holder).allLayout.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            ((PriMsgHolder) holder).allLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (priMsgInfo != null && !TextUtils.isEmpty(priMsgInfo.getSender_id())) {
                        MyPriMsgActivity.inVoke(mContext, priMsgInfo.getParent_id());
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if (type == 0) {
            return msgList == null ? 0 : msgList.size();
        }
        return priMsgList == null ? 0 : priMsgList.size();
    }

    public class MsgHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView timeTv;
        ImageView statusIv;
        View allLayout;

        public MsgHolder(View view) {
            super(view);
            titleTv = (TextView) view.findViewById(R.id.item_msgCenter_msg_msgTv);
            timeTv = (TextView) view.findViewById(R.id.item_msgCenter_msg_timeTv);
            statusIv = (ImageView) view.findViewById(R.id.item_msgCenter_msg_statusIv);
            allLayout = view.findViewById(R.id.item_msgCenter_msg_allLayout);
        }
    }

    public class PriMsgHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTv;
        TextView timeTv;
        TextView nameTv;
        ImageView statusIv;
        View allLayout;

        public PriMsgHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.item_msgCenter_primsg_avatarIv);
            titleTv = (TextView) view.findViewById(R.id.item_msgCenter_primsg_msgTv);
            timeTv = (TextView) view.findViewById(R.id.item_msgCenter_primsg_timeTv);
            nameTv = (TextView) view.findViewById(R.id.item_msgCenter_primsg_nameTv);
            statusIv = (ImageView) view.findViewById(R.id.item_msgCenter_primsg_statusIv);
            allLayout = view.findViewById(R.id.item_msgCenter_primsg_allLayout);
        }
    }

    private void jumpForNotify(Context context, int ptype, String link_id) {
        switch (ptype) {
            case 0://私信
                MyPriMsgActivity.inVoke(context, link_id);
                break;
            case 1:// 笔记
            case 2://好友
                PersonalSpaceActivity.inVoke(context, link_id);
                break;
            case 3://笔记
            case 4:
            case 5:
                CommonDetailActivity.inVoke(context, "9", link_id);
                break;
        }
    }

    /**
     * 查看消息的回调
     *
     * @param msg_id
     */
    void msgResult(String msg_id) {
        Map<String, String> map = new HashMap<>();
        map.put("msg_id", msg_id);
        NetUtil.postData(mContext, Constant.BASE_URL + "center.php?", map, "notice_info", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
            }

            @Override
            public void onScuccess(String response, int id) {
            }
        });
    }
}


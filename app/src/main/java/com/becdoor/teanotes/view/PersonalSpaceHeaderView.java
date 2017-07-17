package com.becdoor.teanotes.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.SendPrivateLetterActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.SpaceInfo;
import com.becdoor.teanotes.until.NetUtil;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/8.
 * 私人空间的头部
 */

public class PersonalSpaceHeaderView extends LinearLayout implements View.OnClickListener {
    @Bind(R.id.header_personelSpace_bgIv)
    ImageView mBgIv;
    @Bind(R.id.header_personelSpace_avatarIv)
    ImageView mAvatarIv;
    @Bind(R.id.header_personelSpace_nameTv)
    TextView mNameTv;
    @Bind(R.id.header_personelSpace_rankNameTv)
    TextView mRankNameTv;
    @Bind(R.id.header_personelSpace_singnTv)
    TextView mSingnTv;
    @Bind(R.id.header_personelSpace_careLayout)
    View mCareView;
    @Bind(R.id.header_personelSpace_careTv)
    TextView mCareTv;
    @Bind(R.id.header_personelSpace_msgLayout)
    View mMsgView;

    private Context mContext;
    private String uid;

    public PersonalSpaceHeaderView(Context context) {
        super(context);
        init(context);
    }

    public PersonalSpaceHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PersonalSpaceHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.mContext = context;
        View view = View.inflate(context, R.layout.view_header_personelspace, null);

        ButterKnife.bind(this, view);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mMsgView.setOnClickListener(this);
        mCareView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_personelSpace_careLayout://关注
                if (!mCareTv.isSelected()) {
                    //没关注才关注
                    care();
                }
                break;
            case R.id.header_personelSpace_msgLayout://私信
                SendPrivateLetterActivity.inVoke(mContext, uid);
                break;
        }
    }

    /**
     * 设置数据
     *
     * @param spaceInfo
     */
    public void setSpaceInfo(SpaceInfo spaceInfo, int screenW, String uid) {
        this.uid = uid;
        if (spaceInfo != null) {
            mNameTv.setText(spaceInfo.getNickname());
            mRankNameTv.setText(spaceInfo.getRank_name());
            mSingnTv.setText(spaceInfo.getSlogan());
            Glide.with(mContext).load(Constant.REALM_NAME + spaceInfo.getAvatar()).transform(new CircleTransform(mContext)).placeholder(R.drawable.icon_login_user).into(mAvatarIv);
            Glide.with(mContext).load(Constant.REALM_NAME + spaceInfo.getFocus_pic()).placeholder(R.drawable.default_member_bg).into(mBgIv);
            mBgIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (screenW * 0.536)));
            if (spaceInfo.getIs_atten() == 1) {
                mCareTv.setText("已关注");
                mCareTv.setCompoundDrawables(null, null, null, null);
                mCareView.setSelected(true);
            }
        }
    }

    /**
     * 关注
     */
    void care() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        NetUtil.postData(mContext, Constant.BASE_URL + "space.php?", map, "add_fri", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(mContext, "关注成功!", Toast.LENGTH_SHORT).show();
                        mCareTv.setText("已关注");
                        mCareTv.setCompoundDrawables(null, null, null, null);
                        mCareView.setSelected(true);
                    } else {
                        Toast.makeText(mContext, object == null ? "关注失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, "关注失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

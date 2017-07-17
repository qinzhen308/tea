package com.becdoor.teanotes.until;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.model.ThirdUserInfo;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;

/**
 * 分享工具类
 *
 * @author jjj
 * @date 2015-8-24
 */
public class ShareUtil {
    private Activity mContext;
    private String title = "";
    private String content = "";
    private UMImage mUmImage;
    private String targeUrl = " ";
    UMShareAPI mShareAPI;
    private boolean isFirst = true;

    /**
     * 登录授权用
     *
     * @param activity
     */
    public ShareUtil(Activity activity) {
        this.mContext = activity;
        mShareAPI = UMShareAPI.get(mContext);
    }

    public ShareUtil(Activity cActivity, String title, String content,
                     String targeUrl, String imgPath) {
        this.mContext = cActivity;
        this.title = title;
        this.content = content;
        this.targeUrl = targeUrl;
        if (targeUrl == null) {
            targeUrl = "";
        }
        if (TextUtils.isEmpty(imgPath)) {
            mUmImage = new UMImage(mContext, R.drawable.icon);
        } else {
            mUmImage = new UMImage(mContext, imgPath);
        }

    }

    /**
     * 设置自定义的dialog
     *
     * @param dialog
     */
    public void setDialog(Dialog dialog) {
        Config.dialog = dialog;
    }

    public static void initShareData(Context context) {
        Config.isUmengSina = true;
//        Config.isUmengWx = true;
        UMShareAPI.get(context);
//		(app_key(appid) secret
        PlatformConfig.setWeixin("wx6a209844922ce7d3", "7124b9242615ce5bef505e7c4bce6fa0");
        PlatformConfig.setSinaWeibo("2574261642", "88140d2f1cf3ac5c9ba61e6f19bc6d6d");
        //微博回调地址
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (shareListener != null) {
                shareListener.onScuccess();
            }
//            showToastForShare(platform, "成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showToastForShare(platform, "失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showToastForShare(platform, "取消了");
        }
    };

    public void showShareDialog() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this,mPermissionList,123);
//        }
        new ShareAction(mContext).withText(content).withMedia(mUmImage).withTitle(title).withTargetUrl(targeUrl)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .setCallback(umShareListener).open();
    }


    private void showToastForShare(SHARE_MEDIA shareMedia, String content) {
        String name = "微信分享";
        if (shareMedia == SHARE_MEDIA.SINA) {
            name = "微博分享";
        } else if (shareMedia == SHARE_MEDIA.WEIXIN_CIRCLE) {
            name = "微信朋友圈分享";
        } else if (shareMedia == SHARE_MEDIA.WEIXIN_FAVORITE) {
            name = "微信收藏";
        }
        Toast.makeText(mContext, name + content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 三方登录
     */
    public void doThirdLogin(SHARE_MEDIA shareMedia) {

        isFirst = true;
        mShareAPI.doOauthVerify(mContext, shareMedia, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Log.e("--onComplete-", isFirst + "--" + isFirst + "platform==" + platform + "--action==" + action + "--data==" + data.toString());
            if (isFirst) {
                isFirst = false;
                mShareAPI.getPlatformInfo(mContext, platform, umAuthListener);
            } else {
                //获取用户信息
                ThirdUserInfo thirdUserInfo = new ThirdUserInfo();
                if (platform == SHARE_MEDIA.SINA) {
                    ThirdUserInfo.SinaUser sinaUser = new ThirdUserInfo.SinaUser();
                    sinaUser.setAccess_token(data.get("access_token"));
                    sinaUser.setUid(data.get("uid"));
                    sinaUser.setScreen_name(data.get("screen_name"));
                    sinaUser.setProfile_image_url(data.get("profile_image_url"));
                    thirdUserInfo.setSinaUser(sinaUser);
                } else if (platform == SHARE_MEDIA.WEIXIN) {

                    ThirdUserInfo.WXUser wxUser = new ThirdUserInfo.WXUser();
                    wxUser.setAccess_token(data.get("access_token"));
                    wxUser.setOpenid(data.get("openid"));
                    wxUser.setNickname(data.get("screen_name"));
                    wxUser.setSex(data.get("gender"));
                    wxUser.setProvince(data.get("prvinice"));
                    wxUser.setCity(data.get("city"));
                    wxUser.setCountry(data.get("country"));
                    wxUser.setHeadimgurl(data.get("profile_image_url"));
                    wxUser.setPrivilege(data.get("[特权信息 ]"));
                    wxUser.setUnionid(data.get("unionid"));
                    thirdUserInfo.setWxUser(wxUser);
                }

                if (thirdLoginCallBack != null) {
                    thirdLoginCallBack.onScuccess(platform, thirdUserInfo);
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            if (thirdLoginCallBack != null) {
                thirdLoginCallBack.onFial(platform, t.getMessage().toString());
            }
//            Log.e("--onError-", "platform==" + platform + "--action==" + action + "--Throwable==" + t.getMessage().toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            Log.e("--onError-", "platform==" + platform + "--action==" + action);
            if (thirdLoginCallBack != null) {
                thirdLoginCallBack.onCancel(platform);
            }
        }
    };
    private ThirdLoginCallBack thirdLoginCallBack;

    public void setThirdLoginCallBack(ThirdLoginCallBack thirdLoginCallBack) {
        this.thirdLoginCallBack = thirdLoginCallBack;
    }

    public interface ThirdLoginCallBack {
        void onScuccess(SHARE_MEDIA platform, ThirdUserInfo thirdUser);

        void onFial(SHARE_MEDIA platform, String fialMsg);

        void onCancel(SHARE_MEDIA platform);
    }

    private ShareListener shareListener;

    public void setShareListener(ShareListener shareListener) {
        this.shareListener = shareListener;
    }

    public interface ShareListener {
        void onScuccess();
    }
}

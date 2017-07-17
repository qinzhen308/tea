package com.becdoor.teanotes.wxapi;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.weixin.callback.WXCallbackActivity;
import com.umeng.weixin.handler.UmengWXHandler;


public class WXEntryActivity extends WXCallbackActivity {
    UmengWXHandler umengWXHandler;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

//    @Override
//    protected void handleIntent(Intent intent){
//
//        umengWXHandler.setAuthListener(new UMAuthListener() {
//            @Override
//            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
////                Log.e("UMWXHandler fsdfsdfs");
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA platform, int action) {
//
//            }
//        });
////        super.handleIntent(intent);
//    }

}

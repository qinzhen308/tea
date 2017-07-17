package com.becdoor.teanotes.service;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.becdoor.teanotes.global.Constant;


/**
 * 检测安装更新文件的助手类
 * <p>
 * Created by Administrator on 2016/10/11.
 */

public class UpdateApkService extends Service {

    /***
     * 安卓系统下载类
     *
     * @param intent
     * @return
     */
    DownloadManager mManager;
    DownloadCompleteReceiver mReceiver;

    private void initDownManager() {
        mManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mReceiver = new DownloadCompleteReceiver();
        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(Constant.APK_URL));
//设置网络类型 wifi才可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //通知栏xianshi
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //显示下载
        down.setVisibleInDownloadsUi(true);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initDownManager();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //注销广播
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);

        }
        super.onDestroy();
    }

    /***
     * 接受完成后的intent
     */
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
//获取下载文件的id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//自动安装apk
                installAPK(mManager.getUriForDownloadedFile(downId));
//停止服务并关闭广播
                UpdateApkService.this.stopSelf();
            }
        }
    }

    /***
     * 安装apk文件
     */
    private void installAPK(Uri apk) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(apk).setDataAndType(apk, "application/vnd.android.package-archive").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}

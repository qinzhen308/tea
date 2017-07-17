package com.becdoor.teanotes.until;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/10/10.
 */

public class AppUtil {
    /**
     * 获取app 版本名称
     */
    public static String localVersionName(Context ct) {

        String versionName = null;
        try {
            versionName = ct.getPackageManager().getPackageInfo(ct.getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 安装apk
     */

    public static void installallAPk(Context ct, Uri apkuri) {
        //通过internt 安装apk文件
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(apkuri);
        intent.setDataAndType(apkuri, "application/vnd.android.package-archive");
        ct.startActivity(intent);

    }

    /**
     * 获取屏幕的宽度和高度
     *
     * @param activity
     * @return
     */
    public static int[] getScreenWH(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        return new int[]{w, h};
    }
}

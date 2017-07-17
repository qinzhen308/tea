package com.becdoor.teanotes;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;

import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.until.ImageUploader;
import com.becdoor.teanotes.until.PreferencesUtils;
import com.becdoor.teanotes.until.Remember;
import com.becdoor.teanotes.until.ScreenUtil;
import com.becdoor.teanotes.until.ShareUtil;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/11/3.
 */

public class DJApplication extends Application {

    private static DJApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        Remember.init(getApplicationContext(), "douji");

        AppStatic.isLogin = Remember.getBoolean(AppStatic.isLoginName, false);
        Constant.VALUE_ACCESS_TOKEN = Remember.getString(Constant.ACCESS_TOKEN, "");
        AppStatic.jpush_id = Remember.getString("jpush_id", "");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());

        ShareUtil.initShareData(getApplicationContext());
        instance=this;
        ScreenUtil.setContextDisplay(this);
        ImageUploader.init();

        CrashReport.initCrashReport(getApplicationContext(), "900059781", true);
    }

    public static DJApplication getInstance(){
        return instance;
    }

    public void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    public boolean isNewVison() {
        int thisVison = getVersionCode();
        int dbVison = PreferencesUtils.getInteger("current_app_vison");
        return thisVison != dbVison;
    }

    public int getVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }




}

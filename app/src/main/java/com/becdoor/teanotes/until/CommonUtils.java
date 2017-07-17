package com.becdoor.teanotes.until;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CommonUtils {


    public static String getVersionName(Context mContext) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException mE) {
            mE.printStackTrace();
        }
        String version = packInfo.versionCode + "";
        return version;
    }
    /***
     * devicesID
     */
    public static String mDeviceId;
    public static String getIMEI(Context context){
        if(!TextUtils.isEmpty(mDeviceId)){
            return mDeviceId;
        }
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mDeviceId = tm.getDeviceId();
        } catch (Exception e) {
            mDeviceId =System.currentTimeMillis()+
                    "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length()
                    % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length()
                    % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                    + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10
                    + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        }
        if (TextUtils.isEmpty(mDeviceId)) {
            mDeviceId =
                    System.currentTimeMillis()+  "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length()
                            % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length()
                            % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                            + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10
                            + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        }
        return mDeviceId;
    }
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    private static boolean isNetworkAvailiable(Context context){

        ConnectivityManager connectivity= (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivity!=null){
            NetworkInfo info=connectivity.getActiveNetworkInfo();
            if (info!=null&& info.isConnected()){

                //当前网络是连接的
                if (info.getState()==NetworkInfo.State.CONNECTED){
                    //当前网络可用
                    return true;
                }
            }


        }
        return  false;
    }
    public static  String isRandomLine="isRandomLine";
//    public static boolean checkSelfPermission(Context context, String permission){
//
//        int hasWriteContactPermission= ContextCompat.checkSelfPermission(context,permission);
//        return hasWriteContactPermission!=PackageManager.PERMISSION_GRANTED;
//    }
//    public static boolean isEmail(String email) {
//        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
//        Pattern p = Pattern.compile(str);
//        Matcher m = p.matcher(email);
//
//        return m.matches();
//    }
//    public static String  getMD5KW(String kw){
//        return MD5Utils.getStringMD5(kw);
//    }
}

package com.becdoor.teanotes.global;

/**
 * Created by Administrator on 2016/10/10.
 * 常用接口存储地址
 */

public class Constant {
    //登录之后返回的 ACCESS_TOKEN值 和app_key
    public static String VALUE_ACCESS_TOKEN = "";
    public static String DJ_APP_KEY = "dj_app_key";


    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "pwd";
    public static final String APP_KEY = "app_key";
    public static final String ACCOUNT = "account";
    public static final int PAZE_SIZE = 10;
    /**
     * 是否为调试模式
     */

    public static boolean isDebug = false;
    /***
     * access_token 初始化
     */
    public static final String ACCESS_TOKEN = "access_token";
    /***
     * 测试使用地址
     */
    public static final String REALM_NAME = "http://test4.m1ju.com/";
    public static final String BASE_URL = REALM_NAME+"api/";
    /***
     * 正式使用域名
     */
//    public  static final  String BASE_URL="dj.com/api/api.php?";
    /***
     * 下载地址
     */
    public static final String APK_URL = "";
    //dj.com/api/api.php?app_key=dj_app_key&act=getLeadPic
    //dj.com/api/api.php?app_key=dj_app_key&act=reg
    //http://test4.m1ju.com/dj_wiki/wiki.php?m=Index&c=Index&a=index&t=dj_showNote
    /**
     * 会员注册
     * get
     */
    public static final String REGISTER = "app_key=dj_app_key&act=reg";
    /***
     * 执行会员注册
     * get
     */
    public static final String REG = "reg";
    /**
     * 登录
     */
    public static final String LOGIN = "login";
    /***
     * 获取引导图片接口
     * get
     */

    public static final String GETLEADPIC = "getLeadPic";
    /***
     * 验证码接口
     * get
     */
    public static final String SENDCODE = "sendCode";
    /***
     * 斗记默认首页接口
     * get
     */
    public static final String INDEX = "index";
    /***
     * 首页笔记最新发布接口
     * get
     */
    public static final String BESTNEW = "bestNew";
    /***
     * 首页 发布笔记
     * get
     */
    public static final String SEENOTE = "seeNote";
    /***
     * 首页 发布笔记
     * get
     */
    public static final String TIMER = "timer";

    /***
     * 首页 发布笔记
     * get
     */
    public static final String STORAGE = "storage";
    /***
     * 首页 发布笔记
     * get
     */
    public static final String MEMO = "memo";
    /***
     * get
     */
    public static final String SPECIAL = "special";
    /***
     * 首页 发布笔记
     * get
     */
    public static final String SPEC_INFO = "TIMER";
    /***
     * 首页 分享
     * get
     */
    public static final String SHARE = "share";
    /***
     * get
     */
    public static final String PRAISE = "praise";

    /***
     * get
     */
    public static final String NOTERE = "noteRe";

    /***
     * get
     */
    public static final String DOSHARE = "doshare";
    public static final String ACT = "act";
    public static final String TYPE = "type";
    public static final String TEL = "trl";
    public static final String REG_ID = "reg_id";
    //register
    public static final String TEMP_ACCESS_TOKEN = "temp_access_token";
    public static final String CODE_OUT_TIME = "time";
    public static final String CODE_YCODE = "ycode";
    public static final String CODE_ACCOUNT = "account";
    public static final String AFFIRMCODE = "affirmCode ";
    public static final String REGISTERACCOUNT = "account";

    public static final String HOME_NOTE= "noteRe";//首页精选接口

}

package com.becdoor.teanotes.data.api;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.POST;

/**
 * Created by Administrator on 2016/10/10.
 */

public interface HDApiService {
    /**
     * Call<ResponseBody> :Call是必须的,ResponseBody是Retrofit默认的返回数据类型,也就是String体
     */
    @POST("/Interface/add_user")
    Call<ResponseBody> getUserInfo(@Field("signid") String signid);

    /**
     * 执行注册 post
     * @param app_key app 标识
     * @param act doreg
     * @param username 用户名
     * @param pwd 密码
     * @param confirm 确认密码
     * @param vceode 验证码
     * @param temp_access_token
     * @return
     */
    @POST("/Interface/register")
    Call<ResponseBody> getRegister(@Field("app_key" ) String app_key,@Field("act")String act,@Field("username")String username,@Field("pwd")String pwd,
                                   @Field("confirm")String confirm,@Field("vcode")String vceode,@Field("temp_access_token")String temp_access_token);

    /**
     * 会员登录 login
     * @param app_key
     * @param act
     * @param account
     * @param pwd
     * @param type
     * @param reg_id
     * @param reg_pass
     * @return
     */
    @POST("/Interface/login")
Call<ResponseBody>getLogin(@Field("app_key")String app_key,@Field("act")String act,@Field("account")String account,@Field("pwd")String pwd,
                           @Field("type")String type,@Field("reg_id")String reg_id,@Field("reg_pass")String reg_pass);

    /**
     * 确定验证码
     * @param account
     * @param ycode
     * @param temp_access_token
     * @param app_key
     * @param act
     * @return
     */
    @POST("/Interface/getcode")
    Call<ResponseBody>getcode(@Field("account")String account,@Field("ycode") String ycode,@Field("temp_access_token") String temp_access_token,
                              @Field("app_key") String app_key,@Field("act") String act);

    /**
     * 填写新密码接口
     * @param app_key
     * @param act
     * @param account
     * @param pwd
     * @param confirm
     * @return
     */
   @POST("/Interface/newPassword")
    Call<ResponseBody>newPassword(@Field("app_key") String app_key ,@Field("act") String act,@Field("account") String account,@Field("pwd") String pwd,
                                  @Field("confirm") String confirm);

    /**
     *
     * @param content 内容
     * @param app_key
     * @param access_token
     * @param act spen_comm
     * @param uid
     * @param username
     * @param aid
     * @param title
     * @return
     */
    @POST("/Interface/getComment")
    Call<ResponseBody>getComment(@Field("content") String content,@Field("app_key") String app_key,@Field("access_token") String access_token,
                                 @Field("act") String act,@Field("uid") String uid,@Field("username") String username,@Field("aid") String aid,
                                 @Field("titlt") String title);

    /**
     * 执行搜索接口
     * @param app_key
     * @param access_token
     * @param act
     * @param page
     * @param type
     * @param cat_id
     * @param keyword
     * @return
     */
    @POST("/Interface/getSeatch")
    Call<ResponseBody>getSearch(@Field("app_key") String app_key,@Field("access_token") String access_token,@Field("act") String act,
                                @Field("page") int  page ,@Field("type") String type ,@Field("cat_id") int cat_id ,@Field("keywords") String keyword);

}

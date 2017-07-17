package com.becdoor.teanotes.until;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.becdoor.teanotes.R;
import com.bumptech.glide.Glide;
/**
 * Created by Administrator on 2016/10/20.
 * 图片加载工具类
 */

public class ImageLoaderUtils {

    public  static  void display(Context context, ImageView imageView,String url,int
                                 placeholder,int error){
        if (imageView==null){

            throw new  IllegalArgumentException("argument error");


        }
        Glide.with(context).load(url).placeholder(placeholder).error(error).crossFade().into(imageView);

    }
public  static  void  display(Context context,ImageView imageView,String url){

    if (imageView==null){
        throw  new IllegalArgumentException("argument error");


    }
    Glide.with(context).load(url).placeholder(R.mipmap.ic_image_loading)
            .error(R.mipmap.ic_image_loadfail).crossFade().into(imageView);

}
}

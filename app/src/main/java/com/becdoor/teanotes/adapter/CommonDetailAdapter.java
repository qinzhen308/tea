package com.becdoor.teanotes.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.NoteElement;
import com.becdoor.teanotes.until.ScreenUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 * 精选详情等适配器
 */

public class CommonDetailAdapter extends BaseAdapter {
    private Context mContext;
    private final static int TYPE_ONE = 0;//图片
    private final static int TYPE_TWO = 1;//文字
    private final static int TYPE_THREE = 2;//地址
    private List<NoteElement> mList;

    public CommonDetailAdapter(Context context) {
        this.mContext = context;
    }

    public void setmList(List<NoteElement> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemViewType(int position) {
        NoteElement bean = getItem(position);
        if (bean.name.equals("img")) {
            return TYPE_ONE;
        } else if (bean.name.equals("content")) {
            return TYPE_TWO;
        } else if (bean.name.equals("address")) {
            return TYPE_THREE;
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public NoteElement getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NoteElement artArrBean = getItem(position);
        if (artArrBean != null) {

            int type = getItemViewType(position);
            switch (type) {
                case TYPE_ONE:
                    ImageViewHolder imageViewHolder = null;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_commondetail_iv, null);
                        imageViewHolder = new ImageViewHolder(convertView);
                        convertView.setTag(imageViewHolder);
                    } else {
                        imageViewHolder = (ImageViewHolder) convertView.getTag();
                    }
//                    imageViewHolder.imageView.setTag(artArrBean.getEnableValue());
                    imageViewHolder.imageView.setTag(R.id.imageloader_uri,artArrBean.getEnableValue());
                    final ImageView iv=imageViewHolder.imageView;
                    Glide.with(mContext).load(Constant.REALM_NAME + artArrBean.getEnableValue()).into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onStart() {
                            super.onStart();
//                            String tag=(String)iv.getTag(R.id.imageloader_uri);
                            ViewGroup.LayoutParams lp=iv.getLayoutParams();
                            lp.height= ScreenUtil.WIDTH - 2*mContext.getResources().getDimensionPixelSize(R.dimen.margin_10);
                            iv.setLayoutParams(lp);
                            iv.setImageResource(R.drawable.ic_stub);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            Log.d("glide",e!=null?e.toString():"未知错误");
                        }


                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            String tag=(String)iv.getTag(R.id.imageloader_uri);
                            if(!tag.equals(artArrBean.getEnableValue()))return;
                            if(resource!=null){
                                float w=(float)resource.getIntrinsicWidth();
                                float h=(float)resource.getIntrinsicHeight();
                                float scale=h/w;
                                Log.d("glide","load----w,h="+w+","+h);
                                ViewGroup.LayoutParams lp=iv.getLayoutParams();
                                lp.height= (int)((ScreenUtil.WIDTH - 2*mContext.getResources().getDimensionPixelSize(R.dimen.margin_10))*scale);
                                iv.setLayoutParams(lp);
//                                iv.setImageDrawable(resource);
                                Glide.with(mContext).load(Constant.REALM_NAME + artArrBean.getEnableValue()).centerCrop().into(iv);
                            }

                        }
                    });
                    break;
                case TYPE_TWO:
                    TextViewHolder textViewHolder = null;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_commondetail_tv, null);
                        textViewHolder = new TextViewHolder(convertView);
                        convertView.setTag(textViewHolder);
                    } else {
                        textViewHolder = (TextViewHolder) convertView.getTag();
                    }
                    textViewHolder.textView.setText(artArrBean.getEnableValue());
                    break;
                case TYPE_THREE:

                    AreaHolder areaHolder = null;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_commondetail_area, null);
                        areaHolder = new AreaHolder(convertView);
                        convertView.setTag(areaHolder);
                    } else {
                        areaHolder = (AreaHolder) convertView.getTag();
                    }
                    areaHolder.areaTv.setText(artArrBean.getEnableValue());
                    break;
            }
        }
        return convertView;
    }

    class ImageViewHolder {
        ImageView imageView;

        ImageViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.item_commonDetail_iv);
        }
    }

    class TextViewHolder {
        TextView textView;

        TextViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.item_commonDetail_tv);
        }
    }

    class AreaHolder {
        TextView areaTv;

        AreaHolder(View view) {
            areaTv = (TextView) view.findViewById(R.id.item_commonDetail_areaTv);
        }
    }
}

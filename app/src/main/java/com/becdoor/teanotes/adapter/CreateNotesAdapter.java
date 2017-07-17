package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.CreateAtticleActivity;
import com.becdoor.teanotes.activity.EditActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.Article;
import com.becdoor.teanotes.model.NoteElement;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.ImageLoaderUtils;
import com.becdoor.teanotes.until.ImageUploader;
import com.becdoor.teanotes.until.ImageUtil;
import com.becdoor.teanotes.until.ScreenUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mobeta.android.dslv.DragSortListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulz on 2016/10/31.
 */

public class CreateNotesAdapter extends AbsAdapter<NoteElement>{
    private String[] classes={NoteElement.TYPE_TEXT,NoteElement.TYPE_ADDRESS,NoteElement.TYPE_IMG};
    private Dialog mDialog;

    public CreateNotesAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type=getItemViewType(position);
        if(type==0){
            convertView=getTextTypeView(position,convertView,parent);
        }else if(type==1){
            convertView=getAddressTypeView(position,convertView,parent);
        }else if(type==2){
            convertView=getImgTypeView(position,convertView,parent);
        }
        return convertView;
    }

    public View getTextTypeView(final int position, View convertView, final ViewGroup parent){
        TextViewHolder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext,R.layout.item_create_note_content_layout,null);
            holder=new TextViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(TextViewHolder)convertView.getTag();
        }
        final NoteElement e=getItem(position);
        if(e.value!=null&& !TextUtils.isEmpty(e.value.value)){
            holder.content.setText(e.value.value);
        }else {
            holder.content.setText("");
        }
       /* holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.value!=null){
                    EditActivity.invoke(mContext,e.value.value,position);
                }else {
                    EditActivity.invoke(mContext,"",position);
                }
            }
        });*/
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.value!=null){
                    EditActivity.invoke(mContext,e.value.value,position);
                }else {
                    EditActivity.invoke(mContext,"",position);
                }
            }
        });
        return convertView;
    }

    public View getAddressTypeView(int position, View convertView, ViewGroup parent){
        AddressViewHolder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext,R.layout.item_create_note_address_layout,null);
            holder=new AddressViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(AddressViewHolder)convertView.getTag();
        }
        final NoteElement e=getItem(position);
        if(e.value!=null&& !TextUtils.isEmpty(e.value.value)){
            holder.tvAddress.setText(e.value.value);
        }else {
            holder.tvAddress.setText("");
        }
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditAddressDialog(e);
            }
        });
        return convertView;
    }

    EditText etAddress;
    ImageView ivCancel;
    Button btnOk;
    private void showEditAddressDialog(final NoteElement element){
        if(mDialog==null){
            View view= LayoutInflater.from(mContext).inflate(R.layout.dialog_simple_edit_address,null);
            btnOk=(Button) view.findViewById(R.id.btn_ok);
            etAddress=(EditText) view.findViewById(R.id.et_address);
            ivCancel=(ImageView) view.findViewById(R.id.iv_cancel);
            mDialog= DialogUtil.getCenterDialog(mContext,view);
        }
        String address=element.getEnableValue();
        if(!TextUtils.isEmpty(address)){
            etAddress.setText(address);
        }else {
            etAddress.setText("");
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mContext.isFinishing())DialogUtil.dismissDialog(mDialog);
                element.setEnableValue(etAddress.getText().toString());
                notifyDataSetChanged();
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mContext.isFinishing())DialogUtil.dismissDialog(mDialog);
            }
        });
        DialogUtil.showDialog(mDialog);
    }

    public View getImgTypeView(final int position, View convertView, final ViewGroup parent){
        PicViewHolder holder=null;
        if(convertView==null){
            convertView=View.inflate(mContext,R.layout.item_create_note_img_item,null);
            holder=new PicViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(PicViewHolder)convertView.getTag();
        }
        final NoteElement e=getItem(position);
        holder.ivPic.setTag(e.getEnableValue());
        final ImageView iv=holder.ivPic;
        if(e.value!=null&& !TextUtils.isEmpty(e.getEnableValue())){
            if(e.isNative()){
                Glide.with(mContext).load(new File(e.getEnableValue())).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        if(!iv.getTag().equals(e.getEnableValue()))return;
                        if(resource!=null){
                            float w=(float)resource.getIntrinsicWidth();
                            float h=(float)resource.getIntrinsicHeight();
                            float scale=h/w;
                            ViewGroup.LayoutParams lp=iv.getLayoutParams();
                            lp.height= (int)((ScreenUtil.WIDTH - 2*mContext.getResources().getDimensionPixelSize(R.dimen.margin_10))*scale);
                            iv.setLayoutParams(lp);
                            iv.setImageDrawable(resource);
                        }

                    }
                });
//                holder.ivPic.setImageURI(Uri.fromFile(new File(e.getEnableValue())));

            }else {
                Glide.with(mContext).load(Constant.REALM_NAME+e.getEnableValue()).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        if(!iv.getTag().equals(e.getEnableValue()))return;
                        if(resource!=null){
                            float w=(float)resource.getIntrinsicWidth();
                            float h=(float)resource.getIntrinsicHeight();
                            float scale=h/w;
                            ViewGroup.LayoutParams lp=iv.getLayoutParams();
                            lp.height= (int)((ScreenUtil.WIDTH - 2*mContext.getResources().getDimensionPixelSize(R.dimen.margin_10))*scale);
                            iv.setLayoutParams(lp);
                            iv.setImageDrawable(resource);
                        }

                    }
                });
//                Glide.with(mContext).load(Constant.REALM_NAME+e.getEnableValue()).into(holder.ivPic);
            }
            holder.layoutAdd.setVisibility(View.GONE);
            holder.layoutPic.setVisibility(View.VISIBLE);
            holder.ivEdit.setVisibility(View.VISIBLE);
            holder.ivAdd.setVisibility(View.VISIBLE);
        }else {
            holder.layoutAdd.setVisibility(View.VISIBLE);
            holder.layoutPic.setVisibility(View.GONE);
            holder.ivEdit.setVisibility(View.GONE);
            holder.ivAdd.setVisibility(View.GONE);
        }
        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //不做任何事情,只是为了阻止点击事件透传到父控件
            }
        });
        holder.layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateAtticleActivity)mContext).showAddPicAlert(position);
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateAtticleActivity)mContext).showAddPicAlert(position);
            }
        });
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteElement e=new NoteElement(NoteElement.TYPE_IMG);
                insert(e,position+1);
                ((ListView)parent).setSelection(position+1);
            }
        });
        return convertView;
    }


    @Override
    public int getViewTypeCount() {
        return classes.length;
    }

    @Override
    public int getItemViewType(int position) {
        for(int i=0,size=classes.length;i<size;i++){
            if(classes[i].equals(getItem(position).getNamge()) ){
                return i;
            }
        }
        return 0;
    }

    static class TextViewHolder {
        TextView content;

        public TextViewHolder(View root){
            content=(TextView)root.findViewById(R.id.content_et);
//            tvIndex=(TextView)root.findViewById(R.id.index);
        }

    }

    static class PicViewHolder {
        View layoutPic;
        View layoutAdd;
        ImageView ivEdit;
        ImageView ivAdd;
        ImageView ivPic;
        ImageView ivRemove;

        public PicViewHolder(View root){
            layoutPic=root.findViewById(R.id.general_pic_layout);
            layoutAdd=root.findViewById(R.id.rl_general_add_pic);
            ivEdit=(ImageView)root.findViewById(R.id.btn_edit_pic);
            ivAdd=(ImageView)root.findViewById(R.id.btn_add_pic);
            ivPic=(ImageView)root.findViewById(R.id.create_tea_img_iv);
            ivRemove=(ImageView)root.findViewById(R.id.click_remove);
        }
    }

    static class AddressViewHolder {
        TextView tvAddress;
        TextView btnEdit;

        public AddressViewHolder(View root){
            tvAddress=(TextView) root.findViewById(R.id.address_tv);
            btnEdit=(TextView)root.findViewById(R.id.update_address_btn);
        }
    }

    public List<NoteElement> getNativeImgItems(){
        List<NoteElement> list=new ArrayList<>();
        for(int i=0,size=getCount();i<size;i++){
            NoteElement e=getItem(i);
            if(e.isNative()){
                list.add(e);
            }
        }
        return list;
    }
}

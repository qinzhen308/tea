package com.becdoor.teanotes.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.ActivityManagerActivity;
import com.becdoor.teanotes.activity.CollectionActivity;
import com.becdoor.teanotes.activity.CommentManagerActivity;
import com.becdoor.teanotes.activity.FriendsActivity;
import com.becdoor.teanotes.activity.MsgCenterActivity;
import com.becdoor.teanotes.activity.MyInfoActivity;
import com.becdoor.teanotes.activity.NoteActivity;
import com.becdoor.teanotes.activity.SpaceManagerActivity;
import com.becdoor.teanotes.activity.TeaAtticleManagerActivity;
import com.becdoor.teanotes.activity.AccountActivity;
import com.becdoor.teanotes.activity.TeaCollectActivity;
import com.becdoor.teanotes.adapter.DrawerAdapter;
import com.becdoor.teanotes.model.UserInfoBean;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.ImageUploader;
import com.becdoor.teanotes.until.ImageUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.Remember;
import com.becdoor.teanotes.view.CircleTransform;
import com.becdoor.teanotes.view.ReGridView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/14.
 * 会员界面
 */
public class FragmentMember extends BaseFragment implements View.OnClickListener {
    private final static int TAKE_PHOTO = 100;// 拍照
    private final static int TAKE_PICTURE = 200;// 本地获取
    private final static int TAKE_CROP = 300;// 裁剪

    @Bind(R.id.f_member_gridView)
    ReGridView mGridView;
    @Bind(R.id.f_member_bgIv)
    ImageView mBgIv;
    @Bind(R.id.f_member_changeBgIv)
    ImageView mChangeBgIv;
    @Bind(R.id.header_member_avatarIv)
    ImageView mAvatarIv;
    @Bind(R.id.header_member_rankNameTv)
    TextView mRankNameTv;
    @Bind(R.id.header_member_nameTv)
    TextView mNameTv;
    @Bind(R.id.header_member_pointsTv)
    TextView mPointsTv;
    @Bind(R.id.header_member_noteTv)
    TextView mNoteTv;
    @Bind(R.id.header_member_msgTv)
    TextView mMsgTv;
    @Bind(R.id.header_member_msgIv)
    ImageView mMsgIv;
    @Bind(R.id.f_member_bgLayout)
    RelativeLayout mBgLayout;

    private DrawerAdapter adapter;
    private int screenW = 0;

    private Dialog mDialog;
    private Button topBtn;
    private Button middleBtn;
    private Button cancelBtn;
    private String mFilePath;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        getMsgCount();
    }

    void getData() {
        NetUtil.getData(getActivity(), Constant.BASE_URL + "center.php?", null, "center", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        JSONObject object1 = object.getJSONObject("data");
                        if (object1 != null) {
                            int noRead_msg = object1.optInt("noRead_msg");
                            //背景
                            String cover = object1.getString("cover");
                            if (!TextUtils.isEmpty(cover)) {
                                Glide.with(getActivity()).load(Constant.REALM_NAME + cover).placeholder(R.drawable.default_member_bg).into(mBgIv);
                            }
//                            mMsgTv.setText(String.valueOf(noRead_msg));

                            UserInfoBean userInfoBean = new Gson().fromJson(object1.getJSONObject("member_info").toString(), UserInfoBean.class);
                            if (userInfoBean != null) {
                                Glide.with(getActivity()).load(Constant.REALM_NAME + userInfoBean.getPic()).transform(new CircleTransform(getActivity())).placeholder(R.drawable.icon_login_user).into(mAvatarIv);
                                Remember.putString("key_user_name",userInfoBean.getUsername());
                                Remember.putString("key_rank_name",userInfoBean.getRank_name());
                                Remember.putString("key_avatar",userInfoBean.getPic()==null?"":userInfoBean.getPic());
                                mRankNameTv.setText(userInfoBean.getRank_name());
                                mNameTv.setText(userInfoBean.getUsername());
                                mNoteTv.setText(userInfoBean.getSigned());
                                if (userInfoBean.getPoint() > 9999) {
                                    mPointsTv.setText("茶籽：" + "9999+");
                                } else {
                                    mPointsTv.setText("茶籽：" + userInfoBean.getPoint());
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void getMsgCount() {
        NetUtil.getData(getActivity(), Constant.BASE_URL + "api.php?", null, "get_nrCount", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        int msg = object.getInt("noRead_msg");
                        if (msg <= 0) {
                            mMsgTv.setVisibility(View.GONE);
                        } else {
                            mMsgTv.setVisibility(View.VISIBLE);
                            mMsgTv.setText(String.valueOf(msg));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        screenW = AppUtil.getScreenWH(getActivity())[0];
        int height = (int) (screenW * 0.539);
        mBgLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        adapter = new DrawerAdapter(getActivity(), screenW);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://空间
                        startActivity(new Intent(getActivity(), SpaceManagerActivity.class));
                        break;
                    case 1://账号
                        startActivity(new Intent(getActivity(), AccountActivity.class));
                        break;
                    case 2://笔记
                        TeaAtticleManagerActivity.invoke(getActivity());
                        break;
                    case 3://藏茶
                        TeaCollectActivity.invoke(getActivity());
                        break;
                    case 4://便签
                        NoteActivity.invoke(getActivity());
                        break;
                    case 5://活动
                        startActivity(new Intent(getActivity(), ActivityManagerActivity.class));
                        break;
                    case 6://好友
                        FriendsActivity.invoke(getActivity());
                        break;
                    case 7://收藏
                        CollectionActivity.invoke(getActivity());
                        break;
                    case 8://评论
                        CommentManagerActivity.invoke(getActivity());
                        break;
                }
            }
        });


    }

    void initListener() {
        mChangeBgIv.setOnClickListener(this);
        mMsgIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_member_changeBgIv:
                showDialog();
                break;
            case R.id.header_member_msgIv:
                MsgCenterActivity.inVoke(getActivity());
                break;
            case R.id.photograph_btn:
                mDialog.dismiss();
                //拍照
                File file = new File(ImageUtil.filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(ImageUtil.filePath, "123.jpg")));
                startActivityForResult(intent1, TAKE_PHOTO);

                break;
            case R.id.select_photo_btn:
                mDialog.dismiss();
                //选取图片
                Intent intent2 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent2.setType("image/*");
                startActivityForResult(intent2, TAKE_PICTURE);

                break;
            case R.id.cancel_btn:
                mDialog.dismiss();
                break;
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            String sdCardState = Environment.getExternalStorageState();
            if (!sdCardState.equals(Environment.MEDIA_MOUNTED)) {
                return;
            } else {

                switch (requestCode) {
                    case TAKE_PHOTO:
                        mFilePath = ImageUtil.filePath + "123.jpg";
                        mFilePath = ImageUtil.bitmap2File(mFilePath,
                                new Date().getTime() + ".jpg");

                        File file = new File(mFilePath);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (Exception e) {
                            }
                        }
//                        changeBg(mFilePath, file, Uri.fromFile(file));
                        startPhotoZoom(Uri.fromFile(file), 100);
//                        File imgFile = new File(ImageUploader.getRealPathFromURI(getActivity(), uri));
////                                    changeBg("123.jpg", imgFile,uri);
                        break;
                    case TAKE_CROP:// // 裁剪成功后显示图片
                        Uri dataUri = data.getData();
                        if(dataUri==null){
                            dataUri=ImageUploader.temp_img_crop_uri;
                        }
                        File imgFile = new File(ImageUploader.getRealPathFromURI(getActivity(), dataUri));
//                        String fileName = new Date().getTime() + ".jpg";
                        changeBg(imgFile.getName(), imgFile);

//                        if (data != null) {
////                            Bitmap bitmap = data.getParcelableExtra("data");
//                            Bundle bundle = data.getExtras();
//                            if (bundle != null) {
//                                bitmap = bundle.getParcelable("data");
//                                if (bitmap != null) {
//                                    String fileName = new Date().getTime() + ".jpg";
//                                    File file1 = ImageUtil.saveImag(bitmap, fileName);
//                                    changeBg(fileName, file1);
//                                    bitmap.recycle();
//                                    bitmap = null;
//                                }
//                            }
//                        }

                        break;
                    case TAKE_PICTURE:

                        Uri imgUri_2 = data.getData();
//                        changeBg("123.jpg", new File(ImageUploader.getRealPathFromURI(getActivity(), imgUri_2)), imgUri_2);
                        startPhotoZoom(imgUri_2, 100);
                        break;
                }

            }
        }
    }


    void changeBg(String filename, File file) {
        NetUtil.postDataWhithFile(getActivity(), Constant.BASE_URL + "center.php?", null, "cover", filename, file, "change_cover", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        getData();
//                        mBgIv.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(getActivity(), object == null ? "修改失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "修改失败!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 跳转至系统截图界面进行截图
     *
     * @param data
     * @param size
     */
    private void startPhotoZoom(Uri data, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");
        //记录剪切后图片的Uri
        ImageUploader.setTempCrop(System.currentTimeMillis()+"_member.png");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ImageUploader.temp_img_crop_path)));
        intent.putExtra("aspectX", 20);
        intent.putExtra("aspectY", 11);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, TAKE_CROP);
    }

    void showDialog() {
        if (mDialog == null) {
            View dView = View.inflate(getActivity(), R.layout.layout_add_pic_dialog, null);
            topBtn = (Button) dView.findViewById(R.id.photograph_btn);
            middleBtn = (Button) dView.findViewById(R.id.select_photo_btn);
            cancelBtn = (Button) dView.findViewById(R.id.cancel_btn);
            mDialog = DialogUtil.getMenuDialog(getActivity(), dView);
            topBtn.setOnClickListener(this);
            middleBtn.setOnClickListener(this);
            cancelBtn.setOnClickListener(this);
            topBtn.setText("拍照");
            middleBtn.setText("从相册中选择");
        }
        mDialog.show();
    }
}

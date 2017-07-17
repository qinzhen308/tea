package com.becdoor.teanotes.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.model.UserInfoBean;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.ImageUploader;
import com.becdoor.teanotes.until.ImageUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.becdoor.teanotes.view.wheel.NumericWheelAdapter;
import com.becdoor.teanotes.view.wheel.OnWheelScrollListener;
import com.becdoor.teanotes.view.wheel.WheelView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/10.
 * 我的资料管理--账号
 */

public class MyInfoActivity extends TitleActivity {
    private final static int TAKE_PHOTO = 1;// 拍照
    private final static int TAKE_PICTURE = 2;// 本地获取
    private final static int TAKE_CROP = 3;// 裁剪

    @Bind(R.id.myinfo_avatarIv)
    ImageView mAvatarIv;
    @Bind(R.id.myinfo_nicknameEdt)
    EditText mNickNameEdt;
    @Bind(R.id.myinfo_sexTv)
    TextView mSexTv;
    @Bind(R.id.myinfo_birthdayTv)
    TextView mBirthdayTv;
    @Bind(R.id.myinfo_addressEdt)
    EditText mAddressEdt;
    @Bind(R.id.myinfo_hobbyEdt)
    EditText mHobbyEdt;
    @Bind(R.id.myinfo_saveTv)
    TextView mSaveTv;

    private int sex = 0;
    private Dialog mDialog;
    private Button topBtn;
    private Button middleBtn;
    private Button cancelBtn;
    private boolean isPhoto = true;//是否是选取头像
    private String mFilePath;
    private Bitmap bitmap;

    private WheelView year;
    private WheelView month;
    private WheelView day;

    private int mYear = 1996;
    private int mMonth = 0;
    private int mDay = 1;
    private View view;
    private String mBirthday;

    private Dialog mTimeDialog;
    public SimpleDateFormat simpleYHMDateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_myinfo);
        setTitleForRightIamgeView("我的资料管理", 0, true, false);
        ButterKnife.bind(this);

        mAvatarIv.setOnClickListener(this);
        mSexTv.setOnClickListener(this);
        mBirthdayTv.setOnClickListener(this);
        mSaveTv.setOnClickListener(this);
        getData1();
        initView();
    }

    void initView() {
        mBirthday = "1996-01-01";
        simpleYHMDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTimeDialog = DialogUtil.getMenuDialog2(this, getDataPick(), AppUtil.getScreenWH(this)[1] / 2);
        mTimeDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.myinfo_avatarIv:
                isPhoto = true;
                showDialog();
                break;
            case R.id.myinfo_sexTv:
                isPhoto = false;
                showDialog();
                break;
            case R.id.myinfo_birthdayTv:
                mTimeDialog.show();
                break;
            case R.id.myinfo_saveTv:
                updateData();
                break;
            case R.id.photograph_btn:
                mDialog.dismiss();
                if (isPhoto) {
                    //拍照
                    File file = new File(ImageUtil.filePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(ImageUtil.filePath, "123.jpg")));
                    startActivityForResult(intent1, TAKE_PHOTO);
                } else {
                    //男
                    sex = 1;
                    mSexTv.setText("男");
                }

                break;
            case R.id.select_photo_btn:
                mDialog.dismiss();
                if (isPhoto) {
                    //选取图片
                    Intent intent2 = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent2.setType("image/*");
                    startActivityForResult(intent2, TAKE_PICTURE);
                } else {
                    sex = 2;
                    mSexTv.setText("女");
                }
                break;
            case R.id.cancel_btn:
                mDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
                        startPhotoZoom(Uri.fromFile(file), 100);

                        break;
                    case TAKE_CROP:// // 裁剪成功后显示图片

                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            bitmap = bundle.getParcelable("data");
                            if (bitmap != null) {
                                mAvatarIv.setImageBitmap(bitmap);
                            }

                        }
                        break;
                    case TAKE_PICTURE:

                        Uri imgUri_2 = data.getData();
                        startPhotoZoom(imgUri_2, 100);
                        break;

                }

            }
        }
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
        // crop为true时表示显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, TAKE_CROP);
    }

    void showDialog() {
        if (mDialog == null) {
            View dView = View.inflate(this, R.layout.layout_add_pic_dialog, null);
            topBtn = (Button) dView.findViewById(R.id.photograph_btn);
            middleBtn = (Button) dView.findViewById(R.id.select_photo_btn);
            cancelBtn = (Button) dView.findViewById(R.id.cancel_btn);
            mDialog = DialogUtil.getMenuDialog(this, dView);
            topBtn.setOnClickListener(this);
            middleBtn.setOnClickListener(this);
            cancelBtn.setOnClickListener(this);
        }
        if (isPhoto) {
            topBtn.setText("拍照");
            middleBtn.setText("从相册中选择");
        } else {
            topBtn.setText("男");
            middleBtn.setText("女");
        }
        mDialog.show();
    }

    /**
     * 更新资料
     */
    void updateData() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("nickname", mNickNameEdt.getText().toString());
        map.put("sex", String.valueOf(sex));
        map.put("birthday", mBirthdayTv.getText().toString());
        map.put("hometown", mAddressEdt.getText().toString());
        map.put("hobby", mHobbyEdt.getText().toString());
        File file = null;
        String fileName = "";
        if (bitmap != null) {
            fileName = new Date().getTime() + ".jpg";
            file = ImageUtil.saveImag(bitmap, fileName);
        } else {
            Toast.makeText(this, "请添加头像", Toast.LENGTH_LONG).show();
            return;
        }
        NetUtil.postDataWhithFile(this, Constant.BASE_URL + "acc_manage.php?", map, "avatar", fileName, file, "editUser", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(MyInfoActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyInfoActivity.this, object == null ? "修改失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MyInfoActivity.this, "修改失败!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    UserInfoBean userInfoBean;

    void getData1() {
        DialogUtil.showDialog(mLoaDailog);
        final Map<String, String> map = new HashMap<>();
        map.put("op", "base");
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "acc_manage", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
            }

            @Override
            public void onScuccess(String response, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        userInfoBean = new Gson().fromJson(object.getJSONObject("data").toString(), UserInfoBean.class);

                        if (userInfoBean != null) {
                            Glide.with(MyInfoActivity.this).load(Constant.REALM_NAME + userInfoBean.getAvatar()).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    if (resource == null) {
                                        mAvatarIv.setImageResource(R.drawable.icon_login_user);
                                    } else {
                                        bitmap = resource;
                                        mAvatarIv.setImageBitmap(resource);
                                    }
                                }
                            });
                            mNickNameEdt.setText(userInfoBean.getNickname());
                            mNickNameEdt.setSelection(mNickNameEdt.getText().toString().length());
                            sex = userInfoBean.getSex();
                            if (userInfoBean.getSex() == 1) {
                                mSexTv.setText("男");
                            } else if (userInfoBean.getSex() == 2) {
                                mSexTv.setText("女");
                            }
                            mAddressEdt.setText(userInfoBean.getHometown());
                            mAddressEdt.setSelection(mAddressEdt.getText().toString().length());
                            mHobbyEdt.setText(userInfoBean.getHobby());
                            mHobbyEdt.setSelection(mHobbyEdt.getText().toString().length());
                            mBirthdayTv.setText(userInfoBean.getBirthday());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * dataPick滑动 scrollListener
	 */
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + 1950;// 年
            int n_month = month.getCurrentItem() + 1;// 月

            initDay(n_year, n_month);

            mBirthday = new StringBuilder()
                    .append((year.getCurrentItem() + 1950))
                    .append("-")
                    .append((month.getCurrentItem() + 1) < 10 ? "0"
                            + (month.getCurrentItem() + 1) : (month
                            .getCurrentItem() + 1))
                    .append("-")
                    .append(((day.getCurrentItem() + 1) < 10) ? "0"
                            + (day.getCurrentItem() + 1) : (day
                            .getCurrentItem() + 1)).toString();

        }
    };

    private void initDay(int arg1, int arg2) {
        // 设置天数
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,
                1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }

    /**
     * @param year
     * @param month
     * @return int
     * @author lilifeng
     */
    private int getDay(int year, int month) {
        int day = 31;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 4:
                day = 30;
                break;
            case 6:
                day = 30;
                break;
            case 9:
                day = 30;
                break;
            case 11:
                day = 30;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 31;
                break;
        }
        return day;
    }

    /**
     * 时间选择控价
     *
     * @return
     */
    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
        final int curYear = mYear;
        final int curMonth = mMonth + 1;
        final int curDate = mDay;

        view = LayoutInflater.from(this).inflate(R.layout.dialog_wheel, null);
        view.findViewById(R.id.time_sure_btn).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        int i = Integer.valueOf(mBirthday.replace("-", ""));
                        String cur = simpleYHMDateFormat.format(new Date()).replace("-", "");

                        if (i > Integer.valueOf(cur)) {
                            CustomToast.showToast(MyInfoActivity.this, "您选择的时间无效，请重新选择", 1000);
                            return;
                        }
                        if (mTimeDialog != null && mTimeDialog.isShowing()) {
                            mTimeDialog.dismiss();
                        }
                        mBirthdayTv.setText(mBirthday);
                    }
                });
        view.findViewById(R.id.time_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeDialog != null && mTimeDialog.isShowing()) {
                    mTimeDialog.dismiss();
                }
            }
        });
        year = (WheelView) view.findViewById(R.id.wheel_yearWv);
        /**
         * 设置年份
         */
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
                this, 1950, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);// 是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.wheel_monthWv);
        /**
         * 设置月份
         */
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
                this, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.wheel_dayWv);
        initDay(curYear, curMonth);
        day.addScrollingListener(scrollListener);
        day.setCyclic(true);

        year.setVisibleItems(9);// 设置显示行数
        month.setVisibleItems(9);
        day.setVisibleItems(9);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}

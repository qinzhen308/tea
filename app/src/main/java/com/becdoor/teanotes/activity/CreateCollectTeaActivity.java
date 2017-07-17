package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.AtticleCate;
import com.becdoor.teanotes.model.CollectedTeaDetail;
import com.becdoor.teanotes.parser.gson.BaseObject;
import com.becdoor.teanotes.parser.gson.GsonParser;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.ImageUploader;
import com.becdoor.teanotes.until.ImageUtil;
import com.becdoor.teanotes.until.JsonData;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.ScreenUtil;
import com.becdoor.teanotes.view.wheel.NumericWheelAdapter;
import com.becdoor.teanotes.view.wheel.OnWheelScrollListener;
import com.becdoor.teanotes.view.wheel.WheelView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nispok.snackbar.SnackbarManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by paulz on 2016/10/27.
 * 创建笔记的首页
 */

public class CreateCollectTeaActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.top_back_btn)
    View btnBack;
    @Bind(R.id.create_collect_tea_save_btn)
    Button btnSave;
    @Bind(R.id.creat_collect_tea_name_et)
    EditText creat_collect_tea_name_et;
    @Bind(R.id.create_collect_tea_content_et)
    EditText create_collect_tea_content_et;
    @Bind(R.id.create_collect_tea_brand_et)
    EditText create_collect_tea_brand_et;
    @Bind(R.id.create_collect_tea_source_et)
    EditText create_collect_tea_source_et;
    @Bind(R.id.create_collect_tea_money_et)
    EditText create_collect_tea_money_et;
    @Bind(R.id.create_collect_tea_address_et)
    EditText create_collect_tea_address_et;

    @Bind(R.id.creat_collect_tea_year_tv)
    TextView tvProductDate;
    @Bind(R.id.create_collect_tea_time_tv)
    TextView tvDate;
    @Bind(R.id.choose_unit_one_btn)
    Button choose_unit_one_btn;
    @Bind(R.id.choose_unit_two_btn)
    Button choose_unit_two_btn;
    @Bind(R.id.choose_unit_three_btn)
    Button choose_unit_three_btn;
    @Bind(R.id.add_pic_btn)
    LinearLayout layoutAddPic;
    @Bind(R.id.choose_time_btn)
    RelativeLayout choose_time_btn;
    @Bind(R.id.create_tea_img_iv)
    ImageView ivImage;
    @Bind(R.id.delete_pic_btn)
    ImageView ivDelete;
    @Bind(R.id.edit_pic_btn)
    ImageView ivChange;
    @Bind(R.id.pic_layout)
    RelativeLayout layoutPic;
    @Bind(R.id.num3_et)
    EditText num3_et;
    @Bind(R.id.num2_et)
    EditText num2_et;
    @Bind(R.id.num1_et)
    EditText num1_et;

    private WheelView year;
    private WheelView month;
    private WheelView day;

    private int mProYear = 1970;
    private int mYear = 1996;
    private int mMonth = 0;
    private int mDay = 1;
    private View view;
    private String pickedDate;
    private Dialog mDialog;
    private Dialog loadDialog;

    private PopupWindow pop;

    private final static int TAKE_PHOTO = 1;// 拍照
    private final static int TAKE_PICTURE = 2;// 本地获取
    private final static int TAKE_CROP = 3;// 裁剪
    private final static int RE_REGION = 4;// 裁剪
    private final static int RE_NAME = 5;// 裁剪

    private String mFilePath;
    private Uri imgUri;

    private List<String> unit1_list;
    private List<String> unit2_list;
    private List<String> unit3_list;

    private String imgUrl;//上传图片到服务器后返回的url

    private CollectedTeaDetail mDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_create_collect_tea);
        setExtra();
        ButterKnife.bind(this);
        initView();
        setListener();
        initData();
        if(savedInstanceState!=null){
            restoreState(savedInstanceState);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("restore_title",creat_collect_tea_name_et.getText().toString());
        outState.putString("restore_brand",create_collect_tea_brand_et.getText().toString());
        outState.putString("restore_money",create_collect_tea_money_et.getText().toString());
        outState.putString("restore_source",create_collect_tea_source_et.getText().toString());
        outState.putString("restore_unit1",choose_unit_one_btn.getText().toString());
        outState.putString("restore_unit2",choose_unit_two_btn.getText().toString());
        outState.putString("restore_unit3",choose_unit_three_btn.getText().toString());
        outState.putString("restore_num1",num1_et.getText().toString());
        outState.putString("restore_num2",num2_et.getText().toString());
        outState.putString("restore_num3",num3_et.getText().toString());
        outState.putString("restore_mtime",tvDate.getText().toString());
        outState.putString("restore_year",tvProductDate.getText().toString());
        outState.putString("restore_address",create_collect_tea_address_et.getText().toString());
        outState.putString("restore_content",create_collect_tea_content_et.getText().toString());
        outState.putString("restore_cover_url",imgUrl);
        outState.putParcelable("restore_cover_uri",imgUri);
        outState.putParcelable("restore_new_selected_uri",ImageUploader.temp_img_crop_uri);
    }


    private void restoreState(Bundle outState){
        creat_collect_tea_name_et.setText(outState.getString("restore_title",creat_collect_tea_name_et.getText().toString()));
        create_collect_tea_brand_et.setText(outState.getString("restore_brand",create_collect_tea_brand_et.getText().toString()));
        create_collect_tea_money_et.setText(outState.getString("restore_money",create_collect_tea_money_et.getText().toString()));
        create_collect_tea_source_et.setText(outState.getString("restore_source",create_collect_tea_source_et.getText().toString()));
        choose_unit_one_btn.setText(outState.getString("restore_unit1",choose_unit_one_btn.getText().toString()));
        choose_unit_two_btn.setText(outState.getString("restore_unit2",choose_unit_two_btn.getText().toString()));
        choose_unit_three_btn.setText(outState.getString("restore_unit3",choose_unit_three_btn.getText().toString()));
        num1_et.setText(outState.getString("restore_num1",num1_et.getText().toString()));
        num2_et.setText(outState.getString("restore_num2",num2_et.getText().toString()));
        num3_et.setText(outState.getString("restore_num3",num3_et.getText().toString()));
        tvDate.setText(outState.getString("restore_mtime",tvDate.getText().toString()));
        tvProductDate.setText(outState.getString("restore_year",tvProductDate.getText().toString()));
        create_collect_tea_address_et.setText(outState.getString("restore_address",create_collect_tea_address_et.getText().toString()));
        create_collect_tea_content_et.setText(outState.getString("restore_content",create_collect_tea_content_et.getText().toString()));

        pickedDate = outState.getString("restore_mtime");
        pickedProDate = outState.getString("restore_year");

        imgUrl = outState.getString("restore_cover_url");
        imgUri = outState.getParcelable("restore_cover_uri");
        ImageUploader.temp_img_crop_uri=outState.getParcelable("restore_new_selected_uri");
    }

    private void setExtra() {
        Bundle data = getIntent().getBundleExtra("data");
        if (data != null) {
            mDetail = (CollectedTeaDetail) data.getSerializable("edit_data");
        }
    }

    private void initView() {
        mDialog = DialogUtil.getMenuDialog2(this, getDataPick(), ScreenUtil.getScreenWH(this)[1] / 2);
        proDateDialog = DialogUtil.getMenuDialog2(this, getProductDatePick(), ScreenUtil.getScreenWH(this)[1] / 2);
        loadDialog = DialogUtil.getLoadingDialog(this);
        mDialog.setCanceledOnTouchOutside(true);
        proDateDialog.setCanceledOnTouchOutside(true);
        RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)ivImage.getLayoutParams();
        lp.height=ScreenUtil.WIDTH - 2*getResources().getDimensionPixelSize(R.dimen.margin_10);
        ivImage.setLayoutParams(lp);
        if (mDetail != null) {

            creat_collect_tea_name_et.setText(mDetail.title);
            create_collect_tea_brand_et.setText(mDetail.brand);
            create_collect_tea_money_et.setText(mDetail.money);
            create_collect_tea_source_et.setText(mDetail.source);
            choose_unit_one_btn.setText(mDetail.unit1);
            choose_unit_two_btn.setText(mDetail.unit2);
            choose_unit_three_btn.setText(mDetail.unit3);
            num1_et.setText(mDetail.num1);
            num2_et.setText(mDetail.num2);
            num3_et.setText(mDetail.num3);
            tvDate.setText(mDetail.mtime);
            tvProductDate.setText(mDetail.year);
            create_collect_tea_address_et.setText(mDetail.address);
            create_collect_tea_content_et.setText(mDetail.content);

            imgUrl = mDetail.img;
            pickedDate = mDetail.mtime;
            pickedProDate=mDetail.year;
            if(TextUtils.isEmpty(mDetail.img)){
                layoutPic.setVisibility(View.GONE);
                layoutAddPic.setVisibility(View.VISIBLE);
                imgUri = null;
                mFilePath = null;
                imgUrl = null;
            }else {
                layoutPic.setVisibility(View.VISIBLE);
                layoutAddPic.setVisibility(View.GONE);
                Glide.with(this).load(Constant.REALM_NAME + mDetail.img).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        ivImage.setImageDrawable(resource);
                    }
                });
            }

        }
    }

    private void setListener() {
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        layoutAddPic.setOnClickListener(this);
        choose_time_btn.setOnClickListener(this);
        tvProductDate.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivChange.setOnClickListener(this);
        choose_unit_one_btn.setOnClickListener(this);
        choose_unit_two_btn.setOnClickListener(this);
        choose_unit_three_btn.setOnClickListener(this);
    }

    private void initData() {
        OkHttpUtils.get().tag(this).
                url(Constant.BASE_URL + "addNote.php?").
                addParams(Constant.APP_KEY, Constant.DJ_APP_KEY).
                addParams(Constant.ACT, "tcollect").
                addParams("access_token", Constant.VALUE_ACCESS_TOKEN).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                SnackbarManager.show(com.nispok.snackbar.Snackbar.with(getApplicationContext()).color(Color.GRAY).textColor(Color.BLACK).text(R.string.net_work_error + R.string.please_again));
            }

            @Override
            public void onResponse(String response, int id) {
                if (response != null) {
                    BaseObject<TeaUnit> obj = GsonParser.getInstance().parseToObj(response, TeaUnit.class);
                    if (obj != null && obj.status == BaseObject.STATUS_OK && obj.data != null) {
                        unit1_list = obj.data.units_list.get(0);
                        unit2_list = obj.data.units_list.get(1);
                        unit3_list = obj.data.units_list.get(2);
                        choose_unit_one_btn.setText(unit1_list.get(0));
                        choose_unit_two_btn.setText(unit2_list.get(0));
                        choose_unit_three_btn.setText(unit3_list.get(0));
                    }
                }
            }
        });

    }

    /**
     * 验证填入信息
     */
    private boolean verifyForm() {
        if (imgUri == null && TextUtils.isEmpty(imgUrl)) {
            Toast.makeText(getApplicationContext(), "请选择封面", Toast.LENGTH_LONG).show();
            return false;
        }
        String title = creat_collect_tea_name_et.getText().toString();
        String brand = create_collect_tea_brand_et.getText().toString();
        String address = create_collect_tea_address_et.getText().toString();
        String money = create_collect_tea_money_et.getText().toString();
        String source = create_collect_tea_source_et.getText().toString();
        String date = tvDate.getText().toString();
        String proDate = tvProductDate.getText().toString();
        if (title.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写标题", Toast.LENGTH_LONG).show();
            return false;
        }
        if (proDate.length() == 0) {
            Toast.makeText(getApplicationContext(), "请输入茶品年份", Toast.LENGTH_LONG).show();
            return false;
        }
        if (brand.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写购入品牌", Toast.LENGTH_LONG).show();
            return false;
        }
        if (address.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写购入地址", Toast.LENGTH_LONG).show();
            return false;
        }
        if (money.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写购入金额", Toast.LENGTH_LONG).show();
            return false;
        }
        if (source.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写购入来源", Toast.LENGTH_LONG).show();
            return false;
        }
        if (date.length() == 0) {
            Toast.makeText(getApplicationContext(), "请选择购入时间", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void submit(final boolean onlySave) {
        if (TextUtils.isEmpty(imgUrl)) {
            if (verifyForm()) {
                DialogUtil.showDialog(loadDialog);
                ImageUploader.upload(this, new File(ImageUploader.getRealPathFromURI(this, imgUri)), new ImageUploader.Callback() {

                    @Override
                    public void onSuccess(String url) {
                        imgUrl = url;
                        create(onlySave);
                    }

                    @Override
                    public void onFailed(String msg) {
                        if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else if (verifyForm()) {
            create(onlySave);
        }

    }

    private void create(boolean onlySave) {
        String content = create_collect_tea_content_et.getText().toString();
        String title = creat_collect_tea_name_et.getText().toString();
        String brand = create_collect_tea_brand_et.getText().toString();
        String address = create_collect_tea_address_et.getText().toString();
        String money = create_collect_tea_money_et.getText().toString();
        String source = create_collect_tea_source_et.getText().toString();
        String date = tvDate.getText().toString();
        String proDate = tvProductDate.getText().toString();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", Constant.VALUE_ACCESS_TOKEN);
        params.put("op", onlySave ? "save" : "pub");
        params.put("title", title);
        params.put("pid", "5");
        params.put("content", content);
        params.put("cover", imgUrl);
        params.put("brand", brand);
        params.put("source", source);
        params.put("money", money);
        params.put("mtime", date);
        params.put("year", proDate);
        params.put("address", address);
        params.put("num1", num1_et.getText().toString());
        params.put("unit1", choose_unit_one_btn.getText().toString());
        params.put("num2", num2_et.getText().toString());
        params.put("unit2", choose_unit_two_btn.getText().toString());
        params.put("num3", num3_et.getText().toString());
        params.put("unit3", choose_unit_three_btn.getText().toString());
        if(mDetail!=null){
            params.put("aid", mDetail.article_id);
        }
        NetUtil.postData(this, Constant.BASE_URL + "addNote.php?", params, mDetail == null ? "doAdd" : "editNote", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                SnackbarManager.show(com.nispok.snackbar.Snackbar.with(getApplicationContext()).color(Color.GRAY).textColor(Color.BLACK).text(R.string.net_work_error + R.string.please_again));
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                if (response != null) {
                    JsonData jsonData = JsonData.create(response);
                    int status = jsonData.optInt("status");
                    if (status == 200) {
                        String msg = jsonData.optString("alert_msg");
                        Toast.makeText(getApplicationContext(), TextUtils.isEmpty(msg) ? "发布成功" : msg, Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonData.optString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        OkHttpUtils.post().tag(this).
                url(Constant.BASE_URL + "addNote.php?").

                build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });

    }


    public static void invoke(Activity context) {
        context.startActivityForResult(new Intent(context, CreateCollectTeaActivity.class),123);
    }

    public static void invoke(Activity context, CollectedTeaDetail ctd) {
        Intent intent = new Intent(context, CreateCollectTeaActivity.class);
        Bundle data = new Bundle();
        data.putSerializable("edit_data", ctd);
        intent.putExtra("data", data);
        context.startActivityForResult(intent,123);
    }


    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == btnSave) {
            submit(false);
        } else if (v == layoutAddPic) {
            showAddPicAlert();
        } else if (v == choose_time_btn) {
            DialogUtil.showDialog(mDialog);
        } else if (v == tvProductDate) {
            DialogUtil.showDialog(proDateDialog);
        } else if (v == ivDelete) {
            layoutPic.setVisibility(View.GONE);
            layoutAddPic.setVisibility(View.VISIBLE);
            imgUri = null;
            mFilePath = null;
            imgUrl = null;
        } else if (v == ivChange) {
            showAddPicAlert();
        } else if (v == choose_unit_three_btn) {
            UnitThreeClick(v);

        } else if (v == choose_unit_two_btn) {
            UnitTwoClick(v);
        } else if (v == choose_unit_one_btn) {
            UnitOneClick(v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String sdCardState = Environment.getExternalStorageState();
            if (requestCode == RE_REGION) {
            } else if (requestCode == RE_NAME) {

            } else if (!sdCardState.equals(Environment.MEDIA_MOUNTED)) {
                return;
            } else if (requestCode == 222) {
                finish();
            } else {

                switch (requestCode) {
                    case TAKE_PHOTO:
                        mFilePath = ImageUtil.filePath + "123.jpg";
                        mFilePath = ImageUtil.bitmap2File(mFilePath, new Date().getTime() + ".jpg");

                        File file = new File(mFilePath);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (Exception e) {
                            }
                        }
                        Uri imgUri1=Uri.fromFile(ImageUploader.compressImage(file));
//                        imgUri = Uri.fromFile(file);
//                        if (imgUri != null) {
//                            ivImage.setImageURI(imgUri);
//                            layoutPic.setVisibility(View.VISIBLE);
//                            layoutAddPic.setVisibility(View.GONE);
//                            imgUrl=null;
//                        } else {
//                            layoutPic.setVisibility(View.GONE);
//                            layoutAddPic.setVisibility(View.VISIBLE);
//                        }
                        startPhotoZoom(imgUri1,400);
                        break;
                    case TAKE_PICTURE:
                        Uri imgUri2 = data.getData();
//                        if (imgUri != null) {
//                            ivImage.setImageURI(imgUri);
//                            layoutPic.setVisibility(View.VISIBLE);
//                            layoutAddPic.setVisibility(View.GONE);
//                        } else {
//                            layoutPic.setVisibility(View.GONE);
//                            layoutAddPic.setVisibility(View.VISIBLE);
//                        }
                        imgUri2=Uri.fromFile(ImageUploader.compressImage(new File(ImageUploader.getRealPathFromURI(this,imgUri2))));
                        startPhotoZoom(imgUri2,400);
                        break;
                    case TAKE_CROP:// // 裁剪成功后显示图片

                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            Bitmap bitmap = bundle.getParcelable("data");
                            if (bitmap != null) {
                            File coverFile=ImageUploader.saveImag(bitmap,"cover.png");
                                imgUri = Uri.fromFile(coverFile);
                                if (imgUri != null) {
                                    ivImage.setImageURI(imgUri);
                                    layoutPic.setVisibility(View.VISIBLE);
                                    layoutAddPic.setVisibility(View.GONE);
                                    imgUrl=null;
                                } else {
                                    layoutPic.setVisibility(View.GONE);
                                    layoutAddPic.setVisibility(View.VISIBLE);
                                }
                                return;
                            }
                        }
                        Uri dataUri = data.getData();
                        if(dataUri!=null){
                            imgUri=dataUri;
                        }else {
                            imgUri=ImageUploader.temp_img_crop_uri;
                        }
                        if (imgUri != null) {
                            Log.d("crop","裁剪后的path"+imgUri.getPath());
                            ivImage.setImageURI(imgUri);
                            layoutPic.setVisibility(View.VISIBLE);
                            layoutAddPic.setVisibility(View.GONE);
                            imgUrl=null;
                        } else {
                            layoutPic.setVisibility(View.GONE);
                            layoutAddPic.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"图片选取失败",Toast.LENGTH_LONG).show();
                        }
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
        //记录剪切后图片的Uri
        ImageUploader.setTempCrop(System.currentTimeMillis()+"_cover_temp.png");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ImageUploader.temp_img_crop_path)));
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, TAKE_CROP);
    }


    private void showAddPicAlert() {
        final AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
        Window localWindow = localAlertDialog.getWindow();
        localAlertDialog.show();
        localWindow.setContentView(R.layout.layout_add_pic_dialog);
        localWindow.setWindowAnimations(R.style.mystyle);
        localWindow.setBackgroundDrawable(new ColorDrawable());
        ((Button) localWindow.findViewById(R.id.photograph_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localAlertDialog.dismiss();
                File file = new File(ImageUtil.filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(ImageUtil.filePath, "123.jpg")));
                startActivityForResult(intent1, TAKE_PHOTO);
            }
        });
        ((Button) localWindow.findViewById(R.id.select_photo_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localAlertDialog.dismiss();
                Intent intent2 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent2.setType("image/*");
                startActivityForResult(intent2, TAKE_PICTURE);
            }
        });
        ((Button) localWindow.findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localAlertDialog.dismiss();
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

            pickedDate = new StringBuilder().append((year.getCurrentItem() + 1950)).append("年")
                    .append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1)
                            : (month.getCurrentItem() + 1))
                    .append("月").append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1)
                            : (day.getCurrentItem() + 1)).append("日")
                    .toString();

        }
    };

    private void initDay(int arg1, int arg2) {
        // 设置天数
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1, getDay(arg1, arg2), "%02d");
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
        int curYear = mYear;
        int curMonth = mMonth + 1;
        int curDate = mDay;

        view = LayoutInflater.from(this).inflate(R.layout.dialog_wheel, null);
        view.findViewById(R.id.time_sure_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tvDate.setText(pickedDate);
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.time_cancel_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        year = (WheelView) view.findViewById(R.id.wheel_yearWv);
        /**
         * 设置年份
         */
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(this, 1950, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);// 是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.wheel_monthWv);
        /**
         * 设置月份
         */
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(this, 1, 12, "%02d");
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

    View proDateView;
    private WheelView proDateYearW;
    private String pickedProDate;
    private Dialog proDateDialog;
    private View getProductDatePick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
        int curYear = mProYear;

        proDateView = LayoutInflater.from(this).inflate(R.layout.dialog_wheel, null);
        proDateView.findViewById(R.id.time_sure_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tvProductDate.setText(pickedProDate);
                if (proDateDialog != null && proDateDialog.isShowing()) {
                    proDateDialog.dismiss();
                }
            }
        });
        proDateView.findViewById(R.id.time_cancel_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (proDateDialog != null && proDateDialog.isShowing()) {
                    proDateDialog.dismiss();
                }
            }
        });
        proDateYearW = (WheelView) proDateView.findViewById(R.id.wheel_yearWv);
        /**
         * 设置年份
         */
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(this, 1970, norYear);
        numericWheelAdapter1.setLabel("年");
        proDateYearW.setViewAdapter(numericWheelAdapter1);
        proDateYearW.setCyclic(false);// 是否可循环滑动
        proDateYearW.addScrollingListener(proDateScrollListener);
        proDateView.findViewById(R.id.wheel_monthWv).setVisibility(View.GONE);
        proDateView.findViewById(R.id.wheel_dayWv).setVisibility(View.GONE);
        proDateYearW.setVisibleItems(9);// 设置显示行数
        proDateYearW.setCurrentItem(curYear - 1970);
        return proDateView;
    }

    OnWheelScrollListener proDateScrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            mProYear = proDateYearW.getCurrentItem() + 1970;// 年
            pickedProDate = new StringBuilder().append(mProYear).append("年").toString();

        }
    };

    public void UnitOneClick(View paramView) {
        this.pop = initSimpalPopWindow(this, this.unit1_list, new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                        CreateCollectTeaActivity.this.pop.dismiss();
                        CreateCollectTeaActivity.this.choose_unit_one_btn.setText((CharSequence) CreateCollectTeaActivity.this.unit1_list.get(paramAnonymousInt));
                    }
                }
                , paramView);
    }

    public void UnitThreeClick(View paramView) {
        this.pop = initSimpalPopWindow(this, this.unit3_list, new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                        CreateCollectTeaActivity.this.pop.dismiss();
                        CreateCollectTeaActivity.this.choose_unit_three_btn.setText((CharSequence) CreateCollectTeaActivity.this.unit3_list.get(paramAnonymousInt));
                    }
                }
                , paramView);
    }

    public void UnitTwoClick(View paramView) {
        this.pop = initSimpalPopWindow(this, this.unit2_list, new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                        CreateCollectTeaActivity.this.pop.dismiss();
                        CreateCollectTeaActivity.this.choose_unit_two_btn.setText((CharSequence) CreateCollectTeaActivity.this.unit2_list.get(paramAnonymousInt));
                    }
                }
                , paramView);
    }


    public PopupWindow initSimpalPopWindow(Activity paramActivity, List<String> paramList, AdapterView.OnItemClickListener paramOnItemClickListener, View paramView) {
        Object localObject = null;
        if (paramActivity != null) {
            localObject = (ListView) LayoutInflater.from(this).inflate(R.layout.layout_popwindow_listview1, null);
            ((ListView) localObject).setAdapter(new ArrayAdapter<String>(paramActivity, R.layout.layout_popwindow_listview_item1, R.id.popwindow_tv, paramList));
            ((ListView) localObject).setOnItemClickListener(paramOnItemClickListener);
            localObject = new PopupWindow((View) localObject, getResources().getDimensionPixelSize(R.dimen.height_60), -2);
            ((PopupWindow) localObject).setFocusable(true);
            ((PopupWindow) localObject).setTouchable(true);
            ((PopupWindow) localObject).setBackgroundDrawable(new BitmapDrawable());
            ((PopupWindow) localObject).setOutsideTouchable(true);
            ((PopupWindow) localObject).showAsDropDown(paramView, -20, 0);
        }
        return (PopupWindow) localObject;
    }

    public class TeaUnit {
        public String pid;
        public List<List<String>> units_list;
    }


}

package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.CreateNotesAdapter;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.Article;
import com.becdoor.teanotes.model.ArticleBean;
import com.becdoor.teanotes.model.AtticleCate;
import com.becdoor.teanotes.model.NoteDetailBean;
import com.becdoor.teanotes.model.NoteElement;
import com.becdoor.teanotes.parser.gson.BaseObject;
import com.becdoor.teanotes.parser.gson.GsonParser;
import com.becdoor.teanotes.tables.ArticleTable;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DateUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.ImageUploader;
import com.becdoor.teanotes.until.ImageUtil;
import com.becdoor.teanotes.until.JsonData;
import com.becdoor.teanotes.until.ScreenUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.SimpleFloatViewManager;
import com.nispok.snackbar.SnackbarManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by paulz on 2016/10/27.
 * 创建茶志
 */

public class CreateAtticleActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.list)
    DragSortListView mListview;
    @Bind(R.id.send_btn)
    Button send_btn;
    @Bind(R.id.tv_add_address)
    TextView tv_add_address;
    @Bind(R.id.tv_add_article)
    TextView tv_add_article;
    @Bind(R.id.tv_add_pic)
    TextView tv_add_pic;
    @Bind(R.id.top_back_btn)
    View top_back_btn;
    @Bind(R.id.save_btn)
    Button save_btn;

    private CreateNotesAdapter adapter;

    private final static int TAKE_PHOTO = 1;// 拍照
    private final static int TAKE_PICTURE = 2;// 本地获取
    private final static int TAKE_CROP = 3;// 裁剪

    private String coverPicUrl;
    private String coverPicPath;
    PageData mData;
    AtticleCate selectedCate;
    List<String> cateNameList = new ArrayList<>();

    private PopupWindow pop;

    private NoteDetailBean offlineNote;

    boolean isOffline=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_create_general_notes);

        setExtra();
        ButterKnife.bind(this);
        initView();
        setListener();
        if(savedInstanceState!=null){
            Log.d("crop","restore----");
            restoreNote(savedInstanceState);
        }else {
            initOfflineNote();
        }
    }

    private void setExtra() {
        Bundle data = getIntent().getBundleExtra("data");
        isOffline=getIntent().getBooleanExtra("is_offline",true);
        if (data != null) {
            offlineNote = (NoteDetailBean) data.getSerializable("note");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        NoteDetailBean art = new NoteDetailBean();
        if(selectedCate!=null){
            art.setCat_id(Integer.valueOf(selectedCate.cat_id));
            art.setCat_name(selectedCate.cat_name);
        }
        if(!TextUtils.isEmpty(coverPicUrl)){
            art.setImg(coverPicUrl);
        }else {
            art.isNative=true;
            art.setImg(coverPicPath);
        }
        art.setTitle(etTitle.getText().toString());
        art.setIs_priv(cbSecret.isChecked() ? "1" : "0");
        art.setArt_arr(adapter.getList());
        if (offlineNote != null) {
            art.setArticle_id(offlineNote.getArticle_id());
        }
        outState.putSerializable("restore_note",art);
        if(offlineNote!=null){
            outState.putSerializable("restore_offline_note",offlineNote);
        }
        outState.putInt("add_pic_position",curAddPicPosition);
        outState.putParcelable("restore_uri",ImageUploader.temp_img_crop_uri);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void setListener() {
        top_back_btn.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
        tv_add_article.setOnClickListener(this);
        tv_add_pic.setOnClickListener(this);
        send_btn.setOnClickListener(this);
        btnCustomType.setOnClickListener(this);
        btnAddCoverPic.setOnClickListener(this);
        btnChangeCover.setOnClickListener(this);
        tvType.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        mListview.setDropListener(onDrop);
    }

    public void initView() {
        mDialog = DialogUtil.getLoadingDialog(this);
        adapter = new CreateNotesAdapter(CreateAtticleActivity.this);
        initHeader();
        mListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListview.setAdapter(adapter);
        mListview.setDragEnabled(true);
//        DragSortController manager=new DragSortController(mListview);
//        manager.setDragInitMode(DragSortController.ON_LONG_PRESS);
//        mListview.setFloatViewManager(manager);
        mListview.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
                adapter.remove(adapter.getItem(which));
            }
        });
    }

    //异常回复
    private void restoreNote(Bundle restore) {
        NoteDetailBean restoreNote=(NoteDetailBean)restore.getSerializable("restore_note");
        curAddPicPosition=restore.getInt("add_pic_position",-1);
        Object o=restore.getSerializable("restore_offline_note");
        if(restore.containsKey("restore_uri")){
            ImageUploader.temp_img_crop_uri = restore.getParcelable("restore_uri");
            Log.d("crop","restore----"+ImageUploader.temp_img_crop_uri.getPath());
        }

        if(o!=null){
            offlineNote=(NoteDetailBean)o;
        }
        if (restoreNote == null) return;
        etTitle.setText(restoreNote.getTitle());
        cbSecret.setChecked("1".equals(restoreNote.getIs_priv()));
        adapter.setList(restoreNote.getArt_arr());
        adapter.notifyDataSetChanged();
        if(!TextUtils.isEmpty(restoreNote.getCat_name())){
            selectedCate = new AtticleCate();
            tvType.setText(restoreNote.getCat_name());
            selectedCate.cat_name = restoreNote.getCat_name();
            selectedCate.cat_id = "" + restoreNote.getCat_id();
        }
        if(restoreNote.isNative){
            coverPicPath = restoreNote.getImg();
        }else {
            coverPicUrl = restoreNote.getImg();
        }
        setCover();
    }

    private void initOfflineNote() {
        if (offlineNote == null) return;
        etTitle.setText(offlineNote.getTitle());
        tvType.setText(offlineNote.getCat_name());
        cbSecret.setChecked("1".equals(offlineNote.getIs_priv()));
        adapter.setList(offlineNote.getArt_arr());
        adapter.notifyDataSetChanged();
        selectedCate = new AtticleCate();
        selectedCate.cat_name = offlineNote.getCat_name();
        selectedCate.cat_id = "" + offlineNote.getCat_id();
        if(offlineNote.isNative){
            coverPicPath = offlineNote.getImg();
        }else {
            coverPicUrl = offlineNote.getImg();
        }
        setCover();
    }

    Button btnCustomType;
    Button btnChangeCover;
    Button btnAddCoverPic;
    View layoutCover;
    EditText etTitle;
    TextView tvType;
    CheckBox cbSecret;
    ImageView ivCover;

    private void initHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_index_create_general_notes_header, null);
        mListview.addHeaderView(header);
        btnAddCoverPic = (Button) header.findViewById(R.id.cover_add_pic_btn);
        layoutCover = header.findViewById(R.id.pic_layout);
        etTitle = (EditText) header.findViewById(R.id.edit_title);
        tvType = (TextView) header.findViewById(R.id.type_tv);
        cbSecret = (CheckBox) header.findViewById(R.id.checkbox_secret);
        btnCustomType = (Button) header.findViewById(R.id.custom_type_btn);
        btnChangeCover = (Button) header.findViewById(R.id.change_cover_btn);
        ivCover = (ImageView) header.findViewById(R.id.pic_iv);
        View picPart=header.findViewById(R.id.layout_part1);
        ViewGroup.LayoutParams lp=picPart.getLayoutParams();
        lp.height= (int)((ScreenUtil.WIDTH - 2*getResources().getDimensionPixelSize(R.dimen.margin_10))*0.52);
        picPart.setLayoutParams(lp);
    }

    private void initData() {
        DialogUtil.showDialog(mDialog);
        OkHttpUtils.get().tag(this).
                url(Constant.BASE_URL + "addNote.php?").
                addParams(Constant.APP_KEY, Constant.DJ_APP_KEY).
                addParams(Constant.ACT, "tcommon").
                addParams("access_token", Constant.VALUE_ACCESS_TOKEN).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
//                SnackbarManager.show(com.nispok.snackbar.Snackbar.with(getApplicationContext()).color(Color.GRAY).textColor(Color.BLACK).text(R.string.net_work_error + R.string.please_again));
            }

            @Override
            public void onResponse(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
                if (response != null) {
                    BaseObject<PageData> obj = GsonParser.getInstance().parseToObj(response, PageData.class);
                    if (obj != null && obj.status == BaseObject.STATUS_OK && obj.data != null) {
                        mData = obj.data;
                        if (mData.cate_list != null) {
                            cateNameList.clear();
                            for (int i = 0; i < mData.cate_list.size(); i++) {
                                cateNameList.add(mData.cate_list.get(i).cat_name);
                            }
                        }
                    }
                }
            }
        });
    }

    private void showCateDialog() {
        this.pop = initSimpalPopWindow(this, cateNameList, new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                        pop.dismiss();
                        selectedCate = mData.cate_list.get(paramAnonymousInt);
                        tvType.setText(selectedCate.cat_name);
                    }
                }
                , tvType);
    }

    private void setCover() {
        if (coverPicUrl != null) {
            Glide.with(this).load(Constant.REALM_NAME + coverPicUrl).into(ivCover);
            layoutCover.setVisibility(View.VISIBLE);
            btnAddCoverPic.setVisibility(View.GONE);
        }else if(!TextUtils.isEmpty(coverPicPath)){
//            Glide.with(this).load(new File(coverPicPath)).into(ivCover);
            ivCover.setImageURI(Uri.fromFile(new File(coverPicPath)));
            layoutCover.setVisibility(View.VISIBLE);
            btnAddCoverPic.setVisibility(View.GONE);
        }
    }


    public static void invoke(Context context) {
        context.startActivity(new Intent(context, CreateAtticleActivity.class));
    }

    public static void invoke(Activity context, NoteDetailBean note,boolean isOffline) {
        Intent intent = new Intent(context, CreateAtticleActivity.class);
        Bundle data = new Bundle();
        data.putSerializable("note", note);
        intent.putExtra("data", data);
        intent.putExtra("is_offline", isOffline);
        context.startActivity(intent);
    }


    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    if (from != to) {
                        NoteElement item = adapter.getItem(from);
                        adapter.remove(item);
                        adapter.insert(item, to);
                        mListview.moveCheckState(from, to);
                    }
                }

            };

    private void addNoteElement(String name) {
        NoteElement e = new NoteElement(name);
        adapter.insert(e, adapter.getCount());
//        mListview.smoothScrollBy(mListview.getBottom(),400);
        mListview.setSelection(adapter.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        if (v == top_back_btn) {
            finish();
        } else if (v == send_btn) {
            checkAndSubmit();
        } else if (v == tv_add_address) {
            addNoteElement(NoteElement.TYPE_ADDRESS);
        } else if (v == tv_add_article) {
            addNoteElement(NoteElement.TYPE_TEXT);
        } else if (v == tv_add_pic) {
            addNoteElement(NoteElement.TYPE_IMG);
        } else if (v == btnAddCoverPic) {
            showAddPicAlert(-1);
        } else if (v == btnCustomType) {

            NavigationActivity.inVoke(this, null);

        } else if (v == btnChangeCover) {
            showAddPicAlert(-1);
        } else if (v == tvType) {
            showCateDialog();
        } else if (v == save_btn) {
            offlineSave();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EditActivity.REQUEST_CODE_EDIT) {
                String content = data.getStringExtra("content");
                if (!TextUtils.isEmpty(content)) {
                    int position = data.getIntExtra("position", -1);
                    adapter.getItem(position).setEnableValue(content);
                    adapter.notifyDataSetChanged();
                }
            } else {
                switch (requestCode) {
                    case TAKE_PHOTO:
                        String mFilePath = ImageUtil.filePath + "123.png";
//                        mFilePath = ImageUtil.bitmap2File(mFilePath, new Date().getTime() + ".png");
//                        File file = new File(mFilePath);
                        File file = ImageUploader.compressImage(new File(mFilePath));
                        if(curAddPicPosition==-1){
                            Uri uri = Uri.fromFile(file);
                            ImageUploader.setTempCrop(System.currentTimeMillis()+"_cover_temp.png");
                            startPhotoZoom(uri);
                        }else {
                            file=ImageUploader.compressImage(file);
                            setImg(file,curAddPicPosition);
                        }

                        break;
                    case TAKE_PICTURE:
                        Uri imgUri = data.getData();
                        File imgFile = new File(ImageUploader.getRealPathFromURI(this, imgUri));
                        if(curAddPicPosition==-1){
                            imgFile=ImageUploader.compressImage(imgFile);
                            Uri uri = Uri.fromFile(imgFile);
                            ImageUploader.setTempCrop(System.currentTimeMillis()+"_cover_temp.png");
                            startPhotoZoom(uri);
                        }else {
                            imgFile=ImageUploader.compressImage(imgFile);
                            setImg(imgFile,curAddPicPosition);
                        }

                        break;
                    case TAKE_CROP:// // 裁剪成功后显示图片
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            Bitmap bitmap = bundle.getParcelable("data");
                            if (bitmap != null) {
                                File coverFile=ImageUploader.saveImag(bitmap,System.currentTimeMillis()+"_cover_temp.png");
                                setImg(coverFile,curAddPicPosition);
                                return;
                            }
                        }
                        String path=ImageUploader.getRealPathFromURI(this,ImageUploader.temp_img_crop_uri);
                        Log.d("crop","裁剪后的path"+path);
                        setImg(new File(path),curAddPicPosition);
                        break;
                }
            }
        }
    }


    /**
     * 跳转至系统截图界面进行截图
     *
     * @param data
     */
    private void startPhotoZoom(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,ImageUploader.temp_img_crop_uri);
        // crop为true时表示显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, TAKE_CROP);
    }

    private void setImg(File file, final int position){
        if(file==null||!file.exists()){
            Toast.makeText(this,"图片选取失败，请重试...",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("crop","set进来  file"+file.getPath()+"--innerPo="+position+"curP"+curAddPicPosition);
        if (position == -1) {
            coverPicPath = file.getAbsolutePath();
            coverPicUrl = null;
            setCover();
        } else {
            adapter.getItem(position).setEnableValue(file.getAbsolutePath(),true);
            adapter.notifyDataSetChanged();
        }
    }

    private Dialog mDialog;

    /**
     *
     * @return 是否需要上传
     */
    private boolean checkAndLoadImage() {
        if(!TextUtils.isEmpty(coverPicPath)){
            ImageUploader.upload(this, new File(coverPicPath), new ImageUploader.Callback() {
                @Override
                public void onSuccess(String url) {
                    coverPicUrl = url;
                    coverPicPath=null;
                    setCover();
                    //upLoadNativeImages 返回false，代表 不需要上传图片，直接提交
                    if(!upLoadNativeImages())submit(false);
                }

                @Override
                public void onFailed(String msg) {
                    if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
        return upLoadNativeImages();
    }

    /**
     *
     * @return 是否需要上传
     */
    private boolean upLoadNativeImages() {
        final List<NoteElement > list=adapter.getNativeImgItems();
        if(list==null||list.isEmpty()){
            return false;
        }
        final List<File> files=new ArrayList<>();
        for(final NoteElement e:list){
            files.add(new File(e.getEnableValue()));
        }
        ImageUploader.uploadBatch(this, files, new ImageUploader.Callback() {
            @Override
            public void onSuccess(String url) {
                Map<String ,String > urls=new Gson().fromJson(url,new TypeToken<Map<String, String>>() {}.getType());
                for(int i=0 ;i<list.size();i++){
                    String u=urls.get(files.get(i).getName().split("\\.")[0]);
                    Log.d("uploads","up---url---"+i+"----"+u);
                    if(u.startsWith("/")){
                        u=u.substring(1,u.length());
                    }
                    list.get(i).setEnableValue(u,false);
                }
                submit(false);
            }

            @Override
            public void onFailed(String msg) {
                if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
        return true;
    }

    private int curAddPicPosition = -1;

    /**
     * @param position -1 代表封面  >=0代表列表位置
     */
    public void showAddPicAlert(final int position) {
        curAddPicPosition = position;
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
                        Uri.fromFile(new File(ImageUtil.filePath, "123.png")));
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

    private void offlineSave() {
        final String title = etTitle.getText().toString();
        if (title.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写标题", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(coverPicUrl)&&TextUtils.isEmpty(coverPicPath)) {
            Toast.makeText(getApplicationContext(), "请设置封面", Toast.LENGTH_LONG).show();
            return;
        }
        if (selectedCate == null) {
            Toast.makeText(getApplicationContext(), "请选择栏目", Toast.LENGTH_LONG).show();
            return;
        }
        DialogUtil.showDialog(mDialog);
        new Thread() {
            @Override
            public void run() {
                NoteDetailBean art = new NoteDetailBean();
                art.setCat_id(Integer.valueOf(selectedCate.cat_id));
                art.setCat_name(selectedCate.cat_name);
                if(!TextUtils.isEmpty(coverPicUrl)){
                    art.setImg(coverPicUrl);

                }else {
                    art.isNative=true;
                    art.setImg(coverPicPath);
                }
                art.setTitle(title);
                art.setIs_priv(cbSecret.isChecked() ? "1" : "0");
                List<NoteElement> list=adapter.getList();
                List<NoteElement> listTarget=new ArrayList<NoteElement>();
                if(list!=null){
                    for(int i=0,size=list.size();i<size;i++){
                        NoteElement e=list.get(i);
                        if(e.isValid()){
                            listTarget.add(e);
                        }
                    }
                }
                art.setArt_arr(listTarget);
                Log.d("offline_save","arr size="+listTarget.size());
                art.setAdd_time(DateUtil.getYMDTime(System.currentTimeMillis()));
                art.setArticle_id(String.valueOf(System.currentTimeMillis()));
                if (offlineNote != null) {
                    art.setArticle_id(offlineNote.getArticle_id());
                }
                final boolean isSuc = ArticleTable.getInstance().save(art);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
                        if (isSuc) {
                            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }.start();
    }

    private void checkAndSubmit(){
        if(!verifyInput())return;
        DialogUtil.showDialog(mDialog);
        if(checkAndLoadImage()){
            return;
        }
        submit(false);
    }

    private boolean verifyInput(){
        String title = etTitle.getText().toString();
        if (title.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写标题", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(coverPicUrl)&&TextUtils.isEmpty(coverPicPath)) {
            Toast.makeText(getApplicationContext(), "请设置封面", Toast.LENGTH_LONG).show();
            return false;
        }
        if (selectedCate == null) {
            Toast.makeText(getApplicationContext(), "请选择栏目", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void submit(boolean onlySave) {
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(coverPicUrl)) {
            Toast.makeText(getApplicationContext(), "封面上传失败，请重新设置...", Toast.LENGTH_LONG).show();
            return;
        }

        PostFormBuilder builder = OkHttpUtils.post().tag(this).
                url(Constant.BASE_URL + "addNote.php?").
                addParams(Constant.APP_KEY, Constant.DJ_APP_KEY).
                addParams("access_token", Constant.VALUE_ACCESS_TOKEN).
                addParams("op", onlySave ? "save" : "pub").
                addParams("title", title).
                addParams("pid", "9").
                addParams("cat_id", selectedCate.cat_id).
                addParams("cover", coverPicUrl).
                addParams("privacy", cbSecret.isChecked() ? "1" : "0");

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < adapter.getCount(); i++) {
            NoteElement e = adapter.getItem(i);
            if (!e.isValid()) continue;
            JSONObject obj = new JSONObject();
            JSONObject value = new JSONObject();
            try {
                value.put("type", e.value.type);
                value.put("value", e.value.value);
                obj.put("value", value);
                obj.put("name", e.name);
                jsonArray.put(obj);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
//            builder.addParams("art_arr["+i+"]",);
        }
        builder.addParams("art_arr", jsonArray.toString());
        if(!isOffline&&offlineNote!=null){//编辑显示文章
            builder.addParams("aid", offlineNote.getArticle_id());
            builder.addParams(Constant.ACT, "editNote");
        }else {
            builder.addParams(Constant.ACT, "doAdd");
        }
        builder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
//                SnackbarManager.show(com.nispok.snackbar.Snackbar.with(getApplicationContext()).color(Color.GRAY).textColor(Color.BLACK).text(R.string.net_work_error + R.string.please_again));
            }

            @Override
            public void onResponse(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
                if (response != null) {
                    JsonData jsonData = JsonData.create(response);
                    int status = jsonData.optInt("status");
                    if (status == 200) {
                        Toast.makeText(getApplicationContext(), jsonData.optString("alert_msg"), Toast.LENGTH_LONG).show();
                        ArticleTable.getInstance().delete(offlineNote);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonData.optString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public PopupWindow initSimpalPopWindow(Activity paramActivity, List<String> paramList, AdapterView.OnItemClickListener paramOnItemClickListener, View paramView) {
        Object localObject = null;
        if (paramActivity != null) {
            localObject = (ListView) LayoutInflater.from(this).inflate(R.layout.layout_popwindow_listview1, null);
            ((ListView) localObject).setAdapter(new ArrayAdapter<String>(paramActivity, R.layout.layout_popwindow_listview_item2, R.id.popwindow_tv, paramList));
            ((ListView) localObject).setOnItemClickListener(paramOnItemClickListener);
            localObject = new PopupWindow((View) localObject, getResources().getDimensionPixelSize(R.dimen.height_120), WindowManager.LayoutParams.WRAP_CONTENT);
            ((PopupWindow) localObject).setFocusable(true);
            ((PopupWindow) localObject).setTouchable(true);
            ((PopupWindow) localObject).setBackgroundDrawable(new BitmapDrawable());
            ((PopupWindow) localObject).setOutsideTouchable(true);
            ((PopupWindow) localObject).showAsDropDown(paramView, -20, 0);
        }
        return (PopupWindow) localObject;
    }

    public class PageData {
        public List<AtticleCate> cate_list;
        public String priv;
        public String pid;

    }


}

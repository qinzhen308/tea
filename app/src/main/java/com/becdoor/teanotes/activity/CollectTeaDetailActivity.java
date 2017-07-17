package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.CollectedTeaDetail;
import com.becdoor.teanotes.parser.gson.BaseObject;
import com.becdoor.teanotes.parser.gson.GsonParser;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.until.ScreenUtil;
import com.bumptech.glide.Glide;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class CollectTeaDetailActivity extends Activity {

    @Bind(R.id.tea_detail_add_time_tv)
    TextView add_time_tv;

    @Bind(R.id.tea_detail_address_tv)
    TextView address_tv;

    @Bind(R.id.tea_detail_brand_tv)
    TextView brand_tv;
    private String cat_id;

    @Bind(R.id.tea_detail_content_tv)
    TextView content_tv;

    @Bind(R.id.collect_tea_edit_btn)
    TextView edit_btn;
    private String id;

    @Bind(R.id.tea_detail_img_iv)
    ImageView img_iv;
    boolean isMynote = false;

    @Bind(R.id.tea_detail_money_tv)
    TextView money_tv;

    @Bind(R.id.tea_detail_mtime_tv)
    TextView mtime_tv;

    @Bind(R.id.tea_detail_num_tv)
    TextView num_tv;

    @Bind(R.id.tea_detail_source_tv)
    TextView source_tv;

    @Bind(R.id.tea_detail_title_tv)
    TextView title_tv;
    @Bind(R.id.tea_detail_proyear_tv)
    TextView year_tv;

    private Dialog loadDialog;

    private CollectedTeaDetail mDetail;

    @OnClick({R.id.top_back_btn})
    public void BackClick(View paramView) {
        finish();
    }

    @OnClick({R.id.collect_tea_edit_btn})
    public void CollectClick(View paramView) {
        CreateCollectTeaActivity.invoke(this,mDetail);
    }

    public void GetInfo() {
        DialogUtil.showDialog(loadDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("aid", id);
        params.put("cat_id", cat_id);
        NetUtil.getData(this, Constant.BASE_URL + "note_manage.php?", params, "showNote", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                BaseObject<CollectedTeaDetail> obj = GsonParser.getInstance().parseToObj(response, CollectedTeaDetail.class);
                if (obj != null && obj.status == 200 && obj.data != null) {
                    mDetail = obj.data;
                    Glide.with(CollectTeaDetailActivity.this).load(Constant.REALM_NAME + obj.data.img).into(img_iv);
                    title_tv.setText(mDetail.title);
                    add_time_tv.setText("发布时间：" + mDetail.add_time);
                    brand_tv.setText(mDetail.brand);
                    money_tv.setText(mDetail.money);
                    source_tv.setText(mDetail.source);
                    String count = "";
                    if (!"0".equals(mDetail.num1))
                        count = mDetail.num1 + mDetail.unit1;
                    if (!"0".equals(mDetail.num2))
                        count += mDetail.num2 + mDetail.unit2;
                    if (!"0".equals(mDetail.num3))
                        count += mDetail.num3 + mDetail.unit3;
                    num_tv.setText(count);
                    mtime_tv.setText(mDetail.mtime);
                    year_tv.setText(mDetail.year);
                    address_tv.setText(mDetail.address);
                    content_tv.setText(mDetail.content);
                } else {
                    Toast.makeText(getApplicationContext(), obj == null ? "加载失败" : obj.message, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    boolean isEdited;

    @Override
    public void finish() {
        if(isEdited){
            setResult(RESULT_OK);
        }
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK==resultCode){
            isEdited=true;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_collect_tea_detail);
        ButterKnife.bind(this);
        loadDialog = DialogUtil.getLoadingDialog(this);
        cat_id = getIntent().getStringExtra("cat_id");
        id = getIntent().getStringExtra("id");
        ViewGroup.LayoutParams lp=img_iv.getLayoutParams();
        lp.height= ScreenUtil.WIDTH;
        img_iv.setLayoutParams(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetInfo();
    }

    public static void invoke(Activity context, String cat_id, String id) {
        Intent intent = new Intent(context, CollectTeaDetailActivity.class);
        intent.putExtra("cat_id", cat_id);
        intent.putExtra("id", id);
        context.startActivityForResult(intent,123);
    }


}
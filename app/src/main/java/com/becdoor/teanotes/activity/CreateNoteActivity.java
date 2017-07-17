package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.SimpleNoteWrapper;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.JsonData;
import com.becdoor.teanotes.until.NetUtil;
import com.nispok.snackbar.SnackbarManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by paulz on 2016/10/27.
 * 创建便签
 */

public class CreateNoteActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.top_back_btn)
    View btnBack;
    @Bind(R.id.save_btn)
    Button btnSave;
    @Bind(R.id.title_et)
    EditText etTitle;
    @Bind(R.id.content_et)
    EditText etContent;
    Dialog mDialog;

    private SimpleNoteWrapper.SimpleNote note;

    public final static int REQUEST_CODE_CREATE = 5723;
    public final static int REQUEST_CODE_EDIT = 5724;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = DialogUtil.getLoadingDialog(this);
        setExtra();
        setContentView(R.layout.activity_index_create_note);
        ButterKnife.bind(this);
        setListener();
        initView();
    }

    private void setExtra() {
        Bundle data = getIntent().getBundleExtra("data");
        if (data != null) {
            note = (SimpleNoteWrapper.SimpleNote) data.getSerializable("note");
        }
    }

    private void setListener() {
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void initView() {
        if (note != null) {
            etContent.setText(note.content);
            etTitle.setText(note.title);
        }
    }

    public static void invoke(Context context) {
        context.startActivity(new Intent(context, CreateNoteActivity.class));
    }

    public static void invokeForResult(Activity context) {
        context.startActivityForResult(new Intent(context, CreateNoteActivity.class), REQUEST_CODE_CREATE);
    }

    public static void invoke(Activity context, SimpleNoteWrapper.SimpleNote note) {
        Intent intent = new Intent(context, CreateNoteActivity.class);
        Bundle data = new Bundle();
        data.putSerializable("note", note);
        intent.putExtra("data", data);
        context.startActivityForResult(intent, REQUEST_CODE_EDIT);
    }


    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == btnSave) {
            submit(false);
        }
    }

    private void submit(boolean onlySave) {
        final String content = etContent.getText().toString();
        final String title = etTitle.getText().toString();
        if (title.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写标题", Toast.LENGTH_LONG).show();
            return;
        }
        if (content.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写内容", Toast.LENGTH_LONG).show();
            return;
        }
        DialogUtil.showDialog(mDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("op", onlySave ? "save" : "pub");
        params.put("title", title);
        params.put("pid", "7");
        params.put("content", content);
        if(note!=null){
            params.put("aid", note.article_id);
        }
        NetUtil.postData(this, Constant.BASE_URL + "addNote.php?", params, "doAdd", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
                Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(mDialog);
                if (response != null) {
                    JsonData jsonData = JsonData.create(response);
                    int status = jsonData.optInt("status");
                    if (status == 200) {
                        String msg=jsonData.optString("alert_msg");
                        Toast.makeText(getApplicationContext(), TextUtils.isEmpty(msg)?"保存成功":msg, Toast.LENGTH_LONG).show();
                        if(note!=null){
                            Intent intent=new Intent();
                            Bundle data=new Bundle();
                            note.content=content;
                            note.title=title;
                            data.putSerializable("note",note);
                            intent.putExtra("data", data);
                            setResult(RESULT_OK,intent);
                        }else {
                            setResult(RESULT_OK);
                        }
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonData.optString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}

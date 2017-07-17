package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by paulz on 2016/11/1.
 */

public class EditActivity extends BaseActivity implements View.OnClickListener{
    public static final int REQUEST_CODE_EDIT=1031;
    @Bind(R.id.top_back_btn)
    Button top_back_btn;
    @Bind(R.id.save_btn)
    Button save_btn;
    @Bind(R.id.edit_content)
    EditText edit_content;
    String content;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        initData();
        setListener();
    }
    private void initData(){
        Intent intent=getIntent();
        content=intent.getStringExtra("content");
        position=intent.getIntExtra("position",-1);
        if(!TextUtils.isEmpty(content)){
            edit_content.setText(content);
        }
    }

    private void setListener(){
        top_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==top_back_btn){
            back();
        }else if(v==save_btn){
            save();
        }
    }

    private void back(){
        setResult(RESULT_CANCELED);
        finish();
    }

    private void save(){
        String con=edit_content.getText().toString();
        if(con.length()==0){
            Toast.makeText(getApplicationContext(),"内容不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=getIntent();
        intent.putExtra("content",con);
        intent.putExtra("position",position);
        setResult(RESULT_OK,intent);
        finish();
    }

    public static void invoke(Activity context,String content,int position){
        Intent intent=new Intent(context,EditActivity.class);
        intent.putExtra("content",content);
        intent.putExtra("position",position);
        context.startActivityForResult(intent,REQUEST_CODE_EDIT);
    }
}

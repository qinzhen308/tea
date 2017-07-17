package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.becdoor.teanotes.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by paulz on 2016/10/27.
 * 创建笔记的首页
 */

public class CreateNotesActivity extends Activity implements View.OnClickListener{
    @Bind(R.id.top_back_btn)
    View btnBack;
    @Bind(R.id.create_general_notes_btn)
    LinearLayout btnCreateAtticle;
    @Bind(R.id.create_collect_tea_btn)
    LinearLayout btnTeaCollection;
    @Bind(R.id.create_note_btn)
    LinearLayout btnCreateNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_create_notes);
        ButterKnife.bind(this);
        setListener();
    }

    private void setListener(){
        btnBack.setOnClickListener(this);
        btnCreateNote.setOnClickListener(this);
        btnCreateAtticle.setOnClickListener(this);
        btnTeaCollection.setOnClickListener(this);
    }



    public static void invoke(Context context){
        context.startActivity(new Intent(context,CreateNotesActivity.class));
    }


    @Override
    public void onClick(View v) {
        if(v==btnBack){
            finish();
        }else if(v==btnCreateNote){//便签
            CreateNoteActivity.invoke(this);
        }else if(v==btnCreateAtticle){//茶志
            CreateAtticleActivity.invoke(this);
        }else if(v==btnTeaCollection){//存茶
            CreateCollectTeaActivity.invoke(this);
        }
    }

}

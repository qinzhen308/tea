package com.becdoor.teanotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.parser.gson.BaseObject;
import com.becdoor.teanotes.parser.gson.GsonParser;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.HotSearchLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.*;

public class SearchActivity extends FragmentActivity
        implements OnClickListener {

    @Bind(R.id.search_btn)
    Button btn;
    private int[] drawable = {R.drawable.hot_search_tv_bg_1, R.drawable.hot_search_tv_bg_2, R.drawable.hot_search_tv_bg_3, R.drawable.hot_search_tv_bg_4, R.drawable.hot_search_tv_bg_5};

    @Bind(R.id.edtitext)
    EditText edittext;


    @Bind(R.id.layout_hot_search)
    HotSearchLayout hotSearchLayout;
    List<String> hotKeywords = new ArrayList<>();

    void initHotView() {
        MarginLayoutParams lp = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        lp.leftMargin = (int) getResources().getDimensionPixelSize(
                R.dimen.tv_hot_search_margin);
        lp.rightMargin = (int) getResources().getDimensionPixelSize(
                R.dimen.tv_hot_search_margin);
        lp.topMargin = (int) getResources().getDimensionPixelSize(
                R.dimen.tv_hot_search_margin);
        lp.bottomMargin = (int) getResources().getDimensionPixelSize(
                R.dimen.tv_hot_search_margin);
        int padding=getResources().getDimensionPixelSize(R.dimen.margin_10);
        for (int i = 0; i < hotKeywords.size(); i++) {
            TextView view = new TextView(this);
            view.setText(hotKeywords.get(i));
            view.setTextColor(getResources().getColor(R.color.white));
            view.setBackgroundDrawable(getResources().getDrawable(drawable[i % 5]));
            view.setPadding(padding,padding,padding,padding);
            hotSearchLayout.addView(view, lp);
        }
    }

    private void loadHotSearchData(){
        HashMap params=new HashMap();
        NetUtil.getData(this, Constant.BASE_URL + "search.php?", params, "search", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onScuccess(String response, int id) {
                BaseObject<PageData> obj= GsonParser.getInstance().parseToObj(response,PageData.class);
                if(obj!=null&&obj.status==200&&obj.data!=null){
                    hotKeywords=obj.data.words;
                    initHotView();
                }

            }
        });

    }

    private static class PageData{
        List<String> words;
    }


    @OnClick({R.id.top_back_btn})
    public void BackClick(View paramView) {
        finish();
    }

    @OnClick({R.id.search_btn})
    public void SearchClick(View paramView) {
        search(edittext.getText().toString());

    }

    public void onClick(View paramView) {
    }

    private void search(String key){
        if(TextUtils.isEmpty(key)){
            Toast.makeText(this,"输人的关键字不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        SearchResultActivity.invoke(SearchActivity.this,key);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_search_index);
        ButterKnife.bind(this);
        setListener();
        loadHotSearchData();
    }

    private void setListener(){
        edittext.setOnEditorActionListener(new EditListener());
        hotSearchLayout.setOnItemClick(new HotSearchLayout.OnItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                String keywords=hotKeywords.get(position);
                edittext.setText(keywords);
                SearchResultActivity.invoke(SearchActivity.this,keywords);
            }
        });
    }


    class EditListener implements OnEditorActionListener {
        EditListener() {
        }

        public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent) {
            if (paramInt == EditorInfo.IME_ACTION_SEARCH) {
                SearchResultActivity.invoke(SearchActivity.this,paramTextView.getText().toString());
            }
            return true;
        }
    }

    public static void invoke(Context context){
        context.startActivity(new Intent(context,SearchActivity.class));
    }
}

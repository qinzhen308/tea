package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.CustomStepInfo;
import com.becdoor.teanotes.model.CustomTeaInfo;
import com.becdoor.teanotes.model.MakeTeaMethod;
import com.becdoor.teanotes.parser.gson.BaseObject;
import com.becdoor.teanotes.parser.gson.BaseObjectList;
import com.becdoor.teanotes.parser.gson.GsonParser;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.PickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomStepActivity extends BaseActivity {
    public static final int REQUEST_CODE=412;
    private int index = 1;

    @Bind(R.id.method_name_et)
    EditText method_name_et;
    private PopupWindow pop;

    @Bind(R.id.scroll_step)
    ScrollView scrollView;

    @Bind(R.id.step_layout)
    LinearLayout step_layout;
    private List<CustomStepInfo> step_list = new ArrayList();
    private List<String> temp_list=new ArrayList<>();
    private List<String> time_list=new ArrayList<>();
    List<View> viewList;

    private Dialog loadDialog;

    public PopupWindow initPopWindow(Activity paramActivity, List<String> paramList, AdapterView.OnItemClickListener paramOnItemClickListener, View paramView) {
        Object localObject = null;
        if (paramActivity != null) {
            localObject = (ListView) LayoutInflater.from(this).inflate(R.layout.layout_popwindow_listview1, null);
            ((ListView) localObject).setAdapter(new ArrayAdapter<String>(paramActivity, R.layout.layout_popwindow_listview_item1, R.id.popwindow_tv, paramList));
            ((ListView) localObject).setOnItemClickListener(paramOnItemClickListener);
            localObject = new PopupWindow((View) localObject, 150, -2);
            ((PopupWindow) localObject).setFocusable(true);
            ((PopupWindow) localObject).setTouchable(true);
            ((PopupWindow) localObject).setBackgroundDrawable(paramActivity.getResources().getDrawable(R.drawable.popwindow_bg));
            ((PopupWindow) localObject).setOutsideTouchable(true);
            ((PopupWindow) localObject).showAsDropDown(paramView, -20, 0);
        }
        return (PopupWindow)localObject;
    }

    @OnClick({R.id.add_step_btn})
    public void AddClick(View paramView) {
        View view = View.inflate(this, R.layout.layout_custom_make_tea_item, null);
        ((TextView) view.findViewById(R.id.index_tv)).setText("" + index);
        EditText etStep = (EditText) view.findViewById(R.id.step_et);
        final Button btnTime = (Button) view.findViewById(R.id.time_btn);
        final Button btnTemp = (Button) view.findViewById(R.id.temp_btn);
        btnTemp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                pop = initPopWindow(CustomStepActivity.this, temp_list, new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> paramAnonymous2AdapterView, View paramAnonymous2View, int paramAnonymous2Int, long paramAnonymous2Long) {
                                pop.dismiss();
                                btnTemp.setText(temp_list.get(paramAnonymous2Int));
                            }
                        }
                        , paramAnonymousView);
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                pop = initPopWindow(CustomStepActivity.this, time_list, new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> paramAnonymous2AdapterView, View paramAnonymous2View, int paramAnonymous2Int, long paramAnonymous2Long) {
                                pop.dismiss();
                                btnTime.setText(time_list.get(paramAnonymous2Int));
                            }
                        }
                        , paramAnonymousView);
            }
        });
        this.step_layout.addView(view);
        new Handler().post(new Runnable() {
            public void run() {
                CustomStepActivity.this.scrollView.fullScroll(130);
            }
        });
        this.index += 1;
    }

    @OnClick({R.id.cancel_btn})
    public void CancelClick(View paramView) {
        finish();
    }

    @OnClick({R.id.create_method_btn})
    public void CreateMethod(View paramView) {
        String title=method_name_et.getText().toString();
        if(title.length()==0){
            Toast.makeText(getApplicationContext(),"请填入泡法名",Toast.LENGTH_LONG).show();
            return;
        }
        JSONArray array=new JSONArray();
        for(int i=0,size=step_layout.getChildCount();i<size;i++){
            View child=step_layout.getChildAt(i);
            String time=((Button)child.findViewById(R.id.time_btn)).getText().toString();
            String temp=((Button)child.findViewById(R.id.temp_btn)).getText().toString();
            String stepName=((EditText)child.findViewById(R.id.step_et)).getText().toString();
            if(TextUtils.isEmpty(stepName)){
                Toast.makeText(getApplicationContext(),"请填写步骤名",Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(time)){
                Toast.makeText(getApplicationContext(),"请选择时间",Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(temp)){
                Toast.makeText(getApplicationContext(),"请选择温度",Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject obj=new JSONObject();
            try {
                obj.put("name",stepName).put("temp",temp).put("time",time);
                array.put(i,obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(array.length()==0){
            Toast.makeText(getApplicationContext(),"请添加步骤",Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject obj=new JSONObject();
        try {
            obj.put("method_name",title).put("list",array);
            SubmitInfo(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetInfo() {
        DialogUtil.showDialog(loadDialog);
        HashMap<String, String> params = new HashMap<>();
        NetUtil.getData(this, Constant.BASE_URL + "addNote.php?", params, "addMethod", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                BaseObject<CustomTeaInfo> obj = GsonParser.getInstance().parseToObj(response, CustomTeaInfo.class);
                if (obj != null && obj.status == 200 && obj.data != null) {
                    if(obj.data.temps_list!=null){
                        temp_list=obj.data.temps_list;
                    }
                    if(obj.data.times_list!=null){
                        time_list=obj.data.times_list;
                    }
                } else if (obj != null && obj.status == 211) {
//                    Toast.makeText(getApplicationContext(), "暂无泡发,请添加", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void SubmitInfo(String data) {
        DialogUtil.showDialog(loadDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("data",data);
        NetUtil.postData(this, Constant.BASE_URL + "addNote.php?", params, "doAddMethod", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                BaseObject<String> obj = GsonParser.getInstance().parseToObj(response, String.class);
                if (obj != null && obj.status == 200 ) {
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else if (obj != null && obj.status == 211) {
                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.dialog_custom_step);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        viewList = new ArrayList();
        loadDialog=DialogUtil.getLoadingDialog(this);
        GetInfo();
    }

    public static void invoke(Activity context){
        Intent intent=new Intent(context,CustomStepActivity.class);
        context.startActivityForResult(intent,REQUEST_CODE);
    }

}
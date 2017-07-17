package com.becdoor.teanotes.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.CustomStepInfo;
import com.becdoor.teanotes.model.MakeTeaMethod;
import com.becdoor.teanotes.parser.gson.BaseObjectList;
import com.becdoor.teanotes.parser.gson.GsonParser;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.PickerView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by paulz on 2016/11/8.
 * 泡茶
 */

public class MakeTeaActivity extends TitleActivity {
    private String chooseMethodId = "";


    @Bind(R.id.compile_btn)
    View compile_btn;

    private List<MakeTeaMethod> list = new ArrayList<>();
    private String method_name = "";
    @Bind(R.id.pick_layout)
    RelativeLayout pick_layout;

    @Bind(R.id.start_btn)
    ImageView start_btn;

    private PickerView type_pv;

    Dialog loadDialog;
    Dialog editDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_tea);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    public void initView() {
        loadDialog = DialogUtil.getLoadingDialog(this);
    }

    @OnClick({R.id.tea_timer_custom_btn})
    public void CustomClick(View paramView) {
        if (!AppStatic.isLogin) {
            Toast.makeText(this, "你还没有登录  请先登录", Toast.LENGTH_LONG).show();
//            startActivity(new Intent().putExtra("isMine", false).setClass(this, LoginActivity.class));
            return;
        }
        CustomStepActivity.invoke(this);
    }

    @OnClick({R.id.top_back_btn})
    public void backClick(View paramView) {
        finish();
    }

    @OnClick({R.id.start_btn})
    public void StartTimeClick(View paramView) {
        if (this.list.size() != 0) {
            MakeTeaTimingActivity.invoke(this, chooseMethodId);
            return;
        }
        Toast.makeText(this, "请添加泡法", Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.compile_btn})
    public void editClick(View paramView) {
        showEditDialog();
    }


    private void initData() {
        DialogUtil.showDialog(loadDialog);
        HashMap<String, String> params = new HashMap<>();
        NetUtil.getData(this, Constant.BASE_URL + "index.php?", params, "timer", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                BaseObjectList<MakeTeaMethod> obj = GsonParser.getInstance().parseToObj4List(response, MakeTeaMethod.class);
                if (obj != null && obj.status == 200 && obj.data != null) {
                    list = obj.data;
                    type_pv = new PickerView(MakeTeaActivity.this);
                    ArrayList<String> methodNames = new ArrayList<String>();
                    for (int i = 0; i < list.size(); i++) {
                        methodNames.add(list.get(i).method_name);
                    }
                    if(list.size()>0){
                        MakeTeaMethod firstMethod = list.get(0);
                        method_name = firstMethod.method_name;
                        chooseMethodId = firstMethod.id;
                        if (firstMethod.type != 0) {
                            compile_btn.setVisibility(View.VISIBLE);
                        } else {
                            compile_btn.setVisibility(View.GONE);
                        }
                    }
                    pick_layout.addView(type_pv);
                    type_pv.setData(methodNames);
                    type_pv.setOnSelectListener(new PickerView.onSelectListener() {
                        @Override
                        public void onSelect(String selected) {
                            for (int i = 0; i < list.size(); i++) {
                                MakeTeaMethod method = list.get(i);
                                if (selected.equals(method.method_name)) {
                                    method_name = selected;
                                    chooseMethodId = method.id;
                                    if (method.type != 0) {
                                        compile_btn.setVisibility(View.VISIBLE);
                                    } else {
                                        compile_btn.setVisibility(View.GONE);
                                    }
                                    break;
                                }
                            }
                        }
                    });
                } else if (obj != null && obj.status == 211) {
                    Toast.makeText(getApplicationContext(), "暂无泡发,请添加", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void showEditDialog() {
        if (editDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_make_tea, null);
            view.findViewById(R.id.delete_soaking_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCustomTea();
                    if (!isFinishing()) DialogUtil.dismissDialog(editDialog);
                }
            });
            view.findViewById(R.id.edit_soaking_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditCustomStepActivity.invoke(MakeTeaActivity.this, chooseMethodId);
                    if (!isFinishing()) DialogUtil.dismissDialog(editDialog);
                }
            });
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFinishing()) DialogUtil.dismissDialog(editDialog);
                }
            });
            editDialog = DialogUtil.getMenuDialog(this, view);
        }
        DialogUtil.showDialog(editDialog);
    }

    private void deleteCustomTea() {
        DialogUtil.showDialog(loadDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("mid", chooseMethodId);
        NetUtil.postData(this, Constant.BASE_URL + "index.php?", params, "delMethod", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                Toast.makeText(getApplicationContext(), "操作失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                BaseObjectList<MakeTeaMethod> obj = GsonParser.getInstance().parseToObj4List(response, MakeTeaMethod.class);
                if (obj != null && obj.status == 200) {
                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                    list.clear();
                    pick_layout.removeAllViews();
                    initData();
                } else if (obj != null && obj.status == 211) {
                    Toast.makeText(getApplicationContext(), "操作失败", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "操作失败", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 223) {
                this.list.clear();
                this.pick_layout.removeAllViews();
                initData();
            } else if (requestCode == CustomStepActivity.REQUEST_CODE) {//dialog 自定义泡茶方法
                this.list.clear();
                this.pick_layout.removeAllViews();
                initData();
            }
        }
    }


    public static void invoke(Context context) {
        Intent intent = new Intent(context, MakeTeaActivity.class);
        context.startActivity(intent);
    }
}

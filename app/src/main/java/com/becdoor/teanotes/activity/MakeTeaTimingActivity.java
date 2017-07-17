package com.becdoor.teanotes.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.CustomTeaMethod;
import com.becdoor.teanotes.model.CustomTeaStep;
import com.becdoor.teanotes.parser.gson.BaseObject;
import com.becdoor.teanotes.parser.gson.GsonParser;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.CountDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class MakeTeaTimingActivity extends Activity {

    @Bind(R.id.back_btn)
    Button back_btn;

    @Bind(R.id.comment_notice_name_tv)
    TextView comment_notice_name_tv;
    private int count;

    @Bind(R.id.count_tv)
    TextView count_tv;
    private int index = 0;
    private boolean isStart = false;
    private List<CustomTeaStep> list = new ArrayList();

    protected Dialog mDialog;
    private String method_id;

    @Bind(R.id.next_btn)
    Button next_btn;

    @Bind(R.id.start_time_btn)
    ImageView start_time_btn;
    private int state = 0;

    @Bind(R.id.step_name_tv)
    TextView step_name_tv;

    @Bind(R.id.step_tv)
    TextView step_tv;

    @Bind(R.id.temp_tv)
    TextView temp_tv;

    @Bind(R.id.time_view)
    CountDown time_view;

    private Dialog loadDialog;

    private void alertTimeEnd(String paramString) {
        final AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
        localAlertDialog.show();
        Window localWindow = localAlertDialog.getWindow();
        localWindow.setContentView(R.layout.dialog_time_end_);
        TextView localTextView = (TextView) localWindow.findViewById(R.id.dialog_content_tv);
        SpannableString localSpannableString = new SpannableString(paramString);
        localSpannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#7D6647")), 0, paramString.length(), 33);
        localTextView.setText("您的" + localSpannableString + "计时完成");
        ((Button) localWindow.findViewById(R.id.cancel_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localAlertDialog.dismiss();
                isStart = false;
            }
        });
        Button nextBtn = (Button) localWindow.findViewById(R.id.next_btn);
        if (index == count - 1){
            nextBtn.setText("完成");
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localAlertDialog.dismiss();
                isStart = false;
                next();
            }
        });
    }

    private void hint(String paramString) {
        if (paramString.equals("醒茶")) {
            this.comment_notice_name_tv.setText("注水三成满，低温醒茶；揭盖浸泡30秒后出汤");
            return;
        }
        if (paramString.equals("润茶")) {
            this.comment_notice_name_tv.setText("注水八成满；时间从注水时开始计算，注水10秒，注完水后立刻出汤");
            return;
        }
        if (paramString.equals("洗茶")) {
            this.comment_notice_name_tv.setText("");
            return;
        }
        if (paramString.equals("第一泡")) {
            this.comment_notice_name_tv.setText("注水八成满；注水8秒，浸泡8秒，出汤约8秒");
            return;
        }
        if (paramString.equals("第二泡")) {
            this.comment_notice_name_tv.setText("注水八成满；注水8秒，浸泡8秒，出汤约8秒");
            return;
        }
        this.comment_notice_name_tv.setText("浸泡时间顺延5秒，注水、出汤时间一样");
    }

    private void next() {
        if (this.list.size() <= 0)
            return;
        if (this.state == 1) {
            this.state = 0;
            this.start_time_btn.setImageResource(R.drawable.start_time_icon);
        }
        this.index += 1;
        if (this.index < this.count) {
            this.step_tv.setText(""+(index + 1));
            this.step_name_tv.setText(this.list.get(this.index).stage_name);
            this.temp_tv.setText(((CustomTeaStep) this.list.get(this.index)).stage_temp.replace('度', '℃'));
            Object localObject2 = ((CustomTeaStep) this.list.get(this.index)).stage_time;
            Object localObject1 = localObject2;
            if (((String) localObject2).equals("无"))
                localObject1 = "0s";
            localObject2 = localObject1;
            if (((String) localObject1).endsWith("s"))
                localObject2 = ((String) localObject1).replace("s", "");
            this.time_view.setSecondUntilFinished(Long.parseLong((String) localObject2));
            if (this.index == this.count - 1){
                this.next_btn.setText("完成");
            }else {
                this.next_btn.setText("下一步");
            }
        }
        if (count == 1)
            back_btn.setVisibility(View.GONE);
        else if ((index == count) || (count == 1)) {
            finish();
        } else {
            back_btn.setVisibility(View.VISIBLE);
            hint(this.list.get(this.index).stage_name);
        }
    }

    @OnClick({R.id.back_btn})
    public void BackClick(View paramView) {
        this.index -= 1;
        this.step_tv.setText(index + 1+"");
        this.step_name_tv.setText(this.list.get(this.index).stage_name);
        String temp = list.get(index).stage_temp.replace('度', '℃');
        this.temp_tv.setText(temp);
        String str = this.list.get(this.index).stage_time;
        temp = str;
        if (str.endsWith("s"))
            temp = str.substring(0, str.length() - 1);
        this.time_view.setSecondUntilFinished(Long.parseLong(temp));
        System.out.println("时间：：" + Long.parseLong(temp));
        if (this.index == 0) {
            this.back_btn.setVisibility(View.GONE);
            this.next_btn.setText("下一步");
        }
        hint(this.list.get(this.index).stage_name);
    }

    @OnClick({R.id.top_back_btn})
    public void BackPageClick(View paramView) {
        finish();
    }

    public void GetInfo(String id) {
        DialogUtil.showDialog(loadDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("mid", id);
        NetUtil.getData(this, Constant.BASE_URL + "addNote.php?", params, "selMethod", new NetUtil.NetUtilCallBack() {
                    @Override
                    public void onFail(Call call, Exception e, int id) {
                        if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                    }

                    @Override
                    public void onScuccess(String response, int id) {
                        if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                        BaseObject<CustomTeaMethod> obj = GsonParser.getInstance().parseToObj(response, CustomTeaMethod.class);
                        if (obj != null && obj.status == 200 && obj.data != null) {
                            if (obj.data.stage_list == null || obj.data.stage_list.size() == 0) {
                                Toast.makeText(MakeTeaTimingActivity.this, "该泡法下无步骤", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            list.addAll(obj.data.stage_list);
                            count = obj.data.count;
                            if (obj.data.method_name.equals("斗记生普泡法")) {
                                comment_notice_name_tv.setVisibility(View.VISIBLE);
                                comment_notice_name_tv.setText("注水三成满，低温醒茶；揭盖浸泡30秒后出汤");
                            }
                            count_tv.setText(""+count);
                            step_tv.setText("" + index + 1);
                            step_name_tv.setText(list.get(index).stage_name);
                            temp_tv.setText(list.get(index).stage_temp.replace('度', '℃'));
                            if (!isStart) {
                                String time = list.get(index).stage_time;
                                if (!TextUtils.isEmpty(time)) {
                                    if (time.endsWith("s")) {
                                        time = time.substring(0, time.length() - 1);
                                    } else if (time.equals("无")) {
                                        time = "0";
                                    }
                                    time_view.setSecondUntilFinished(Long.parseLong(time));
                                }
                            }
                            comment_notice_name_tv.setVisibility(View.GONE);

                        } else if (obj != null && obj.status == 211) {
//                    Toast.makeText(getApplicationContext(), "暂无泡发,请添加", Toast.LENGTH_LONG).show();
                        } else {
//                    Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

    }

    @OnClick({R.id.next_btn})
    public void NextClick(View paramView) {
        next();
    }

    @OnClick({R.id.start_time_btn})
    public void StartClick(View paramView) {
        if (this.list.size() > 0) {
            if (this.state == 0) {
                this.isStart = true;
                this.time_view.start();
                this.start_time_btn.setImageResource(R.drawable.time_pause_btn_bg);
                this.state = 1;
                return;
            } else if (this.state == 1) {
                this.isStart = false;
                this.time_view.pause();
                this.state = 2;
                this.start_time_btn.setImageResource(R.drawable.time_continue_btn_bg);
                return;
            } else if (this.state == 2) {
                this.isStart = true;
                this.time_view.continueTime();
                this.state = 1;
                this.start_time_btn.setImageResource(R.drawable.time_pause_btn_bg);
            }
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_start_time);
        ButterKnife.bind(this);
        loadDialog = DialogUtil.getLoadingDialog(this);
        this.method_id = getIntent().getStringExtra("method_id");
        GetInfo(this.method_id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(time_view!=null){
            time_view.setUntilFinishedCallBack(null);
            time_view.stop();
        }
    }

    protected void onResume() {
        super.onResume();
        this.time_view.setUntilFinishedCallBack(new CountDown.UntilFinished() {
            public void finishedCallback() {
                if ((isStart) && (list != null) && (list.size() > index))
                    alertTimeEnd(((CustomTeaStep) list.get(index)).stage_name);
            }
        });
    }

    public static void invoke(Context context, String id) {
        Intent intent = new Intent(context, MakeTeaTimingActivity.class);
        intent.putExtra("method_id", id);
        context.startActivity(intent);
    }
}
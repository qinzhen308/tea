package com.becdoor.teanotes.fragment;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.becdoor.teanotes.DJApplication;
import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.BaseActivity;
import com.becdoor.teanotes.activity.CreateNotesActivity;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.update.VersionS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/***
 * 主要的fragment界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    //定义fragment界面
    @Bind(R.id.layout_main)
    TextView FrameLayout_main;
    @Bind(R.id.layout_activity)
    TextView Flayout_Activity;
    @Bind(R.id.layout_douji)
    TextView Flayout_Douji;
    @Bind(R.id.layout_member)
    TextView Flayout_Memeber;
    @Bind(R.id.plus_btn)
    ImageView getPlusImageview;

    private FragmentTransaction fragmentTransaction;
    public FragmentActivity1 fragmentActivity1;

    public static String TAB1 = "tab1";
    public static String TAB2 = "tab2";
    public static String TAB3 = "tab3";
    public static String TAB4 = "tab4";
    public static String TAB5 = "tab5";

    public static String tag = "";
    private boolean isFirst = true;
    private Handler mHandler = new Handler();
    private int mClickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        intiData();
        initFragment();
        if (savedInstanceState != null) {
//            tag=savedInstanceState.getString("cur_tag");
            restoreFrag();
        }
        updateApp();
    }

    private void restoreFrag() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f1 = getSupportFragmentManager().findFragmentByTag(TAB1);
        if (f1 != null && f1.isAdded()) ft.show(f1);

        Fragment f2 = getSupportFragmentManager().findFragmentByTag(TAB2);
        if (f2 != null && f2.isAdded()) ft.hide(f2);

        Fragment f3 = getSupportFragmentManager().findFragmentByTag(TAB3);
        if (f3 != null && f3.isAdded()) ft.hide(f3);

        Fragment f4 = getSupportFragmentManager().findFragmentByTag(TAB4);
        if (f4 != null && f4.isAdded()) ft.hide(f4);

        Fragment f5 = getSupportFragmentManager().findFragmentByTag(TAB5);
        if (f5 != null && f5.isAdded()) ft.hide(f5);

        ft.commitAllowingStateLoss();
    }

    private void restoreRemoveFrag() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f1 = getSupportFragmentManager().findFragmentByTag(TAB1);
        if (f1 != null) ft.remove(f1);

        Fragment f2 = getSupportFragmentManager().findFragmentByTag(TAB2);
        if (f2 != null) ft.remove(f2);

        Fragment f3 = getSupportFragmentManager().findFragmentByTag(TAB3);
        if (f3 != null) ft.remove(f3);

        Fragment f4 = getSupportFragmentManager().findFragmentByTag(TAB4);
        if (f4 != null) ft.remove(f4);

        Fragment f5 = getSupportFragmentManager().findFragmentByTag(TAB5);
        if (f5 != null) ft.remove(f5);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String t = intent.getStringExtra("tag");
            if (t != null) {
                tag = t;
                switchToFragment(tag, intent);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cur_tag", tag);
    }

    /***
     * 初始化组件
     *
     * @param
     */
    private void intiData() {
        FrameLayout_main.setOnClickListener(this);
        Flayout_Activity.setOnClickListener(this);
        Flayout_Douji.setOnClickListener(this);
        Flayout_Memeber.setOnClickListener(this);
        getPlusImageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_activity:
                tag = TAB2;
                break;
            case R.id.layout_douji:
                tag = TAB4;
                break;
            case R.id.layout_member:
                tag = TAB5;
                break;
            case R.id.layout_main:
                tag = TAB1;
                break;
            case R.id.plus_btn:
                tag = TAB3;
                break;
        }
        switchToFragment(tag, null);

    }

    private void initFragment() {
        if (isFirst) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            currentFrag = new FragmentActivity1();
            Bundle bundle = new Bundle();
            bundle.putInt(FragmentActivity1.POSITION_NAME, 1);
            currentFrag.setArguments(bundle);
            fragmentTransaction.add(R.id.frame_content, currentFrag, TAB1).commit();
            isFirst = false;
            tag = TAB1;
            changeData(tag);
        } else {
            if (!tag.equals(TAB4)) {
                switchToFragment(tag, null);
            }
        }
    }

    Fragment currentFrag = null;

    /**
     * fragment跳转
     *
     * @param Tag
     */
    public void switchToFragment(String Tag, Intent intent) {
        changeData(tag);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment findresult;
        findresult = getSupportFragmentManager().findFragmentByTag(Tag);
        if (currentFrag != null && currentFrag.getTag().equals(Tag)) {
            // 判断为相同fragment不切换
        } else {
            if (findresult != null) {
                fragmentTransaction.hide(currentFrag).show(findresult).commit();
            } else {

                if (Tag.equals(TAB1)) {
                    findresult = new FragmentActivity1();
                    if (intent != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActivity1.POSITION_NAME, intent.getIntExtra(FragmentActivity1.POSITION_NAME, 1));
                        findresult.setArguments(bundle);
                    }

                } else if (Tag.equals(TAB2)) {
                    findresult = new FragmentActivities();

                } else if (Tag.equals(TAB3)) {
                    CreateNotesActivity.invoke(this);
                    return;

                } else if (Tag.equals(TAB4)) {
                    findresult = new FragmentRank();

                } else if (Tag.equals(TAB5)) {
                    findresult = new FragmentMember();

                }
                fragmentTransaction.hide(currentFrag).add(R.id.frame_content, findresult, Tag).commit();
            }
        }
        currentFrag = findresult;

    }

    private void changeData(String tag) {
        if (tag.equals(TAB1)) {
            FrameLayout_main.setSelected(true);
            Flayout_Activity.setSelected(false);
            Flayout_Douji.setSelected(false);
            Flayout_Memeber.setSelected(false);
        } else if (tag.equals(TAB2)) {
            FrameLayout_main.setSelected(false);
            Flayout_Activity.setSelected(true);
            Flayout_Douji.setSelected(false);
            Flayout_Memeber.setSelected(false);
        } else if (tag.equals(TAB3)) {

        } else if (tag.equals(TAB4)) {
            FrameLayout_main.setSelected(false);
            Flayout_Activity.setSelected(false);
            Flayout_Douji.setSelected(true);
            Flayout_Memeber.setSelected(false);
        } else if (tag.equals(TAB5)) {
            FrameLayout_main.setSelected(false);
            Flayout_Activity.setSelected(false);
            Flayout_Douji.setSelected(false);
            Flayout_Memeber.setSelected(true);
        }
    }


    @Override
    public void onBackPressed() {

        if (mClickCount++ < 1) {
            CustomToast.showToast(this, "再按一次就退出斗记", 1000);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mClickCount = 0;
                }
            }, 2000);
            return;
        }

        mHandler.removeCallbacksAndMessages(null);
        super.onBackPressed();
    }


    //--------------------升级-----------------------------

    /**
     * 检测版本更新
     */
    private void updateApp() {
        HashMap<String, String> params = new HashMap<>();
        params.put("app_version_id", "" + DJApplication.getInstance().getVersionName());
        params.put("app_type", "2");
        //测试用
//        params.put("app_version_id", "1.0");
//        params.put("op", "up");
//        params.put("desc", "第一版");
        NetUtil.postData(this, Constant.BASE_URL+"api.php?", params, "init", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object != null && object.getString("status").equals("200")) {
                        String desc = object.getString("update_desc");
                        String url = object.getString("apk_url");
                        int status = object.getInt("upStatus");
                        if(url!=null&&url.startsWith("/")){
                            url=url.substring(1,url.length());
                        }
                        AppStatic.getInstance().down_url = Constant.REALM_NAME+url;
                        if (status == 1) {//升级
                            showUpdateDialog(status);
                        } else if (status == 2) {//强制升级
                            showUpdateDialog(status);
                        }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });
    }

    private Dialog mUpdateDialog;

    private void showUpdateDialog(final int status) {
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_update, null);
        TextView mDTextTv = (TextView) view
                .findViewById(R.id.dialog_updateTextTv);
        TextView shaohouTv = (TextView) view.findViewById(R.id.dialog_update_shaohou);
        TextView lijiTv = (TextView) view.findViewById(R.id.dialog_update_liji);
        if (status == 1) {
            mDTextTv.setText("已有新版本，请更新...");
            shaohouTv.setText("取消");
        } else if (status == 2) {
            shaohouTv.setText("退出");
            mDTextTv.setText("您的版本过旧，请下载最新版本");
        }
        lijiTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUpdateDialog.dismiss();
                checkUpdata();
            }
        });
        shaohouTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUpdateDialog.dismiss();
                if (status == 1) {

                } else if (status == 2) {
                    DJApplication.getInstance().exit();
                }
            }
        });
        mUpdateDialog = DialogUtil.getCenterDialog(this, view);
        mUpdateDialog.show();

    }

    /***
     * 检查更新
     */
    public void checkUpdata() {
        if (!isServiceRunning(MainActivity.this, VersionS.class.getName(), 200)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(MainActivity.this,
                            VersionS.class);
                    intent.putExtra("isUpdateDownload", true);
                    startService(intent);
                }
            }, 200);
        } else {
            Toast.makeText(MainActivity.this, "正在下载...", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 判断服务是否在运行
     *
     * @param context
     * @param className
     * @param maxServiceNum
     * @return
     */
    public boolean isServiceRunning(Context context, String className,
                                    int maxServiceNum) {
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getRunningServices(maxServiceNum);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfos) {
            if (runningServiceInfo.service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}

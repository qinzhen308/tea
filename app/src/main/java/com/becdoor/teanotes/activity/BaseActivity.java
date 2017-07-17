package com.becdoor.teanotes.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by Administrator on 2016/9/19.
 */
public abstract class BaseActivity extends FragmentActivity {
    PopupWindow popupWindow;
    Dialog mLoaDailog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoaDailog = DialogUtil.getLoadingDialog(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle arg0) {
        // super.onSaveInstanceState(arg0);
        if (arg0 != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            arg0.remove(FRAGMENTS_TAG);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    void initRightWindow(int width, int height, final BaseAdapter adapter) {
        if (popupWindow == null) {
            View pView = LayoutInflater.from(this).inflate(R.layout.layout_popwindow_listview1, null);
            ListView popWindowListView = (ListView) pView.findViewById(R.id.popwindow_lv);
            int[] screenWH = AppUtil.getScreenWH(this);
            if (width == 0) {
                width = (int) ((float) screenWH[0] / 5);
            }

//            if (height==0){
//                height= ViewGroup.LayoutParams.WRAP_CONTENT;
//            }
            if (height != 0) {
//                height = (int) ((float) screenWH[1] / 3);
                popupWindow = new PopupWindow(pView, width, height);
            } else {
                popupWindow = new PopupWindow(pView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setTouchable(true);

            popWindowListView.setAdapter(adapter);
            popWindowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.dismiss();
                    onRightItemClick(adapter.getItem(position), position);

                }
            });
        }

    }

    void onRightItemClick(Object object, int position) {
    }

    /**
     * 显示右边的Ｗｉｎｄｏｗｓ
     *
     * @param view
     */
    void showRightVindow(View view) {
        if (popupWindow != null) {
            showWindow(view);
        }
    }

    /**
     * 显示右边的 分享、点赞等
     *
     * @param view
     */
//    @TargetApi(Build.VERSION_CODES.KITKAT)
    void showCommonRightView(View view) {
        if (popupWindow == null) {
            String[] strings = getResources().getStringArray(R.array.list_popWindow);
            ArrayAdapter<String> wAdapter = new ArrayAdapter<>(this, R.layout.layout_popwindow_listview_item1, R.id.popwindow_tv, strings);
            initRightWindow(0, 0, wAdapter);
        }
        showWindow(view);

    }

    /**
     * 显示右边的 分享、点赞等(活动详情 含报名 没有点赞)
     *
     * @param view
     */
    void showCommonRightViewForEnroll(View view) {
        if (popupWindow == null) {
            String[] strings = getResources().getStringArray(R.array.list_popWindow_enroll);
            ArrayAdapter<String> wAdapter = new ArrayAdapter<>(this, R.layout.layout_popwindow_listview_item1, R.id.popwindow_tv, strings);
            initRightWindow(0, 0, wAdapter);
        }
        showWindow(view);
    }

    private void showWindow(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(view, 0, 0, Gravity.BOTTOM);
        } else {
//            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            popupWindow.showAsDropDown(view);
        }
    }
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    void showRightWindow(View view, int width, int height) {
//        if (popupWindow == null) {
//            View pView = LayoutInflater.from(this).inflate(R.layout.layout_popwindow_listview1, null);
//            ListView mListView = (ListView) pView.findViewById(R.id.popwindow_lv);
//            int[] screenWH = AppUtil.getScreenWH(this);
//            if (width == 0) {
//                width = (int) ((float) screenWH[0] / 5);
//            }
//            popupWindow = new PopupWindow(pView, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//            popupWindow.setOutsideTouchable(true);
//            String[] strings = getResources().getStringArray(R.array.list_popWindow);
//            ArrayAdapter<String> wAdapter = new ArrayAdapter<>(this, R.layout.layout_popwindow_listview_item1, R.id.popwindow_tv, strings);
//            mListView.setAdapter(wAdapter);
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    popupWindow.dismiss();
//                }
//            });
//        }
//        popupWindow.showAsDropDown(view, 0, 0, Gravity.BOTTOM);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}

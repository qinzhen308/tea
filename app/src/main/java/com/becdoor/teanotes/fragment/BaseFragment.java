package com.becdoor.teanotes.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.until.DialogUtil;

/**
 * Created by Administrator on 2016/10/12.
 */

public abstract class BaseFragment extends Fragment {
    PopupWindow popupWindow;
    Dialog mLoaDailog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoaDailog = DialogUtil.getLoadingDialog(getActivity());
    }


    void showRightWindow(View view, int width, int height, final BaseAdapter adapter) {
        if (popupWindow == null) {
            View pView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popwindow_listview1, null);
            ListView popWindowListView = (ListView) pView.findViewById(R.id.popwindow_lv);
            int[] screenWH = AppUtil.getScreenWH(getActivity());
            if (width == 0) {
                width = (int) ((float) screenWH[0] / 5);
            }

            if (height != 0) {
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

    void onRightItemClick(Object object, int position) {
    }
}

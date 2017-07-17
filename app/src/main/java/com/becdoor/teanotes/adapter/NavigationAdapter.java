package com.becdoor.teanotes.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.model.CateInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jjj on 2016/11/10.
 * 栏目管理
 * "cat_type":分类类型(1为系统默认，3为用户自定义 -1:保存 )
 */

public class NavigationAdapter extends BaseAdapter {
    private Context mContext;
    private List<CateInfo> mList;

    public NavigationAdapter(Context context, List<CateInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CateInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_navigation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CateInfo info = getItem(position);
        if (info != null) {
            holder.nameEdt.setText(info.getCat_name());

            if ("1".equals(info.getCat_type())) {
                holder.deleteTv.setVisibility(View.INVISIBLE);
                holder.writeTv.setVisibility(View.INVISIBLE);
                holder.nameEdt.clearFocus();
            } else {
                if ("-1".equals(info.getCat_type())) {
                    holder.writeTv.setText("保存");

                    holder.nameEdt.setFocusable(true);
                    holder.nameEdt.setFocusableInTouchMode(true);
                    holder.nameEdt.requestFocus();
                } else {
                    holder.nameEdt.setFocusable(false);
                    holder.nameEdt.clearFocus();
                    holder.writeTv.setText("编辑");
                }
                holder.deleteTv.setVisibility(View.VISIBLE);
                holder.writeTv.setVisibility(View.VISIBLE);
            }
        }

        if (position == getCount() - 1) {
            holder.line.setVisibility(View.INVISIBLE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationListener != null) {
                    navigationListener.deteleNavigation(position);
                }
            }
        });

      final EditText editText = holder.nameEdt;
        holder.writeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationListener != null) {
                    navigationListener.updateOrAddNavigation(position, editText.getText().toString());
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.item_navigation_nameEdt)
        EditText nameEdt;
        @Bind(R.id.item_navigation_writeTv)
        TextView writeTv;
        @Bind(R.id.item_navigation_deleteTv)
        TextView deleteTv;
        @Bind(R.id.item_navigation_line)
        View line;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private NavigationListener navigationListener;

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public interface NavigationListener {
        void deteleNavigation(int position);

        void updateOrAddNavigation(int position, String name);


    }
}

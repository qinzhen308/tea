package com.becdoor.teanotes.data.api;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.becdoor.teanotes.R;


/**
 * Created by Administrator on 2016/10/12.
 */

public class TextViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;
    public TextViewHolder(View itemView) {
        super(itemView);
        mTextView= (TextView) itemView.findViewById(R.id.text);
    }
}

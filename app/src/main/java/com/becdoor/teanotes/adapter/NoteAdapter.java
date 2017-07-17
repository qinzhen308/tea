package com.becdoor.teanotes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.activity.CommonDetailActivity;
import com.becdoor.teanotes.activity.CreateNoteActivity;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.model.NoteReInfoBean;
import com.becdoor.teanotes.model.SimpleNoteWrapper;
import com.becdoor.teanotes.until.AppUtil;
import com.becdoor.teanotes.view.CircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 便签
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ItemViewHolder> {
    private Context mContext;
    private List<SimpleNoteWrapper.SimpleNote> mData;
    int screenW = 0;
    private int mode;

    /**
     * 设置数据
     *
     * @param
     */
    public void setData(List<SimpleNoteWrapper.SimpleNote> data) {
        this.mData = data;
    }


    public NoteAdapter(Context context) {
        this.mContext = context;
        screenW = AppUtil.getScreenWH((Activity) context)[0];
    }



    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public NoteAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_notes_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ItemViewHolder holder, int position) {
        final SimpleNoteWrapper.SimpleNote note = mData.get(position);
        holder.contentTv.setText(note.content);
        holder.titleTv.setText(note.title);
        holder.timeTv.setText(note.add_time);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                note.isChecked = isChecked;
            }
        });
        holder.checkBox.setChecked(note.isChecked);
        if (mode == 1) {
            holder.checkBox.setVisibility(View.VISIBLE);

        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNoteActivity.invoke((Activity) mContext,note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView contentTv;
        TextView timeTv;
        CheckBox checkBox;

        public ItemViewHolder(View view) {
            super(view);
            titleTv = (TextView) view.findViewById(R.id.title_tv);
            contentTv = (TextView) view.findViewById(R.id.content_tv);
            timeTv = (TextView) view.findViewById(R.id.time_tv);
            checkBox = (CheckBox) view.findViewById(R.id.notes_checkbox);
        }
    }

    public List<SimpleNoteWrapper.SimpleNote> getSelectedAtticles() {
        ArrayList selecteds = new ArrayList();
        if (mData == null) return selecteds;
        for (int i = 0, count = getItemCount(); i < count; i++) {
            SimpleNoteWrapper.SimpleNote bean = mData.get(i);
            if (bean.isChecked) {
                selecteds.add(bean);
            }
        }
        return selecteds;
    }
}

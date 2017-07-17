package com.becdoor.teanotes.listener;

import android.view.View;

import com.becdoor.teanotes.model.NoteCommentBean;

/**
 * Created by jjj on 2016/11/18.
 * 详情
 */

public interface DetailListener {
    void onShare();

    void onComment(NoteCommentBean noteCommentBean);

    void onPrise(View view);

    void onEnroll();

    void onCollect(View view);
}

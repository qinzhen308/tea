package com.becdoor.teanotes.listener;

import com.becdoor.teanotes.model.NoteCommentBean;

/**
 * Created by jjj on 2016/11/23.
 */

public interface NoteCommentListener {
    /**
     * 评价别人的评价
     */
    void commSingleNote(NoteCommentBean noteComment);
}

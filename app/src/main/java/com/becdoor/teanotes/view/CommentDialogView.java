package com.becdoor.teanotes.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.becdoor.teanotes.until.NetUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jjj on 2016/11/18.
 * 评论
 */

public class CommentDialogView extends LinearLayout {
    private Context mContext;
    @Bind(R.id.Dcomment_commentEdt)
    EditText mCommentEdt;
    @Bind(R.id.Dcomment_numTv)
    TextView mNumTv;
    NoteCommentBean noteCommentBean;

    public CommentDialogView(Context context) {
        super(context);
        init(context);
    }

    public CommentDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommentDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_comment, null);
        ButterKnife.bind(this, view);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mCommentEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int num = 0;
                if (!TextUtils.isEmpty(s)) {
                    num = s.toString().length();
                }
                mNumTv.setText(num + "/150");
            }
        });
        mNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment(mCommentEdt.getText().toString());
            }
        });
        mCommentEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    mCommentEdt.setText("");
                    submitComment(mCommentEdt.getText().toString());
                }
                return false;
            }
        });
    }

    void submitComment(String content) {
        if (TextUtils.isEmpty(content)) {
            CustomToast.showToast(mContext, "请输入评论内容！", 1500);
            return;
        }
        if (commentListener != null) {
            commentListener.onComment(content, noteCommentBean);
        }

    }

    /**
     * 回复某人评价是改变hint
     */
    public void updateEdtHintContent(NoteCommentBean noteCommentBean) {
        this.noteCommentBean = noteCommentBean;
        mCommentEdt.setText("");
        if (noteCommentBean != null) {
            mCommentEdt.setHint("回复 " + noteCommentBean.getNickname() + "：");
        } else {
            mCommentEdt.setHint("请输入评论内容");
        }
    }

    private CommentListener commentListener;

    public void setCommentListener(CommentListener commentListener) {
        this.commentListener = commentListener;
    }

    public interface CommentListener {
        void onComment(String content, NoteCommentBean noteCommentBean);
    }


}

package com.becdoor.teanotes.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;

import butterknife.ButterKnife;
import wang.raye.preioc.annotation.BindById;

/**
 * 带标题的activity
 */
public class TitleActivity extends BaseActivity implements View.OnClickListener {
    ImageView mTitleIv;
    TextView mMiddleTv;
    TextView mLeftTv;
    TextView mRightTv;
    ImageView mRightIv;
    FrameLayout mContentLayout;

    /**
     * 显示布局
     *
     * @param paramInt
     */
    void setActivityContextView(int paramInt) {
        setContentView(R.layout.activity_title);
        mTitleIv = (ImageView) findViewById(R.id.title_titleIv);
        mMiddleTv = (TextView) findViewById(R.id.title_titleTv);
        mLeftTv = (TextView) findViewById(R.id.title_leftTv);
        mRightTv = (TextView) findViewById(R.id.title_rightTv);
        mRightIv = (ImageView) findViewById(R.id.title_rightIv);
        mContentLayout = (FrameLayout) findViewById(R.id.title_contentLayout);

        mContentLayout.removeAllViews();
        LayoutInflater.from(this).inflate(paramInt, mContentLayout);
        initListener();
    }

    /**
     * 右边是TextView的title
     *
     * @param title                     标题
     * @param right                     textView值
     * @param showLeft                  是否显示左边
     * @param hasBackgroundForRigthView 右边按钮是否有背景
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void setTitleForRightTextView(String title, String right, boolean showLeft, boolean hasBackgroundForRigthView) {
        if (!TextUtils.isEmpty(title)) {
            mMiddleTv.setText(title);
        }

        if (!showLeft) {
            mLeftTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(right)) {
            mRightTv.setText(right);
            mRightTv.setVisibility(View.VISIBLE);

            if (hasBackgroundForRigthView) {
                mRightTv.setBackgroundResource(R.drawable.bg_rectangle_btn_coner);
//                Drawable drawable = getResources().getDrawable(R.drawable.right_arrow_icon2, null);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mRightTv.setCompoundDrawables(null, null, drawable, null);
            }
        }
    }

    /**
     * 右边是ImageView
     *
     * @param title                     标题
     * @param right                     按钮值 （没有则为0）
     * @param showLeft                  是否显示左边按钮
     * @param hasBackgroundForRigthView 是否有背景
     */
    void setTitleForRightIamgeView(String title, int right, boolean showLeft, boolean hasBackgroundForRigthView) {
        if (!TextUtils.isEmpty(title)) {
            mMiddleTv.setText(title);
        }

        if (!showLeft) {
            mLeftTv.setVisibility(View.GONE);
        }

        if (right != 0) {
            mRightIv.setImageResource(right);
            mRightIv.setVisibility(View.VISIBLE);

            if (hasBackgroundForRigthView) {
                mRightIv.setBackgroundResource(R.drawable.bg_rectangle_btn_coner);
            }
        }
    }

    /**
     * 显示标题为ImageView
     */
    void showTietleImageView() {
        mMiddleTv.setVisibility(View.GONE);
        mTitleIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftTv:
                finish();
                break;
            case R.id.title_rightTv:
                onRightClick(mRightTv);
                break;
            case R.id.title_rightIv:
                onRightClick(mRightIv);
                break;
        }
    }

    private void initListener() {
        mLeftTv.setOnClickListener(this);
        mRightTv.setOnClickListener(this);
        mRightIv.setOnClickListener(this);
    }

    /**
     * 右边按钮的点击事件
     *
     * @param rightView
     */
    protected void onRightClick(View rightView) {
    }

    ;

}

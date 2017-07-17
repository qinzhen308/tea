package com.becdoor.teanotes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PickerView extends View {
    public static final float MARGIN_ALPHA = 2.8F;
    public static final float SPEED = 2.0F;
    public static final String TAG = "PickerView";
    private boolean isInit = false;
    private int mColorText = 3355443;
    private int mCurrentSelected;
    private List<String> mDataList;
    private float mLastDownY;
    private float mMaxTextAlpha = 255.0F;
    private float mMaxTextSize = 60.0F;
    private float mMinTextAlpha = 120.0F;
    private float mMinTextSize = 30.0F;
    private float mMoveLen = 0.0F;
    private Paint mPaint;
    private onSelectListener mSelectListener;
    private MyTimerTask mTask;
    private int mViewHeight;
    private int mViewWidth;
    private Timer timer;
    Handler updateHandler = new Handler() {
        public void handleMessage(Message paramAnonymousMessage) {
            if (Math.abs(mMoveLen) < SPEED) {
                PickerView.this.mMoveLen = 0.0F;
                if (PickerView.this.mTask != null) {
                    PickerView.this.mTask.cancel();
                    PickerView.this.mTask = null;
                    PickerView.this.performSelect();
                }
            }else {
                mMoveLen -= mMoveLen / Math.abs(mMoveLen) * 2.0F;
                invalidate();
            }

        }
    };

    public PickerView(Context paramContext) {
        super(paramContext);
        init();
    }

    public PickerView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void doDown(MotionEvent paramMotionEvent) {
        if (this.mTask != null) {
            this.mTask.cancel();
            this.mTask = null;
        }
        this.mLastDownY = paramMotionEvent.getY();
    }

    private void doMove(MotionEvent paramMotionEvent) {
        this.mMoveLen += paramMotionEvent.getY() - this.mLastDownY;
        if (this.mMoveLen > this.mMinTextSize * 2.8F / 2.0F) {
            moveTailToHead();
            this.mMoveLen -= this.mMinTextSize * 2.8F;
        }else if(this.mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2.0F){
            moveHeadToTail();
            this.mMoveLen += this.mMinTextSize * 2.8F;
        }
        this.mLastDownY = paramMotionEvent.getY();
        invalidate();
    }

    private void doUp(MotionEvent paramMotionEvent) {
        if (Math.abs(this.mMoveLen) < 0.0001D) {
            this.mMoveLen = 0.0F;
            return;
        }
        if (this.mTask != null) {
            this.mTask.cancel();
            this.mTask = null;
        }
        this.mTask = new MyTimerTask(this.updateHandler);
        this.timer.schedule(this.mTask, 0L, 10L);
    }

    private void drawData(Canvas paramCanvas) {
        float f1 = parabola(this.mViewHeight / 4.0F, this.mMoveLen);
        float f2 = this.mMaxTextSize;
        float f3 = this.mMinTextSize;
        float f4 = this.mMinTextSize;
        this.mPaint.setTextSize((f2 - f3) * f1 + f4);
        this.mPaint.setAlpha((int) ((this.mMaxTextAlpha - this.mMinTextAlpha) * f1 + this.mMinTextAlpha));
        f1 = (float) (this.mViewWidth / 2.0D);
        f2 = (float) (this.mViewHeight / 2.0D + this.mMoveLen);
        Paint.FontMetricsInt localFontMetricsInt = this.mPaint.getFontMetricsInt();
        f2 = (float) (f2 - (localFontMetricsInt.bottom / 2.0D + localFontMetricsInt.top / 2.0D));
        paramCanvas.drawText((String) this.mDataList.get(this.mCurrentSelected), f1, f2, this.mPaint);
        for(int i = 1; mCurrentSelected - i >= 0;i++){
            drawOtherText(paramCanvas, i, -1);
        }
        for (int i=1;mCurrentSelected + i < this.mDataList.size();i++) {
            drawOtherText(paramCanvas, i, 1);
        }
    }

    private void drawOtherText(Canvas paramCanvas, int paramInt1, int paramInt2) {
        float f1 = 2.8F * this.mMinTextSize * paramInt1 + paramInt2 * this.mMoveLen;
        float f2 = parabola(this.mViewHeight / 4.0F, f1);
        float f3 = this.mMaxTextSize;
        float f4 = this.mMinTextSize;
        float f5 = this.mMinTextSize;
        this.mPaint.setTextSize((f3 - f4) * f2 + f5);
        this.mPaint.setAlpha((int) ((this.mMaxTextAlpha - this.mMinTextAlpha) * f2 + this.mMinTextAlpha));
        f1 = (float) (this.mViewHeight / 2.0D + paramInt2 * f1);
        Paint.FontMetricsInt localFontMetricsInt = this.mPaint.getFontMetricsInt();
        f1 = (float) (f1 - (localFontMetricsInt.bottom / 2.0D + localFontMetricsInt.top / 2.0D));
        paramCanvas.drawText((String) this.mDataList.get(this.mCurrentSelected + paramInt2 * paramInt1), (float) (this.mViewWidth / 2.0D), f1, this.mPaint);
    }

    private void init() {
        this.timer = new Timer();
        this.mDataList = new ArrayList();
        this.mPaint = new Paint(1);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mPaint.setColor(this.mColorText);
    }

    private void moveHeadToTail() {
        String str = (String) this.mDataList.get(0);
        this.mDataList.remove(0);
        this.mDataList.add(str);
    }

    private void moveTailToHead() {
        String str = (String) this.mDataList.get(this.mDataList.size() - 1);
        this.mDataList.remove(this.mDataList.size() - 1);
        this.mDataList.add(0, str);
    }

    private float parabola(float paramFloat1, float paramFloat2) {
        paramFloat2 = (float) (1.0D - Math.pow(paramFloat2 / paramFloat1, 2.0D));
        paramFloat1 = paramFloat2;
        if (paramFloat2 < 0.0F)
            paramFloat1 = 0.0F;
        return paramFloat1;
    }

    private void performSelect() {
        if (this.mSelectListener != null)
            this.mSelectListener.onSelect((String) this.mDataList.get(this.mCurrentSelected));
    }

    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        if (this.isInit)
            drawData(paramCanvas);
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, paramInt2);
        this.mViewHeight = getMeasuredHeight();
        this.mViewWidth = getMeasuredWidth();
        this.mMaxTextSize = (this.mViewHeight / 4.0F);
        this.mMinTextSize = (this.mMaxTextSize / 2.0F);
        this.isInit = true;
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        switch (paramMotionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(paramMotionEvent);
            case MotionEvent.ACTION_MOVE:
                doMove(paramMotionEvent);
            case MotionEvent.ACTION_UP:
                doUp(paramMotionEvent);
        }
        return true;
    }

    public void setData(List<String> paramList) {
        mDataList = paramList;
        mCurrentSelected = paramList.size() / 2;
        invalidate();
    }

    public void setOnSelectListener(onSelectListener paramonSelectListener) {
        this.mSelectListener = paramonSelectListener;
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler arg2) {
            this.handler = arg2;
        }

        public void run() {
            this.handler.sendMessage(this.handler.obtainMessage());
        }
    }

    public static abstract interface onSelectListener {
        public abstract void onSelect(String selected);
    }
}
package com.becdoor.teanotes.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.becdoor.teanotes.R;

import java.util.Timer;
import java.util.TimerTask;


public class CountDown extends LinearLayout {
    private static String TAG = "TimerDemo";
    private static final int UPDATE_TIME = 0;
    private static int delay = 1000;
    private static int period = 1000;
    Context context;
    private boolean isPause = false;
    private boolean isStop = true;
    ImageView[] ivTimes;
    private Handler mHandler = null;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    long second;
    long secondUntilFinished;
    int t1 = 0;
    int t2 = 0;
    int t3 = 0;
    int t4 = 0;
    UntilFinished untilFinished;

    public CountDown(Context paramContext) {
        super(paramContext);
        init(paramContext);
    }

    public CountDown(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init(paramContext);
    }

    @SuppressLint({"NewApi"})
    public CountDown(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init(paramContext);
    }

    private int imageId(int paramInt) {
        switch (paramInt) {
            case 0:
                return R.drawable.num_00;
            case 1:
                return R.drawable.num_01;
            case 2:
                return R.drawable.num_02;
            case 3:
                return R.drawable.num_03;
            case 4:
                return R.drawable.num_04;
            case 5:
                return R.drawable.num_05;
            case 6:
                return R.drawable.num_06;
            case 7:
                return R.drawable.num_07;
            case 8:
                return R.drawable.num_08;
            case 9:
                return R.drawable.num_09;
            default:
                return 0;
        }
    }

    private void init(Context context) {
        this.context = context;
        this.ivTimes = new ImageView[5];
        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_time, null);
        this.ivTimes[0] = ((ImageView) view.findViewById(R.id.iv_1));
        this.ivTimes[1] = ((ImageView) view.findViewById(R.id.iv_2));
        this.ivTimes[2] = ((ImageView) view.findViewById(R.id.iv_3));
        this.ivTimes[3] = ((ImageView) view.findViewById(R.id.iv_4));
        this.ivTimes[4] = ((ImageView) view.findViewById(R.id.iv_5));
        addView(view);
        this.mHandler = new Handler() {
            public void handleMessage(Message paramAnonymousMessage) {
                switch (paramAnonymousMessage.what) {
                    default:
                        return;
                    case 0:
                }
                CountDown.this.timeArrs(CountDown.this.secondUntilFinished);
            }
        };
    }

    private void stopTimer() {
        this.isStop = true;
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        if (this.mTimerTask != null) {
            this.mTimerTask.cancel();
            this.mTimerTask = null;
        }
        this.secondUntilFinished = this.second;
    }

    @SuppressLint({"NewApi"})
    private void timeArrs(long paramLong) {
        if (paramLong <= 0L) {
            stopTimer();
            if(untilFinished!=null)untilFinished.finishedCallback();
        }
        int i = (int) (paramLong / 60L);
        int j = (int) (paramLong % 60L);
        if (this.t1 != i / 10) {
            this.t1 = (i / 10);
            this.ivTimes[0].setBackgroundDrawable(this.context.getResources().getDrawable(imageId(this.t1)));
        }
        if (this.t2 != i % 10) {
            this.t2 = (i % 10);
            this.ivTimes[1].setBackgroundDrawable(this.context.getResources().getDrawable(imageId(this.t2)));
        }
        if (this.t3 != j / 10) {
            this.t3 = (j / 10);
            this.ivTimes[3].setBackgroundDrawable(this.context.getResources().getDrawable(imageId(this.t3)));
        }
        if (this.t4 != j % 10) {
            this.t4 = (j % 10);
            this.ivTimes[4].setBackgroundDrawable(this.context.getResources().getDrawable(imageId(this.t4)));
        }
    }

    public void continueTime() {
        this.isPause = false;
    }

    public void pause() {
        this.isPause = true;
    }

    public void sendMessage(int paramInt) {
        if (this.mHandler != null) {
            Message localMessage = Message.obtain(this.mHandler, paramInt);
            this.mHandler.sendMessage(localMessage);
        }
    }

    public void setSecondUntilFinished(long paramLong) {
        this.secondUntilFinished = paramLong;
        this.second = paramLong;
        timeArrs(this.secondUntilFinished);
    }

    public void setUntilFinishedCallBack(UntilFinished paramUntilFinished) {
        this.untilFinished = paramUntilFinished;
    }

    public void start() {
        if (this.isStop)
            startTimer();
        this.isStop = false;
    }

    public void startTimer() {
        if (this.mTimer == null)
            this.mTimer = new Timer();
        if (this.mTimerTask == null)
            this.mTimerTask = new TimerTask() {
                public void run() {
                    CountDown.this.sendMessage(0);
                    try {
                        do
                            Thread.sleep(1000L);
                        while (CountDown.this.isPause);
                        CountDown localCountdown = CountDown.this;
                        localCountdown.secondUntilFinished -= 1L;
                        return;
                    } catch (InterruptedException localInterruptedException) {
                    }
                }
            };
        if ((this.mTimer != null) && (this.mTimerTask != null))
            this.mTimer.schedule(this.mTimerTask, delay, period);
    }


    public void stop(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
    }

    public static abstract interface UntilFinished {
        public abstract void finishedCallback();
    }
}
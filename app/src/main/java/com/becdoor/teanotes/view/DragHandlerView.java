package com.becdoor.teanotes.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

/**
 * Created by Administrator on 2016/11/1.
 */

public class DragHandlerView extends LinearLayout{
    DragSortListView mDSLV;
    DragSortController mDSC;


    public DragHandlerView(Context context) {
        super(context);
        init();
    }

    public DragHandlerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragHandlerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    private void getRoot(){
        if(getParent()==null)return;
        ViewParent v=getParent().getParent();
        if(mDSC!=null)return;
        if(v instanceof DragSortListView){
            Log.d("drag","parent--get");
            mDSLV=(DragSortListView)v;
        }
    }


    public void checkLongPress(){
        if(checkForLongPress==null){
            checkForLongPress=new CheckForLongPress();
        }
        postDelayed(checkForLongPress, ViewConfiguration.getLongPressTimeout());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(longPress){
            return false;
        }
        boolean flags=super.onTouchEvent(event);
        Log.d("drag","flags="+flags);
        return flags;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getRoot();
                downX=ev.getX();
                downY=ev.getY();
                mDSC.onDown(ev);
                checkLongPress();
                return false;
            case MotionEvent.ACTION_MOVE:
                float dx=ev.getX()-downX;
                float dy=ev.getY()-downY;
                if(dx*dx+dy*dy>225&&!longPress){
                    removeCallbacks(checkForLongPress);
                }
                return longPress;
            case MotionEvent.ACTION_UP:
                if(!longPress){
                    removeCallbacks(checkForLongPress);
                }
                dx=ev.getX()-downX;
                dy=ev.getY()-downY;
                longPress=false;
                break;
        }
        return false;
    }

    float downX,downY;
    boolean longPress;
    CheckForLongPress checkForLongPress;



    private final class CheckForLongPress implements Runnable {
        @Override
        public void run() {
            longPress = true;
        }
    }


}

package com.becdoor.teanotes.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.becdoor.teanotes.R;

/**
 * Created by Administrator on 2016/9/19.
 */
public class PageFragment extends Fragment {
    public static final  String ARGS_PAGE="args_page";
    private  int mPage;
    public  static  PageFragment newInstance(int page){
        Bundle args=new Bundle();
        args.putInt(ARGS_PAGE,page);
        PageFragment fragment=new PageFragment();
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage=getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.item_viewpager,container,false);
        TextView textView= (TextView) view.findViewById(R.id.main_tablayout_textview);
        textView.setText("第"+mPage+"页");
        return view;
    }
}

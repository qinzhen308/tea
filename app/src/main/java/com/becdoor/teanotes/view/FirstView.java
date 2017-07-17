package com.becdoor.teanotes.view;


import com.becdoor.teanotes.model.FeauturedBean;

import java.util.List;


/**
 * Created by Administrator on 2016/10/20.
 */

public interface FirstView {
    void showProgress();
    void hideProgress();
    void addData(List<FeauturedBean>mlist);
    void showLoadFail();
}

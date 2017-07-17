package com.becdoor.teanotes.until;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Administrator on 2016/10/10.
 */

public class ConbertUtil {
    /**
     * dp 转px
     *
     */
    public static float dp2px(Context ct, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ct.getResources().getDisplayMetrics());
    }
}

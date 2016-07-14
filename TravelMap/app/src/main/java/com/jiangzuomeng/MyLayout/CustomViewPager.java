package com.jiangzuomeng.MyLayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by wilbert on 2015/11/6.
 */
public class CustomViewPager  extends ViewPager {
    public CustomViewPager(Context context) {
        super(context);
    }
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof HorizontalScrollView) {
            return true;
        }
//        return super.canScroll(v, checkV, dx, x, y);
        return true;
    }
}

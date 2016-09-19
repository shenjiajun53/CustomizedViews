package com.shenjiajun.customizeviewdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by shenjiajun on 2016/9/19.
 */
public class MyListLayout extends ViewGroup {
    public MyListLayout(Context context) {
        super(context);
        initView();
    }

    public MyListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}

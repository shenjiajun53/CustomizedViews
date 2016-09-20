package com.shenjiajun.customizeviewdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int maxWidth = 0;
        int maxHeight = 0;

        int childCount = getChildCount();
//        measureChildren(widthMeasureSpec, heightMeasureSpec);

        View child1 = getChildAt(0);

//        final LayoutParams marginLayoutParams = child1.getLayoutParams();
//
//        final MarginLayoutParams marginLayoutParams1 = (MarginLayoutParams) marginLayoutParams;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            measureChild(childView, widthMeasureSpec, heightMeasureSpec);


            if (maxWidth < childView.getMeasuredWidth()) {
                maxWidth = childView.getMeasuredWidth();
            }
            maxHeight += childView.getMeasuredHeight();

        }

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, heightSize);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, maxHeight);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(maxWidth, heightSize);
        } else {
            setMeasuredDimension(maxWidth, maxHeight);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int maxWidth = 0;
        int maxHeight = 0;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

//        int paddingH = paddingLeft + paddingRight;
//        int paddingV = paddingTop + paddingBottom;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

//            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();


//            if (maxWidth < childView.getMeasuredWidth()) {
//                maxWidth = childView.getMeasuredWidth();
//            }

            childView.layout(0 + paddingLeft, 0 + maxHeight + paddingTop
                    , childView.getMeasuredWidth() + paddingLeft, childView.getMeasuredHeight() + maxHeight + paddingTop);

            maxHeight += childView.getMeasuredHeight();
        }
    }
}

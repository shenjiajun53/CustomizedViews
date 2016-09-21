package com.shenjiajun.customizeviewdemo.views;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by shenjiajun on 2016/9/21.
 */

public class DragDownLayout extends RelativeLayout {

    private String TAG = "DragDownLayout";
    private View displayView;
    private ViewGroup dragView;
    private View dragContentView;
    private View dragAnchorView;

    private ViewDragHelper dragHelper;


    private static final int MIN_DRAWER_MARGIN = 64; // dp
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /**
     * drawer离父容器右边的最小外边距
     */
    private int mMinDrawerMargin;

    private int dragStartPos = 0;
    private int dragEndPos = 0;

    private int dragHeight = 0;
    private int dragContentHeight = 0;
    private int dragAnchorHeight = 0;

    private boolean isDroped = false;

    private boolean isInited = false;

    public DragDownLayout(Context context) {
        super(context);
        initView();
    }

    public DragDownLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragDownLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public boolean isDroped() {
        return isDroped;
    }

    public void setDroped(boolean droped) {
        isDroped = droped;
    }

    private void initView() {
        Log.e(TAG, "view Inited");
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
//        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);

        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                Log.e(TAG, "tryCaptureView pointerId=" + pointerId);
                return (child == dragView);
            }

            @Override
            public void onViewDragStateChanged(int state) {
                Log.e(TAG, "onViewDragStateChanged state=" + state);
                super.onViewDragStateChanged(state);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.e(TAG, "onViewPositionChanged left=" + left + " top=" + top + " dx=" + dx + " dy=" + dy);
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                Log.e(TAG, "onViewCaptured activePointerId=" + activePointerId);
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                int dy = dragStartPos - releasedChild.getTop();
                Log.e(TAG, "onViewReleased dy=" + dy + " contentHeight=" + dragContentHeight);
                float offset = (dragContentHeight - dy) * 1.0f / dragContentHeight;
                Log.e(TAG, "onViewReleased xvel=" + xvel + " yvel=" + yvel);
                Log.e(TAG, "onViewReleased offset=" + offset);

                isDroped = yvel > 0 || (yvel == 0 && offset > 0.5f) ? true : false;

                int finalPosY = isDroped ? dragStartPos : (dragStartPos - dragContentHeight);

                dragHelper.settleCapturedViewAt(0, finalPosY);
                invalidate();
            }


            @Override
            public int getViewHorizontalDragRange(View child) {
                Log.e(TAG, "getViewHorizontalDragRange");
                return super.getViewHorizontalDragRange(child);
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                Log.e(TAG, "getViewVerticalDragRange");
                return child == dragView ? dragHeight : 0;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.e(TAG, "clampViewPositionHorizontal left=" + left);
                return super.clampViewPositionHorizontal(child, left, dx);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.e(TAG, "clampViewPositionVertical top=" + top);
                if (top < (-dragContentHeight + dragStartPos)) {
                    top = (-dragContentHeight + dragStartPos);
                }
                if (top > dragStartPos) {
                    top = dragStartPos;
                }
                return top;
            }
        });
        dragHelper.setMinVelocity(minVel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.e(TAG, "onMeasure");
        displayView = getChildAt(1);
        dragView = (ViewGroup) getChildAt(0);
        dragContentView = dragView.getChildAt(0);
        dragAnchorView = dragView.getChildAt(1);

        dragHeight = dragView.getMeasuredHeight();
        dragContentHeight = dragContentView.getMeasuredHeight();
        dragAnchorHeight = dragAnchorView.getMeasuredHeight();

        int startHeight = displayView.getMeasuredHeight();
        int endHeight = startHeight + dragView.getMeasuredHeight();

        dragStartPos = startHeight;
        dragEndPos = endHeight;

//        if (!isDroped && !isInited) {
//            isInited = true;
////            dragView.scrollBy(0, dragContentHeight);
////            dragView.setY((- dragContentHeight));
//            dragHelper.smoothSlideViewTo(dragView, 0, (dragStartPos - dragContentHeight));
//            invalidate();
//        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        Log.e(TAG, "onLayout");

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

//        int paddingH = paddingLeft + paddingRight;
//        int paddingV = paddingTop + paddingBottom;

        MarginLayoutParams lp = (MarginLayoutParams) dragView.getLayoutParams();

        if (!isDroped) {
            Log.e(TAG, "onLayout 2222");
            dragView.layout(paddingLeft + lp.leftMargin,
                    (dragStartPos - dragContentHeight) + paddingTop + lp.topMargin,
                    dragView.getMeasuredWidth() + paddingLeft + lp.leftMargin,
                    dragView.getMeasuredHeight() + (dragStartPos - dragContentHeight) + paddingTop + lp.topMargin);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void dropDown() {
        Log.e(TAG, "dropDown");
        isDroped = true;
        dragHelper.smoothSlideViewTo(dragView, 0, dragStartPos);
        invalidate();
    }

    public void slideUp() {
        Log.e(TAG, "slideUp");
        isDroped = false;
        dragHelper.smoothSlideViewTo(dragView, 0, (dragStartPos - dragContentHeight));
        invalidate();
    }
}

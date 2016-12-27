package com.shenjiajun.customizeviewdemo.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.shenjiajun.customizeviewdemo.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shenjj on 2016/12/26.
 */

public class MarqueeTextView extends TextView {
    private final String TAG = "MarqueeTextView";
    private ArrayList<String> contentList = new ArrayList<>();
    private boolean isVerticalSwitch = true;
    private boolean isHorizontalScroll = true;
    private int verticalSwitchSpeed = 500;
    private int verticalSwitchInterval = 2000;
    private int horizontalScrollSpeed = 1000;
    private int horizontalScrollInterval = 4000;

    private int viewHeight;
    private int viewWidth;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private int contentColor = Color.WHITE; //内容的颜色
    private int contentTextSize = 100; //内容文字大小

    private boolean hasInited = false;
    private int currentY = 0; //文字的Y坐标
    private int currnetX = 0; //文字的Y坐标
    private int xOffset = 0;//文字和view宽度差
    private int yStartPos = 0;//文字初始位置
    private int currnetIndex = 0;
    private Paint contentPaint;
    private int contentWidth;
    private int contentHeight;
    private int maxContentWidth = 0;
    private int maxContentHeight = 0;
    private boolean isHorizontalRunning = false;
    private boolean isVerticalRunning = false;
    private boolean isTextAtMiddle = true;
//    private int textPos = 0; //0,中间  1，上方  -1，下方

    public void setContentList(ArrayList<String> contentList) {
        this.contentList = contentList;
        postInvalidate();
    }

    public void setVerticalSwitch(boolean verticalSwitch) {
        isVerticalSwitch = verticalSwitch;
        postInvalidate();
    }

    public void setHorizontalScroll(boolean horizontalScroll) {
        isHorizontalScroll = horizontalScroll;
        postInvalidate();
    }

    public void setVerticalSwitchSpeed(int verticalSwitchSpeed) {
        this.verticalSwitchSpeed = verticalSwitchSpeed;
        postInvalidate();
    }

    public void setVerticalSwitchInterval(int verticalSwitchInterval) {
        this.verticalSwitchInterval = verticalSwitchInterval;
        postInvalidate();
    }

    public void setHorizontalScrollSpeed(int horizontalScrollSpeed) {
        this.horizontalScrollSpeed = horizontalScrollSpeed;
        postInvalidate();
    }

    public void setHorizontalScrollInterval(int horizontalScrollInterval) {
        this.horizontalScrollInterval = horizontalScrollInterval;
        postInvalidate();
    }

    public MarqueeTextView(Context context) {
        super(context);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }


    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
    }


    public void setContentTextSize(int contentTextSize) {
        this.contentTextSize = contentTextSize;
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeTextView);
        verticalSwitchSpeed = array.getInt(R.styleable.MarqueeTextView_vertical_switch_speed, 500);
        verticalSwitchInterval = array.getInt(R.styleable.MarqueeTextView_vertical_switch_interval, 2000);
        horizontalScrollSpeed = array.getInt(R.styleable.MarqueeTextView_horizontal_scroll_speed, 1000);
        horizontalScrollInterval = array.getInt(R.styleable.MarqueeTextView_horizontal_scroll_interval, 4000);
        contentColor = array.getColor(R.styleable.MarqueeTextView_content_text_color, Color.BLACK);
        contentTextSize = (int) array.getDimension(R.styleable.MarqueeTextView_content_text_size, Sp2Px(getContext(), 15));

//        Logger.e("contentColor=" + contentColor);
//        Logger.e("contentTextSize=" + contentTextSize);
        array.recycle();
    }

    private void init() {


        contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        contentPaint.setDither(true);
        contentPaint.setTextSize(contentTextSize);
        contentPaint.setColor(contentColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Logger.d("onMeasure");
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();


        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        if (null != contentList && contentList.size() > 0) {
//            Logger.d("contentList=" + contentList);
            for (int i = 0; i < contentList.size(); i++) {
                String contentString = contentList.get(i);
                Rect contentBound = new Rect();
                contentPaint.getTextBounds(contentString, 0, contentString.length(), contentBound);
                int tempWidth = contentBound.width();
                int tempHeight = contentBound.height();
                maxContentHeight = Math.max(maxContentHeight, tempHeight);
                maxContentWidth = Math.max(maxContentWidth, tempWidth);
            }
        }

//        Logger.d("onMeasure viewWidth=" + viewWidth + " viewHeight=" + viewHeight);
//        Logger.d("onMeasure widthSize=" + widthSize + " heightSize=" + heightSize);
//        Logger.d("onMeasure maxWidth=" + maxWidth + " maxHeight=" + maxHeight);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, heightSize);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, maxContentHeight);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(maxContentWidth, heightSize);
        } else {
            setMeasuredDimension(maxContentWidth, maxContentHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        Logger.d("onLayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Logger.d("onSizeChanged");
//        Logger.d("onSizeChanged viewWidth=" + viewWidth + " viewHeight=" + viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != contentList && contentList.size() > 0) {
            if (currnetIndex >= contentList.size()) {
                currnetIndex = 0;                        //播放完，循环
//                isVerticalRunning = false;
            }
//            Logger.d("onDraw viewWidth=" + viewWidth + " viewHeight=" + viewHeight);
//            Logger.d("onDraw" + contentList);
            String contentString = contentList.get(currnetIndex);

            Rect contentBound = new Rect();
            contentPaint.getTextBounds(contentString, 0, contentString.length(), contentBound);
//            Logger.d("contentBound top=" + contentBound.top + " bottom=" + contentBound.bottom + " \n  left=" + contentBound.left + " right=" + contentBound.right);
//            Logger.d("contentBound height=" + contentBound.height() + " width=" + contentBound.width());

            contentWidth = contentBound.width();
            xOffset = (int) ((contentWidth - viewWidth) * 1.2);                 //文字超出View的部分。需要水平播放，另外加点留白


            if (!hasInited) {
                hasInited = true;
                currentY = viewHeight / 2 + maxContentHeight / 2;
                isTextAtMiddle = true;
            }

            if (isTextAtMiddle) {
                yStartPos = viewHeight / 2 + maxContentHeight / 2;              //文字初始位置在view中间
            } else {
                yStartPos = viewHeight + viewHeight + maxContentHeight / 2;  //文字位置在View下方
            }

//            Logger.d("contentHeight=" + contentHeight);
//            Logger.d("xOffset=" + xOffset);
//            Logger.d("yStartPos=" + yStartPos);
//            Logger.d("currentX=" + currnetX);
//            Logger.d("currentY=" + currentY);


            if (!isVerticalRunning) {                        //垂直滚动
//                Logger.d("start run");
                isVerticalRunning = true;
                if (isTextAtMiddle) {              //如果在中间，暂停一段时间，否则直接垂直滚动
                    startVerticalInterval();
                    if ((xOffset > 0) && !isHorizontalRunning) {
                        isHorizontalRunning = true;
                        startHorizontalScroll();
                    }
                } else {
                    currnetX = 0;
                    startVerticalSwitch();
                }
            }
            canvas.drawText(contentString, currnetX, currentY, contentPaint);
        }
    }

    /*
    * */
    private void startVerticalInterval() {
        ValueAnimator verticalIntervalAnimator = ValueAnimator.ofFloat(0, 1);
        verticalIntervalAnimator.setDuration(verticalSwitchInterval);
        verticalIntervalAnimator.setInterpolator(new LinearInterpolator());
        verticalIntervalAnimator.start();
        verticalIntervalAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value >= 1) {
                    startVerticalSwitch();
                }
            }
        });
    }

    /*
    * */
    private void startVerticalSwitch() {
        ValueAnimator verticalSwitchAnimator = ValueAnimator.ofFloat(0, 1);
        verticalSwitchAnimator.setDuration(verticalSwitchSpeed);
        verticalSwitchAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        verticalSwitchAnimator.start();
        verticalSwitchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value < 1) {
                    currentY = (int) (yStartPos - value * viewHeight * 1.5);
                } else {
                    if (isTextAtMiddle) {
                        isTextAtMiddle = false;
                        currnetIndex++;
                    } else {
                        isTextAtMiddle = true;
                    }
//                    Logger.e("y transition finished");
                    isVerticalRunning = false;
                }
                postInvalidate();
            }
        });
    }

    private void startHorizontalScroll() {
        ValueAnimator horizontalScrollAnimator = ValueAnimator.ofFloat(0, 1);
        horizontalScrollAnimator.setDuration(horizontalScrollSpeed);
        horizontalScrollAnimator.setInterpolator(new LinearInterpolator());
        horizontalScrollAnimator.start();
        horizontalScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value < 1) {
                    currnetX = (int) (-xOffset * value);
                } else {
                    isHorizontalRunning = false;
                }
                postInvalidate();
            }
        });
    }

    //dpi转px
    public static int Dp2Px(Context context, int dpi) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, context.getResources().getDisplayMetrics());
    }

    //px转dp
    public static int Px2Dp(Context context, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
    }

    //sp转px
    public static int Sp2Px(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    //px转sp
    public static int Px2Sp(Context context, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
    }
}

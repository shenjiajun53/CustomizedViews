package com.shenjiajun.customizeviewdemo.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
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
    private String singleText = "";
    private boolean isVerticalSwitch = true;
    private boolean isHorizontalScroll = true;

    private int DEFAULT_VERTICAL_SPEED = 500;
    private int DEFAULT_VERTICAL_INTERVAL = 1000;
    private int DEFAULT_HORIZONTAL_SPEED = 100;     //滚动每个字的时间
    private int DEFAULT_HORIZONTAL_INTERVAL = 4000;
    private int DEFAULT_HORIZONTAL_LOOP_SPEED = 100;  //滚动每个字的时间

    private int verticalSwitchSpeed;
    private int verticalSwitchInterval;
    private int horizontalScrollSpeed;
    private int horizontalScrollInterval;
    private int horizontalLoopSpeed;

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
    private int xStartPos = 0;//文字初始位置
    private int currnetIndex = 0;
    private Paint contentPaint;
    private int contentWidth;
    private int contentHeight;
    private int maxContentWidth = 0;
    private int maxContentHeight = 0;
    private boolean isHorizontalRunning = false;
    private boolean isVerticalRunning = false;
    private boolean isTextAtMiddle = true;
    private boolean horizontalOriLeft = true;
//    private int textPos = 0; //0,中间  1，上方  -1，下方

    public void setContentList(ArrayList<String> contentList) {
        this.contentList = contentList;
        requestLayout();
        postInvalidate();
    }

    public void setSingleText(String singleText) {
        this.singleText = singleText;
        requestLayout();
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
        verticalSwitchSpeed = array.getInt(R.styleable.MarqueeTextView_vertical_switch_speed, DEFAULT_VERTICAL_SPEED);
        verticalSwitchInterval = array.getInt(R.styleable.MarqueeTextView_vertical_switch_interval, DEFAULT_VERTICAL_INTERVAL);
        horizontalScrollSpeed = array.getInt(R.styleable.MarqueeTextView_horizontal_scroll_speed, DEFAULT_HORIZONTAL_SPEED);
        horizontalScrollInterval = array.getInt(R.styleable.MarqueeTextView_horizontal_scroll_interval, DEFAULT_HORIZONTAL_INTERVAL);
        horizontalLoopSpeed = array.getInt(R.styleable.MarqueeTextView_horizontal_loop_speed, DEFAULT_HORIZONTAL_LOOP_SPEED);
        contentColor = array.getColor(R.styleable.MarqueeTextView_content_text_color, Color.BLACK);
        contentTextSize = (int) array.getDimension(R.styleable.MarqueeTextView_content_text_size, Sp2Px(getContext(), 15));
        singleText = (String) array.getString(R.styleable.MarqueeTextView_content_single_text);

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
        } else if (!TextUtils.isEmpty(singleText)) {
            Rect contentBound = new Rect();
            contentPaint.getTextBounds(singleText, 0, singleText.length(), contentBound);
            maxContentWidth = contentBound.width();
            maxContentHeight = contentBound.height();
        }

//        Logger.d("onMeasure viewWidth=" + viewWidth + " viewHeight=" + viewHeight);
//        Logger.d("onMeasure widthSize=" + widthSize + " heightSize=" + heightSize);
//        Logger.d("onMeasure maxWidth=" + maxContentWidth + " maxHeight=" + maxContentHeight);

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
        if (null != contentList && contentList.size() > 1) {
            if (currnetIndex >= contentList.size()) {
                currnetIndex = 0;                        //播放完，循环
            }
            viewHeight = getMeasuredHeight();
            viewWidth = getMeasuredWidth();

            String currentString = contentList.get(currnetIndex);
            int nextIndex = currnetIndex + 1;
            if (currnetIndex + 1 >= contentList.size()) {
                nextIndex = 0;
            }
            String nextString = contentList.get(nextIndex);

            Rect contentBound = new Rect();
            contentPaint.getTextBounds(currentString, 0, currentString.length(), contentBound);
            contentWidth = contentBound.width();
            xOffset = contentWidth - viewWidth;                 //文字超出View的部分。需要水平播放

            Paint.FontMetrics fontMetrics = contentPaint.getFontMetrics();
            int textHeight = (int) ((-fontMetrics.ascent - fontMetrics.descent) / 2);
            int textWholeHeight = (int) ((-fontMetrics.top - fontMetrics.bottom) / 2);
//            Logger.e("textHeight=" + textHeight + " textWholeHeight=" + textWholeHeight);

            yStartPos = viewHeight / 2 + maxContentHeight / 4 + textHeight / 4;
            if (!hasInited) {
                hasInited = true;
                currentY = yStartPos;

//                Logger.d("top=" + fontMetrics.top + "  bottom=" + fontMetrics.bottom +
//                        " \n ascent=" + fontMetrics.ascent + " descent=" + fontMetrics.descent +
//                        " \n leading=" + fontMetrics.leading);
            }

            if (xOffset > 0) {
                xOffset += contentTextSize * 2;   //另外加点留白.设留白两个字宽
                if (!isHorizontalRunning && !isVerticalRunning) {
                    isHorizontalRunning = true;
                    startHorizontalScroll();
                    currnetX = 0;
                }
            } else {
                if (!isVerticalRunning) {
                    isVerticalRunning = true;
                    startVerticalInterval();
                    currnetX = 0;
                }
            }

            canvas.drawText(currentString, currnetX, currentY, contentPaint);
            canvas.drawText(nextString, 0, currentY + viewHeight, contentPaint);
        } else if (!TextUtils.isEmpty(singleText)) {
            viewHeight = getMeasuredHeight();
            viewWidth = getMeasuredWidth();
            Rect contentBound = new Rect();
            contentPaint.getTextBounds(singleText, 0, singleText.length(), contentBound);
            contentWidth = contentBound.width();
            xOffset = contentWidth - viewWidth;
            Paint.FontMetrics fontMetrics = contentPaint.getFontMetrics();
            int textHeight = (int) ((-fontMetrics.ascent - fontMetrics.descent) / 2);
            int textWholeHeight = (int) ((-fontMetrics.top - fontMetrics.bottom) / 2);
//            Logger.e("textHeight=" + textHeight + " textWholeHeight=" + textWholeHeight);
            yStartPos = viewHeight / 2 + maxContentHeight / 4 + textHeight / 4;

            if (!hasInited) {
                hasInited = true;
                currnetX = 0;
                xStartPos = currnetX;
            }

            if (xOffset > 0) {
                xOffset += contentTextSize * 2;
                if (!isHorizontalRunning) {
                    isHorizontalRunning = true;
                    startHorizontalLoop();
                }
            }
            canvas.drawText(singleText, currnetX, yStartPos, contentPaint);
        }
    }

    /*
    * */
    private void startVerticalInterval() {
        ValueAnimator verticalIntervalAnimator = ValueAnimator.ofFloat(0, 1);
        verticalIntervalAnimator.setDuration(verticalSwitchInterval);
        verticalIntervalAnimator.setInterpolator(new LinearInterpolator());
        verticalIntervalAnimator.start();
        verticalIntervalAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startVerticalSwitch();
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
                currentY = (int) (yStartPos - value * viewHeight * 1);
                postInvalidate();
            }
        });
        verticalSwitchAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currnetIndex++;
                currentY = yStartPos;
                isVerticalRunning = false;
                postInvalidate();
            }
        });
    }

    private void startHorizontalScroll() {
        ValueAnimator horizontalScrollAnimator = ValueAnimator.ofFloat(0, 1);
        //在崩溃统计上看到值<0的bug
        if (horizontalScrollSpeed * xOffset / contentTextSize < 0) {
            isHorizontalRunning = false;
            return;
        }
        horizontalScrollAnimator.setDuration(horizontalScrollSpeed * xOffset / contentTextSize);
        horizontalScrollAnimator.setInterpolator(new LinearInterpolator());
        horizontalScrollAnimator.start();
        horizontalScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                currnetX = (int) (-xOffset * value);
                postInvalidate();
            }
        });
        horizontalScrollAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isHorizontalRunning = false;

                isVerticalRunning = true;
                startVerticalInterval();

                postInvalidate();
            }
        });
    }

    private void startHorizontalLoop() {
        ValueAnimator horizontalScrollAnimator;
        if (horizontalOriLeft) {
            horizontalScrollAnimator = ValueAnimator.ofFloat(0, 1);
        } else {
            horizontalScrollAnimator = ValueAnimator.ofFloat(0, -1);
        }
        if (horizontalScrollSpeed * xOffset / contentTextSize < 0) {
            isHorizontalRunning = false;
            return;
        }
        horizontalScrollAnimator.setDuration(horizontalLoopSpeed * xOffset / contentTextSize);
        horizontalScrollAnimator.setInterpolator(new LinearInterpolator());
        horizontalScrollAnimator.start();
        horizontalScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                currnetX = (int) (xStartPos - xOffset * value);
                postInvalidate();
            }
        });
        horizontalScrollAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isHorizontalRunning = false;
                horizontalOriLeft = !horizontalOriLeft;
                xStartPos = currnetX;
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

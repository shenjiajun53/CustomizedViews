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
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.orhanobut.logger.Logger;
import com.shenjiajun.customizeviewdemo.R;
import com.shenjiajun.customizeviewdemo.models.AwardModel;

import java.util.ArrayList;

/**
 * Created by shenjj on 2016/12/27.
 */

public class LuckyWheelView extends View {

    private ArrayList<AwardModel> awardsList = new ArrayList<>();

    private Paint itemTitleTextPaint;
    private Paint itemContentTextPaint;
    private Paint circleLinePaint;
    private Paint intervalLinePaint;
    private Paint unSelectPiecePaint;
    private Paint selectedPiecePaint;
    private Paint arrowPaint;

    private int viewWidth;
    private int viewHeight;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private int startAngle = 0;
    private int radius = 0;

    private boolean eachPieceEqual = true;

    private int lastAngle = 0;
    private int lastPos = 0;

    private int rotationSpeed = 300;
    private boolean isRotationing = false;

    private int itemTitleColor = Color.BLACK;
    private int itemContentColor = Color.BLUE;
    private int lineColor = Color.BLUE;
    private int pieceUnSelectColor = Color.YELLOW;
    private int pieceSelectedColor = Color.WHITE;

    private int itemTitleTextSize = 70;
    private int itemContentTextSize = 40;

    private int intervalStrokeWidth = 10;
    private int circleStrokeWidth = 20;

//    private int[] countList = {0, 0, 0, 0, 0, 0};

    public void setAwardsList(ArrayList<AwardModel> awardsList) {
        this.awardsList = awardsList;
    }

    public void setItemTitleColor(int itemTitleColor) {
        this.itemTitleColor = itemTitleColor;
    }

    public void setItemContentColor(int itemContentColor) {
        this.itemContentColor = itemContentColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setPieceUnSelectColor(int pieceUnSelectColor) {
        this.pieceUnSelectColor = pieceUnSelectColor;
    }

    public void setPieceSelectedColor(int pieceSelectedColor) {
        this.pieceSelectedColor = pieceSelectedColor;
    }

    public void setItemTitleTextSize(int itemTitleTextSize) {
        this.itemTitleTextSize = itemTitleTextSize;
    }

    public void setItemContentTextSize(int itemContentTextSize) {
        this.itemContentTextSize = itemContentTextSize;
    }

    public void setIntervalStrokeWidth(int intervalStrokeWidth) {
        this.intervalStrokeWidth = intervalStrokeWidth;
    }

    public void setCircleStrokeWidth(int circleStrokeWidth) {
        this.circleStrokeWidth = circleStrokeWidth;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public LuckyWheelView(Context context) {
        super(context);
        init();
    }

    public LuckyWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public LuckyWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LuckyWheelView);
        intervalStrokeWidth = (int) array.getDimension(R.styleable.LuckyWheelView_interval_line_width, Dp2Px(getContext(), 5));
        circleStrokeWidth = (int) array.getDimension(R.styleable.LuckyWheelView_circle_line_width, Dp2Px(getContext(), 10));
        itemTitleTextSize = (int) array.getDimension(R.styleable.LuckyWheelView_item_title_text_size, Sp2Px(getContext(), 15));
        itemContentTextSize = (int) array.getDimension(R.styleable.LuckyWheelView_item_content_text_size, Sp2Px(getContext(), 15));


        itemTitleColor = array.getColor(R.styleable.LuckyWheelView_item_title_color, Color.BLACK);
        itemContentColor = array.getColor(R.styleable.LuckyWheelView_item_content_color, Color.BLACK);
        lineColor = array.getColor(R.styleable.LuckyWheelView_line_color, Color.BLACK);
        pieceUnSelectColor = array.getColor(R.styleable.LuckyWheelView_un_select_color, Color.YELLOW);
        pieceSelectedColor = array.getColor(R.styleable.LuckyWheelView_selected_color, Color.WHITE);
        array.recycle();
    }

    private void init() {
        itemTitleTextPaint = new Paint();
        itemTitleTextPaint.setAntiAlias(true);
        itemTitleTextPaint.setDither(true);
        itemTitleTextPaint.setTextSize(itemTitleTextSize);
        itemTitleTextPaint.setColor(itemTitleColor);

        itemContentTextPaint = new Paint();
        itemContentTextPaint.setAntiAlias(true);
        itemContentTextPaint.setDither(true);
        itemContentTextPaint.setTextSize(itemContentTextSize);
        itemContentTextPaint.setColor(itemContentColor);


        unSelectPiecePaint = new Paint();
        unSelectPiecePaint.setAntiAlias(true);
        unSelectPiecePaint.setDither(true);
        unSelectPiecePaint.setColor(pieceUnSelectColor);

        selectedPiecePaint = new Paint();
        selectedPiecePaint.setAntiAlias(true);
        selectedPiecePaint.setDither(true);
        selectedPiecePaint.setColor(pieceSelectedColor);


        intervalLinePaint = new Paint();
        intervalLinePaint.setAntiAlias(true);
        intervalLinePaint.setDither(true);
        intervalLinePaint.setColor(lineColor);
        intervalLinePaint.setStrokeWidth(intervalStrokeWidth);
        intervalLinePaint.setStyle(Paint.Style.STROKE);

        circleLinePaint = new Paint();
        circleLinePaint.setAntiAlias(true);
        circleLinePaint.setDither(true);
        circleLinePaint.setColor(lineColor);
        circleLinePaint.setStrokeWidth(circleStrokeWidth);
        circleLinePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

//        viewWidth = viewWidth - paddingLeft - paddingRight;
//        viewHeight = viewHeight - paddingBottom - paddingTop;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

//        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
//            setMeasuredDimension(widthSize, heightSize);
//        } else if (widthMode == MeasureSpec.EXACTLY) {
//            setMeasuredDimension(widthSize, maxContentHeight);
//        } else if (heightMode == MeasureSpec.EXACTLY) {
//            setMeasuredDimension(maxContentWidth, heightSize);
//        } else {
//            setMeasuredDimension(maxContentWidth, maxContentHeight);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != awardsList && awardsList.size() > 0) {

            RectF rectF = new RectF(0, 0, viewWidth, viewHeight);
            float outRadius = Math.min(viewWidth / 2, viewHeight / 2) - circleStrokeWidth / 2;
            float intRadius = Math.min(viewWidth / 4, viewHeight / 4);
            float ringWidth = outRadius - intRadius;


            RectF outRect = new RectF(viewWidth / 2 - outRadius, viewHeight / 2 - outRadius + circleStrokeWidth / 4, outRadius + viewWidth / 2, outRadius + viewHeight / 2);
            RectF inRect = new RectF(0, 0, intRadius * 2, intRadius * 2);

            canvas.drawCircle(viewWidth / 2, viewHeight / 2, outRadius, circleLinePaint);
//            canvas.drawCircle(viewWidth / 2, viewHeight / 2, intRadius, intervalLinePaint);
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, outRadius - 2, unSelectPiecePaint);

            if (!isRotationing) {
                canvas.drawArc(outRect, 240, 60, true, selectedPiecePaint);
            }
//            canvas.drawRect(outRect, intervalLinePaint);

            int awardsSize = awardsList.size();
            int percentAngle = 360 / awardsSize;


            canvas.rotate(lastAngle, viewWidth / 2, viewHeight / 2);

            canvas.save();
            for (int i = 0; i < awardsSize; i++) {
                if (i != 0) {
                    canvas.rotate(percentAngle, viewWidth / 2, viewHeight / 2);
                }

                canvas.drawLine((viewWidth / 2 - outRadius), viewHeight / 2, (viewWidth / 2), viewHeight / 2, intervalLinePaint);

                String awardTitleStr = awardsList.get(i).getTitle();
                String awardContentStr = awardsList.get(i).getContent();
                float titleWidth = itemTitleTextPaint.measureText(awardTitleStr);
                float contentWidth = itemTitleTextPaint.measureText(awardContentStr);
                canvas.drawText(awardTitleStr, viewWidth / 2 - titleWidth / 2, viewHeight / 2 - outRadius + ringWidth / 2, itemTitleTextPaint);
                canvas.drawText(awardContentStr, viewWidth / 2 - contentWidth / 2, viewHeight / 2 - outRadius + ringWidth - 30, itemTitleTextPaint);
            }
            canvas.restore();
        }
    }

    //根据每项百分比来转
    public void startRotationByProbability() {
        for (int i = 0; i < awardsList.size(); i++) {
            double randomResult = Math.random();
            if (randomResult < awardsList.get(i).getPercent()) {
                startRotation(awardsList.get(i).getIndex());
                return;
            } else {
                continue;
            }
        }
        //循环结局还是没击中，再来一波
        startRotationByProbability();

    }

    //pos=-1,随机旋转  pos=x,旋转到x
    public void startRotation(int pos) {
        if (isRotationing) {
            return;
        }
//        Logger.e("startRotation pos=" + pos);
        if (pos < 0 || pos > awardsList.size()) {
            int newPos = (int) (Math.random() * 10);
            startRotation(newPos);
            return;
        }

        isRotationing = true;
        int lap = (int) (Math.random() * 12) + 4;
        int angle = 0;

        final int currentPos = pos;
//        Logger.e("lastPos=" + lastPos);
        if (currentPos > lastPos) {
            angle = ((awardsList.size() - currentPos) + lastPos) * 60;
        } else if (currentPos < lastPos) {
            angle = (lastPos - currentPos) * 60;
        } else {
            angle = 0;
        }


        int increaseDegree = lap * 360 + angle;
        long time = (lap + angle / 360) * rotationSpeed;
        int DesRotate = increaseDegree + lastAngle;

//        //TODO 为了每次都能旋转到转盘的中间位置
//        int offRotate = DesRotate % 360 % 60;
//        DesRotate -= offRotate;
//        DesRotate += 30;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(lastAngle, DesRotate);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                lastAngle = (value % 360 + 360) % 360;
//                LuckyWheelView.this.setRotation(lastAngle);
                postInvalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                Logger.e("lastAngle=" + lastAngle);
                isRotationing = false;
                lastPos = currentPos;
            }
        });
    }

    private int queryPosition() {
        lastAngle = (lastAngle % 360 + 360) % 360;
        int pos = lastAngle / 60;
        return calcumAngle(pos);
    }

    private int calcumAngle(int pos) {
        if (pos >= 0 && pos <= 3) {
            pos = 3 - pos;
        } else {
            pos = (6 - pos) + 3;
        }
//        Logger.e("pos=" + pos);
        return pos;
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

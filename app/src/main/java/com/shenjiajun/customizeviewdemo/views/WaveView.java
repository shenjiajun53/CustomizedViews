package com.shenjiajun.customizeviewdemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by shenjiajun on 2016/4/7.
 */
public class WaveView extends View {

    private Paint mPaint;
    private Path mPath;

    private float heightY;
    private float ctrlX, ctrlY;

    private float mPercent = 0;
    private float currentPercent = 0;
    private float mHeight, mWidth;
    private boolean toRight = true;

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public WaveView(Context context) {
        super(context);
        initPaint();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public void setmPercent(float mPercent) {
        this.mPercent = mPercent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    private void initPaint() {
        // 实例化画笔并设置参数
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0xFFA2D6AE);

        // 实例化路径对象
        mPath = new Path();

        ctrlX = (float) (-0.25 * mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ctrlY = mHeight * (1 - currentPercent) - 200;

        mPath.moveTo((float) (-0.25 * mWidth), mHeight * (1 - currentPercent));
        mPath.quadTo(ctrlX, ctrlY, (float) (1.25 * mWidth), mHeight * (1 - currentPercent));
        mPath.lineTo((float) (1.25 * mWidth), mHeight);
        mPath.lineTo((float) (-0.25 * mWidth), mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        if (ctrlX >= mWidth) {
            toRight = false;
        } else if (ctrlX <= 0) {
            toRight = true;
        }
        ctrlX = toRight ? ctrlX + 20 : ctrlX - 20;

        if (Math.abs(currentPercent - mPercent) > 0.02) {
            if (currentPercent < mPercent) {
                currentPercent += 0.01;
            } else if (currentPercent > mPercent) {
                currentPercent -= 0.01;
            }
        }
        mPath.reset();
        invalidate();

    }
}

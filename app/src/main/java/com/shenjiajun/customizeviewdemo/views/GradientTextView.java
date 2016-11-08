package com.shenjiajun.customizeviewdemo.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.shenjiajun.customizeviewdemo.R;

/**
 * Created by shenjj on 2016/11/7.
 */

public class GradientTextView extends TextView {
    private String TAG = "GradientTextView";

    private LinearGradient linearGradient;
    private Paint mPaint;
    private int viewWidth;
    private Matrix mMatrix;
    private Context mContext;
    private int bottomColor;
    private int topColor;
    private boolean forward = true;
    private int animationTime = 700;


    public GradientTextView(Context context) {
        super(context);
        init(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = getPaint();
        mMatrix = new Matrix();
        mContext = context;

        bottomColor = ContextCompat.getColor(mContext, R.color.colorPrimaryDark);
        topColor = ContextCompat.getColor(mContext, R.color.colorAccent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredWidth();
        Log.e(TAG, "onMeasure viewWidth=" + viewWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = getMeasuredWidth();
        Log.e(TAG, "onSizeChanged viewWidth=" + viewWidth);

        linearGradient = new LinearGradient(-viewWidth, 0, 0, 0, topColor, bottomColor, Shader.TileMode.CLAMP);

        if (mPaint.getShader() == null) {
            mPaint.setShader(linearGradient);
        }

    }

    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    public void setTopColorRes(int topColorId, int bottomColorId) {
        topColor = ContextCompat.getColor(mContext, topColorId);
        bottomColor = ContextCompat.getColor(mContext, bottomColorId);

        linearGradient = new LinearGradient(-viewWidth, 0, 0, 0, topColor, bottomColor, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);

        postInvalidate();
    }

    public void setInit() {
        mMatrix = new Matrix();

        mPaint.setShader(null);
        linearGradient = new LinearGradient(-viewWidth, 0, 0, 0, topColor, bottomColor, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        postInvalidate();
    }

    public void setDone() {
        mMatrix = new Matrix();
        mMatrix.setTranslate(2 * viewWidth, 0);

        mPaint.setShader(null);
        linearGradient = new LinearGradient(-viewWidth, 0, 0, 0, topColor, bottomColor, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        postInvalidate();
    }

    public void startAnimation() {
        ValueAnimator valueAnimator;
        if (forward) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            forward = false;
        } else {
            valueAnimator = ValueAnimator.ofFloat(1,0);
            forward = true;
        }
        valueAnimator.setDuration(animationTime);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mMatrix.setTranslate(2 * value * viewWidth, 0);
                postInvalidate();
                Log.e("GradientTextView", "translate value=" + 2 * value * viewWidth);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        linearGradient.setLocalMatrix(mMatrix);
//        // translate the shader local matrix
//        linearGradientMatrix.setTranslate(2 * gradientX, 0);
//
//        // this is required in order to invalidate the shader's position
//        linearGradient.setLocalMatrix(linearGradientMatrix);
    }
}

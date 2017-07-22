package com.shenjiajun.customizeviewdemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.shenjiajun.customizeviewdemo.R;
import com.shenjiajun.customizeviewdemo.utils.DemisionUtil;

/**
 * Created by Administrator on 2017/7/6.
 */

public class CouponView extends View {
    private static final int DEFAULT_BG_COLOR = Color.GRAY;
    private static final int DEFAULT_TEXT_SIZE = 100;
    private static final int DEFAULT_CONER_RADIUS = 3;

    private Paint bgPaint, textPaint, circlePaint;
    private RectF bgRect;
    private Rect titleRect;
    private int viewHeight;
    private int viewWidth;
    private int titleHeight;
    private int titleWidth;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private Bitmap circleBitmap;

    private int textSize;
    private int bgColor;
    private boolean checked = true;
    private String titleStr;
    private int cornerRadius;

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        postInvalidate();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public CouponView(Context context) {
        super(context);
        init();
    }

    public CouponView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
        init();
    }

    public CouponView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        init();
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CouponView);
        bgColor = array.getColor(R.styleable.CouponView_background_color, DEFAULT_BG_COLOR);
        textSize = (int) array.getDimension(R.styleable.CouponView_title_text_size, DemisionUtil.Sp2Px(getContext(), DEFAULT_TEXT_SIZE));
        titleStr = array.getString(R.styleable.CouponView_title_text);
        cornerRadius = (int) array.getDimension(R.styleable.CouponView_corner_radius, DemisionUtil.Dp2Px(getContext(), DEFAULT_CONER_RADIUS));
        array.recycle();
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.material_white));

        bgRect = new RectF();
        titleRect = new Rect();
        circleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat_checked);
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


        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        textPaint.getTextBounds(titleStr, 0, titleStr.length(), titleRect);
        titleWidth = titleRect.width();
        titleHeight = titleRect.height();

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, heightSize);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, titleHeight + paddingBottom + paddingTop);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(titleWidth + textSize * 2 + paddingRight * 2 + paddingLeft, heightSize);
        } else {
            setMeasuredDimension(titleWidth + textSize * 2 + paddingRight + paddingLeft, titleHeight + paddingBottom + paddingTop);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        bgRect.set(0, 0, viewWidth, viewHeight);

        textPaint.getTextBounds(titleStr, 0, titleStr.length(), titleRect);
        canvas.drawRoundRect(bgRect, cornerRadius, cornerRadius, bgPaint);

        if (!checked) {
            canvas.drawCircle(textSize + paddingRight, viewHeight / 2, textSize / 2, circlePaint);
        } else {
            circleBitmap = resizeImage(circleBitmap, textSize, textSize);
            canvas.drawBitmap(circleBitmap, textSize / 2 + paddingRight, viewHeight / 2 - textSize / 2, circlePaint);
        }

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int textHeight = (int) ((-fontMetrics.ascent - fontMetrics.descent) / 2);

        int yStartPos = viewHeight / 2 + titleHeight / 4 + textHeight / 4;
        canvas.drawText(titleStr, textSize * 2 + paddingRight, yStartPos, textPaint);
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }
}

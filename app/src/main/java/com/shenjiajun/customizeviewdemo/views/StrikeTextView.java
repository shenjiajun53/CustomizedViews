package com.shenjiajun.customizeviewdemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.shenjiajun.customizeviewdemo.R;

/**
 * Created by shenjj on 2017/5/2.
 */

public class StrikeTextView extends TextView {
    private Paint linePaint;

    private static final int DEFAULT_LINE_COLOR = Color.GRAY;
    private static final int DEFAULT_LINE_WIDTH = 3;

    private static final int DEFAULT_LEFT_PADDING = 10;
    private static final int DEFAULT_RIGHT_PADDING = 10;

    private int lineWidth = DEFAULT_LINE_WIDTH;
    private int lineColor = DEFAULT_LINE_COLOR;


    private static final int DEFAULT_TYPE = 0;

    private static final int TYPE_UNDER_LINE = 0;
    private static final int TYPE_MIDDLE_LINE = 1;
    private static final int TYPE_OBLIQUE_LINE = 2;


    private int leftPadding;
    private int rightPadding;

    private int lineType = 0;


    public StrikeTextView(Context context) {
        super(context);
        init();

    }

    public StrikeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public StrikeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.StrikeTextView);

        lineColor = array.getColor(R.styleable.StrikeTextView_strike_line_color, DEFAULT_LINE_COLOR);
        lineWidth = (int) array.getDimension(R.styleable.StrikeTextView_strike_line_width, DEFAULT_LINE_WIDTH);
        leftPadding = (int) array.getDimension(R.styleable.StrikeTextView_strike_left_padding, DEFAULT_LEFT_PADDING);
        rightPadding = (int) array.getDimension(R.styleable.StrikeTextView_strike_right_padding, DEFAULT_RIGHT_PADDING);
        lineType = array.getInt(R.styleable.StrikeTextView_strike_type, 0);

        array.recycle();
    }

    public void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(lineColor);
        linePaint.setTextSize(getTextSize());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();


        linePaint.getTextBounds(getText().toString(), 0, getText().length(), rect);

        int width = getWidth();
        int height = getHeight();
        int textWidth = rect.width();
        int textHeight = rect.height();

        if (lineType == 0) {
            canvas.drawLine((width - textWidth) / 2 + leftPadding, height, (width + textWidth) / 2 - rightPadding, height, linePaint);
        } else if (lineType == 1) {
            canvas.drawLine((width - textWidth) / 2 + leftPadding, height / 2, (width + textWidth) / 2 - rightPadding, height / 2, linePaint);
        } else if (lineType == 2) {
            canvas.drawLine((width - textWidth) / 2 + leftPadding, (height - textHeight) / 2, (width + textWidth) / 2 - rightPadding, (height + textHeight) / 2, linePaint);
        }
    }
}

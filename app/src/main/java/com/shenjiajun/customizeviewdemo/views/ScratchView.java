package com.shenjiajun.customizeviewdemo.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.shenjiajun.customizeviewdemo.R;

/**
 * Created by shenjiajun on 2016/4/7.
 */
public class ScratchView extends ImageView {

    private Bitmap fgBitmap, bgBitmap;// 前景橡皮擦的Bitmap和背景我们底图的Bitmap
    private Paint bgPaint, fgPaint, pathPaint;
    private Path mPath;

    private float lastX, lastY;
    private Rect rect;
    private Bitmap mBitmap;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private Canvas mCanvas;
    private Context mContext;

    public ScratchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    public ScratchView(Context context) {
        super(context);
        setUp();
    }

    public ScratchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("onMeasure", "width=" + getWidth() + " height=" + getHeight());
        rect = new Rect(0, 0, getWidth(), getHeight());
    }

    private void setUp() {
        mPath = new Path();
        pathPaint = new Paint();
        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 设置画笔透明度为0是关键！我们要让绘制的路径是透明的，然后让该路径与前景的底色混合“抠”出绘制路径
        pathPaint.setARGB(255, 255, 143, 0);

        // 设置混合模式为DST_IN
        pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 设置画笔风格为描边
        pathPaint.setStyle(Paint.Style.STROKE);

        // 设置路径结合处样式
        pathPaint.setStrokeJoin(Paint.Join.ROUND);

        // 设置笔触类型
        pathPaint.setStrokeCap(Paint.Cap.ROUND);

        // 设置描边宽度
        pathPaint.setStrokeWidth(50);


//        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 生成前景图Bitmap
        fgBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);

        // 将其注入画布
        mCanvas = new Canvas(fgBitmap);

        // 绘制画布背景为中性灰
        mCanvas.drawColor(0xFF808080);

        // 获取背景底图Bitmap
        if (mContext == null) {
            return;
        }
        bgBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bare_bears);

        // 缩放背景底图Bitmap至屏幕大小
        bgBitmap = Bitmap.createScaledBitmap(bgBitmap, rect.width(), rect.height(), true);


        Log.e("onDraw", "rect=" + rect.toString());
        pathPaint.setARGB(255, 63, 81, 181);
        pathPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, pathPaint);
        pathPaint.setARGB(255, 255, 143, 0);
        canvas.drawRect(rect, pathPaint);
        pathPaint.setARGB(255, 255, 143, 0);
        mCanvas.drawPath(mPath, pathPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.e("onTouchEvent", "x=" + x + "  y=" + y);
        Log.e("onTouchEvent", "event.getAction()=" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //mPath.reset();
                mPath.moveTo(x, y);
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("ActionMove", "x=" + x + "  y=" + y);
                mPath.quadTo(lastX, lastY, x, y);
                lastY = y;
                lastX = x;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        //setUp();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        //setUp();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        //setUp();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

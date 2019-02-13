package com.lzf.myhfuteducn.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义圆形进度条（中心有精确的进度数值显示、圆环颜色确定。）
 * 专用于activity_referral_program_statistics；自由训练>>>推荐计划>>>训练统计
 * <p>
 * Created by MJCoder on 2018-04-08.
 */

public class CircleProgressBar extends View {
    //    private Paint mBackPaint;
    private Paint mFrontPaint;
    private Paint mTextPaint;
    private float mStrokeWidth = 70;
    private float mHalfStrokeWidth = mStrokeWidth / 2;
    private float mRadius;
    private RectF mRect;
    private int mProgress = 0;
    private int mTargetProgress = 100; //目标值，想改多少就改多少
    private int mMax = 100;
    private int mWidth;
    private int mHeight;

    private int cache = 0;
    private boolean isClear = false;
    private CircleProgressEndListener circleProgressEndListener;

    public interface CircleProgressEndListener {
        void onProgressEndListener();
    }

    public void setOnProgressEndListener(CircleProgressEndListener circleProgressEndListener) {
        this.circleProgressEndListener = circleProgressEndListener;
    }

    public CircleProgressBar(Context context) {
        super(context);
        init();
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //完成相关参数初始化
    private void init() {
        //        mBackPaint = new Paint();
        //        mBackPaint.setColor(Color.WHITE);
        //        mBackPaint.setAntiAlias(true);
        //        mBackPaint.setStyle(Paint.Style.STROKE);
        //        mBackPaint.setStrokeWidth(mStrokeWidth);

        mFrontPaint = new Paint();
        mFrontPaint.setShader(new SweepGradient(mWidth / 2, mHeight / 2,
                new int[]{Color.parseColor("#00e4eb"),
                        Color.parseColor("#7288fc"),
                        Color.parseColor("#a92ce8"),
                        Color.parseColor("#980ccd"),
                        Color.parseColor("#7288fc")}, new float[]{0.0f,
                0.25f, 0.5f, 0.75f, 1.0f}));
        mFrontPaint.setAntiAlias(true);
        mFrontPaint.setStyle(Paint.Style.STROKE);
        mFrontPaint.setStrokeWidth(mStrokeWidth);
        mFrontPaint.setStrokeCap(Paint.Cap.ROUND);


        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(70);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    //重写测量大小的onMeasure方法和绘制View的核心方法onDraw()
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getRealSize(widthMeasureSpec);
        mHeight = getRealSize(heightMeasureSpec);
        mRadius = Math.min(mWidth / 2 - mHalfStrokeWidth, mHeight / 2 - mHalfStrokeWidth);
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (!isClear) {
            initRect();
            float angle = mProgress / (float) mMax * 360;
            //            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBackPaint);
            canvas.drawArc(mRect, -90, angle, false, mFrontPaint);
            canvas.drawText(cache + " KB  ", mWidth / 2 + mHalfStrokeWidth, mHeight / 2 + mHalfStrokeWidth, mTextPaint);
            if (mProgress < mTargetProgress) {
                mProgress += 1;
                try {
                    double random = Math.random();
                    cache += (int) (random * 10);
                    Thread.sleep((long) (random * 250));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                invalidate();
            } else if (circleProgressEndListener != null) {
                circleProgressEndListener.onProgressEndListener();
            }
        } else {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mFrontPaint);
            if (cache > 0) {
                canvas.drawText(cache + " KB  ", mWidth / 2 + mHalfStrokeWidth, mHeight / 2 + mHalfStrokeWidth, mTextPaint);
                try {
                    double random = Math.random();
                    cache -= (int) (random * 10);
                    Thread.sleep((long) (random * 250));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                invalidate();
            } else {
                isClear = false;
                canvas.drawText("空空如也  ", mWidth / 2 + mHalfStrokeWidth, mHeight / 2 + mHalfStrokeWidth, mTextPaint);
                circleProgressEndListener.onProgressEndListener();
            }
        }
    }

    public int getRealSize(int measureSpec) {
        int result = 1;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            //自己计算
            result = (int) (mRadius * 2 + mStrokeWidth);
        } else {
            result = size;
        }

        return result;
    }

    private void initRect() {
        if (mRect == null) {
            mRect = new RectF();
            int viewSize = (int) (mRadius * 2);
            int left = (mWidth - viewSize) / 2;
            int top = (mHeight - viewSize) / 2;
            int right = left + viewSize;
            int bottom = top + viewSize;
            mRect.set(left, top, right, bottom);
        }
    }

    public void clearCache(boolean isClear) {
        this.isClear = isClear;
        //        requestLayout(); //执行onMeasure方法和onLayout方法
        invalidate(); //执行onDraw方法
    }
}

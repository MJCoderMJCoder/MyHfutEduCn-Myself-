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
 * 自定义圆形进度条（中心有精确的进度数值显示、圆环颜色可变。）
 *
 * @author MJCoder
 * @see android.view.View
 */
public class CircleProgressBar extends View {
    //    private Paint mBackPaint;
    /**
     * 前景图形绘画面板
     */
    private Paint mFrontPaint;
    /**
     * 文字绘画面板
     */
    private Paint mTextPaint;
    /**
     * 前景图形绘画面板的画笔的宽度
     */
    private float mStrokeWidth = 70;
    /**
     * 前景图形绘画面板的画笔宽度的一半
     */
    private float mHalfStrokeWidth = mStrokeWidth / 2;
    /**
     * 前景图形绘画面板的圆环的半径
     */
    private float mRadius;
    /**
     * RectF为一个矩形保存四个浮点坐标。矩形由其4条边（左、上、右、下）的坐标表示.
     */
    private RectF mRect;
    /**
     * 前景图形绘画面板的圆环的当前进度
     */
    private int mProgress = 0;
    /**
     * 前景图形绘画面板的圆环的目标值
     */
    private int mTargetProgress = 100;
    /**
     * 前景图形绘画面板的圆环的最大值
     */
    private int mMax = 100;
    /**
     * 该CircleProgressBar在界面中所占用的宽度
     */
    private int mWidth;
    /**
     * 该CircleProgressBar在界面中所占用的高度
     */
    private int mHeight;

    /**
     * 当前扫描到多少缓存
     */
    private int cache = 0;
    /**
     * 是否清除缓存（true：清除缓存；false：不清除）
     */
    private boolean isClear = false;
    /**
     * 圆环进度结束监听器/缓存清除完成监听器
     */
    private CircleProgressEndListener circleProgressEndListener;

    /**
     * 圆环进度结束监听器/缓存清除完成监听器
     */
    public interface CircleProgressEndListener {
        void onProgressEndListener();
    }

    /**
     * 设置圆环进度结束监听器/缓存清除完成监听器
     *
     * @param circleProgressEndListener 圆环进度结束监听器/缓存清除完成监听器
     */
    public void setOnProgressEndListener(CircleProgressEndListener circleProgressEndListener) {
        this.circleProgressEndListener = circleProgressEndListener;
    }

    /**
     * 自定义圆形进度条的构造器方法
     *
     * @param context 环境/上下文
     */
    public CircleProgressBar(Context context) {
        super(context);
        init();
    }

    /**
     * 自定义圆形进度条的构造器方法
     *
     * @param context 环境/上下文
     * @param attrs   与XML中的标记关联的属性集合
     * @see AttributeSet
     */
    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 自定义圆形进度条的构造器方法
     *
     * @param context      环境/上下文
     * @param attrs        与XML中的标记关联的属性集合
     * @param defStyleAttr 当前主题中的一个属性，它包含对为视图提供默认值的样式资源的引用。
     */
    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 完成自定义圆形进度条UI相关的初始化
     */
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

    /**
     * 准确有效的测量视图及其内容的宽度和高度
     *
     * @param widthMeasureSpec  父级强加的水平空间要求
     * @param heightMeasureSpec 父级强加的垂直空间要求
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getRealSize(widthMeasureSpec);
        mHeight = getRealSize(heightMeasureSpec);
        mRadius = Math.min(mWidth / 2 - mHalfStrokeWidth, mHeight / 2 - mHalfStrokeWidth);
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 绘制View的核心方法onDraw()
     *
     * @param canvas 自定义圆形进度条的整个画布
     */
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

    /**
     * 根据度量规范模式
     * UNSPECIFIED：父级没有对子级施加任何约束，它可以是任意大小；
     * AT_MOST：子元素可以是任意大小，直到指定的大小；
     * EXACTLY：父级为子级确定了准确的大小，不管孩子想要多大，都会得到这些界限；
     * 得到实际尺寸
     *
     * @param measureSpec 父级强加的空间要求
     * @return 实际尺寸
     */
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

    /**
     * 初始化RectF【RectF为一个矩形保存四个浮点坐标。矩形由其4条边(left, top, right, bottom)的坐标表示】
     */
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

    /**
     * 清除缓存
     *
     * @param isClear 是否清除缓存（true：清除缓存；false：不清除）
     */
    public void clearCache(boolean isClear) {
        this.isClear = isClear;
        //        requestLayout(); //执行onMeasure方法和onLayout方法
        invalidate(); //执行onDraw方法
    }
}

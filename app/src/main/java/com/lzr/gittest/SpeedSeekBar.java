package com.lzr.gittest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 10302
 * 创建时间：2019/4/18
 */

public class SpeedSeekBar extends View {

    private final int mLineColor;
    private final int mTextColor;
    private final int mCircleColor;
    private Context mContext;
    private final List<String> mTextList;
    private Paint mGridLinePaint;
    private Paint mTextPaint;
    private Paint mCirclePaint;
    private int mLongHeight = 40;
    private int mShortHeight = 25;
    private int mLongPart = 4;
    private float mShortPart = 20;
    private int mRadius = 20;
    private int max = 20;
    private OnPointSeekBarChangedListener mOnPointSeekBarChangedListener;

    private float progress = 5f;//默认在1
    private float mAvgWidth;


    public SpeedSeekBar(Context context) {
        this(context, null);
    }

    public SpeedSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SpeedSeekBar);
        mLineColor = ta.getColor(R.styleable.SpeedSeekBar_background_line_color, Color.WHITE);
        mTextColor = ta.getColor(R.styleable.SpeedSeekBar_kedu_text_color, Color.parseColor("#718089"));
        mCircleColor = ta.getColor(R.styleable.SpeedSeekBar_background_circle_color, Color.parseColor("#f55061"));

        mTextList = new ArrayList<>();
        mTextList.add("0X");
        mTextList.add("1X");
        mTextList.add("2X");
        mTextList.add("3X");
        mTextList.add("4X");

        mLongHeight = dip2px(context, 13);
        mShortHeight = dip2px(context, 9);
        mRadius = dip2px(context, 7);
        initPaint();

    }

    private void initPaint() {
        mGridLinePaint = new Paint();
        mGridLinePaint.setAntiAlias(false);
        mGridLinePaint.setStrokeWidth(2);
        mGridLinePaint.setColor(mLineColor);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(sp2px(mContext, 12));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setShadowLayer(2, 1, 1, Color.parseColor("#aa000000"));

//        实例化画笔对象
        mCirclePaint = new Paint();
//        给画笔设置颜色
        mCirclePaint.setColor(mCircleColor);
//        设置画笔属性
        mCirclePaint.setStyle(Paint.Style.FILL);//画笔属性是空心圆
        mCirclePaint.setAntiAlias(true);//抗锯齿
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //左右需要间隔，否则显示不全，所以需要设置个间隔
        int newWidth = width - 100;
        //平均每段的距离
        mAvgWidth = (float) newWidth / mShortPart;
//        Log.e("lzr", "平均距离：" + mAvgWidth + "总距离：" + newWidth);
        float paintHeight = height * 3 / 5;

        //画中间白线
        canvas.drawLine(50, paintHeight, newWidth + 50, paintHeight, mGridLinePaint);
        //画字
        for (int i = 0; i < mTextList.size(); i++) {
            float offset = (float) (0.5 * mTextPaint.measureText(mTextList.get(i)));
            canvas.drawText(mTextList.get(i), i * (newWidth / 4) - offset + 50, paintHeight - dip2px(mContext, 25), mTextPaint);
        }
        //画长的分割线
        for (int i = 0; i < 5; i++) {
            float pointX = i * newWidth / mLongPart + 50;
            canvas.drawLine(pointX, paintHeight - mLongHeight,
                    pointX, paintHeight + mLongHeight, mGridLinePaint);
//            Log.e("lzr", "当前i=" + i + "长的x点：" + pointX);
        }
        //画短的分割线
        for (int i = 0; i < 20; i++) {
            if (i != 0 && i != 5 && i != 10 && i != 15) {
                float pointX = i * mAvgWidth + 50;
                canvas.drawLine(pointX, paintHeight - mShortHeight,
                        pointX, paintHeight + mShortHeight, mGridLinePaint);
//                Log.e("lzr", "当前i=" + i + "短的x点：" + pointX);
            }
        }
        //画圆
        canvas.drawCircle((float) progress * mAvgWidth + 50, paintHeight, mRadius, mCirclePaint);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float offset = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setCurrentValueByLocation(offset);
                return true;
            case MotionEvent.ACTION_UP:
                onValueDone();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 设置刻度数据
     *
     * @param datas
     */
    public void setDatas(List<String> datas) {
        mTextList.clear();
        mTextList.addAll(datas);
        invalidate();
    }


    public interface OnPointSeekBarChangedListener {
        /**
         * @param progress
         */
        void onProgressChangeing(SpeedSeekBar speedSeekBar, float progress);

        void onChanged(float progress);
    }


    public void setOnSeekBarChangedListener(
            OnPointSeekBarChangedListener onPointSeekBarChangedListener) {
        this.mOnPointSeekBarChangedListener = onPointSeekBarChangedListener;
    }

    private void setCurrentValueByLocation(float x) {
        int block = 0;
        if (x > 0) {
            block = (int) (x % mAvgWidth > 0 ? x / mAvgWidth - 1
                    : x / mAvgWidth - 1);
            block = block > max ? max : block;
        } else {
            block = 0;
        }
        setProgress(block);
    }


    public void setProgress(float progress) {
        if (this.progress != progress) {
            this.progress = progress;
            if (mOnPointSeekBarChangedListener != null) {
                mOnPointSeekBarChangedListener.onProgressChangeing(this, progress);
            }
            invalidate();
        }
    }


    private void onValueDone() {
        if (mOnPointSeekBarChangedListener != null) {
            mOnPointSeekBarChangedListener.onChanged(progress);
        }
    }

    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}

package com.chang.circlemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pioneer chang on 2017/6/3.
 */

public class CircleMenu extends ViewGroup {

    private float d = 1080;//直径
    private float startAngle;//
    private float oldX = 0;
    private float oldY = 0;

    public CircleMenu(Context context) {
        this(context, null);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int windowSize = getDefaultSize();
        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = Math.min(widthSize, windowSize);
        } else {
            //获取背景宽带
            int minimumWidth = getSuggestedMinimumWidth();
            if (minimumWidth == 0) {
                measureWidth = windowSize;
            } else {
                measureWidth = Math.min(minimumWidth, windowSize);
            }
        }
        d = measureWidth;
        setMeasuredDimension(measureWidth, measureWidth);

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int measureSpec = MeasureSpec.makeMeasureSpec((int) (d/3), MeasureSpec.EXACTLY);
            childView.measure(measureSpec, measureSpec);
        }
    }

    /**
     * 屏幕宽高最小值
     */
    private int getDefaultSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        return Math.min(widthPixels, heightPixels);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float radiu = d / 2.0f;
        float temp = radiu * 2 / 3.0f;//子view的几何中心距离圆心的距离
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int chileWidth = child.getMeasuredWidth();
            int left = (int) (radiu + Math.round(temp * Math.cos(Math.toRadians(startAngle))) - chileWidth / 2);
            int right = left + chileWidth;
            int top = (int) (radiu + Math.round(temp * Math.sin(Math.toRadians(startAngle))) - chileWidth / 2);
            int bottom = top + chileWidth;
            child.layout(left, top, right, bottom);
            startAngle += 360 / getChildCount();
        }
    }

    /**
     * 给控件添加子视图
     */
    public void setDatas(int[] images, String[] tests) {
        for (int i = 0; i < images.length; i++) {
            View itemView = View.inflate(getContext(), R.layout.item_circlemenu, null);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
            TextView textView = (TextView) itemView.findViewById(R.id.text);
            imageView.setImageResource(images[i]);
            textView.setText(tests[i]);
            addView(itemView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                float touchRadiu = (float) Math.hypot((oldX - d / 2), (oldY - d / 2));
                if (touchRadiu > d / 2) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                float newY = event.getY();
                float oldTouchAngle = CircleUtil.getAngle(oldX, oldY, (int) d);
                float newTouchAngle = CircleUtil.getAngle(newX, newY, (int) d);
                float diffAngle;
                if (CircleUtil.getQuadrant(newX, newY, (int) d) == 1 || CircleUtil.getQuadrant(newX, newY, (int) d) == 4) {
                    diffAngle = newTouchAngle - oldTouchAngle;
                } else {
                    diffAngle = oldTouchAngle - newTouchAngle;
                }
                startAngle += diffAngle;
                oldX = newX;
                oldY = newY;
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}

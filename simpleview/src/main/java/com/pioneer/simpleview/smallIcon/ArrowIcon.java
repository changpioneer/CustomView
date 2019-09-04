package com.pioneer.simpleview.smallIcon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 箭头头标
 */
public class ArrowIcon extends View {
    private Paint mPaint;

    public ArrowIcon(Context context) {
        super(context);
        init();
    }

    public ArrowIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArrowIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int width = getWidth();
        int height = getHeight();
        Path path = new Path();
        path.moveTo(width / 4, height / 2);
        path.lineTo(width * 3 / 4, 0);
        path.moveTo(width / 4, height / 2);
        path.lineTo(width * 3 / 4, height);
        canvas.drawPath(path, mPaint);
        canvas.restore();
    }
}
